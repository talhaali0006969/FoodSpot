package com.example.myapplication;

public class CartItem {

    private int cartId ;
    private NewArrival newArrival;
    private Product product;
    private int quantity;

    public CartItem(int cartId, deals product, int quantity) {
    }
    public CartItem(int cartId, NewArrival product, int quantity) {
    }

    public CartItem(int cartId, Product product,int quantity) {
        this.cartId = cartId;
        this.product = product;
        this.quantity = quantity;
    }


    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
