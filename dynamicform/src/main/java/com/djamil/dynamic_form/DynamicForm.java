package com.djamil.dynamic_form;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup; ;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.IntDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.djamil.contactlist.ContactList;
import com.djamil.contactlist.ContactsInfo;
import com.djamil.contactlist.interfaces.OnClickCantactListener;
import com.djamil.dynamic_form.annotations.TypeButton;
import com.djamil.dynamic_form.models.CustomButton;
import com.djamil.dynamic_form.utils.NumberTextWatcherWithSeperator;
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
import java.util.List;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */
public class DynamicForm extends NestedScrollView {
    private static final String TAG = "FormDynamic";

    //Container
    private NestedScrollView rootLayout;
    private RelativeLayout containerForm;
    private LinearLayout currentFormContainer;
    private LayoutInflater inflater;

    private TextView textView;
    private Activity activity;
    private List<IOFieldsItem> listIOField;

    private List<IOFieldsItem> listFormsInPage;
    private List<List<IOFieldsItem>> arrayListsForms;


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

    private ContactList contactList;

    private boolean checkShowButton = true;

    private boolean showNextButton = false;

    //Button
    public static final int BUTTON_DONE   = 3414;
    public static final int BUTTON_NEXT   = 3412;
    public static final int BUTTON_BACK   = 3413;
    public static final int BUTTON_CANCEL = 3411;

