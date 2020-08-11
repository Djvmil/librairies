package com.djamil.dynamic_form.models;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */

public class ItemDF {
    private int id;
    private int idView;
    private String label;
    private String value;
    private String field;

    public ItemDF() {
    }

    public ItemDF(int id, String label, String value, String field) {
        this.id = id;
        this.label = label;
        this.value = value;
        this.field = field;
    }

    public ItemDF(int id, int idView, String label, String value, String field) {
        this.id = id;
        this.idView = idView;
        this.label = label;
        this.value = value;
        this.field = field;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdView() {
        return idView;
    }

    public void setIdView(int idView) {
        this.idView = idView;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", value='" + value + '\'' +
                ", field='" + field + '\'' +
                '}';
    }
}
