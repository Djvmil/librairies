package com.suntelecoms.djamil.dynamic_form;
/**
 *
 *   Djvmil 19/12/2020
 *
 **/

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;
import com.suntelecoms.djamil.dynamic_form.models.FieldObject;
import com.suntelecoms.djamil.dynamic_form.models.RichFieldItem;
import com.suntelecoms.djamil.dynamic_form.models.Sequence;

import java.util.ArrayList;
import java.util.Calendar;

public class FormDynamic {

    private static TextView textView;
    private static EditText  editText;
    private static CheckBox checkBox;
    private static AppCompatActivity context;
    private static ArrayList<RichFieldItem> listForms;
    private static View view;
    private static LayoutInflater inflater;

    private static Calendar calendar ;
    private static DatePickerDialog datePickerDialog ;
    private static int Year, Month, Day ;


    static volatile FormDynamic singleton = null;

    /**   **/
    public  FormDynamic(AppCompatActivity activity){
        context = activity;
        inflater =  context.getLayoutInflater();
    }


    /**   **/
    public static FormDynamic with(AppCompatActivity activity) {
        if (singleton == null) {
            synchronized (FormDynamic.class) {
                if (singleton == null) {
                    singleton = new FormDynamic(activity);
                }
            }
        }
        return singleton;
    }


    /** Cette methode charge le formulaire en fonction de la liste de type RichField fourni **/
    public FormDynamic loadForm(LinearLayout layout, ArrayList<RichFieldItem> listForm){
        view = layout;
        listForms = listForm;
        layout.setOrientation(LinearLayout.VERTICAL);

       for(RichFieldItem item : listForm){
           View rowView = getView(item);
           if(rowView == null)
               throw new IllegalArgumentException(item.getLabel()+" must not be null");

           layout.addView(rowView);
       }

       return singleton;
    }

    /**  **/
    @SuppressWarnings("UnusedDeclaration") // Public API.
    public static class Builder {
        private final Context context;

