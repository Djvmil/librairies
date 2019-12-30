package com.suntelecoms.djamil.dynamic_form;
/**
 *   Djvmil 19/12/2020
 **/

import android.content.Context;
import android.text.InputType;
import android.util.Log;
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
import com.suntelecoms.djamil.dynamic_form.models.IOFieldsItem;
import com.suntelecoms.djamil.dynamic_form.models.Sequence;

import java.util.ArrayList;
import java.util.Calendar;

public class FormDynamic {

    private static TextView textView;
    private static EditText  editText;
    private static Context context;
    private static ArrayList<IOFieldsItem> listForms;
    private static View view;
    private static LayoutInflater inflater;

    private static int Year, Month, Day ;


    private static volatile FormDynamic singleton = null;

    /**   **/
    private FormDynamic(Context context){
        FormDynamic.context = context;
        inflater =  LayoutInflater.from(context);
    }


    /**   **/
    public static FormDynamic with(Context context) {
        if (singleton == null) {
            synchronized (FormDynamic.class) {
                if (singleton == null) {
                    singleton = new FormDynamic(context);
                }
            }
        }
        return singleton;
    }


    /** Cette methode charge le formulaire en fonction de la liste de type RichField fourni **/
    public void loadForm(LinearLayout layout, ArrayList<IOFieldsItem> listForm){
        view = layout;
        listForms = listForm;
        layout.setOrientation(LinearLayout.VERTICAL);

       for(IOFieldsItem item : listForm){
           View rowView = getView(item);
           if(rowView == null)
               throw new IllegalArgumentException(item.getLabel()+" must not be null");

           layout.addView(rowView);
       }

    }

    /**  **/
    @SuppressWarnings("UnusedDeclaration") // Public API.
    public static class Builder {

