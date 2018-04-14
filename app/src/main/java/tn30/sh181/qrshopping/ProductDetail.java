package tn30.sh181.qrshopping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tn30.sh181.qrshopping.FirebaseClass.Product;

public class ProductDetail extends AppCompatActivity {

    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        textview = findViewById(R.id.textViewProduct);
        Intent intent = getIntent();
        String id = intent.getStringExtra("product");

        retriveProductDetail(id);

    }

    private void retriveProductDetail(String id){
        DatabaseReference databaseProduct = FirebaseDatabase.getInstance().getReference("Product").child(id);
        databaseProduct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textview.setText(dataSnapshot.child("productCategory").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
