package tn30.sh181.qrshopping;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import tn30.sh181.qrshopping.FirebaseClass.Product;

public class ProductDetail extends AppCompatActivity {
    TextView txtViewProductName,txtViewProductCategory, txtViewProductPrice;
    ImageView imgViewProduct;
    Button btnProceed,btnCancel;

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

            }
        });
    }

    private void retrieveProductDetail(String id){
        DatabaseReference databaseProduct = FirebaseDatabase.getInstance().getReference("Product").child(id);
        databaseProduct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtViewProductName.setText(dataSnapshot.child("productName").getValue().toString());
                txtViewProductCategory.setText(dataSnapshot.child("productCategory").getValue().toString());
                txtViewProductPrice.setText(dataSnapshot.child("productPrice").getValue().toString());
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
