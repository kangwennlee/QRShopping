package tn30.sh181.qrshopping.FirebaseClass;

import java.util.ArrayList;

public class Transaction {
    private String transactionId;
    private String userToken;
    private ArrayList<Product> product;

    public Transaction(){}
    public Transaction(String transactionId, String userToken, ArrayList<Product> product){
        this.setTransactionId(transactionId);
        this.setUserToken(userToken);
        this.setProduct(product);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public ArrayList<Product> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<Product> product) {
        this.product = product;
    }
}
