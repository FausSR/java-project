package com.projectTest.demo.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private static int count = 0; 
    private int id;

    private Map<Integer, Product> products;

    public int getId() {
        return this.id;
    }

    public Map<Integer, Product> getProducts() {
        return this.products;
    }

    public void setProducts(Map<Integer, Product> newProducts) {
        this.products = newProducts;
    }

    public void addProducts(ArrayList<Product> newProducts){
        for(Product product: newProducts) addProduct(product);
    }

    public void addProduct(Product product){
        int productId = product.getId();
        if(products.containsKey(productId)){
            Product existingProduct = products.get(productId);
            existingProduct.setAmount(existingProduct.getAmount() + product.getAmount());
            products.replace(productId, existingProduct);
        }
        else products.put(productId, product);
    }


    public Cart(){
        this.id = ++count; 
        this.products = new HashMap<>();  
    }
}