    /**
     * DynamicForm
     * @param ctx context
     * @param attrs attributes
     */
    public DynamicForm(Context ctx, @androidx.annotation.Nullable AttributeSet attrs) {
        super(ctx, attrs);
        activity = ((Activity)ctx);
        inflater =  LayoutInflater.from(activity);
        init();

        TypedArray attr = activity.getTheme().obtainStyledAttributes(attrs, R.styleable.DynamicForm, 0, 0);

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
    private void init(){
        //Load RootView from xml
        inflater.inflate(R.layout.container_dynamic_form, this, true);
        rootLayout    = findViewById(R.id.container);
        containerForm = findViewById(R.id.body);
    }

    /**
     * loadForm
     * Cette methode charge le formulaire en fonction de la liste de type IOFieldsItem fourni
     * @param ioFieldsItems liste InputOutputField (IOFieldsItem) fourni
     * @return
     */
    public DynamicForm loadForm(List<IOFieldsItem> ioFieldsItems){
        listIOField = ioFieldsItems;
        pageList = new ArrayList<>();
        arrayListsForms = new ArrayList<>();
        Collections.sort(listIOField);

        int j = ZERO;

        for(int i = ZERO; i < ioFieldsItems.size(); i++ ){
            if (j == (nbFieldPerPage - 1)) j = ZERO;

            else{
                if (j == ZERO){
                    Log.i(TAG, "loadForm: "+nbFieldPerPage );

                    int id = Sequence.nextValue();
                    currentFormContainer = new LinearLayout(activity);
                    currentFormContainer.setId(id);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //params.addRule(RelativeLayout.ABOVE, R.id.buttons);
                    currentFormContainer.setLayoutParams(params);
                    currentFormContainer.setOrientation(LinearLayout.VERTICAL);
                    containerForm.addView(currentFormContainer);

                    pageList.add(currentFormContainer);
                    currentFormContainer.setVisibility(INVISIBLE);

                    //La liste des champs d'une page
                    listFormsInPage = new ArrayList<>();

                    //ajout de la liste dans une liste qui a toutes les pages
                    arrayListsForms.add(listFormsInPage);

                }
                j++;
            }


            //ajout de l'IOField dans la page
            View rowView = getView(ioFieldsItems.get(i));

            //ajout de l'IOField dans la liste correspondant la page
            listFormsInPage.add(ioFieldsItems.get(i));

            if(rowView == null)
                Log.e(TAG, "loadForm: "+ioFieldsItems.get(i).getLabel()+" ne peut pas etre null" );
            //throw new IllegalArgumentException(ioFieldsItems.get(i).getLabel()+" ne peut pas etre null");


            if (!ioFieldsItems.get(i).isReadOnly())
                showNextButton = true;
        }

        pageList.get(numCurrentPage).setVisibility(VISIBLE);
        initButtons();

        if (showNextButton){
            getButton(BUTTON_NEXT).setVisibility(VISIBLE);
            getButton(BUTTON_DONE).setVisibility(GONE);
            Log.e(TAG, "loadForm: button next" );
        }

        return this;
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

                if (v.getId() == R.id.done_btn){
                    if (checkIOFieldRequired())
                        doneClickListener(v);
                }

                else if(v.getId() == R.id.next_btn){
                    if (checkIOFieldRequired())
                        nextClickListener(v);
                }

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

        if (checkShowButton)
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
        //chaque IOField a le numero de la page ou il se trouve
        item.setNumPage(pageList.size() - 1);

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
            /*case INPUT_TYPE_DF.Email     :
            case INPUT_TYPE_DF.String    :
            case INPUT_TYPE_DF.Text      : return getChampsEditText(item, InputType.TYPE_CLASS_TEXT);*/
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

        if (!item.getShouldBeShown())
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

        if (item.isMoney())
            editText.addTextChangedListener(new NumberTextWatcherWithSeperator(editText));

        textView.setTextColor(activity.getResources().getColor(R.color.black_semi_transparent));
        try {
            currentIdView = Sequence.nextValue();

            editText.setId(currentIdView);
            item.setIdView(currentIdView);
            if (item.getValue() != null && !item.getValue().isEmpty())
                editText.setText(item.getValue());
            else
                editText.setHint(item.getMsgHint() != null ? item.getMsgHint() : "Saisir "+ item.getLabel());

            editText.setTag(item.getLabel());
            editText.setInputType(inputType);

            try{
                Log.i("getChampsEditText: ", item.getField());
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
        final EditText editText    = rowView.findViewById(R.id.editText);
        textView.registerCarrierNumberEditText(editText);

        contactList = ContactList.getInstance(activity);
        findViewById(R.id.contacts).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                contactList.showContactList();
            }
        });
        contactList.setOnClickCantactListener(new OnClickCantactListener() {
            @Override
            public void onClickCantact(View v, ContactsInfo contactsInfo) {
                Log.e(TAG, "onClickCantact: "+ contactsInfo.getDisplayName());

                editText.setText(contactsInfo.getPhoneNumber());
            }
        });

        try {
            currentIdView = Sequence.nextValue();

            textView.setId(currentIdView);
            item.setIndicatif(currentIdView);
        }catch (Exception e){
            Log.e("getForm Error", e.toString());

        }

        try {
            currentIdView = Sequence.nextValue();

            editText.setId(currentIdView);
            item.setIdView(currentIdView);
            editText.setTag(item.getLabel());
            editText.setHint(item.getMsgHint() != null ? item.getMsgHint() : "Saisir "+ item.getLabel());
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            try{
                Log.i("getChampsPhone: ", item.getField());
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
        if(item.getColor() != ZERO)
            textView.setTextColor(item.getColor());

        try {
            int nextValue = Sequence.nextValue();
            editText.setId(nextValue);
            item.setIdView(nextValue);
            editText.setTag(item.getLabel());

            SpinAdapter spinnerArrayAdapter = new SpinAdapter(activity, android.R.layout.simple_spinner_item, item.getListItemDF());
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            editText.setAdapter(spinnerArrayAdapter);

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
            RadioButton radioButton = new RadioButton(activity);
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
                datePickerFragmentDialog.setCancelColor(activity.getResources().getColor(colorId));
                datePickerFragmentDialog.setOkColor(activity.getResources().getColor(colorId));
                datePickerFragmentDialog.setAccentColor(activity.getResources().getColor(R.color.amdp_accent_color));
            }

            datePickerFragmentDialog.show(((AppCompatActivity) activity).getSupportFragmentManager(), null);
            //datePickerFragmentDialog.setMaxDate(System.currentTimeMillis());
            //datePickerFragmentDialog.setYearRange(1900, Year);
            datePickerFragmentDialog.setOkText(activity.getResources().getString(R.string.ok_dob));
            datePickerFragmentDialog.setCancelText(activity.getResources().getString(R.string.cancel_dob));
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

        final View rowView = inflater.inflate((item.getTemplate() != ZERO) ? item.getTemplate() : R.layout.input_date, currentFormContainer, true);
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
     * getChampsPhoto
     * @param item
     * @return
     */
    private View getChampsPhoto(final IOFieldsItem item) {

        final View rowView = inflater.inflate((item.getTemplate() != ZERO) ? item.getTemplate() : R.layout.input_date, currentFormContainer, true);
        rowView.setId(NO_ID);

        textView = rowView.findViewById(R.id.textView);
        EditText editText = rowView.findViewById(R.id.editText);

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
     * getChampsImageView
     * @param item
     * @return
     */
    private View getChampsImageView(final IOFieldsItem item) {

        final View rowView = inflater.inflate((item.getTemplate() != ZERO) ? item.getTemplate() : R.layout.input_date, currentFormContainer, true);
        rowView.setId(NO_ID);

        textView = rowView.findViewById(R.id.textView);
        EditText editText = rowView.findViewById(R.id.editText);

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
     * getChampsSignView
     * @param item
     * @return
     */
    private View getChampsSignView(final IOFieldsItem item) {

        final View rowView = inflater.inflate((item.getTemplate() != ZERO) ? item.getTemplate() : R.layout.input_date, currentFormContainer, true);
        rowView.setId(NO_ID);

        textView = rowView.findViewById(R.id.textView);
        EditText editText = rowView.findViewById(R.id.editText);

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

        View rowView = inflater.inflate((item.getTemplate() != ZERO) ? item.getTemplate() : R.layout.separator, currentFormContainer, true);

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
    private List<IOFieldsItem> done() throws EmptyValueException {
        String value = null;

        for(int i = 0; i < listIOField.size(); i++){
            switch (listIOField.get(i).getType()){

                case INPUT_TYPE_DF.Country  : value =  ((CountryCodePicker)rootLayout.findViewById(listIOField.get(i).getIdView())).getSelectedCountryName();
                    listIOField.get(i).setIndicatif(Integer.parseInt(((CountryCodePicker)rootLayout.findViewById(listIOField.get(i).getIdView())).getSelectedCountryCode()));
                    listIOField.get(i).setPaysAlpha2(((CountryCodePicker)rootLayout.findViewById(listIOField.get(i).getIdView())).getSelectedCountryNameCode());
                    break;
/*
                case INPUT_TYPE_DF.Phone    : value = String.format("%s%s",
                        ((CountryCodePicker)rootLayout.findViewById(listIOField.get(i).getIndicatif())).getSelectedCountryCode(),
                        ((TextView)rootLayout.findViewById(listIOField.get(i).getIdView())).getText().toString());

                    listIOField.get(i).setIndicatif(Integer.parseInt(((CountryCodePicker)rootLayout.findViewById(listIOField.get(i).getIndicatif())).getSelectedCountryCode()));
                    //listIOField.get(i).setPaysAlpha2(((CountryCodePicker)rootLayout.findViewById(listIOField.get(i).getIndicatif())).getSelectedCountryNameCode());*/

                case INPUT_TYPE_DF.Phone    : value = String.format("%s",((TextView)rootLayout.findViewById(listIOField.get(i).getIdView())).getText().toString()); break;

                case INPUT_TYPE_DF.Select   : ItemDF item = (ItemDF)((Spinner)rootLayout.findViewById(listIOField.get(i).getIdView())).getSelectedItem();
                    listIOField.get(i).setItemDFSelected(item);
                    value = String.valueOf(item.getValue());
                    break;


                case INPUT_TYPE_DF.CheckBox : listIOField.get(i).getListItemDFByView(rootLayout);
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
                    if (rootLayout.findViewById(listIOField.get(i).getIdView()) != null )
                        value = ((TextView)rootLayout.findViewById(listIOField.get(i).getIdView())).getText().toString(); break;

                case INPUT_TYPE_DF.Radio    : RadioGroup radioGroup = rootLayout.findViewById(listIOField.get(i).getIdView());
                    int idView = radioGroup.getCheckedRadioButtonId();
                    value   =  ((RadioButton)radioGroup.findViewById(idView)).getText().toString();
                    listIOField.get(i).getItemDFByIdView(idView);
                    break;

                default: value = null;
            }

            if (listIOField.get(i).isRequired() && (value == null || value.isEmpty()))
                Log.w(TAG, "Le champ "+ listIOField.get(i).getLabel()+" \nest obligatoire" );
            //throw new EmptyValueException(listForms.get(i).getLabel());

            listIOField.get(i).setValue(value);
        }

        return listIOField;
    }

    /**
     *
     * @return
     */
    public List<IOFieldsItem> getListIOField(){
        return done();
    }

    /**
     * findIOFieldByIdView
     * @param idView id view
     * @return IOFieldsItem
     */
    public IOFieldsItem findIOFieldByIdView(int idView) {

        IOFieldsItem item = null;
        for (IOFieldsItem fieldsItem : listIOField)
            if (fieldsItem.getIdView() == idView)
                item = fieldsItem;

        return item;
    }

    /**
     * findIOFieldByFieldName
     * @param field field name
     * @return IOFieldsItem
     */
    public IOFieldsItem findIOFieldByFieldName(String field) {

        IOFieldsItem item = null;
        for (IOFieldsItem fieldsItem : listIOField)
            if (fieldsItem.getField().toLowerCase().equals(field.toLowerCase()))
                item = fieldsItem;

        return item;
    }

    /**
     * getListFormsInPage
     * @param numPage num page
     * @return list IOFieldsItem
     */
    private List<IOFieldsItem> getListFormsByPageId(int numPage) {
        if (numPage < 0 || numPage > listIOField.size())
            return null;
        else
            return arrayListsForms.get(numPage);
    }

    /**
     * checkIOFieldRequired
     * @return boolean required
     */
    private boolean checkIOFieldRequired() {

        List<IOFieldsItem> list = getListFormsByPageId(numCurrentPage);
        for (IOFieldsItem item : list){
            Log.i(TAG, "checkIOFieldRequired: "+item.getLabel() );

            if (item.getIdView() != ZERO && item.isRequired() && item.getShouldBeShown()){
                try {
                    EditText editText = findViewById(item.getIdView());
                    if (editText.getText().toString().isEmpty()){
                        editText.setError("Champs "+item.getLabel()+" obligatoire");
                        editText.requestFocus();
                        editText.setFocusable(true);
                        return false;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return true;
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
            onClickDynamicFormListener.OnDoneClicked(v, done());
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
            Log.i(TAG, "nextClickListener => page: "+ numCurrentPage);
        }


        if (onNextClickListener != null)
            onNextClickListener.OnNextClicked(v);

        if (onClickDynamicFormListener != null)
            onClickDynamicFormListener.OnNextClicked(v);
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
            Log.i(TAG, "backClickListener => page: "+ numCurrentPage);
        }

        if (onBackClickListener != null)
            onBackClickListener.OnBackClicked(v);

        if (onClickDynamicFormListener != null)
            onClickDynamicFormListener.OnBackClicked(v);
    }

    /**
     * cancelClickListener
     * @param v view
     */
    public void cancelClickListener(View v) {
        if (onCancelClickListener != null)
            onCancelClickListener.OnCancelClicked(v);

        if (onClickDynamicFormListener != null)
            onClickDynamicFormListener.OnCancelClicked(v);
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

        if (onClickDynamicFormListener != null)
            onClickDynamicFormListener.OnFormCreated(listIOField);
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
     *
     * @param customButton
     */
    public void customDoneButton(CustomButton customButton) {
        if (customButton.getText() != null)
            ((Button)findViewById(R.id.done_btn)).setText(customButton.getText());

        if (customButton.getBackground() != -1)
            findViewById(R.id.done_btn).setBackground(getResources().getDrawable(customButton.getBackground()));

        if (customButton.getBackgroundTint() != -1)
            findViewById(R.id.done_btn).setBackgroundColor(getResources().getColor(customButton.getBackgroundTint()));

        if (customButton.getTextColor() != -1)
            ((Button)findViewById(R.id.done_btn)).setTextColor(getResources().getColor(customButton.getTextColor()));

        if (customButton.getTextSize() != -1)
            ((Button)findViewById(R.id.done_btn)).setTextSize(customButton.getTextSize());
    }

    /**
     *
     * @param customButton
     */
    public void customNextButton(CustomButton customButton) {
        if (customButton.getText() != null)
            ((Button)findViewById(R.id.next_btn)).setText(customButton.getText());

        if (customButton.getBackground() != -1)
            findViewById(R.id.next_btn).setBackground(getResources().getDrawable(customButton.getBackground()));

        if (customButton.getBackgroundTint() != -1)
            findViewById(R.id.next_btn).setBackgroundColor(getResources().getColor(customButton.getBackgroundTint()));

        if (customButton.getTextColor() != -1)
            ((Button)findViewById(R.id.next_btn)).setTextColor(getResources().getColor(customButton.getTextColor()));

        if (customButton.getTextSize() != -1)
            ((Button)findViewById(R.id.next_btn)).setTextSize(customButton.getTextSize());
    }

    /**
     *
     * @param customButton
     */
    public void customCancelButton(CustomButton customButton) {
        if (customButton.getText() != null)
            ((Button)findViewById(R.id.cancel_btn)).setText(customButton.getText());

        if (customButton.getBackground() != -1)
            findViewById(R.id.cancel_btn).setBackground(getResources().getDrawable(customButton.getBackground()));

        if (customButton.getBackgroundTint() != -1)
            findViewById(R.id.cancel_btn).setBackgroundColor(getResources().getColor(customButton.getBackgroundTint()));

        if (customButton.getTextColor() != -1)
            ((Button)findViewById(R.id.cancel_btn)).setTextColor(getResources().getColor(customButton.getTextColor()));

        if (customButton.getTextSize() != -1)
            ((Button)findViewById(R.id.cancel_btn)).setTextSize(customButton.getTextSize());
    }

    /**
     *
     * @param customButton
     */
    public void customBackButton(CustomButton customButton) {
        if (customButton.getText() != null)
            ((Button)findViewById(R.id.back_btn)).setText(customButton.getText());

        if (customButton.getBackground() != -1)
            findViewById(R.id.back_btn).setBackground(getResources().getDrawable(customButton.getBackground()));

        if (customButton.getBackgroundTint() != -1)
            findViewById(R.id.back_btn).setBackgroundColor(getResources().getColor(customButton.getBackgroundTint()));

        if (customButton.getTextColor() != -1)
            ((Button)findViewById(R.id.back_btn)).setTextColor(getResources().getColor(customButton.getTextColor()));

        if (customButton.getTextSize() != -1)
            ((Button)findViewById(R.id.back_btn)).setTextSize(customButton.getTextSize());
    }

    public DynamicForm visibiltyButtons(int visibility){
        findViewById(R.id.footer).setVisibility(visibility);
        return this;
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

    public Button getButton(@TypeButton int button){
        switch (button){
            case BUTTON_DONE: return findViewById(R.id.done_btn);
            case BUTTON_NEXT: return findViewById(R.id.next_btn);
            case BUTTON_CANCEL: return findViewById(R.id.cancel_btn);
            case BUTTON_BACK: return findViewById(R.id.back_btn);
            default: return null;
        }
    }

    public boolean isCheckShowButton() {
        return checkShowButton;
    }

    public DynamicForm setCheckShowButton(boolean checkShowButton) {
        this.checkShowButton = checkShowButton;
        return this;
    }


    /**
     * remove viewField
     */
    public void clearFormView(){
        containerForm.removeAllViews();
    }

}