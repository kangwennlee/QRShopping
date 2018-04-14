package tn30.sh181.qrshopping;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPurchaseHistory extends Fragment {
    Button btnManage;
    TextView txtViewTransactionId,txtViewTransactionDate,txtViewProductName,txtViewProductQuantity;

    public FragmentPurchaseHistory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_purchase_history, container, false);
        btnManage = v.findViewById(R.id.btnManage);
        txtViewTransactionId = v.findViewById(R.id.txtViewTransactionId);
        txtViewTransactionDate = v.findViewById(R.id.txtViewTransactionDate);
        txtViewProductName = v.findViewById(R.id.txtViewProductName);
        txtViewProductQuantity = v.findViewById(R.id.txtViewProductQuantity);

        return v;
    }

}
