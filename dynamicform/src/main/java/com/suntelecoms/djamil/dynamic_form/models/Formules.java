package com.suntelecoms.djamil.dynamic_form.models;

/**
 * Created by Djvmil_ on 2020-02-10
 */

public class Formules {

    private int id;
    private String name;
    private String prix;

    public Formules() {
    }

    public Formules(int id, String name, String prix) {
        this.id = id;
        this.name = name;
        this.prix = prix;
    }

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

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }
}
