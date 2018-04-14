package tn30.sh181.qrshopping;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tn30.sh181.qrshopping.FirebaseClass.Product;
import tn30.sh181.qrshopping.FirebaseClass.Cart;

public class CartList extends AppCompatActivity {
    ListView listViewCart;
    TextView txtViewTotalAmount;
    Button btnProceed,btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listViewCart = findViewById(R.id.listViewCart);
        txtViewTotalAmount = findViewById(R.id.txtViewTotalAmount);
        btnProceed = findViewById(R.id.btnProceed);
        btnCancel = findViewById(R.id.btnCancel);

        retrieveCart();
    }

    private void retrieveCart(){
        DatabaseReference databaseProduct = FirebaseDatabase.getInstance().getReference().child("CartList").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseProduct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cart cart = new Cart();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                     cart = ds.getValue(Cart.class);

                    ArrayList<Product> products = new ArrayList<>();
                    for(DataSnapshot dss : ds.child("productPurchased").getChildren()){
                        Product product = new Product(dss.child("productId").getValue(String.class),
                                dss.child("productName").getValue(String.class),
                                dss.child("productCategory").getValue(String.class),
                                dss.child("productPrice").getValue(Double.class));
                        products.add(product);
                    }
                    cart.setCarts(products);
                }
                CartAdapter arrayAdapter = new CartAdapter(getApplicationContext(), cart);
                arrayAdapter.addAll(cart.getCarts());
                listViewCart.setAdapter(arrayAdapter);

                txtViewTotalAmount.setText(cart.getTotal().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class CartAdapter extends ArrayAdapter<Product> {
        Cart cart;
        CartAdapter(Context context, Cart cart){
            super(context, R.layout.fragment_cart_product, R.id.listViewCart);
            this.cart = cart;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_cart_product, parent, false);
            }

            TextView txtViewProductName = convertView.findViewById(R.id.txtViewProductName);
            TextView txtViewProductQuantity = convertView.findViewById(R.id.txtViewProductQuantity);
            TextView txtViewProductPrice = convertView.findViewById(R.id.txtViewProductPrice);
            ImageView imgViewProduct = convertView.findViewById(R.id.imgViewProduct);

            Product product = getItem(position);
            txtViewProductName.setText(product.getProductName());
            txtViewProductQuantity.setText("1");
            txtViewProductPrice.setText(product.getProductPrice().toString());

            return convertView;
        }
    }
}
