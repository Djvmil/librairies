package com.djamil.dynamic_form.interfaces;

import android.view.View;

import com.djamil.dynamic_form.models.IOFieldsItem;

import java.util.List;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */

public interface OnDoneClickListener {
    public void OnDoneClicked(View v, List<IOFieldsItem> ioFieldsItems);
}
