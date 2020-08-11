package com.djamil.dynamic_form;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.djamil.dynamic_form.models.ItemDF;

import java.util.ArrayList;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */

public class SpinAdapter extends ArrayAdapter<ItemDF> {


    public SpinAdapter(Context context, int textViewResourceId,
                       ArrayList<ItemDF> values) {
        super(context, textViewResourceId, values);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(this.getItem(position).getLabel());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(this.getItem(position).getLabel());
        return label;
    }
}