package com.suntelecoms.dynamicform;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.suntelecoms.djamil.dynamic_form.ConstantesView;
import com.suntelecoms.djamil.dynamic_form.FormDynamic;
import com.suntelecoms.djamil.dynamic_form.models.RichFieldItem;

import java.util.ArrayList;

public class FormulaireActivity extends AppCompatActivity {
private LinearLayout contentForm;
private ArrayList<RichFieldItem> fieldItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire);
        contentForm = findViewById(R.id.contentForm);

        String []list = {"Nom", "Nom 1", "Nom 2"};

        fieldItems = new ArrayList<>();
        fieldItems.add(new RichFieldItem("Nom", "nom", ConstantesView.Label,R.layout.label_text));
        fieldItems.add(new RichFieldItem("Prenom", "prenom", ConstantesView.Text, list));
        fieldItems.add(new RichFieldItem("Telephone", "telephone", ConstantesView.Date, list));
        fieldItems.add(new RichFieldItem("Email", "email", ConstantesView.Password, list));
        fieldItems.add(new RichFieldItem("Test", "test", ConstantesView.Radio, list));

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
