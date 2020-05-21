package com.suntelecoms.dynamicform;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.suntelecoms.djamil.dynamic_form.ConstantesView;
import com.suntelecoms.djamil.dynamic_form.DynamicForm;
import com.suntelecoms.djamil.dynamic_form.interfaces.OnBackClickListener;
import com.suntelecoms.djamil.dynamic_form.interfaces.OnDoneClickListener;
import com.suntelecoms.djamil.dynamic_form.interfaces.OnClickDynamicFormListener;
import com.suntelecoms.djamil.dynamic_form.models.IOFieldsItem;

import java.util.ArrayList;

public class FormulaireActivity extends AppCompatActivity {
    private static final String TAG = "FormulaireActivity";
    private DynamicForm dynamicForm;
    private ArrayList<IOFieldsItem> fieldItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire);
        dynamicForm = findViewById(R.id.dynamic_form);

        String []list = {"Nom", "Nom 1", "Nom 2"};

        fieldItems = new ArrayList<>();
        fieldItems.add(new IOFieldsItem("Nom", "nom", ConstantesView.Label,R.layout.label_text));
        fieldItems.add(new IOFieldsItem("Prenom", "prenom", ConstantesView.Text));
        fieldItems.add(new IOFieldsItem("Telephone", "telephone", ConstantesView.Date));
        fieldItems.add(new IOFieldsItem("Email", "email", ConstantesView.Password));
        fieldItems.add(new IOFieldsItem("Djamil", "email", ConstantesView.Password));
        fieldItems.add(new IOFieldsItem("Dieme", "email", ConstantesView.Password));
        //fieldItems.add(new IOFieldsItem("Test", "test", ConstantesView.Radio));
        fieldItems.add(new IOFieldsItem("Nom", "nom", ConstantesView.Label,R.layout.label_text));
        fieldItems.add(new IOFieldsItem("Prenom", "prenom", ConstantesView.Text));
        fieldItems.add(new IOFieldsItem("Telephone", "telephone", ConstantesView.Date));
        fieldItems.add(new IOFieldsItem("Email", "email", ConstantesView.Password));
        //fieldItems.add(new IOFieldsItem("Test", "test", ConstantesView.Radio));

        dynamicForm.loadForm(fieldItems);


        dynamicForm.setOnClickDynamicFormListener(new OnClickDynamicFormListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " );
            }

            @Override
            public void OnDoneClicked(ArrayList<IOFieldsItem> ioFieldsItems) {
                Log.d(TAG, "OnDoneClicked: " );
            }

            @Override
            public void OnNextClicked() {
                Log.d(TAG, "OnNextClicked: " );

            }

            @Override
            public void OnBackClicked() {
                Log.d(TAG, "OnBackClicked: " );

            }

            @Override
            public void OnCancelClicked() {
                Log.d(TAG, "OnCancelClicked: " );

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
