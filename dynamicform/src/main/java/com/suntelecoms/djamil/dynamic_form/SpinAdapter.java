package com.suntelecoms.djamil.dynamic_form;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.suntelecoms.djamil.dynamic_form.models.Formules;

import java.util.ArrayList;

/**
 * Created by Djvmil_ on 2020-02-10
 */

public class SpinAdapter extends ArrayAdapter<Formules> {


    public SpinAdapter(Context context, int textViewResourceId,
                       ArrayList<Formules> values) {
        super(context, textViewResourceId, values);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(this.getItem(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(this.getItem(position).getName());
        return label;
    }
}