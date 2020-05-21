package com.suntelecoms.djamil.dynamic_form;

/**
 *   Djvmil 19/12/2020
 **/

import android.content.Context;
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
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.hbb20.CountryCodePicker;
import com.suntelecoms.djamil.dynamic_form.exceptions.EmptyValueException;
import com.suntelecoms.djamil.dynamic_form.interfaces.OnBackClickListener;
import com.suntelecoms.djamil.dynamic_form.interfaces.OnCancelClickListener;
import com.suntelecoms.djamil.dynamic_form.interfaces.OnDoneClickListener;
import com.suntelecoms.djamil.dynamic_form.interfaces.OnClickDynamicFormListener;
import com.suntelecoms.djamil.dynamic_form.interfaces.OnNextClickListener;
import com.suntelecoms.djamil.dynamic_form.models.Formules;
import com.suntelecoms.djamil.dynamic_form.models.IOFieldsItem;
import com.suntelecoms.djamil.dynamic_form.models.Sequence;
import com.suntelecoms.djamil.dynamic_form.utils.FonctionUtils;

import java.util.ArrayList;
import java.util.Collections;

public class DynamicForm extends NestedScrollView {
    private static final String TAG = "FormDynamic";

    private NestedScrollView rootLayout;
    private RelativeLayout containerForm;
    private LinearLayout currentFormContainer;
    private LayoutInflater inflater;

    private TextView textView;
    private Context context;
    private ArrayList<IOFieldsItem> listForms;

    //current idView
    private int belowId;
    private static final int ZERO = 0;

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

    //list page
    ArrayList<LinearLayout> pageList;

