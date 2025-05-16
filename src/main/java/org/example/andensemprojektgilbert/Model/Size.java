package org.example.andensemprojektgilbert.Model;

public class Size {
    private int id;
    private String size_value;
    private int size_type_id;

    public Size(int id, String size_value, int size_type_id) {
        this.id = id;
        this.size_value = size_value;
        this.size_type_id = size_type_id;
    }

    public Size(){}
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getSize_value() {
        return size_value;
    }
    public void setSizeValue(String size_value) {
        this.size_value = size_value;
    }

    public int getSizeTypeId() {
        return size_type_id;
    }
    public void setSizeTypeId(int size_type_id) {
        this.size_type_id = size_type_id;
    }
    public int getSize_type_id() {
        return size_type_id;
    }
    public void setSize_type_id(int size_type_id) {
        this.size_type_id = size_type_id;
    }


    @Override
    public String toString() {
        return size_value;
    }
}
