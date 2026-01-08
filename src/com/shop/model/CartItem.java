package com.shop.model;

public class CartItem {
    private int productId;
    private String name;
    private double price;
    private int qty;

    public CartItem(int productId, String name, double price, int qty) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public double getLineTotal() { return price * qty; }
}
