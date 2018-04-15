package tn30.sh181.qrshopping;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;
import tn30.sh181.qrshopping.FirebaseClass.Cart;
import tn30.sh181.qrshopping.FirebaseClass.Product;

public class ProductDetail extends AppCompatActivity {


    TextView txtViewProductName,txtViewProductCategory, txtViewProductPrice;
    ImageView imgViewProduct;
    Button btnProceed,btnCancel;
    //String[] prodDetail;
    Product product;
    private int dialogTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        getSupportActionBar().setTitle("Product Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtViewProductName = findViewById(R.id.txtViewProductName);
        txtViewProductCategory = findViewById(R.id.txtViewProductCategory);
        txtViewProductPrice = findViewById(R.id.txtViewProductPrice);
        imgViewProduct = findViewById(R.id.imgViewProduct);
        btnProceed = findViewById(R.id.btnProceed);
        btnCancel = findViewById(R.id.btnCancel);
        //prodDetail = new String[4];
        product = new Product();

        Intent intent = getIntent();
        String id = intent.getStringExtra("product");

        retrieveProductDetail(id);
        Fabric.with(this, new Answers(), new Crashlytics());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart();
            }
        });
    }

    private void addCart() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cart cart = dataSnapshot.getValue(Cart.class);
                ArrayList<Product> products;
                if (cart != null) {
                    products = cart.getCarts();
                    products.add(product);
                } else {
                    products = new ArrayList<>();
                    products.add(product);
                }
                Double total = 0.0;
                for (Product product : products) {
                    total += product.getProductPrice();
                }
                Cart newCart = new Cart(products, total);
                databaseReference.setValue(newCart);
                Toast.makeText(getApplicationContext(), "Added to cart!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void retrieveProductDetail(final String id){
        DatabaseReference databaseProduct = FirebaseDatabase.getInstance().getReference("Product").child(id);
        databaseProduct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                product = dataSnapshot.getValue(Product.class);
                txtViewProductName.setText(product.getProductName());
                txtViewProductCategory.setText(product.getProductCategory());
                txtViewProductPrice.setText("RM " + product.getProductPrice());
                FirebaseStorage.getInstance().getReference().child("Product").child(dataSnapshot.child("productId").getValue().toString()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        BitmapDownloaderTask task = new BitmapDownloaderTask(imgViewProduct);
                        task.execute(uri.toString());
                    }
                });
                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentName("Product Detail")
                        .putCustomAttribute("Product Name", product.getProductName())
                );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