        /**  **/
        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            Context context1 = context.getApplicationContext();
        }
    }

    /**  **/
    private View getView(IOFieldsItem item){

        switch (item.getType()){
            case ConstantesView.Int      :
            case ConstantesView.Double   :
            case ConstantesView.Number   : return getChampsEditText(item, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            case ConstantesView.Email    :
            case ConstantesView.String   :
            case ConstantesView.Text     : return getChampsEditText(item, InputType.TYPE_CLASS_TEXT);
            case ConstantesView.Password : return getChampsEditText(item, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            case ConstantesView.Phone    : return getChampsEditText(item, InputType.TYPE_CLASS_PHONE);
            case ConstantesView.Date     : return getChampsDate(item);
            case ConstantesView.CheckBox : return getChampsCheckbox(item);
            case ConstantesView.Label    : return getChampsLabel(item);
            case ConstantesView.Radio    : return getChampsRadio(item);
            case ConstantesView.Spinner  : return getChampsSelect(item);

            default: return  null;
        }
    }


    /**  **/
    private static View getChampsEditText(IOFieldsItem item, int inputType) {
        View rowView = inflater.inflate((item.getTemplate() != 0) ? item.getTemplate() : com.suntelecoms.djamil.dynamic_form.R.layout.input_text, null,true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById(com.suntelecoms.djamil.dynamic_form.R.id.textView);
        editText = rowView.findViewById(com.suntelecoms.djamil.dynamic_form.R.id.editText);

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
    private static View getChampsCheckbox(IOFieldsItem item) {

        View rowView = inflater.inflate((item.getTemplate() != 0) ? item.getTemplate() : com.suntelecoms.djamil.dynamic_form.R.layout.input_checkbox, null,true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById(com.suntelecoms.djamil.dynamic_form.R.id.textView);
        CheckBox checkBox = rowView.findViewById(com.suntelecoms.djamil.dynamic_form.R.id.checkBox);

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
    private static View getChampsLabel(IOFieldsItem item) {

        View rowView = inflater.inflate((item.getTemplate() != 0) ? item.getTemplate() : com.suntelecoms.djamil.dynamic_form.R.layout.label_text, null,true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById(com.suntelecoms.djamil.dynamic_form.R.id.textView);
        TextView editText = rowView.findViewById(com.suntelecoms.djamil.dynamic_form.R.id.editText);

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
    private static View getChampsDate(final IOFieldsItem item) {

        Calendar calendar = Calendar.getInstance();
        Year     = calendar.get(Calendar.YEAR) ;
        Month    = calendar.get(Calendar.MONTH);
        Day      = calendar.get(Calendar.DAY_OF_MONTH);


        final View rowView = inflater.inflate((item.getTemplate() != 0) ? item.getTemplate() : com.suntelecoms.djamil.dynamic_form.R.layout.input_date, null,true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById(com.suntelecoms.djamil.dynamic_form.R.id.textView);
        editText = rowView.findViewById(com.suntelecoms.djamil.dynamic_form.R.id.editText);
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
    private static View getChampsSelect(IOFieldsItem item) {
        View rowView = inflater.inflate((item.getTemplate() != 0) ? item.getTemplate() : com.suntelecoms.djamil.dynamic_form.R.layout.input_select, null,true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById(com.suntelecoms.djamil.dynamic_form.R.id.textView);
        Spinner editText = rowView.findViewById(com.suntelecoms.djamil.dynamic_form.R.id.editText);

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
    private static View getChampsRadio(IOFieldsItem item) {

        View rowView = inflater.inflate((item.getTemplate() != 0) ? item.getTemplate() : com.suntelecoms.djamil.dynamic_form.R.layout.input_radio, null,true);
        RadioGroup group_radio = rowView.findViewById(com.suntelecoms.djamil.dynamic_form.R.id.group_radio);

        boolean active = true;
        if(item.getListCheckBox() != null)
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


        datePickerFragmentDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), null);
        datePickerFragmentDialog.setMaxDate(System.currentTimeMillis());
        datePickerFragmentDialog.setYearRange(1900,Year);
        datePickerFragmentDialog.setCancelColor(context.getResources().getColor(com.suntelecoms.djamil.dynamic_form.R.color.vertKaki));
        datePickerFragmentDialog.setOkColor(context.getResources().getColor(com.suntelecoms.djamil.dynamic_form.R.color.colorPrimary));
        datePickerFragmentDialog.setAccentColor(context.getResources().getColor(com.suntelecoms.djamil.dynamic_form.R.color.vertKaki));
        datePickerFragmentDialog.setOkText(context.getResources().getString(com.suntelecoms.djamil.dynamic_form.R.string.ok_dob));
        datePickerFragmentDialog.setCancelText(context.getResources().getString(com.suntelecoms.djamil.dynamic_form.R.string.cancel_dob));

    }

    /**  **/
    public static void done(){
        String value;

        for(int i = 0; i < listForms.size(); i++){
            switch (listForms.get(i).getType()){

                case ConstantesView.Int      :
                case ConstantesView.Double   :
                case ConstantesView.Number   :
                case ConstantesView.Email    :
                case ConstantesView.String   :
                case ConstantesView.Label    :
                case ConstantesView.Text     :
                case ConstantesView.Password :
                case ConstantesView.Phone    :
                case ConstantesView.Date     : value = ((TextView)view.findViewById(listForms.get(i).getId())).getText().toString(); break;
                case ConstantesView.Spinner: value = ((Spinner)view.findViewById(listForms.get(i).getId())).getSelectedItem().toString(); break;

                case ConstantesView.CheckBox : value = ((CheckBox)view.findViewById(listForms.get(i).getId())).isChecked()? "true" : "false"; break;
                case ConstantesView.Radio    : RadioGroup radioGroup = view.findViewById(listForms.get(i).getId());
                                                int idx = radioGroup.getCheckedRadioButtonId();
                                                value   =  ((RadioButton)radioGroup.findViewById(idx)).getText().toString(); break;
                default: value = null;
            }
            //Log.e("Liste bug", "done: "+i );
            listForms.get(i).setValue(value);
        }
    }
}