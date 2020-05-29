package com.djamil.dynamic_form;

/**
 * Created by Djvmil_ on 2019-12-10
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup; ;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.hbb20.CountryCodePicker;
import com.djamil.dynamic_form.exceptions.EmptyValueException;
import com.djamil.dynamic_form.interfaces.OnBackClickListener;
import com.djamil.dynamic_form.interfaces.OnCancelClickListener;
import com.djamil.dynamic_form.interfaces.OnDoneClickListener;
import com.djamil.dynamic_form.interfaces.OnClickDynamicFormListener;
import com.djamil.dynamic_form.interfaces.OnNextClickListener;
import com.djamil.dynamic_form.models.ItemDF;
import com.djamil.dynamic_form.models.IOFieldsItem;
import com.djamil.dynamic_form.models.Sequence;
import com.djamil.dynamic_form.utils.Utils;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class DynamicForm extends NestedScrollView {
    private static final String TAG = "FormDynamic";

    //Container
    private NestedScrollView rootLayout;
    private RelativeLayout containerForm;
    private LinearLayout currentFormContainer;
    private LayoutInflater inflater;

    private TextView textView;
    private Context context;
    private ArrayList<IOFieldsItem> listForms;


    //Attributes
    private int nbFieldPerPage;
    private String typeForm;
    private int colorField;
    private float paddingField;
    private float marginField;

    //Listener
    private OnDoneClickListener onDoneClickListener;
    private OnNextClickListener onNextClickListener;
    private OnBackClickListener onBackClickListener;
    private OnCancelClickListener onCancelClickListener;
    private OnClickDynamicFormListener onClickDynamicFormListener;

    //current idView
    private int currentIdView;
    private static final int ZERO = 0;

    //date
    private static int Year, Month, Day ;

    //list page
    private ArrayList<LinearLayout> pageList;

    //current page
    private int numCurrentPage = ZERO;

    private Boolean isShowing = false;

    /**
     * DynamicForm
     * @param ctx context
     * @param attrs attributes
     */
    public DynamicForm(Context ctx, @Nullable AttributeSet attrs) {
        super(ctx, attrs);
        context  = ctx;
        inflater =  LayoutInflater.from(ctx);
        init();

        TypedArray attr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DynamicForm, 0, 0);

        try {
            nbFieldPerPage = attr.getInteger(R.styleable.DynamicForm_nb_field_per_page, ZERO);
            typeForm       = attr.getString(R.styleable.DynamicForm_type_form);
            colorField     = attr.getColor(R.styleable.DynamicForm_color_field, ZERO);
            paddingField   = attr.getDimension(R.styleable.DynamicForm_padding_field, ZERO);
            marginField    = attr.getDimension(R.styleable.DynamicForm_margin_field, ZERO);
        } finally {
            attr.recycle();
        }

        // Throw an exception if required attributes are not set
        if (nbFieldPerPage != ZERO) {
            //throw new RuntimeException("No title provided");
        }
        else if (typeForm != null) {
            //throw new RuntimeException("No subtitle provided");
        }
        else if (colorField != ZERO) {
            //throw new RuntimeException("No subtitle provided");
        }
        else if (paddingField != ZERO) {
           // throw new RuntimeException("No subtitle provided");
        }
        else if (marginField != ZERO) {
            //throw new RuntimeException("No subtitle provided");
        }
    }

    /**
     * init
     */
    public void init(){
        //Load RootView from xml
        inflater.inflate(R.layout.container_dynamic_form, this, true);
        rootLayout    = findViewById(R.id.container);
        containerForm = findViewById(R.id.body);
    }

    /**
     * loadForm
     * Cette methode charge le formulaire en fonction de la liste de type IOFieldsItem fourni
     * @param listForm liste InputOutputField (IOFieldsItem) fourni
     */
    public void loadForm(ArrayList<IOFieldsItem> listForm){
        listForms = listForm;
        pageList = new ArrayList<>();
        Collections.sort(listForm);

        int j = ZERO;

        for(int i = ZERO; i < listForm.size(); i++ ){
            if (j == (nbFieldPerPage - 1)) j = ZERO;

            else{
                if (j == ZERO){
                    Log.i(TAG, "loadForm: "+nbFieldPerPage );

                    int id = Sequence.nextValue();
                    currentFormContainer = new LinearLayout(context);
                    currentFormContainer.setId(id);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //params.addRule(RelativeLayout.ABOVE, R.id.buttons);
                    currentFormContainer.setLayoutParams(params);
                    currentFormContainer.setOrientation(LinearLayout.VERTICAL);
                    containerForm.addView(currentFormContainer);

                    pageList.add(currentFormContainer);
                    currentFormContainer.setVisibility(INVISIBLE);

                }
                j++;
            }

            View rowView = getView(listForm.get(i));
            if(rowView == null)
                Log.e(TAG, "loadForm: "+listForm.get(i).getLabel()+" ne peut pas etre null" );
                //throw new IllegalArgumentException(listForm.get(i).getLabel()+" ne peut pas etre null");
        }

        pageList.get(numCurrentPage).setVisibility(VISIBLE);
        initButtons();
    }

    /**
     * initButtons
     * Ajout de listener dans les button
     */
    private void initButtons(){

        /*
        View buttons = inflater.inflate(R.layout.buttons_bar, this, true);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, pageList.get(0).getId());
        buttons.setLayoutParams(params);

        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, pageList.get(0).getId());
        findViewById(R.id.footer).setLayoutParams(params);
        */

        findViewById(R.id.done_btn).setOnClickListener(onClickListener());
        findViewById(R.id.next_btn).setOnClickListener(onClickListener());
        findViewById(R.id.back_btn).setOnClickListener(onClickListener());
        findViewById(R.id.cancel_btn).setOnClickListener(onClickListener());
        showButtons();
    }

    /**
     * OnClickListener
     * @return listener
     */
    private OnClickListener onClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onClickDynamicFormListener != null)
                    onClickDynamicFormListener.onClick(v);

                if (v.getId() == R.id.done_btn)
                    doneClickListener(v);

                else if(v.getId() == R.id.next_btn)
                    nextClickListener(v);

                else if(v.getId() == R.id.back_btn)
                    backClickListener(v);

                else if(v.getId() == R.id.cancel_btn)
                    cancelClickListener(v);

                showButtons();
            }
        };
    }

    /**
     * Affiche et cache les button en fonction de la page ou on se trouve
     */
    private void showButtons(){
        Log.i(TAG, "----------------- PAGE: "+numCurrentPage+" -----------------" );
        if (pageList.size() == 1){
            findViewById(R.id.back_btn).setVisibility(GONE);
            findViewById(R.id.cancel_btn).setVisibility(VISIBLE);
            findViewById(R.id.next_btn).setVisibility(GONE);
            findViewById(R.id.done_btn).setVisibility(VISIBLE);
            //Log.e(TAG, "----------------- showButtons: 1 -----------------" );

        } else if (numCurrentPage == 0){
            findViewById(R.id.back_btn).setVisibility(GONE);
            findViewById(R.id.cancel_btn).setVisibility(VISIBLE);
            findViewById(R.id.next_btn).setVisibility(VISIBLE);
            findViewById(R.id.done_btn).setVisibility(GONE);
            //Log.e(TAG, "----------------- showButtons: 2 -----------------" );

        } else if (numCurrentPage >= pageList.size() - 1){
            findViewById(R.id.back_btn).setVisibility(VISIBLE);
            findViewById(R.id.cancel_btn).setVisibility(GONE);
            findViewById(R.id.next_btn).setVisibility(GONE);
            findViewById(R.id.done_btn).setVisibility(VISIBLE);
            //Log.e(TAG, "----------------- showButtons: 3 -----------------" );

        }else{
            findViewById(R.id.back_btn).setVisibility(VISIBLE);
            findViewById(R.id.cancel_btn).setVisibility(GONE);
            findViewById(R.id.next_btn).setVisibility(VISIBLE);
            findViewById(R.id.done_btn).setVisibility(GONE);
            //Log.e(TAG, "----------------- showButtons: 4 -----------------" );

        }
    }

    /**
     * cette methode retourne un champ en fonction de l'item(IOFieldsItem) qu'on la fourni
     * getView
     * @param item IOFieldsItem
     * @return champs view
     */
    private View getView(IOFieldsItem item){

        switch (item.getType()){
            case INPUT_TYPE_DF.Separator : return getSeparator(item);
            case INPUT_TYPE_DF.Date      : return getChampsDate(item);
            case INPUT_TYPE_DF.CheckBox  : return getChampsCheckbox(item);
            case INPUT_TYPE_DF.Select    : return getChampsSelect(item);
            case INPUT_TYPE_DF.Radio     : return getChampsRadio(item);
            case INPUT_TYPE_DF.Country   : return getChampsCountry(item);
            case INPUT_TYPE_DF.Double    :
            case INPUT_TYPE_DF.Int       :
            case INPUT_TYPE_DF.Integer   :
            case INPUT_TYPE_DF.Number    : return getChampsEditText(item, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            case INPUT_TYPE_DF.Email     :
            case INPUT_TYPE_DF.String    :
            case INPUT_TYPE_DF.Text      : return getChampsEditText(item, InputType.TYPE_CLASS_TEXT);
            case INPUT_TYPE_DF.Password  : return getChampsEditText(item, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            case INPUT_TYPE_DF.Phone     : return getChampsPhone(item);
            case INPUT_TYPE_DF.Label     : return getChampsLabel(item);

            default: return getChampsEditText(item, InputType.TYPE_CLASS_TEXT);
        }
    }

    /**
     * getChampsLabel
     * @param item IOFieldsItem
     * @return champs textView
     */
    private View getChampsLabel(IOFieldsItem item) {

        View rowView = inflater.inflate((item.getTemplate() != ZERO) ? item.getTemplate() : R.layout.label_text, currentFormContainer, true);
        rowView.setId(NO_ID);

        textView = rowView.findViewById( R.id.textView);
        TextView editText = rowView.findViewById( R.id.editText);

        textView.setId(NO_ID);
        textView.setText(item.getLabel());
        if(item.getColor() != ZERO)
            textView.setTextColor(item.getColor());

        try {
            currentIdView = Sequence.nextValue();
            editText.setId(currentIdView);
            item.setIdView(currentIdView);

            String str = item.getValue();

            if (item.isFormatter())
                str = Utils.getNumberFloatFormat(Float.valueOf(str.replaceAll("\\s", "")));

            if (item.isMoney())
                str = String.format("%s %s", str, item.getDevise());

            editText.setText(str);
            editText.setTag(item.getLabel());
        } catch (Exception e) {
            Log.e("getForm Error", e.toString());
        }

        if (item.getShouldBeShown() != null && !item.getShouldBeShown())
            rowView.setVisibility(View.GONE);
        else
            item.setShouldBeShown(true);

        return rowView;
    }

    /**
     * getChampsEditText
     * @param item IOFieldsItem
     * @return champs editText
     */
    private View getChampsEditText(IOFieldsItem item, int inputType) {
        View rowView = inflater.inflate((item.getTemplate() != ZERO) ? item.getTemplate() :  R.layout.input_text, currentFormContainer, true);
        rowView.setId(NO_ID);


        textView = rowView.findViewById( R.id.textView);
        EditText editText = rowView.findViewById(R.id.editText);

        textView.setId(NO_ID);
        textView.setText(item.getLabel());

        if(item.getColor() != ZERO)
            textView.setTextColor(item.getColor());

        textView.setTextColor(context.getResources().getColor(R.color.black_semi_transparent));
        try {
            currentIdView = Sequence.nextValue();

            editText.setId(currentIdView);
            item.setIdView(currentIdView);
            editText.setHint("Saisir "+ item.getLabel());
            editText.setTag(item.getLabel());
            editText.setInputType(inputType);

            try{
                Log.e("getChampsEditText: ", item.getField());
            }catch (Exception e){
                e.printStackTrace();
            }

        } catch (Exception e) {
            Log.e("getForm Error", e.toString());
        }

        return rowView;
    }

    /**
     * getChampsCountry
     * @param item IOFieldsItem
     * @return champs CountryCodePicker
     */
    private View getChampsCountry(IOFieldsItem item) {
        View rowView = inflater.inflate((item.getTemplate() != ZERO) ? item.getTemplate() :  R.layout.input_country, currentFormContainer, true);
        rowView.setId(NO_ID);

        TextView textView = rowView.findViewById(R.id.textView);
        CountryCodePicker country = rowView.findViewById(R.id.country);

        textView.setId(NO_ID);
        textView.setText(item.getLabel());

        if(item.getColor() != ZERO)
            country.setContentColor(item.getColor());

        try {
            currentIdView = Sequence.nextValue();
            country.setId(currentIdView);
            item.setIdView(currentIdView);

            if (!item.getShouldBeShown())
                rowView.setVisibility(View.GONE);
        }catch (Exception e){
            Log.e("getForm Error", e.toString());
            e.printStackTrace();

        }
        return rowView;
    }

    /**
     * getChampsPhone
     * @param item IOFieldsItem
     * @return champs CountryCodePicker
     */
    private View getChampsPhone(IOFieldsItem item) {
        View rowView = inflater.inflate((item.getTemplate() != ZERO) ? item.getTemplate() :  R.layout.input_phone, currentFormContainer, true);
        rowView.setId(NO_ID);

        CountryCodePicker textView = rowView.findViewById( R.id.indicatif);
        EditText editText          = rowView.findViewById(R.id.editText);

        try {
            currentIdView = Sequence.nextValue();

            textView.setId(currentIdView);
            item.setIndicatif(currentIdView);
        }catch (Exception e){
            Log.e("getForm Error", e.toString());

        }
        item.setIsRequired(true);
        try {
            currentIdView = Sequence.nextValue();

            editText.setId(currentIdView);
            item.setIdView(currentIdView);
            editText.setTag(item.getLabel());
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            try{
                Log.e("getChampsPhone: ", item.getField());
            }catch (Exception e){
                e.printStackTrace();
            }

            if (!item.getShouldBeShown())
                rowView.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e("getForm Error", e.toString());
        }

        return rowView;
    }

    /**
     * getChampsSelect
     * @param item IOFieldsItem
     * @return champs spinner
     */
    private View getChampsSelect(IOFieldsItem item) {
        View rowView = inflater.inflate((item.getTemplate() != ZERO) ? item.getTemplate() : R.layout.input_select, currentFormContainer, true);
        rowView.setId(NO_ID);

        textView         = rowView.findViewById(R.id.textView);
        Spinner editText = rowView.findViewById(R.id.editText);

        textView.setId(NO_ID);
        textView.setText(item.getLabel());
        if(item.getColor() != ZERO){
            textView.setTextColor(item.getColor());
        }

        try {
            int nextValue = Sequence.nextValue();
            editText.setId(nextValue);
            item.setIdView(nextValue);
            editText.setTag(item.getLabel());

            SpinAdapter spinnerArrayAdapter = new SpinAdapter(context, android.R.layout.simple_spinner_item, item.getListItemDF());
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            editText.setAdapter(spinnerArrayAdapter);

            /*
            editText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
                    //Toast.makeText(context, ""+ ((Formules)editText.getSelectedItem()).getPrix(), Toast.LENGTH_SHORT).show();

                    try {
                        if (foundAmount){
                            String mtn = ((Formules)editText.getSelectedItem()).getPrix();
                            if (view.findViewById(idAmount) != null)
                                ((TextView)view.findViewById(idAmount)).setText(String.format("%s XOF", FonctionUtils.getNumberFloatFormat(Float.valueOf(mtn))));
                        }
                        else
                            for (IOFieldsItem montant : listForms){
                                if (montant.getField() != null && montant.getField().equals("montant")){
                                    idAmount = montant.getId();
                                    foundAmount = true;
                                }
                            }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });*/

            if (!item.getShouldBeShown())
                rowView.setVisibility(View.GONE);

        } catch (Exception e) {
            Log.e("getForm Error", e.toString());
        }

        return rowView;
    }

    /**
     * getChampsCheckbox
     * @param item IOFieldsItem
     * @return champs checkBox
     */
    private View getChampsCheckbox(IOFieldsItem item) {
        View rowView = null;

        for (ItemDF itemDF : item.getListItemDF()){

            rowView = inflater.inflate((item.getTemplate() != 0) ? item.getTemplate() : R.layout.input_checkbox, null, true);
            rowView.setId(NO_ID);

            textView          = rowView.findViewById(R.id.textView);
            CheckBox checkBox = rowView.findViewById(R.id.checkBox);

            textView.setId(NO_ID);
            textView.setText(itemDF.getLabel());
            if(item.getColor() != 0){
                textView.setTextColor(item.getColor());
                textView.setTextColor(item.getColor());
            }

            try {
                int nextValue = Sequence.nextValue();

                checkBox.setId(nextValue);
                itemDF.setIdView(nextValue);

                currentFormContainer.addView(rowView);

            } catch (Exception e) {
                Log.e("getForm Error", e.toString());
            }
        }

        return rowView;
    }

    /**
     * getChampsRadio
     * @param item IOFieldsItem
     * @return champs radio
     */
    private View getChampsRadio(IOFieldsItem item) {

        View rowView = inflater.inflate((item.getTemplate() != 0) ? item.getTemplate() : R.layout.input_radio, currentFormContainer, true);
        RadioGroup group_radio = rowView.findViewById(R.id.group_radio);

        boolean active = true;
        for (ItemDF itemDF : item.getListItemDF()){
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(itemDF.getLabel());

            if(item.getColor() != 0)
                radioButton.setTextColor(item.getColor());

            if(active){
                radioButton.setChecked(true);
                active = false;
            }

            int id = Sequence.nextValue();
            itemDF.setIdView(id);

            try {
                radioButton.setId(id);
            } catch (Exception e) {
                Log.e("getForm Error", e.toString());
            }
            group_radio.addView(radioButton);
        }


        try {
            int nextValue = Sequence.nextValue();
            group_radio.setId(nextValue);
            item.setIdView(nextValue);

        } catch (Exception e) {
            Log.e("getForm Error", e.toString());
        }

        return rowView;
    }

    /**
     * datePicker
     * @param viewById id view
     * @param id id
     * @param colorId color
     */
    private void datePicker(final View viewById, final int id, @ColorRes int colorId) {
        if (!isShowing){
            isShowing = true;
            DatePickerFragmentDialog datePickerFragmentDialog = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {

                public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                    isShowing = false;
                    ((TextView) viewById.findViewById(id)).setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            }, Year, Month, Day);


            if (colorId != 0){
                datePickerFragmentDialog.setCancelColor(context.getResources().getColor(colorId));
                datePickerFragmentDialog.setOkColor(context.getResources().getColor(colorId));
                datePickerFragmentDialog.setAccentColor(context.getResources().getColor(R.color.amdp_accent_color));
            }

            datePickerFragmentDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), null);
            //datePickerFragmentDialog.setMaxDate(System.currentTimeMillis());
            //datePickerFragmentDialog.setYearRange(1900, Year);
            datePickerFragmentDialog.setOkText(context.getResources().getString(R.string.ok_dob));
            datePickerFragmentDialog.setCancelText(context.getResources().getString(R.string.cancel_dob));
            datePickerFragmentDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    isShowing = false;
                }
            });
            datePickerFragmentDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isShowing = false;
                }
            });
        }

    }

    /**
     * getChampsDate
     * @param item IOFieldsItem
     * @return champs date
     */
    private View getChampsDate(final IOFieldsItem item) {

        Calendar calendar = Calendar.getInstance();
        Year     = calendar.get(Calendar.YEAR) ;
        Month    = calendar.get(Calendar.MONTH);
        Day      = calendar.get(Calendar.DAY_OF_MONTH);


        final View rowView = inflater.inflate((item.getTemplate() != 0) ? item.getTemplate() : R.layout.input_date, currentFormContainer, true);
        rowView.setId(NO_ID);

        textView = rowView.findViewById(R.id.textView);
        EditText editText = rowView.findViewById(R.id.editText);
        editText.setText(Day + "/" + (Month + 1) + "/" + Year);
        textView.setId(NO_ID);
        textView.setText(item.getLabel());
        if(item.getColor() != 0){
            textView.setTextColor(item.getColor());
            textView.setTextColor(item.getColor());
        }

        try {
            int nextValue = Sequence.nextValue();

            editText.setId(nextValue);
            item.setIdView(nextValue);
            editText.setTag(item.getLabel());

            /*editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus)
                        datePicker(rowView, item.getId());
                }
            });*/

            editText.setClickable(true);
            editText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePicker(rowView, item.getIdView(), item.getColor());
                }
            });

        } catch (Exception e) {
            Log.e("getForm Error", e.toString());
        }

        return rowView;
    }

    /**
     * getSeparator
     * @param item IOFieldsItem
     * @return view separator
     */
    private View getSeparator(IOFieldsItem item) {

        View rowView = inflater.inflate(R.layout.separator, currentFormContainer, true);
        rowView.setId(NO_ID);
        try {
            currentIdView = Sequence.nextValue();

            rowView.setId(currentIdView);
            item.setIdView(currentIdView);

        } catch (Exception e) {
            Log.e("getForm Error", e.toString());
        }

        return rowView;
    }

    /**
     *
     * done
     * @return list values
     */
    private ArrayList<IOFieldsItem>  done() throws EmptyValueException {
        String value = null;

        for(int i = 0; i < listForms.size(); i++){
            switch (listForms.get(i).getType()){

                case INPUT_TYPE_DF.Country  : value =  ((CountryCodePicker)rootLayout.findViewById(listForms.get(i).getIdView())).getSelectedCountryName();
                    listForms.get(i).setIndicatif(Integer.parseInt(((CountryCodePicker)rootLayout.findViewById(listForms.get(i).getIdView())).getSelectedCountryCode()));
                    listForms.get(i).setPaysAlpha2(((CountryCodePicker)rootLayout.findViewById(listForms.get(i).getIdView())).getSelectedCountryNameCode());
                    break;

                case INPUT_TYPE_DF.Phone    : value = String.format("%s%s",
                        ((CountryCodePicker)rootLayout.findViewById(listForms.get(i).getIndicatif())).getSelectedCountryCode(),
                        ((TextView)rootLayout.findViewById(listForms.get(i).getIdView())).getText().toString());

                    listForms.get(i).setIndicatif(Integer.parseInt(((CountryCodePicker)rootLayout.findViewById(listForms.get(i).getIndicatif())).getSelectedCountryCode()));
                    listForms.get(i).setPaysAlpha2(((CountryCodePicker)rootLayout.findViewById(listForms.get(i).getIndicatif())).getSelectedCountryNameCode());
                    break;

                case INPUT_TYPE_DF.Select   : ItemDF item = (ItemDF)((Spinner)rootLayout.findViewById(listForms.get(i).getIdView())).getSelectedItem();
                    listForms.get(i).setItemDFSelected(item);
                    value = String.valueOf(item.getValue());
                    break;


                case INPUT_TYPE_DF.CheckBox : listForms.get(i).getListItemDFByView(rootLayout);
                    value = "CheckBox";
                    break;

                case INPUT_TYPE_DF.Int      :
                case INPUT_TYPE_DF.Double   :
                case INPUT_TYPE_DF.Number   :
                case INPUT_TYPE_DF.Email    :
                case INPUT_TYPE_DF.String   :
                case INPUT_TYPE_DF.Label    :
                case INPUT_TYPE_DF.Text     :
                case INPUT_TYPE_DF.Password :
                case INPUT_TYPE_DF.Date     :
                    if (rootLayout.findViewById(listForms.get(i).getIdView()) != null )
                        value = ((TextView)rootLayout.findViewById(listForms.get(i).getIdView())).getText().toString(); break;

                case INPUT_TYPE_DF.Radio    : RadioGroup radioGroup = rootLayout.findViewById(listForms.get(i).getIdView());
                    int idView = radioGroup.getCheckedRadioButtonId();
                    value   =  ((RadioButton)radioGroup.findViewById(idView)).getText().toString();
                    listForms.get(i).getItemDFByIdView(idView);
                    break;

                default: value = null;
            }

            if (listForms.get(i).isRequired() && (value == null || value.isEmpty()))
                Log.w(TAG, "Le champ "+listForms.get(i).getLabel()+" \nest obligatoire" );
            //throw new EmptyValueException(listForms.get(i).getLabel());

            listForms.get(i).setValue(value);
        }

        return listForms;
    }

    /**
     * OnDoneClickListener
     * @return click
     */
    public OnDoneClickListener getOnDoneClickListener() {
        return onDoneClickListener;
    }

    /**
     * setOnDoneClickListener
     * @param onDoneClickListener click
     */
    public void setOnDoneClickListener(OnDoneClickListener onDoneClickListener) {
        this.onDoneClickListener = onDoneClickListener;
    }

    /**
     * setOnDoneClickListener
     * @param nameButton nama
     * @param onDoneClickListener click
     */
    public void setOnDoneClickListener(String nameButton, OnDoneClickListener onDoneClickListener) {
        ((TextView)findViewById(R.id.done_btn)).setText(nameButton);
        this.onDoneClickListener = onDoneClickListener;
    }

    /**
     * getOnNextClickListener
     * @return click
     */
    public OnNextClickListener getOnNextClickListener() {
        return onNextClickListener;
    }

    /**
     * setOnNextClickListener
     * @param onNextClickListener click
     */
    public void setOnNextClickListener(OnNextClickListener onNextClickListener) {
        this.onNextClickListener = onNextClickListener;
    }

    /**
     * setOnNextClickListener
     * @param nameButton name
     * @param onNextClickListener click
     */
    public void setOnNextClickListener(String nameButton, OnNextClickListener onNextClickListener) {
        ((TextView)findViewById(R.id.next_btn)).setText(nameButton);
        this.onNextClickListener = onNextClickListener;
    }

    /**
     * getOnBackClickListener
     * @return click
     */
    public OnBackClickListener getOnBackClickListener() {
        return onBackClickListener;
    }

    /**
     * setOnBackClickListener
     * @param onBackClickListener click
     */
    public void setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    /**
     * setOnBackClickListener
     * @param nameButton name
     * @param onBackClickListener click
     */
    public void setOnBackClickListener(String nameButton, OnBackClickListener onBackClickListener) {
        ((TextView)findViewById(R.id.back_btn)).setText(nameButton);
        this.onBackClickListener = onBackClickListener;
    }

    /**
     * getOnCancelClickListener
     * @return click
     */
    public OnCancelClickListener getOnCancelClickListener() {
        return onCancelClickListener;
    }

    /**
     * setOnCancelClickListener
     * @param onCancelClickListener click
     */
    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }

    /**
     * setOnCancelClickListener
     * @param nameButton name
     * @param onCancelClickListener click
     */
    public void setOnCancelClickListener(String nameButton, OnCancelClickListener onCancelClickListener) {
        ((TextView)findViewById(R.id.cancel_btn)).setText(nameButton);
        this.onCancelClickListener = onCancelClickListener;
    }

    /**
     * doneClickListener
     * @param v view
     */
    public void doneClickListener(View v) {
        if (onDoneClickListener != null)
            onDoneClickListener.OnDoneClicked(v, done());

        if (onClickDynamicFormListener != null)
            onClickDynamicFormListener.OnDoneClicked(done());
    }

    /**
     * nextClickListener
     * @param v view
     */
    public void nextClickListener(View v) {
        if ((numCurrentPage + 1) <= (pageList.size() - 1)){
            pageList.get(numCurrentPage).setVisibility(INVISIBLE);
            numCurrentPage ++;
            pageList.get(numCurrentPage).setVisibility(VISIBLE);
            Log.i(TAG, "nextClickListener: page num : "+ numCurrentPage);
        }


        if (onNextClickListener != null)
            onNextClickListener.OnNextClicked(v);

        if (onClickDynamicFormListener != null)
            onClickDynamicFormListener.OnNextClicked();
    }

    /**
     * backClickListener
     * @param v view
     */
    public void backClickListener(View v) {
        if (numCurrentPage > ZERO){
            pageList.get(numCurrentPage).setVisibility(INVISIBLE);
            numCurrentPage --;
            pageList.get(numCurrentPage).setVisibility(VISIBLE);
            Log.i(TAG, "backClickListener: page num : "+ numCurrentPage);
        }

        if (onBackClickListener != null)
            onBackClickListener.OnBackClicked(v);

        if (onClickDynamicFormListener != null)
            onClickDynamicFormListener.OnBackClicked();
    }

    /**
     * cancelClickListener
     * @param v view
     */
    public void cancelClickListener(View v) {
        if (onCancelClickListener != null)
            onCancelClickListener.OnCancelClicked(v);

        if (onClickDynamicFormListener != null)
            onClickDynamicFormListener.OnCancelClicked();
    }

    /**
     * getOnClickDynamicFormListener
     * @return click
     */
    public OnClickDynamicFormListener getOnClickDynamicFormListener() {
        return onClickDynamicFormListener;
    }

    /**
     * setOnClickDynamicFormListener
     * @param onClickDynamicFormListener click
     */
    public void setOnClickDynamicFormListener(OnClickDynamicFormListener onClickDynamicFormListener) {
        this.onClickDynamicFormListener = onClickDynamicFormListener;
    }

    /**
     * getNbFieldPerPage
     * @return nbFieldPerPage
     */
    public int getNbFieldPerPage() {
        return nbFieldPerPage;
    }

    /**
     * setNbFieldPerPage
     * @param nbFieldPerPage nbFieldPerPage
     */
    public void setNbFieldPerPage(int nbFieldPerPage) {
        this.nbFieldPerPage = nbFieldPerPage;
    }

    /**
     * getTypeForm
     * @return typeForm
     */
    public String getTypeForm() {
        return typeForm;
    }

    /**
     * setTypeForm
     * @param typeForm typeForm
     */
    public void setTypeForm(String typeForm) {
        this.typeForm = typeForm;
    }

    /**
     * getColorField
     * @return colorField
     */
    public int getColorField() {
        return colorField;
    }

    /**
     * setColorField
     * @param colorField colorField
     */
    public void setColorField(int colorField) {
        this.colorField = colorField;
    }

    /**
     * getPaddingField
     * @return paddingField
     */
    public float getPaddingField() {
        return paddingField;
    }

    /**
     * setPaddingField
     * @param paddingField paddingField
     */
    public void setPaddingField(float paddingField) {
        this.paddingField = paddingField;
    }

    /**
     * getMarginField
     * @return marginField
     */
    public float getMarginField() {
        return marginField;
    }

    /**
     * setMarginField
     * @param marginField marginField
     */
    public void setMarginField(float marginField) {
        this.marginField = marginField;
    }

    /**
     * setDoneText
     * @param nameButton name
     */
    public void setDoneText(String nameButton) {
        ((TextView)findViewById(R.id.done_btn)).setText(nameButton);
    }

    /**
     * setNextText
     * @param nameButton name
     */
    public void setNextText(String nameButton) {
        ((TextView)findViewById(R.id.next_btn)).setText(nameButton);
    }

    /**
     * setCancelText
     * @param nameButton name
     */
    public void setCancelText(String nameButton) {
        ((TextView)findViewById(R.id.cancel_btn)).setText(nameButton);
    }

    /**
     * setBackText
     * @param nameButton name
     */
    public void setBackText(String nameButton) {
        ((TextView)findViewById(R.id.back_btn)).setText(nameButton);
    }

    /**
     * setNameButton
     * @param textDone name
     * @param textNext name
     * @param textCancel name
     * @param textBack name
     */
    public void setNameButton(String textDone, String textNext, String textCancel, String textBack) {
        ((TextView)findViewById(R.id.done_btn)).setText(textDone);
        ((TextView)findViewById(R.id.next_btn)).setText(textNext);
        ((TextView)findViewById(R.id.cancel_btn)).setText(textCancel);
        ((TextView)findViewById(R.id.back_btn)).setText(textBack);
    }

    /**
     * setNameButtonFr
     */
    public void setNameButtonFr() {
        ((TextView)findViewById(R.id.done_btn)).setText(R.string.valider);
        ((TextView)findViewById(R.id.next_btn)).setText(R.string.suivant);
        ((TextView)findViewById(R.id.cancel_btn)).setText(R.string.annuler);
        ((TextView)findViewById(R.id.back_btn)).setText(R.string.precedent);
    }

}