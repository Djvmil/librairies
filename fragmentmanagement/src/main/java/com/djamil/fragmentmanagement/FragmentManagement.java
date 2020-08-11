package com.djamil.fragmentmanagement;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.djamil.fragmentmanagement.interfaces.OnBackClickListener;

/**
 * Created by Djvmil_ on 6/8/20
 */

public class FragmentManagement extends FrameLayout implements OnBackClickListener {
    private static final String TAG = "FragmentManagement";

    private Context context;
    private NestedScrollView rootLayout;
    private LayoutInflater inflater;

    private static final int ZERO = 0;

    public FragmentManagement(@NonNull Context context) {
        super(context);
    }

    public FragmentManagement(@NonNull Context ctx, @Nullable AttributeSet attrs) {
        super(ctx, attrs);

        context  = ctx;
        inflater =  LayoutInflater.from(ctx);


        TypedArray attr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FragmentMag, 0, 0);
/*
        try {
            nbFieldPerPage = attr.getInteger(R.styleable.FragmentMag_nb_field_per_page, ZERO);
            typeForm       = attr.getString(R.styleable.FragmentMag_type_form);
            colorField     = attr.getColor(R.styleable.FragmentMag_color_field, ZERO);
            paddingField   = attr.getDimension(R.styleable.FragmentMag_padding_field, ZERO);
            marginField    = attr.getDimension(R.styleable.FragmentMag_margin_field, ZERO);
        } finally {
            attr.recycle();
        }

        // Throw an exception if required attributes are not set
        if (nbFieldPerPage != ZERO) {
            //throw new RuntimeException("No title provided");
        }
        else if (typeForm != null) {
            //throw new RuntimeException("No subtitle provided");
        }
        else if (colorField != ZERO) {
            //throw new RuntimeException("No subtitle provided");
        }
        else if (paddingField != ZERO) {
            // throw new RuntimeException("No subtitle provided");
        }
        else if (marginField != ZERO) {
            //throw new RuntimeException("No subtitle provided");
        }*/
    }

    /**
     * init
     */
  /*  private void init(){
        //Load RootView from xml
        inflater.inflate(R.layout.container_dynamic_form, this, true);
        rootLayout    = findViewById(R.id.container);
        containerForm = findViewById(R.id.body);
    }*/


    public FragmentManagement(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void OnBackClicked(View v) {

    }
}
