package tn30.sh181.qrshopping;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tn30.sh181.qrshopping.FirebaseClass.Product;
import tn30.sh181.qrshopping.FirebaseClass.Purchase;

public class PurchaseHistory extends AppCompatActivity {
    ListView listViewPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        listViewPurchase = findViewById(R.id.listViewPurchase);

        retrievePurchaseHistory();
    }

    private void retrievePurchaseHistory(){
        final ArrayList<Purchase> purchases = new ArrayList<>();
        DatabaseReference databaseProduct = FirebaseDatabase.getInstance().getReference().child("Purchase").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseProduct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    purchases.add(ds.getValue(Purchase.class));
                    purchases.get(purchases.size()-1).setPurchaseId(ds.getKey());
                    //purchases.get(purchases.size()-1).setBalanceBefore(Double.parseDouble(ds.child("balanceBefore").getValue(String.class)));
                    //purchases.get(purchases.size()-1).setBalanceAfter(Double.parseDouble(ds.child("balanceAfter").getValue(String.class)));
                    ArrayList<Product> products = new ArrayList<>();
                    for(DataSnapshot dss : ds.child("productPurchased").getChildren()){
                        Product product = new Product(dss.child("productId").getValue(String.class),
                                dss.child("productName").getValue(String.class),
                                dss.child("productCategory").getValue(String.class),
                                dss.child("productPrice").getValue(Double.class));
                        products.add(product);
                    }
                    purchases.get(purchases.size()-1).setProduct(products);
                }
                PurchaseAdapter arrayAdapter = new PurchaseAdapter(getApplicationContext(), purchases);
                arrayAdapter.addAll(purchases);
                listViewPurchase.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class PurchaseAdapter extends ArrayAdapter<Purchase> {
        ArrayList<Purchase> purchases;
        PurchaseAdapter(Context context, ArrayList<Purchase> purchases){
            super(context, R.layout.activity_purchase_history, R.id.listViewPurchase);
            this.purchases = purchases;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_purchase_history, parent, false);
            }
            Button btnManage = convertView.findViewById(R.id.btnManage);
            TextView txtViewPurchaseId = convertView.findViewById(R.id.txtViewPurchaseId);
            TextView txtViewPurchaseDate = convertView.findViewById(R.id.txtViewPurchaseDate);
            ListView listViewPurchaseProduct = convertView.findViewById(R.id.listViewPurchaseProduct);

            Purchase purchase = getItem(position);
            txtViewPurchaseId.setText(purchase.getPurchaseId());

            try {
                // trim and display date
                String date = purchase.getPurchaseId().split("_")[0];
                SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
                Date dateObj = formater.parse(date,new ParsePosition(5));
                txtViewPurchaseDate.setText(DateFormat.format("dd/MM/yyyy ", dateObj));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            ArrayAdapter<Product> productAdapter = new ArrayAdapter<Product>(getApplicationContext(), R.layout.fragment_purchase_product, purchase.getProduct());

            ProductAdapter productArrayAdapter = new ProductAdapter(getApplicationContext(), purchase.getProduct());
            productArrayAdapter.addAll(purchase.getProduct());
            listViewPurchase.setAdapter(productArrayAdapter);

            // Future Improvement to view Purchase
            btnManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            return convertView;
        }
    }
    public class ProductAdapter extends ArrayAdapter<Product> {
        ArrayList<Product> products;
        ProductAdapter(Context context, ArrayList<Product> products){
            super(context, R.layout.fragment_purchase_product, R.id.listViewPurchase);
            this.products = products;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_purchase_product, parent, false);
            }
            TextView txtViewProductName = convertView.findViewById(R.id.txtViewProductName);
            TextView txtViewProductQuantity = convertView.findViewById(R.id.txtViewProductQuantity);

            Product product = getItem(position);
            txtViewProductName.setText(product.getProductName());
            txtViewProductQuantity.setText('1');

            return convertView;
        }
    }
}
