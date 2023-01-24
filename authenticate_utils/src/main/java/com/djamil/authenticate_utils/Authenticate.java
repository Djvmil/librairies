package com.djamil.authenticate_utils;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.djamil.authenticate_utils.fingerprint.FingerPrintHandler;
import com.djamil.authenticate_utils.interfaces.OnResultAuth;
import com.djamil.utils.UtilsFunction;
import com.google.android.material.textfield.TextInputLayout;

import java.security.KeyStore;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.crypto.Cipher;

/**
 * Created by Djvmil_ on 2020-03-05
 */
public class Authenticate extends RelativeLayout {
    private static final String TAG = "KeyboardDynamic";
    public final static Boolean SHUFFLE = true;
    public final static Boolean NORMAL  = false;

    public final static int TO_MANY_ATTEMPTS = 7;
    public final static int AUTH_FAIL = 22;

    public final static int IMPRIMED = 334;
    public final static int CODED = 433;

    private TextView textView;
    private TextInputLayout textinput;
    protected static Activity activity;
    private RelativeLayout rootLayout, relative;
    private Button doneBtn;
    private LayoutInflater inflater;
    private RecyclerView recyclerView;

    private String secret;
    private boolean secretIsMd5 = false;

    private String textTitle, textDesc, textSubTitle, textNegativeButton, errorMsg;

    private static final int ZERO = 0;

    private boolean isShuffle = true;
    private boolean useAnotherEditText = false;
    private boolean goneValidBtn = false;
    private Button validButtonSetted = null;
    private int editTextId = -1;
    private int colorKey = 0;
    private Drawable iconFingerPrint = null;
    private Drawable iconNoFingerPrint = null;
    private Drawable iconBackSpace = null;
    private Drawable backgroundBtn = null;


    private KeyBoardAdapter keyBoardAdapter;

