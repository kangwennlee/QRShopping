package tn30.sh181.qrshopping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProductDetail extends AppCompatActivity {

    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        textview = findViewById(R.id.textViewProduct);
        Intent intent = getIntent();
        String product = intent.getStringExtra("product");
        textview.setText(product);

    }
}
