package tn30.sh181.qrshopping;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
        //private ArrayList<Product> product;
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
            TextView txtViewProductName = convertView.findViewById(R.id.txtViewProductName);
            TextView txtViewProductQuantity = convertView.findViewById(R.id.txtViewProductQuantity);

            Purchase purchase = getItem(position);
            txtViewPurchaseId.setText(purchase.getPurchaseId());
            txtViewPurchaseDate.setText(purchase.getPurchaseId());
            txtViewProductName.setText(purchase.getProduct().getProductName());
            txtViewProductQuantity.setText("1");

            // Future Improvement to view Purchase
            btnManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            return convertView;
        }

    }
}
