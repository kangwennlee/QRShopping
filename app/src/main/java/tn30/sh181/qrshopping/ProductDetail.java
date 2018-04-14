package tn30.sh181.qrshopping;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Date;

import tn30.sh181.qrshopping.FirebaseClass.Product;

public class ProductDetail extends AppCompatActivity {
    TextView txtViewProductName,txtViewProductCategory, txtViewProductPrice;
    ImageView imgViewProduct;
    Button btnProceed,btnCancel;
    String[] prodDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        txtViewProductName = findViewById(R.id.txtViewProductName);
        txtViewProductCategory = findViewById(R.id.txtViewProductCategory);
        txtViewProductPrice = findViewById(R.id.txtViewProductPrice);
        imgViewProduct = findViewById(R.id.imgViewProduct);
        btnProceed = findViewById(R.id.btnProceed);
        btnCancel = findViewById(R.id.btnCancel);
        prodDetail = new String[4];

        Intent intent = getIntent();
        String id = intent.getStringExtra("product");

        retrieveProductDetail(id);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseItem(Double.parseDouble(prodDetail[3]));
            }
        });
    }

    private void purchaseItem(final Double itemPrice){

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double balance = Double.parseDouble(dataSnapshot.child("balance").getValue().toString());
                double amt = itemPrice;
                Double newBalance = balance - amt;
                FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("balance").setValue(newBalance);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String currentDateandTime = sdf.format(new Date());
                FirebaseDatabase.getInstance().getReference().child("Transaction").child(FirebaseAuth.getInstance().getUid()).child(currentDateandTime).child("PurchaseAmount").setValue(amt);
                FirebaseDatabase.getInstance().getReference().child("Transaction").child(FirebaseAuth.getInstance().getUid()).child(currentDateandTime).child("BalanceBefore").setValue(balance);
                FirebaseDatabase.getInstance().getReference().child("Transaction").child(FirebaseAuth.getInstance().getUid()).child(currentDateandTime).child("BalanceAfter").setValue(newBalance);
                Product product = new Product(prodDetail[0],prodDetail[1],prodDetail[2],Double.parseDouble(prodDetail[3]));
                FirebaseDatabase.getInstance().getReference().child("Transaction").child(FirebaseAuth.getInstance().getUid()).child(currentDateandTime).child("ProductPurchased").child(prodDetail[0]).setValue(product);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(getApplicationContext(), "Successfully Purchased!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void retrieveProductDetail(final String id){
        DatabaseReference databaseProduct = FirebaseDatabase.getInstance().getReference("Product").child(id);
        databaseProduct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                prodDetail[0] = id;
                prodDetail[1] = dataSnapshot.child("productName").getValue().toString();
                prodDetail[2] = dataSnapshot.child("productCategory").getValue().toString();
                prodDetail[3] = dataSnapshot.child("productPrice").getValue().toString();
                txtViewProductName.setText(prodDetail[1]);
                txtViewProductCategory.setText(prodDetail[2]);
                txtViewProductPrice.setText("RM "+ prodDetail[3]);
                FirebaseStorage.getInstance().getReference().child("Product").child(dataSnapshot.child("productId").getValue().toString()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        BitmapDownloaderTask task = new BitmapDownloaderTask(imgViewProduct);
                        task.execute(uri.toString());
                    }
                });
                //imgViewProduct.setImageBitmap();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
