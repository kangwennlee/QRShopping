package tn30.sh181.qrshopping.FirebaseClass;

public class Product {
    private String productId;
    private String productName;
    private String productCategory;
    private Double productPrice;
    private String productStorageReference;

    public Product(){}

    public Product(String productId, String productName, String productCategory, Double productPrice, String productStorageReference){
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productStorageReference = productStorageReference;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductStorageReference() {
        return productStorageReference;
    }

    public void setProductStorageReference(String productStorageReference) {
        this.productStorageReference = productStorageReference;
    }
}
