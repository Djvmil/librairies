package com.suntelecoms.dynamicform.materialLeanBack;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Djvmil_ on 2020-02-05
 */

public class PlaceHolderViewHolder extends RecyclerView.ViewHolder {

    public PlaceHolderViewHolder(View itemView, boolean horizontal, int dimen) {
        super(itemView);

        if(horizontal) {
            if (dimen != -1) {
                //itemView.getLayoutParams().width = dimen;
                itemView.requestLayout();
            }
        }else{
            if (dimen != -1) {
                itemView.getLayoutParams().height = dimen;
                itemView.requestLayout();
            }
        }
    }

}