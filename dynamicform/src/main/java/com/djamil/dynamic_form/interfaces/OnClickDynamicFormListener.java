package com.djamil.dynamic_form.interfaces;

import android.view.View;

import com.djamil.dynamic_form.models.IOFieldsItem;

import java.util.List;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */

public interface OnClickDynamicFormListener extends View.OnClickListener {

    //======================= deb methode dynamic form =======================
    public void onClick(View v);
    public void OnDoneClicked(View v, List<IOFieldsItem> ioFieldsItems);
    public void OnNextClicked(View v);
    public void OnBackClicked(View v);
    public void OnCancelClicked(View v);
    public void OnFormCreated(List<IOFieldsItem> ioFieldsItems);
    //======================= end methode dynamic form =======================
}
