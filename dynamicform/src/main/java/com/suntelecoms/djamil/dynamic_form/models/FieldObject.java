package com.suntelecoms.djamil.dynamic_form.models;

import com.suntelecoms.djamil.dynamic_form.models.annotations.Expose;
import com.suntelecoms.djamil.dynamic_form.models.annotations.SerializedName;

/**
 *
 *   Djvmil 19/12/2020
 *
 **/
public class FieldObject {

    @SerializedName("field")
    @Expose
    private String field;

    @SerializedName("value")
    @Expose
    private String value;

    public FieldObject() {
    }

    public FieldObject(String field, String value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
