package com.djamil.dynamic_form.interfaces;

import android.view.View;

import com.djamil.dynamic_form.models.IOFieldsItem;

import java.util.ArrayList;

/**
 * Created by Djvmil_ on 5/21/20
 */

public interface OnClickDynamicFormListener extends View.OnClickListener {
    public void onClick(View v);
    public void OnDoneClicked(ArrayList<IOFieldsItem> ioFieldsItems);
    public void OnNextClicked();
    public void OnBackClicked();
    public void OnCancelClicked();
    public void OnFormCreated(ArrayList<IOFieldsItem> ioFieldsItems);
}
