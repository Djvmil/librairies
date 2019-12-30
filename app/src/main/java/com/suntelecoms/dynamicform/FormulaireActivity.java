package com.suntelecoms.dynamicform;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.suntelecoms.djamil.dynamic_form.ConstantesView;
import com.suntelecoms.djamil.dynamic_form.FormDynamic;
import com.suntelecoms.djamil.dynamic_form.models.IOFieldsItem;

import java.util.ArrayList;

public class FormulaireActivity extends AppCompatActivity {
private LinearLayout contentForm;
private ArrayList<IOFieldsItem> fieldItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire);
        contentForm = findViewById(R.id.contentForm);

        String []list = {"Nom", "Nom 1", "Nom 2"};

        fieldItems = new ArrayList<>();
        fieldItems.add(new IOFieldsItem("Nom", "nom", ConstantesView.Label,R.layout.label_text));
        fieldItems.add(new IOFieldsItem("Prenom", "prenom", ConstantesView.Text));
        fieldItems.add(new IOFieldsItem("Telephone", "telephone", ConstantesView.Date));
        fieldItems.add(new IOFieldsItem("Email", "email", ConstantesView.Password));
        fieldItems.add(new IOFieldsItem("Test", "test", ConstantesView.Radio));

        FormDynamic.with(this)
                .loadForm(contentForm, fieldItems);

    }

    public void onAnnuler(View v) {
        onBackPressed();
    }

    /**
     * Etape suivante
     */
    public void onSuivant(View v) {
        FormDynamic.done();

        try {
            Toast.makeText(this,fieldItems.get(4).getField()+" "+fieldItems.get(4).getValue(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
