package com.djamil.dynamic_form.utils;

import com.djamil.dynamic_form.models.IOFieldsItem;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */

public class Utils {


    public static String getNumberFloatFormat(Float val){

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRENCH);

        String valFormat = numberFormat.format(val);

        return valFormat;
    }

    public static String formatDate(String dateFormat) {

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }

     return (new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE)).format(date);
    }

    public static Object getFieldObject(ArrayList<IOFieldsItem> ioFieldsItems) {

        String griff = "\"";
        String fieldString = "{";

        for (IOFieldsItem item : ioFieldsItems){
            try{
                item.setValue((item.getValue() != null) ? item.getValue().trim() : "");

                fieldString += griff + item.getField() + griff + ":" + griff + item.getValue()+ griff + ",";
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        fieldString = fieldString.substring(0,fieldString.length()-1).concat("}");
        JsonElement jo =  (new JsonParser()).parse(fieldString);

        return (new Gson()).fromJson(jo, Object.class);
    }

}
