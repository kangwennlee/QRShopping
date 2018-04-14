package tn30.sh181.qrshopping.FirebaseClass;

import java.util.ArrayList;

public class Purchase {
    private String purchaseId;
    private Double balanceBefore;
    private Double balanceAfter;
    private ArrayList<Product> product;

    public Purchase(){}
    public Purchase(String purchaseId, String purchaseDate, ArrayList<Product> product){
        this.purchaseId = purchaseId;
        this.product = product;
    }

    public String getPurchaseId() {
        return purchaseId;
    }
    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public ArrayList<Product> getProduct() {
        return product;
    }
    public void setProduct(ArrayList<Product> product) {
        this.product = product;
    }

    public Double getBalanceBefore() {
        return balanceBefore;
    }
    public void setBalanceBefore(Double balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public Double getBalanceAfter() {
        return balanceAfter;
    }
    public void setBalanceAfter(Double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
}
