package tn30.sh181.qrshopping.FirebaseClass;

public class Purchase {
    private String purchaseId;
    private String purchaseDate;
    private Product product;

    public Purchase(){}
    public Purchase(String purchaseId, String purchaseDate, Product product){
        this.purchaseId = purchaseId;
        this.setPurchaseDate(purchaseDate);
        this.product = product;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
