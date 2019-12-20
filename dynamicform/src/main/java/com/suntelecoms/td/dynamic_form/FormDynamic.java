package com.suntelecoms.td.dynamic_form;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.suntelecoms.td.dynamic_form.models.FieldObject;
import com.suntelecoms.td.dynamic_form.models.RichFieldItem;

import java.util.ArrayList;

public class FormDynamic {

    private static TextView textView;
    private static EditText  editText;
    private static AppCompatActivity context;
    private static ArrayList<FieldObject> fieldObjects;
    private static ArrayList<RichFieldItem> listForms;
    private static View view;


    static volatile FormDynamic singleton = null;


    public  FormDynamic(AppCompatActivity activity){
        context = activity;
    }



    public static FormDynamic with(AppCompatActivity activity) {
        if (singleton == null) {
            synchronized (FormDynamic.class) {
                if (singleton == null) {
                    singleton = new FormDynamic(activity);
                }
            }
        }
        return singleton;
    }

    public static FormDynamic getForm(LinearLayout layout, ArrayList<RichFieldItem> listForm){
        view = layout;
        listForms = listForm;

        final LayoutInflater inflater =  context.getLayoutInflater();
        layout.setOrientation(LinearLayout.VERTICAL);

       for(RichFieldItem item : listForm){

           View rowView = inflater.inflate(R.layout.input_text, null,true);
           rowView.setId(View.NO_ID);

           textView = rowView.findViewById(R.id.textView);
           editText = rowView.findViewById(R.id.editText);

           textView.setId(View.NO_ID);
           textView.setText(item.getLabel());

           try {

               int nextValue = Sequence.nextValue();

               editText.setId(nextValue);
               item.setId(nextValue);
               editText.setTag(item.getLabel());
               editText.setInputType(InputType.TYPE_CLASS_TEXT);
           } catch (Exception e) {
               Log.e("getForm Error", e.toString());
           }
           layout.addView(rowView);
       }

       return singleton;
    }

    public static void in(ArrayList<FieldObject> fieldObject){
        fieldObjects = fieldObject;
    }

    /** Fluent API for creating {@link FormDynamic} instances. */
    @SuppressWarnings("UnusedDeclaration") // Public API.
    public static class Builder {
        private final Context context;
        /**
         * Start building a new {@link FormDynamic} instance.
         */
        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }
    }

    public static void load(){
        for(RichFieldItem item : listForms){
            String value = ((TextView)view.findViewById(item.getId())).getText().toString();
            fieldObjects.add(new FieldObject(item.getField(), ((TextView)view.findViewById(item.getId())).getText().toString() ));
        }
    }
}