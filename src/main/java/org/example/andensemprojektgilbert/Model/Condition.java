package org.example.andensemprojektgilbert.Model;

public class Condition {

    private int idcondition;
    private String itemcondition;

    public Condition(int idcondition, String itemcondition) {
        this.idcondition = idcondition;
        this.itemcondition = itemcondition;
    }
    public Condition() {}
    public int getIdcondition() {
        return idcondition;
    }
    public void setIdcondition(int idcondition) {
        this.idcondition = idcondition;
    }
    public String getItemcondition() {
        return itemcondition;
    }
    public void setItemcondition(String itemcondition) {
        this.itemcondition = itemcondition;
    }
}