        /**  **/
        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }
    }

    /**  **/
    private View getView(RichFieldItem item){

        switch (item.getType()){
            case CONSTANTES_VIEW.Int      :
            case CONSTANTES_VIEW.Double   :
            case CONSTANTES_VIEW.Number   : return getChampsEditText(item, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            case CONSTANTES_VIEW.Email    :
            case CONSTANTES_VIEW.String   :
            case CONSTANTES_VIEW.Text     : return getChampsEditText(item, InputType.TYPE_CLASS_TEXT);
            case CONSTANTES_VIEW.Password : return getChampsEditText(item, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            case CONSTANTES_VIEW.Phone    : return getChampsEditText(item, InputType.TYPE_CLASS_PHONE);
            case CONSTANTES_VIEW.Date     : return getChampsDate(item);
            case CONSTANTES_VIEW.CheckBox : return getChampsCheckbox(item);
            case CONSTANTES_VIEW.Label    : return getChampsLabel(item);
            case CONSTANTES_VIEW.Radio    : return getChampsRadio(item);
            case CONSTANTES_VIEW.ComboBox :
            case CONSTANTES_VIEW.Select   : return getChampsSelect(item);


            default: return  null;
        }
    }


    /**  **/
    private static View getChampsEditText(RichFieldItem item, int inputType) {
        View rowView = inflater.inflate(R.layout.input_text, null,true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById(R.id.textView);
        editText = rowView.findViewById(R.id.editText);

        textView.setId(View.NO_ID);
        textView.setText(item.getLabel());
        if(item.getColor() != 0){
            textView.setTextColor(item.getColor());
            textView.setTextColor(item.getColor());
        }

        try {
            int nextValue = Sequence.nextValue();

            editText.setId(nextValue);
            item.setId(nextValue);
            editText.setHint("Saisir "+ item.getLabel());
            editText.setTag(item.getLabel());
            editText.setInputType(inputType);
        } catch (Exception e) {
            Log.e("getForm Error", e.toString());
        }

        return rowView;
    }

    /**  **/
    private static View getChampsCheckbox(RichFieldItem item) {

        View rowView = inflater.inflate((item.getTemplate() != 0) ? item.getTemplate() : R.layout.input_checkbox, null,true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById(R.id.textView);
        checkBox = rowView.findViewById(R.id.checkBox);

        textView.setId(View.NO_ID);
        textView.setText(item.getLabel());
        if(item.getColor() != 0){
            textView.setTextColor(item.getColor());
            textView.setTextColor(item.getColor());
        }

        try {
            int nextValue = Sequence.nextValue();

            checkBox.setId(nextValue);
            item.setId(nextValue);
            checkBox.setTag(item.getLabel());
        } catch (Exception e) {
            Log.e("getForm Error", e.toString());
        }

        return rowView;
    }

    /**  **/
    private static View getChampsLabel(RichFieldItem item) {

        View rowView = inflater.inflate((item.getTemplate() != 0) ? item.getTemplate() : R.layout.label_text, null,true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById(R.id.textView);
        TextView editText = rowView.findViewById(R.id.editText);

        textView.setId(View.NO_ID);
        textView.setText(item.getLabel());
        if(item.getColor() != 0){
            textView.setTextColor(item.getColor());
            textView.setTextColor(item.getColor());
        }

        try {
            int nextValue = Sequence.nextValue();

            editText.setId(nextValue);
            item.setId(nextValue);
            editText.setText(item.getValue());
            editText.setTag(item.getLabel());
        } catch (Exception e) {
            Log.e("getForm Error", e.toString());
        }

        return rowView;
    }

    /**  **/
    private static View getChampsDate(final RichFieldItem item) {

        calendar = Calendar.getInstance();
        Year     = calendar.get(Calendar.YEAR) ;
        Month    = calendar.get(Calendar.MONTH);
        Day      = calendar.get(Calendar.DAY_OF_MONTH);


        final View rowView = inflater.inflate((item.getTemplate() != 0) ? item.getTemplate() : R.layout.input_date, null,true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById(R.id.textView);
        editText = rowView.findViewById(R.id.editText);
        editText.setText(Day + "/" + (Month + 1) + "/" + Year);
        textView.setId(View.NO_ID);
        textView.setText(item.getLabel());
        if(item.getColor() != 0){
            textView.setTextColor(item.getColor());
            textView.setTextColor(item.getColor());
        }

        try {
            int nextValue = Sequence.nextValue();

            editText.setId(nextValue);
            item.setId(nextValue);
            editText.setTag(item.getLabel());

            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus)
                        datePicker(rowView, item.getId());
                }
            });

        } catch (Exception e) {
            Log.e("getForm Error", e.toString());
        }

        return rowView;
    }

    /**  **/
    private static View getChampsSelect(RichFieldItem item) {
        View rowView = inflater.inflate((item.getTemplate() != 0) ? item.getTemplate() : R.layout.input_select, null,true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById(R.id.textView);
        Spinner editText = rowView.findViewById(R.id.editText);

        textView.setId(View.NO_ID);
        textView.setText(item.getLabel());
        if(item.getColor() != 0){
            textView.setTextColor(item.getColor());
            textView.setTextColor(item.getColor());
        }

        try {
            int nextValue = Sequence.nextValue();

            editText.setId(nextValue);
            item.setId(nextValue);
            editText.setTag(item.getLabel());

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context,   android.R.layout.simple_spinner_item, item.getListSelect());
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            editText.setAdapter(spinnerArrayAdapter);

        } catch (Exception e) {
            Log.e("getForm Error", e.toString());
        }

        return rowView;
    }

    /**  **/
    private static View getChampsRadio(RichFieldItem item) {

        View rowView = inflater.inflate((item.getTemplate() != 0) ? item.getTemplate() : R.layout.input_radio, null,true);
        RadioGroup group_radio = rowView.findViewById(R.id.group_radio);

        boolean active = true;
        for (String val : item.getListRadio()){
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(val);

            if(item.getColor() != 0)
                radioButton.setTextColor(item.getColor());

            if(active){
                radioButton.setChecked(true);
                active = false;
            }

            try {
                radioButton.setId(Sequence.nextValue());
            } catch (Exception e) {
                Log.e("getForm Error", e.toString());
            }
            group_radio.addView(radioButton);
        }


        try {
            int nextValue = Sequence.nextValue();
            rowView.setId(nextValue);
            item.setId(nextValue);

        } catch (Exception e) {
            Log.e("getForm Error", e.toString());
        }


        return rowView;
    }

    /**  **/
    private static void datePicker(final View viewById, final int id) {
        final DatePickerFragmentDialog datePickerFragmentDialog = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {

            public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {

                ((TextView) viewById.findViewById(id)).setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        },Year, Month, Day);


        datePickerFragmentDialog.show(context.getSupportFragmentManager(), null);
        datePickerFragmentDialog.setMaxDate(System.currentTimeMillis());
        datePickerFragmentDialog.setYearRange(1900,Year);
        datePickerFragmentDialog.setCancelColor(context.getResources().getColor(R.color.vertKaki));
        datePickerFragmentDialog.setOkColor(context.getResources().getColor(R.color.colorPrimary));
        datePickerFragmentDialog.setAccentColor(context.getResources().getColor(R.color.vertKaki));
        datePickerFragmentDialog.setOkText(context.getResources().getString(R.string.ok_dob));
        datePickerFragmentDialog.setCancelText(context.getResources().getString(R.string.cancel_dob));

    }

    /**  **/
    public static void done(){
        String value;

        for(int i = 0; i < listForms.size(); i++){
            switch (listForms.get(i).getType()){

                case CONSTANTES_VIEW.Int      :
                case CONSTANTES_VIEW.Double   :
                case CONSTANTES_VIEW.Number   :
                case CONSTANTES_VIEW.Email    :
                case CONSTANTES_VIEW.String   :
                case CONSTANTES_VIEW.Label    :
                case CONSTANTES_VIEW.Text     :
                case CONSTANTES_VIEW.Password :
                case CONSTANTES_VIEW.Phone    :
                case CONSTANTES_VIEW.Date     : value = ((TextView)view.findViewById(listForms.get(i).getId())).getText().toString(); break;
                case CONSTANTES_VIEW.Select   : value = ((Spinner)view.findViewById(listForms.get(i).getId())).getSelectedItem().toString(); break;

                case CONSTANTES_VIEW.CheckBox : value = ((CheckBox)view.findViewById(listForms.get(i).getId())).isChecked()? "true" : "false"; break;
                case CONSTANTES_VIEW.Radio    : RadioGroup radioGroup = view.findViewById(listForms.get(i).getId());
                                                int idx = radioGroup.getCheckedRadioButtonId();
                                                value   =  ((RadioButton)radioGroup.findViewById(idx)).getText().toString(); break;
                default: value = null;
            }
            //Log.e("Liste bug", "done: "+i );
            listForms.get(i).setValue(value);
        }
    }
}