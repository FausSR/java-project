package com.projectTest.demo.models;

public class Product {

    private int id;
    private String description;
    private int amount;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setdescription(String description){
        this.description = description;
    }

    public String getdescription(){
        return description;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public Product(String description, int amount, int id){
        this.description = description;
        this.amount = amount;
        this.id = id; 
    }
}
