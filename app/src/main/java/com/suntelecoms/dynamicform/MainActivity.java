package com.suntelecoms.dynamicform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.suntelecoms.td.dynamic_form.FormDynamic;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onForm(View v) {
        Intent intent = new Intent(this, FormulaireActivity.class);
        startActivity(intent);
    }
}
