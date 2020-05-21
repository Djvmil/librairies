package com.suntelecoms.djamil.dynamic_form.interfaces;

import android.view.View;

import com.suntelecoms.djamil.dynamic_form.models.IOFieldsItem;

import java.util.ArrayList;

/**
 * Created by Djvmil_ on 5/20/20
 */

public interface OnDoneClickListener {
    public void OnDoneClicked(View v, ArrayList<IOFieldsItem> ioFieldsItems);
}
