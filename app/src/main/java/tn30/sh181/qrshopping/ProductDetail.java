package tn30.sh181.qrshopping;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.PurchaseEvent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.jesusm.kfingerprintmanager.KFingerprintManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import io.fabric.sdk.android.Fabric;
import tn30.sh181.qrshopping.FirebaseClass.Product;

public class ProductDetail extends AppCompatActivity {

    private static final String KEY = "KEY";
    TextView txtViewProductName,txtViewProductCategory, txtViewProductPrice;
    ImageView imgViewProduct;
    Button btnProceed,btnCancel;
    String[] prodDetail;
    private int dialogTheme;

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
        Fabric.with(this, new Answers(), new Crashlytics());
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Product Detail")
                .putCustomAttribute("Product Name", prodDetail[1])
        );

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerAuth();
            }
        });
    }

    private void fingerAuth() {
        createFingerprintManagerInstance().authenticate(new KFingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationSuccess() {
                //messageText.setText("Successfully authenticated");
                purchaseItem(Double.parseDouble(prodDetail[3]));
            }

            @Override
            public void onSuccessWithManualPassword(@NotNull String password) {
                //messageText.setText("Manual password: " + password);
            }

            @Override
            public void onFingerprintNotRecognized() {
                //messageText.setText("Fingerprint not recognized");
            }

            @Override
            public void onAuthenticationFailedWithHelp(@Nullable String help) {
                //messageText.setText(help);
            }

            @Override
            public void onFingerprintNotAvailable() {
                //messageText.setText("Fingerprint not available");
            }

            @Override
            public void onCancelled() {
                //messageText.setText("Operation cancelled by user");
            }
        }, getSupportFragmentManager());

    }

    private KFingerprintManager createFingerprintManagerInstance() {
        KFingerprintManager fingerprintManager = new KFingerprintManager(this, KEY);
        //fingerprintManager.setAuthenticationDialogStyle(dialogTheme);
        return fingerprintManager;
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
                DatabaseReference purchaseReference = FirebaseDatabase.getInstance().getReference().child("Purchase").child(FirebaseAuth.getInstance().getUid()).child(currentDateandTime);
                purchaseReference.child("purchaseAmount").setValue(amt);
                purchaseReference.child("balanceBefore").setValue(balance);
                purchaseReference.child("balanceAfter").setValue(newBalance);
                Product product = new Product(prodDetail[0],prodDetail[1],prodDetail[2],Double.parseDouble(prodDetail[3]));
                purchaseReference.child("productPurchased").child(prodDetail[0]).setValue(product);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(getApplicationContext(), "Successfully Purchased!", Toast.LENGTH_SHORT).show();
        Answers.getInstance().logPurchase(new PurchaseEvent()
                .putItemPrice(BigDecimal.valueOf(Double.parseDouble(prodDetail[3])))
                .putItemName(prodDetail[1])
                .putItemId(prodDetail[0])
                .putCurrency(Currency.getInstance("MYR"))
                .putSuccess(true)
        );
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
