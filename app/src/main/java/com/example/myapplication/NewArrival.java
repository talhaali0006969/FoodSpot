package com.example.myapplication;

import java.io.Serializable;

public class NewArrival implements Serializable {

    private String productId;
    private String productName;
    private String catId;
    private String productPrice;
    private String productStatus;
    private String productImage;
    private String productDescription;

    public NewArrival() {
    }

    public NewArrival(String productId, String productName, String catId, String productPrice, String productStatus, String productImage, String productDescription) {
        this.productId = productId;
        this.productName = productName;
        this.catId = catId;
        this.productPrice = productPrice;
        this.productStatus = productStatus;
        this.productImage = productImage;
        this.productDescription = productDescription;
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

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