    //current page
    private int numCurrentPage = ZERO;

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
        if (nbFieldPerPage != -1) {
            //throw new RuntimeException("No title provided");
        }
        else if (typeForm != null) {
            //throw new RuntimeException("No subtitle provided");
        }
        else if (colorField != -1) {
            //throw new RuntimeException("No subtitle provided");
        }
        else if (paddingField != -1) {
           // throw new RuntimeException("No subtitle provided");
        }
        else if (marginField != -1) {
            //throw new RuntimeException("No subtitle provided");
        }
    }


    //Initialization
    public void init(){
        //Load RootView from xml
        inflater.inflate(R.layout.container_dynamic_form, this, true);
        rootLayout    = findViewById(R.id.container);
        containerForm = findViewById(R.id.body);
    }

    /** Cette methode charge le formulaire en fonction de la liste de type RichField fourni **/
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
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
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
                throw new IllegalArgumentException(listForm.get(i).getLabel()+" ne peut pas etre null");
        }

        pageList.get(ZERO).setVisibility(VISIBLE);
        initButtons();
    }



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

        findViewById(R.id.done_btn).setOnClickListener(onClickLictener());
        findViewById(R.id.next_btn).setOnClickListener(onClickLictener());
        findViewById(R.id.back_btn).setOnClickListener(onClickLictener());
        findViewById(R.id.cancel_btn).setOnClickListener(onClickLictener());
        showButtons();
    }

    private OnClickListener onClickLictener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                showButtons();

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
            }
        };
    }

    private void showButtons(){
        if (pageList.size() == 1 || numCurrentPage == 0){
            findViewById(R.id.back_btn).setVisibility(GONE);
            findViewById(R.id.cancel_btn).setVisibility(VISIBLE);

            if (pageList.size() == 1 && numCurrentPage == ZERO){
                findViewById(R.id.next_btn).setVisibility(GONE);
                findViewById(R.id.done_btn).setVisibility(VISIBLE);
            }else {
                findViewById(R.id.next_btn).setVisibility(VISIBLE);
                findViewById(R.id.done_btn).setVisibility(GONE);

            }
        } else if (numCurrentPage == pageList.size()){

            findViewById(R.id.back_btn).setVisibility(VISIBLE);
            findViewById(R.id.cancel_btn).setVisibility(GONE);
            findViewById(R.id.next_btn).setVisibility(VISIBLE);
            findViewById(R.id.done_btn).setVisibility(GONE);
        }else{

            findViewById(R.id.back_btn).setVisibility(VISIBLE);
            findViewById(R.id.cancel_btn).setVisibility(GONE);
            findViewById(R.id.next_btn).setVisibility(GONE);
            findViewById(R.id.done_btn).setVisibility(VISIBLE);
        }
    }

    /**  **/
    private View getView(IOFieldsItem item){

        switch (item.getType()){
            case ConstantesView.Separator : return getSeparator(item);
            //case ConstantesView.Select    : return getChampsSelect(item);
            case ConstantesView.Double    :
            case ConstantesView.Int       :
            case ConstantesView.Integer   :
            case ConstantesView.Number    : return getChampsEditText(item, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            case ConstantesView.Email     :
            case ConstantesView.String    :
            case ConstantesView.Text      : return getChampsEditText(item, InputType.TYPE_CLASS_TEXT);
            case ConstantesView.Password  : return getChampsEditText(item, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            case ConstantesView.Phone     : return getChampsPhone(item, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            case ConstantesView.Label     : return getChampsLabel(item);

            default: return getChampsEditText(item, InputType.TYPE_CLASS_TEXT);
        }
    }

    /**  **/
    private View getChampsEditText(IOFieldsItem item, int inputType) {
        View rowView = inflater.inflate((item.getTemplate() != ZERO) ? item.getTemplate() :  R.layout.input_text, currentFormContainer, true);
        rowView.setId(View.NO_ID);


        textView = rowView.findViewById( R.id.textView);
        EditText editText = rowView.findViewById(R.id.editText);

        textView.setId(View.NO_ID);
        textView.setText(item.getLabel());

        if(item.getColor() != ZERO)
            textView.setTextColor(item.getColor());


        item.setIsRequired(true);

        textView.setTextColor(context.getResources().getColor(R.color.black_semi_transparent));
        try {
            belowId = Sequence.nextValue();

            editText.setId(belowId);
            item.setId(belowId);
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

    private View getChampsPhone(IOFieldsItem item, int inputType) {
        View rowView = inflater.inflate((item.getTemplate() != ZERO) ? item.getTemplate() :  R.layout.input_phone, currentFormContainer, true);
        rowView.setId(View.NO_ID);

        CountryCodePicker textView = rowView.findViewById( R.id.indicatif);
        EditText editText = rowView.findViewById(R.id.editText);

        try {
            belowId = Sequence.nextValue();

            textView.setId(belowId);
            item.setIdIndicatif(belowId);
        }catch (Exception e){
            Log.e("getForm Error", e.toString());

        }
        item.setIsRequired(true);
        try {
            belowId = Sequence.nextValue();

            editText.setId(belowId);
            item.setId(belowId);
            editText.setTag(item.getLabel());
            editText.setInputType(inputType);

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

    /**  **/
    private View getChampsLabel(IOFieldsItem item) {

        View rowView = inflater.inflate((item.getTemplate() != ZERO) ? item.getTemplate() : R.layout.label_text, currentFormContainer, true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById( R.id.textView);
        TextView editText = rowView.findViewById( R.id.editText);

        textView.setId(View.NO_ID);
        textView.setText(item.getLabel());
        if(item.getColor() != ZERO)
            textView.setTextColor(item.getColor());

        try {
            belowId = Sequence.nextValue();
            editText.setId(belowId);
            item.setId(belowId);

            String str = item.getValue();

            if (item.isFormatter())
                str = FonctionUtils.getNumberFloatFormat(Float.valueOf(str.replaceAll("\\s", "")));

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

    /**  **/
    private ArrayList<IOFieldsItem>  done() throws EmptyValueException {
        String value = null;

        for(int i = 0; i < listForms.size(); i++){
            switch (listForms.get(i).getType()){

                case ConstantesView.Phone    : value = String.format("%s%s",
                                                                    ((CountryCodePicker)rootLayout.findViewById(listForms.get(i).getIdIndicatif())).getSelectedCountryCode(),
                                                                    ((TextView)rootLayout.findViewById(listForms.get(i).getId())).getText().toString());
                                                                    break;

                case ConstantesView.Select   : value = String.valueOf(((Formules)((Spinner)rootLayout.findViewById(listForms.get(i).getId())).getSelectedItem()).getId()); break;
                case ConstantesView.Int      :
                case ConstantesView.Double   :
                case ConstantesView.Number   :
                case ConstantesView.Email    :
                case ConstantesView.String   :
                case ConstantesView.Label    :
                case ConstantesView.Text     :
                case ConstantesView.Password :
                case ConstantesView.Date     :
                    if (rootLayout.findViewById(listForms.get(i).getId()) != null )
                        value = ((TextView)rootLayout.findViewById(listForms.get(i).getId())).getText().toString(); break;

                case ConstantesView.Spinner  : value = ((Spinner)rootLayout.findViewById(listForms.get(i).getId())).getSelectedItem().toString(); break;

                case ConstantesView.CheckBox : value = ((CheckBox)rootLayout.findViewById(listForms.get(i).getId())).isChecked()? "true" : "false"; break;
                case ConstantesView.Radio    : RadioGroup radioGroup = rootLayout.findViewById(listForms.get(i).getId());
                    int idx = radioGroup.getCheckedRadioButtonId();
                    value   =  ((RadioButton)radioGroup.findViewById(idx)).getText().toString(); break;
                default: value = null;
            }
            /*
            if (listForms.get(i).isRequired() && (value == null || value.isEmpty()))
                throw new EmptyValueException(listForms.get(i).getLabel());*/

            listForms.get(i).setValue(value);
        }

        return listForms;
    }


    /**  **//*
    private static View getChampsSelect(IOFieldsItem item) {
        View rowView = inflater.inflate((item.getTemplate() != ZERO) ? item.getTemplate() : R.layout.input_select, currentFormContainer, true);
        rowView.setId(View.NO_ID);

        textView = rowView.findViewById(R.id.textView);
        Spinner editText = rowView.findViewById(R.id.editText);

        textView.setId(View.NO_ID);
        textView.setText(item.getLabel());
        if(item.getColor() != ZERO){
            textView.setTextColor(item.getColor());
        }

        try {
            int nextValue = Sequence.nextValue();
            editText.setId(nextValue);
            item.setId(nextValue);
            editText.setTag(item.getLabel());

            SpinAdapter spinnerArrayAdapter = new SpinAdapter(context, android.R.layout.simple_spinner_item, getListFormules(AppDanaya.getBodyRequest().getCodeBiller()));
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view

            editText.setAdapter(spinnerArrayAdapter);
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
            });

            if (!item.getShouldBeShown())
                rowView.setVisibility(View.GONE);

        } catch (Exception e) {
            Log.e("getForm Error", e.toString());
        }

        return rowView;
    }*/

    /**  **/
    private View getSeparator(IOFieldsItem item) {

        View rowView = inflater.inflate(R.layout.separator, currentFormContainer, true);
        try {
            belowId = Sequence.nextValue();

            rowView.setId(belowId);
            item.setId(belowId);

        } catch (Exception e) {
            Log.e("getForm Error", e.toString());
        }

        return rowView;
    }


    public OnDoneClickListener getOnDoneClickListener() {
        return onDoneClickListener;
    }

    public void setOnDoneClickListener(OnDoneClickListener onDoneClickListener) {
        this.onDoneClickListener = onDoneClickListener;
    }

    public OnNextClickListener getOnNextClickListener() {
        return onNextClickListener;
    }

    public void setOnNextClickListener(OnNextClickListener onNextClickListener) {
        this.onNextClickListener = onNextClickListener;
    }

    public OnBackClickListener getOnBackClickListener() {
        return onBackClickListener;
    }

    public void setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    public OnCancelClickListener getOnCancelClickListener() {
        return onCancelClickListener;
    }

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }

    public void doneClickListener(View v) {
        if (onDoneClickListener != null)
            onDoneClickListener.OnDoneClicked(v, done());

        if (onClickDynamicFormListener != null)
            onClickDynamicFormListener.OnDoneClicked(done());
    }

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

    public void cancelClickListener(View v) {
        if (onCancelClickListener != null)
            onCancelClickListener.OnCancelClicked(v);

        if (onClickDynamicFormListener != null)
            onClickDynamicFormListener.OnCancelClicked();
    }

    public int getNbFieldPerPage() {
        return nbFieldPerPage;
    }

    public void setNbFieldPerPage(int nbFieldPerPage) {
        this.nbFieldPerPage = nbFieldPerPage;
    }

    public String getTypeForm() {
        return typeForm;
    }

    public void setTypeForm(String typeForm) {
        this.typeForm = typeForm;
    }

    public int getColorField() {
        return colorField;
    }

    public void setColorField(int colorField) {
        this.colorField = colorField;
    }

    public float getPaddingField() {
        return paddingField;
    }

    public void setPaddingField(float paddingField) {
        this.paddingField = paddingField;
    }

    public float getMarginField() {
        return marginField;
    }

    public void setMarginField(float marginField) {
        this.marginField = marginField;
    }

    public OnClickDynamicFormListener getOnClickDynamicFormListener() {
        return onClickDynamicFormListener;
    }

    public void setOnClickDynamicFormListener(OnClickDynamicFormListener onClickDynamicFormListener) {
        this.onClickDynamicFormListener = onClickDynamicFormListener;
    }
}