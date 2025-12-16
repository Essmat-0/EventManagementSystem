/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.eventmanagment.models;

/**
 *
 * @author compu one
 */
public class BillItem {
    
    private String description;
    private double price;
    public BillItem(){
        
    }
    public BillItem(String description, double price){
        this.description = description;
        this.price = price;
    }
    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public String getItem(){
        return toString();
    }
    
@Override
public String toString() {
    return "BillItem{" +
            "description='" + description + '\'' +
            ", price=" + price +
            '}';
}

}