    private boolean userFingerPrint = false;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    public OnResultAuth onResultAuth;
    private int nbAttempts = 0;

    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";
    private int keyHeight = -1;
    private int keyWidth = -1;
    private Boolean autoAuth = false;
    private int nbChar = -1;
    private int valideBtnId = -1;

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (autoAuth && s.length() >= nbChar && doneBtn != null) {
                textView.setText("");
                doneBtn.performClick();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public Authenticate(Context context) {
        super(context);
    }

    public void setActionAutoAuth(TextWatcher watcher){
        this.watcher = watcher;

    }

    public Authenticate(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        activity = ((Activity)ctx);
        inflater =  LayoutInflater.from(ctx);

        TypedArray attr = activity.getTheme().obtainStyledAttributes(attrs, R.styleable.DynamicKeyBoard, 0, 0);

        try {
            colorKey           = attr.getColor(R.styleable.DynamicKeyBoard_color_field, ZERO);
            iconBackSpace      = attr.getDrawable(R.styleable.DynamicKeyBoard_icon_backspace);
            iconFingerPrint    = attr.getDrawable(R.styleable.DynamicKeyBoard_icon_fingerprint);
            iconNoFingerPrint  = attr.getDrawable(R.styleable.DynamicKeyBoard_icon_no_fingerprint);
            backgroundBtn      = attr.getDrawable(R.styleable.DynamicKeyBoard_background_done_btn);
            isShuffle          = attr.getBoolean(R.styleable.DynamicKeyBoard_is_shuffle, true);
            useAnotherEditText = attr.getBoolean(R.styleable.DynamicKeyBoard_use_another_edit_text, false);
            goneValidBtn       = attr.getBoolean(R.styleable.DynamicKeyBoard_gone_valid_btn,  false);
            editTextId         = attr.getResourceId(R.styleable.DynamicKeyBoard_edit_text_id, -1);
            keyHeight          = attr.getResourceId(R.styleable.DynamicKeyBoard_key_height, -1);
            keyWidth           = attr.getResourceId(R.styleable.DynamicKeyBoard_key_width, -1);
            autoAuth           = attr.getBoolean(R.styleable.DynamicKeyBoard_auto_auth, false);
            nbChar             = attr.getInteger(R.styleable.DynamicKeyBoard_nb_char, -1);
            nbChar             = attr.getInteger(R.styleable.DynamicKeyBoard_nb_char, -1);
//            valideBtn          = attr.getInteger(R.styleable.DynamicKeyBoard_valide_btn, -1);
//            valideBtnId = attr.getResourceId(R.styleable.DynamicKeyBoard_valide_btn, R.id.done_btn);

        } finally {
            attr.recycle();
        }

        init();
    }

    public Authenticate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * init
     */
    private void init(){
        //Load RootView from xml
        inflater.inflate(R.layout.container_authenticate, this, true);
        rootLayout   = findViewById(R.id.container);
        textView     = findViewById(R.id.textedit);
        textinput     = findViewById(R.id.textinput);
        textinput.setVisibility(useAnotherEditText ? View.GONE : View.VISIBLE);
        recyclerView = findViewById(R.id.recyclerviewKeyBoard);
        relative = findViewById(R.id.relative);
        doneBtn = findViewById(R.id.done_btn);
//        Log.e(TAG, "init: valideBtnId = "+valideBtnId );
//
//        if (valideBtnId == -1)
//        else doneBtn = getActivity(getContext()).getWindow().getDecorView().findViewById(valideBtnId);

        relative.setVisibility(goneValidBtn ? View.GONE : View.VISIBLE);
        textView.setKeyListener(null);

        notifyChange();
    }

    /**
     * Get activity instance from desired context.
     */
    public static Activity getActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) return (Activity) context;
        if (context instanceof ContextWrapper) return getActivity(((ContextWrapper)context).getBaseContext());
        return null;
    }

    public void notifyChange(){
        doneBtn.setBackground(backgroundBtn);

        doneBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!textView.getText().toString().isEmpty()){
                    onResultAuth.onDoneClicked(textView.getText().toString(), UtilsFunction.md5Hash(textView.getText().toString()), false);
                    if (secret != null){
                        boolean tmp = false;
                        if (secretIsMd5 && secret.equals(UtilsFunction.md5Hash(textView.getText().toString().trim()))){
                            tmp = true;
                            new SessionManager().setUseFingerprint(true);
                            onResultAuth.onAuthSucceeded(textView.getText().toString().trim(), UtilsFunction.md5Hash(textView.getText().toString().trim()));

                        } else if (!secretIsMd5 && secret.equals(textView.getText().toString().trim())){
                            tmp = true;
                            new SessionManager().setUseFingerprint(true);
                            onResultAuth.onAuthSucceeded(textView.getText().toString().trim(), UtilsFunction.md5Hash(textView.getText().toString().trim()));

                        }else{
                            onResultAuth.onAuthFailed(CODED);
                        }

                    }
                    onResultAuth.onAttempts(increment());
                }else {
                    textView.setError(errorMsg != null ? errorMsg : "Entrez votre code secret");
                    textView.requestFocus();
                }
            }
        });

        keyBoardAdapter = new KeyBoardAdapter(activity, textView, isShuffle,
                colorKey, iconBackSpace, iconFingerPrint, iconNoFingerPrint,
                userFingerPrint, keyHeight, keyWidth);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 4));
        recyclerView.setAdapter(keyBoardAdapter);
        //keyBoardAdapter.shuffleKey();
        keyBoardAdapter.notifyDataSetChanged();
        textView.removeTextChangedListener(watcher);
        textView.addTextChangedListener(watcher);

    }

    public void setEditText(TextView editText){
        useAnotherEditText = false;
        textinput.setVisibility(View.GONE);
        textView = editText;
        notifyChange();
    }

    public void setDoneBtn(Button doneBtn){
        doneBtn.setVisibility(View.GONE);
        this.doneBtn = doneBtn;
        notifyChange();
    }

    public void setGoneValidBtn(boolean value){
        useAnotherEditText = value;
        notifyChange();
    }

    public void recycle(TextView editText, boolean shuffle){
        setShuffle(shuffle);
        setEditText(editText);
    }

    public void recycle(TextView editText, boolean shuffle, boolean goneValidBtn){
        setShuffle(shuffle);
        setEditText(editText);
        setGoneValidBtn(goneValidBtn);
    }

    public void setShuffle(boolean value){
        isShuffle = value;
        textinput.setVisibility(goneValidBtn ? View.GONE : View.VISIBLE);
    }

    public static Result checkFingerPrint(Activity activity){
        Result result = null;
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                FingerprintManager fingerprintManager = (FingerprintManager) activity.getSystemService(Context.FINGERPRINT_SERVICE);
                KeyguardManager keyguardManager = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);

                if((fingerprintManager != null) && ! fingerprintManager.isHardwareDetected()){
                    result = new Result(false, "Scanner d'empreintes digitales non détecté dans l'appareil.\n");

                } else if (ContextCompat.checkSelfPermission(activity, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
                    result = new Result(false, "Autorisation d'utiliser le scanner d'empreintes digitales non accordée\n");

                } else if (! keyguardManager.isKeyguardSecure()){
                    result = new Result(false, "Ajouter un verrou à votre téléphone dans les paramètres.\n");

                } else if (fingerprintManager != null &&  ! fingerprintManager.hasEnrolledFingerprints()){
                    result = new Result(false, "Vous devez ajouter au moins 1 empreinte digitale pour utiliser cette fonction.\n");

                }else {
                    result = new Result(true, "Placez votre doigt sur le scanner pour accéder à l'application.\n");

                }

            }else  result = new Result(false, "Empreinte digitale non disponible pour cette version android (V-"+Build.VERSION.SDK_INT+")\n");
        }catch (Exception e){
            result = new Result(false, "Empreinte digitale non disponible pour cette version android (V-"+Build.VERSION.SDK_INT+")\n");
        }

        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Authenticate useFingerPrintForAuth(boolean useFingerPrint){
        this.userFingerPrint = useFingerPrint;
        Result result = checkFingerPrint(activity);

        this.userFingerPrint = useFingerPrint && result.status;

        if (!useFingerPrint)
            result = new Result(false, "L'empreint digitale est désactiver\n");
        else
            initFingerPrint();

        return this;
    }

    private void initFingerPrint() {
        notifyChange();
        Executor executor = Executors.newSingleThreadExecutor();
        biometricPrompt = new BiometricPrompt(((FragmentActivity)activity), executor, new FingerPrintHandler(onResultAuth, secret, secretIsMd5));
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(textTitle != null ? textTitle : getContext().getString(R.string.title_auth_dialog))
                .setSubtitle(textSubTitle != null ? textSubTitle : getContext().getString(R.string.subtitle_auth_dialog))
                .setDescription(textDesc != null ? textDesc : getContext().getString(R.string.desc_auth_dialog))
                .setNegativeButtonText(textNegativeButton != null ? textNegativeButton : getContext().getString(R.string.text_nagative_dialog))
                .build();

        biometricPrompt.authenticate(promptInfo);

        keyBoardAdapter.setOnClickListener(new com.djamil.authenticate_utils.interfaces.OnClickListener() {
            @Override
            public void keyFingerPrintClicked() {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }

    public static class Result{
        boolean status;
        String message;

        public Result() {
        }

        public Result(boolean status, String message) {
            this.status = status;
            this.message = message;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "status=" + status +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    public OnResultAuth getOnResultAuth() {
        return onResultAuth;
    }

    public Authenticate setOnResultAuth(OnResultAuth onResultAuth) {
        this.onResultAuth = onResultAuth;
        return this;
    }

    public int increment(){
        return nbAttempts++;
    }

    public void setMsgError(String msg){
        textView.setError(msg);
        textView.requestFocus();
    }

    public Authenticate setErrorMsg(String errorMsg){
        this.errorMsg = errorMsg;
        return this;
    }

    public String getTextTitle() {
        return textTitle;
    }

    public Authenticate setTextTitle(String textTitle) {
        this.textTitle = textTitle;
        return this;
    }

    public String getTextDesc() {
        return textDesc;
    }

    public Authenticate setTextDesc(String textDesc) {
        this.textDesc = textDesc;
        return this;
    }

    public String getTextSubTitle() {
        return textSubTitle;
    }

    public Authenticate setTextSubTitle(String textSubTitle) {
        this.textSubTitle = textSubTitle;
        return this;
    }

    public String getTextNegativeButton() {
        return textNegativeButton;
    }

    public Authenticate setTextNegativeButton(String textNegativeButton) {
        this.textNegativeButton = textNegativeButton;
        return this;
    }

    public Authenticate setSecret(String secret, boolean secretIsMd5) {
        this.secret = secret;
        this.secretIsMd5 = secretIsMd5;

        return this;
    }

    public Button getDoneButton(){
        return ((Button)findViewById(R.id.done_btn));
    }

    public void cancelAuth(){
        biometricPrompt.cancelAuthentication();
    }
}
