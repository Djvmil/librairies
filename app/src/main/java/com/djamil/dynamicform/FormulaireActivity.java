package com.djamil.dynamicform;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.djamil.dynamic_form.INPUT_TYPE_DF;
import com.djamil.dynamic_form.DynamicForm;
import com.djamil.dynamic_form.interfaces.OnClickDynamicFormListener;
import com.djamil.dynamic_form.models.IOFieldsItem;
import com.djamil.dynamic_form.models.ItemDF;

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
        fieldItems.add(new IOFieldsItem("Nom", "nom", INPUT_TYPE_DF.Label, R.layout.label_text));
        fieldItems.add(new IOFieldsItem("Prenom", "prenom", INPUT_TYPE_DF.Text));

        fieldItems.add(new IOFieldsItem("Telephone", "telephone", INPUT_TYPE_DF.Date));
        fieldItems.add(new IOFieldsItem("Email", "email", INPUT_TYPE_DF.Password));
        fieldItems.add(new IOFieldsItem("Djamil", "email", INPUT_TYPE_DF.Password));
        fieldItems.add(new IOFieldsItem("Dieme", "email", INPUT_TYPE_DF.Password));
        //fieldItems.add(new IOFieldsItem("Test", "test", ConstantesView.Radio));
        fieldItems.add(new IOFieldsItem("Nom", "nom", INPUT_TYPE_DF.Label,R.layout.label_text));
        fieldItems.add(new IOFieldsItem("Prenom", "prenom", INPUT_TYPE_DF.Country));
        fieldItems.add(new IOFieldsItem("Telephone", "telephone", INPUT_TYPE_DF.Date));
        fieldItems.add(new IOFieldsItem("Email", "email", INPUT_TYPE_DF.Password));
        //fieldItems.add(new IOFieldsItem("Test", "test", ConstantesView.Radio));

        ArrayList<ItemDF> itemDFS = new ArrayList<>();
        itemDFS.add(new ItemDF(1, "Label 1", "Value 1", "Field 1"));
        itemDFS.add(new ItemDF(2, "Label 2", "Value 2", "Field 2"));
        itemDFS.add(new ItemDF(3, "Label 3", "Value 3", "Field 3"));
        itemDFS.add(new ItemDF(4, "Label 4", "Value 4", "Field 4"));

        IOFieldsItem ioFieldsItem = new IOFieldsItem("ListSelect", "select", INPUT_TYPE_DF.Select);
        ioFieldsItem.setListItemDF(itemDFS);

        IOFieldsItem ioFieldsItem2 = new IOFieldsItem("ListRadio", "radio", INPUT_TYPE_DF.Radio);
        ioFieldsItem2.setListItemDF(itemDFS);

        IOFieldsItem ioFieldsItem1 = new IOFieldsItem("ListRadio", "radio", INPUT_TYPE_DF.CheckBox);
        ioFieldsItem1.setListItemDF(itemDFS);

        fieldItems.add(ioFieldsItem);
        fieldItems.add(ioFieldsItem1);

        dynamicForm.setNbFieldPerPage(2);
        dynamicForm.loadForm(fieldItems);


        dynamicForm.setOnClickDynamicFormListener(new OnClickDynamicFormListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " );
            }

            @Override
            public void OnDoneClicked(ArrayList<IOFieldsItem> ioFieldsItems) {
                Log.d(TAG, "OnDoneClicked: " );
                Log.d(TAG, "value: "+ioFieldsItems.get(1).getValue() );

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
