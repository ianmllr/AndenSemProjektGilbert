package org.example.andensemprojektgilbert.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jdk.jfr.Name;


public class User {
    private int id;

    @Name("Name has to be valid")
    @NotBlank(message = "Cannot be blank")
    private String name;

    @Email(message = "Email has to be valid")
    @NotBlank(message = "Email field cannot be blank")
    private String email;

    @NotBlank(message = "Cannot be blank")
    @Size(min = 3, message = "The password must be at least 3 characters")
    private String password;


    private int sales;

    private String rating;

    private String role;

    private String imgsrc;

    private String Fname;
    private String Lname;
    private String address;

    public User(int id, String name, String email, String password, int sales, String rating, String role, String imgsrc, String Fname, String Lname, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.sales = sales;
        this.rating = rating;
        this.role = role;
        this.imgsrc = imgsrc;
        this.Fname = Fname;
        this.Lname = Lname;
        this.address = address;
    }
    public User() {}

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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getSales() {
        return sales;
    }
    public void setSales(int sales) {
        this.sales = sales;
    }
    public String getImgsrc(){
        return imgsrc;
    }
    public void setImgsrc(String imgsrc){
        this.imgsrc = imgsrc;
    }

    public String getRating() {
        if (sales == 0) {
            rating = "New seller";
        } else if (sales == 50) {
            rating = "Experience seller";
        }
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public String getFname() {
        return Fname;
    }
    public void setFname(String Fname) {
        this.Fname = Fname;
    }
    public String getLname() {
        return Lname;
    }
    public void setLname(String Lname) {
        this.Lname = Lname;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return name;
    }
}
