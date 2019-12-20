package com.suntelecoms.dynamicform;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.suntelecoms.td.dynamic_form.CONSTANTES_VIEW;
import com.suntelecoms.td.dynamic_form.FormDynamic;
import com.suntelecoms.td.dynamic_form.models.FieldObject;
import com.suntelecoms.td.dynamic_form.models.RichFieldItem;

import java.util.ArrayList;

public class FormulaireFacturierActivity extends AppCompatActivity {
private LinearLayout contentForm;
private ArrayList<RichFieldItem> fieldItems;
private ArrayList<FieldObject> fieldObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_facturier);
        contentForm = findViewById(R.id.contentForm);

        fieldItems = new ArrayList<>();
        fieldObjects = new ArrayList<>();

        fieldItems.add(new RichFieldItem("Nom", "nom", CONSTANTES_VIEW.TEXT_VIEW));
        fieldItems.add(new RichFieldItem("Prenom", "prenom", CONSTANTES_VIEW.TEXT_VIEW));
        fieldItems.add(new RichFieldItem("Telephone", "telephone", CONSTANTES_VIEW.TEXT_VIEW));
        fieldItems.add(new RichFieldItem("Email", "email", CONSTANTES_VIEW.TEXT_VIEW));
        fieldItems.add(new RichFieldItem("Test", "test", CONSTANTES_VIEW.TEXT_VIEW));

        FormDynamic
                .with(this)
                .getForm(contentForm, fieldItems)
                .in(fieldObjects);

    }

    public void onAnnuler(View v) {
        onBackPressed();
    }

    /**
     * Etape suivante
     */
    public void onSuivant(View v) {


        if (recupererInformations()) {

        }
    }

    private boolean recupererInformations() {
        FormDynamic.load();

        try {
            Toast.makeText(this,fieldObjects.get(1).getField()+" "+fieldObjects.get(1).getValue(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MenuPaimentFactureActivity.class);
        startActivity(intent);
        finish();
    }
}
