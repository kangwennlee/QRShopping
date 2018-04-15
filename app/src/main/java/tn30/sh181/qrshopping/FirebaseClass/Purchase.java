package tn30.sh181.qrshopping.FirebaseClass;

import java.util.ArrayList;

public class Purchase {
    private String purchaseId;
    private Double balanceBefore;
    private Double balanceAfter;
    private Cart productPurchased;
    private Double purchaseAmount;

    public Purchase(){}
    public Purchase(String purchaseId,Double balanceBefore, Double balanceAfter,Cart productPurchased, Double purchaseAmount){
        this.purchaseId = purchaseId;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.productPurchased = productPurchased;
        this.purchaseAmount = purchaseAmount;
    }

    public String getPurchaseId() {
        return purchaseId;
    }
    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Cart getProduct() {
        return productPurchased;
    }
    public void setProduct(Cart product) {
        this.productPurchased = product;
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

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }
    public void setPurchaseAmount(Double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }
}
