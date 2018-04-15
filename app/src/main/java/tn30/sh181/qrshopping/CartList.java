package tn30.sh181.qrshopping;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.PurchaseEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jesusm.kfingerprintmanager.KFingerprintManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import tn30.sh181.qrshopping.FirebaseClass.Cart;
import tn30.sh181.qrshopping.FirebaseClass.Product;

public class CartList extends AppCompatActivity {
    private static final String KEY = "KEY";
    ListView listViewCart;
    TextView txtViewTotalAmount;
    Button btnProceed,btnCancel;
    Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setTitle("Shopping Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listViewCart = findViewById(R.id.listViewCart);
        txtViewTotalAmount = findViewById(R.id.txtViewTotalAmount);
        btnProceed = findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerAuth();
            }
        });
        btnCancel = findViewById(R.id.btnCancel);

        retrieveCart();
    }

    private void retrieveCart(){
        DatabaseReference databaseProduct = FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseProduct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cart = dataSnapshot.getValue(Cart.class);
                if (cart == null) {
                    //Handle the error when the cart does not exist
                    Toast.makeText(getApplicationContext(), "Cart Empty ! Please Add An Item to Cart Before Proceed.", Toast.LENGTH_SHORT).show();
                }else{
                    CartAdapter arrayAdapter = new CartAdapter(getApplicationContext(), cart);
                    arrayAdapter.addAll(cart.getCarts());
                    listViewCart.setAdapter(arrayAdapter);
                    txtViewTotalAmount.setText(cart.getTotal().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fingerAuth() {
        createFingerprintManagerInstance().authenticate(new KFingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationSuccess() {
                //messageText.setText("Successfully authenticated");
                purchaseItem();
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

    private void purchaseItem() {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double balance = Double.parseDouble(dataSnapshot.child("balance").getValue().toString());
                double amt = cart.getTotal();
                Double newBalance = balance - amt;
                if (newBalance > 0) {
                    FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("balance").setValue(newBalance);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String currentDateandTime = sdf.format(new Date());
                    DatabaseReference purchaseReference = FirebaseDatabase.getInstance().getReference().child("Purchase").child(FirebaseAuth.getInstance().getUid()).child(currentDateandTime);
                    purchaseReference.child("purchaseId").setValue(currentDateandTime);
                    purchaseReference.child("purchaseAmount").setValue(amt);
                    purchaseReference.child("balanceBefore").setValue(balance);
                    purchaseReference.child("balanceAfter").setValue(newBalance);
                    purchaseReference.child("productPurchased").setValue(cart);
                    Toast.makeText(getApplicationContext(), "Successfully Purchased!", Toast.LENGTH_SHORT).show();
                    Answers.getInstance().logPurchase(new PurchaseEvent()
                            .putItemPrice(BigDecimal.valueOf(cart.getTotal()))
                            .putCurrency(Currency.getInstance("MYR"))
                            .putSuccess(true)
                    );
                    FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Insufficient Balance! Please Top Up Your Wallet.", Toast.LENGTH_SHORT).show();
                    Answers.getInstance().logPurchase(new PurchaseEvent()
                            .putItemPrice(BigDecimal.valueOf(cart.getTotal()))
                            .putCurrency(Currency.getInstance("MYR"))
                            .putSuccess(false)
                    );
                }
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
