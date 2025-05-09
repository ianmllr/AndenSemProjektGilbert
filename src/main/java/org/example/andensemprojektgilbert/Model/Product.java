package org.example.andensemprojektgilbert.Model;

import java.util.Date;

public class Product {

    private int id;
    private String name;
    private String brand;
    private String department;
    private String category;
    private String subcategory;
    private double price;
    private String p_condition;
    private String size;
    private String color;
    private Date postedDate;
    private String description;
    private String location;
    private int createdByID;


    public Product(int id, String name, String brand, String location, String description, String department, String category, String subcategory, Date postedDate, double price, String p_condition, String size, String color, int createdByID)
    {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.location = location;
        this.description = description;
        this.department = department;
        this.category = category;
        this.subcategory = subcategory;
        this.price = price;
        this.p_condition = p_condition;
        this.size = size;
        this.color = color;
        this.postedDate = postedDate;
        this.createdByID = createdByID;
    }

    public Product() {}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getSubcategory() {
        return subcategory;
    }
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getP_condition() {
        return p_condition;
    }
    public void setP_condition(String p_condition) {
        this.p_condition = p_condition;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public Date getPostedDate() {
        return postedDate;
    }
    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public int getCreatedByID() {
        return createdByID;
    }
    public void setCreatedByID(int createdByID) {
        this.createdByID = createdByID;
    }

}
