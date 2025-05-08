package org.example.andensemprojektgilbert.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jdk.jfr.Name;


public class User {
    private int id;

    @Name("Navn skal være gyldigt")
    @NotBlank(message = "Må ikke være tom")
    private String name;

    @Email(message = "Email skal være gyldig")
    @NotBlank(message = "Email må ikke være tom")
    private String email;

    @NotBlank(message = "Må ikke være tom")
    @Size(min = 3, message = "Password skal være mindst 3 tegn")
    private String password;


    private int sales;

    private String rating;

    private String role;

    public User(int id, String name, String email, String password, int sales, String rating, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.sales = sales;
        this.rating = rating;
        this.role = role;
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

    public String getRating() {
        if (sales == 0) {
            rating = "New seller";
        } else if (sales == 50) {
            rating = "Experience seller";
        }
        return rating;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
