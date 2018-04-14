package tn30.sh181.qrshopping.FirebaseClass;

import java.util.ArrayList;

public class Cart {
    private String cartId;
    private String userToken;
    private ArrayList<Product> productList;
    private int totalProductCount;
    private Double totalPrice;

    public Cart(){}
    public Cart(String cartId, String userToken, ArrayList<Product> productList, int totalProductCount, Double totalPrice){
        this.cartId = cartId;
        this.userToken = userToken;
        this.productList = productList;
        this.totalProductCount = totalProductCount;
        this.totalPrice = totalPrice;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    public int getTotalProductCount() {
        return totalProductCount;
    }

    public void setTotalProductCount(int totalProductCount) {
        this.totalProductCount = totalProductCount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
