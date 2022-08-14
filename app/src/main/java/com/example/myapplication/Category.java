package com.example.myapplication;


import java.io.Serializable;

public class Category implements Serializable {
    private String catId;
    private String catName;
    private String catImage;
    private String catStatus;


    public Category() {
    }

    public Category(String catId, String catName, String catImage, String catStatus) {
        this.catId = catId;
        this.catName = catName;
        this.catImage = catImage;
        this.catStatus = catStatus;
    }

    public String getCatId() {

        return catId;
    }

    public void setCatId(String catId) {

        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    public String getCatStatus() {
        return catStatus;
    }

    public void setCatStatus(String catStatus) {
        this.catStatus = catStatus;
    }
}
