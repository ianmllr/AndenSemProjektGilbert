package org.example.andensemprojektgilbert.Model;

public class Subcategory {
    private int id;
    private String name;
    private int categoryId;
    private int sizeTypeId;

    public Subcategory(int id, String name, int categoryId, int sizeTypeId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.sizeTypeId = sizeTypeId;
    }

    public Subcategory() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public int getSizeTypeId() { return sizeTypeId; }
    public void setSizeTypeId(int sizeTypeId) { this.sizeTypeId = sizeTypeId; }


    @Override
    public String toString() {
    return name;
    }
}
