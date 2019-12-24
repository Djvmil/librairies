package com.suntelecoms.td.dynamic_form;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;
import com.suntelecoms.td.dynamic_form.models.FieldObject;
import com.suntelecoms.td.dynamic_form.models.RichFieldItem;
import com.suntelecoms.td.dynamic_form.models.Sequence;

import java.util.ArrayList;
import java.util.Calendar;

public class FormDynamic {

    private static TextView textView;
    private static EditText  editText;
    private static CheckBox checkBox;
    private static AppCompatActivity context;
    private static ArrayList<FieldObject> fieldObjects;
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

    /**   **/
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

    /** Liste  **/
    public void loadValuesInto(ArrayList<FieldObject> fieldObject){
        fieldObjects = fieldObject;
    }

    /** Fluent API for creating {@link FormDynamic} instances. */
    @SuppressWarnings("UnusedDeclaration") // Public API.
    public static class Builder {
        private final Context context;

        /**
         * Start building a new {@link FormDynamic} instance.
         */
        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }
    }


    public View getView(RichFieldItem item){

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

            //case CONSTANTES_VIEW.Time:     return InputType.TYPE_CLASS_NUMBER;
            //case CONSTANTES_VIEW.Select:   return InputType.TYPE_CLASS_NUMBER;
            //case CONSTANTES_VIEW.ComboBox: return InputType.TYPE_CLASS_NUMBER;

            default: return  null;
        }
    }



    public static View getChampsEditText(RichFieldItem item, int inputType) {
        View rowView = inflater.inflate(R.layout.input_text, null,true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById(R.id.textView);
        editText = rowView.findViewById(R.id.editText);

        textView.setId(View.NO_ID);
        textView.setText(item.getLabel());

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

    public static View getChampsCheckbox(RichFieldItem item) {

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setId(View.NO_ID);

        ArrayList<String> values = new ArrayList<>();
        values.add("value");
        values.add("value 1");
        values.add("value 2");
        values.add("value 3");

        for (String val : values){
            View rowView = inflater.inflate(R.layout.input_checkbox, null,true);
            rowView.setId(View.NO_ID);

            textView = rowView.findViewById(R.id.textView);
            checkBox = rowView.findViewById(R.id.checkBox);

            textView.setId(View.NO_ID);
            textView.setText(val);

            try {
                int nextValue = Sequence.nextValue();

                checkBox.setId(nextValue);
                item.setId(nextValue);
                checkBox.setTag(item.getLabel());
            } catch (Exception e) {
                Log.e("getForm Error", e.toString());
            }

            linearLayout.addView(rowView);
        }


        return linearLayout;
    }

    public static View getChampsLabel(RichFieldItem item) {
        View rowView = inflater.inflate(R.layout.label_text, null,true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById(R.id.textView);
        editText = rowView.findViewById(R.id.editText);

        textView.setId(View.NO_ID);
        textView.setText(item.getLabel());

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

    public static View getChampsDate(final RichFieldItem item) {

        calendar = Calendar.getInstance();
        Year     = calendar.get(Calendar.YEAR) ;
        Month    = calendar.get(Calendar.MONTH);
        Day      = calendar.get(Calendar.DAY_OF_MONTH);


        final View rowView = inflater.inflate(R.layout.input_date, null,true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById(R.id.textView);
        editText = rowView.findViewById(R.id.editText);
        editText.setText(Day + "/" + (Month + 1) + "/" + Year);
        textView.setId(View.NO_ID);
        textView.setText(item.getLabel());

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

    public static View getChampsSelect(RichFieldItem item, int inputType) {
        View rowView = inflater.inflate(R.layout.label_text, null,true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById(R.id.textView);
        editText = rowView.findViewById(R.id.editText);

        textView.setId(View.NO_ID);
        textView.setText(item.getLabel());

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


    public static View getChampsRadio(RichFieldItem item) {

        ArrayList<String> values = new ArrayList<>();
        values.add("value");
        values.add("value 1");
        values.add("value 2");
        values.add("value 3");

        View rowView = inflater.inflate(R.layout.input_radio, null,true);
        RadioGroup group_radio = rowView.findViewById(R.id.group_radio);

        boolean active = true;
        for (String val : values){
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(val);
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


    public static void done(){
        String value;

        for(RichFieldItem item : listForms){
            switch (item.getType()){

                case CONSTANTES_VIEW.Int      :
                case CONSTANTES_VIEW.Double   :
                case CONSTANTES_VIEW.Number   :
                case CONSTANTES_VIEW.Email    :
                case CONSTANTES_VIEW.String   :
                case CONSTANTES_VIEW.Label    :
                case CONSTANTES_VIEW.Text     :
                case CONSTANTES_VIEW.Password :
                case CONSTANTES_VIEW.Phone    :
                case CONSTANTES_VIEW.Date     : value = ((TextView)view.findViewById(item.getId())).getText().toString(); break;
                case CONSTANTES_VIEW.CheckBox : value = ((CheckBox)view.findViewById(item.getId())).isChecked()? "true" : "false"; break;
                case CONSTANTES_VIEW.Radio    : RadioGroup radioGroup = view.findViewById(item.getId());
                                                int idx = radioGroup.getCheckedRadioButtonId();
                                                value  =  ((RadioButton)radioGroup.findViewById(idx)).getText().toString();  break;
                default: value = null;
            }

            fieldObjects.add(new FieldObject(item.getField(), value));
            item.setValue(value);

        }
    }
}