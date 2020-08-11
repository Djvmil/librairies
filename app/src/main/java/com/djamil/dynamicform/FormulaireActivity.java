package com.djamil.dynamicform;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.djamil.dynamic_form.INPUT_TYPE_DF;
import com.djamil.dynamic_form.DynamicForm;
import com.djamil.dynamic_form.interfaces.OnClickDynamicFormListener;
import com.djamil.dynamic_form.models.CustomButton;
import com.djamil.dynamic_form.models.IOFieldsItem;
import com.djamil.dynamic_form.models.ItemDF;
import com.djamil.utils.RandomStringUUID;

import java.util.ArrayList;

public class FormulaireActivity extends AppCompatActivity implements OnClickDynamicFormListener {
    private static final String TAG = "FormulaireActivity";
    private DynamicForm dynamicForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire);
        dynamicForm = findViewById(R.id.dynamic_form);

        ArrayList<IOFieldsItem> fieldItems = new ArrayList<>();
        IOFieldsItem ioFieldsItem;

        fieldItems.add(new IOFieldsItem("Nom", "nom", INPUT_TYPE_DF.Label, R.layout.label_text));
        fieldItems.add(new IOFieldsItem("Prénom", "prenom", INPUT_TYPE_DF.Text));

        ioFieldsItem = new IOFieldsItem("Required", "required", INPUT_TYPE_DF.Text);
        ioFieldsItem.setIsRequired(true);
        fieldItems.add(ioFieldsItem);

        fieldItems.add(new IOFieldsItem("Téléphone", "telephone", INPUT_TYPE_DF.Phone));
        fieldItems.add(new IOFieldsItem("Date", "date", INPUT_TYPE_DF.Date));
        fieldItems.add(new IOFieldsItem("Email", "email", INPUT_TYPE_DF.Email));
        fieldItems.add(new IOFieldsItem("Password", "password", INPUT_TYPE_DF.Password));

        ArrayList<ItemDF> itemDFS = new ArrayList<>();
        itemDFS.add(new ItemDF(1, "Label 1", "Value 1", "Field 1"));
        itemDFS.add(new ItemDF(2, "Label 2", "Value 2", "Field 2"));
        itemDFS.add(new ItemDF(3, "Label 3", "Value 3", "Field 3"));
        itemDFS.add(new ItemDF(4, "Label 4", "Value 4", "Field 4"));

        ioFieldsItem = new IOFieldsItem("ListSelect", "select", INPUT_TYPE_DF.Select);
        ioFieldsItem.setListItemDF(itemDFS);
        fieldItems.add(ioFieldsItem);

        ioFieldsItem = new IOFieldsItem("ListRadio", "radio", INPUT_TYPE_DF.Radio);
        ioFieldsItem.setListItemDF(itemDFS);
        fieldItems.add(ioFieldsItem);

        ioFieldsItem = new IOFieldsItem("ListRadio", "CheckBox", INPUT_TYPE_DF.CheckBox);
        ioFieldsItem.setListItemDF(itemDFS);
        fieldItems.add(ioFieldsItem);

        dynamicForm.loadForm(fieldItems);

        dynamicForm.setNameButtonFr();
        dynamicForm.customNextButton(new CustomButton(R.color.black_semi_transparent, -1, R.drawable.fieldset_default, "SuivantRR", -1));
        dynamicForm.setOnClickDynamicFormListener(this);


        RandomStringUUID.getUUID();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: " );
    }

    @Override
    public void OnDoneClicked(View v, ArrayList<IOFieldsItem> ioFieldsItems) {
        Log.d(TAG, "OnDoneClicked: " );
        Log.d(TAG, "value: "+ioFieldsItems.get(1).getValue());

    }

    @Override
    public void OnNextClicked(View v) {
        Log.d(TAG, "OnNextClicked: " );

    }

    @Override
    public void OnBackClicked(View v) {
        Log.d(TAG, "OnBackClicked: " );

    }

    @Override
    public void OnCancelClicked(View v) {
        Log.d(TAG, "OnCancelClicked: " );
        finish();
    }

    @Override
    public void OnFormCreated(ArrayList<IOFieldsItem> ioFieldsItems) {
        Log.e(TAG, "OnFormCreated: ");

        IOFieldsItem item = dynamicForm.findIOFieldByFieldName("seck");

        if (item != null){
            final EditText editText = dynamicForm.findViewById(item.getIdView());
            editText.setClickable(true);
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(FormulaireActivity.this, "akk mom "+editText.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    public void finish() {
        super.finish();
    }


}
