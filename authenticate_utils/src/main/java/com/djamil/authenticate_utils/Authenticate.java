package com.djamil.authenticate_utils;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.djamil.authenticate_utils.fingerprint.FingerPrintHandler;
import com.djamil.authenticate_utils.interfaces.OnResultAuth;
import com.djamil.utils.FonctionUtils;

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
    protected static Activity activity;
    private RelativeLayout rootLayout;
    private Button doneBtn;
    private LayoutInflater inflater;
    private RecyclerView recyclerView;

    private String secret;
    private boolean secretIsMd5 = false;

    private String textTitle, textDesc, textSubTitle, textNegativeButton, errorMsg;

    private static final int ZERO = 0;

    private boolean isShuffle = true;
    private int colorKey = 0;
    private Drawable iconFingerPrint = null;
    private Drawable iconNoFingerPrint = null;
    private Drawable iconBackSpace = null;


    private KeyBoardAdapter keyBoardAdapter;

    private boolean userFingerPrint = false;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    public OnResultAuth onResultAuth;
    private int nbAttempts = 0;

    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";

    public Authenticate(Context context) {
        super(context);
    }

    public Authenticate(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        activity = ((Activity)ctx);
        inflater =  LayoutInflater.from(ctx);

        TypedArray attr = activity.getTheme().obtainStyledAttributes(attrs, R.styleable.DynamicKeyBoard, 0, 0);

        Log.e(TAG, "Authenticate: color "+colorKey );

        try {
            colorKey          = attr.getColor(R.styleable.DynamicKeyBoard_color_field, ZERO);
            iconBackSpace     = attr.getDrawable(R.styleable.DynamicKeyBoard_icon_backspace);
            iconFingerPrint   = attr.getDrawable(R.styleable.DynamicKeyBoard_icon_fingerprint);
            iconNoFingerPrint = attr.getDrawable(R.styleable.DynamicKeyBoard_icon_no_fingerprint);

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
        recyclerView = findViewById(R.id.recyclerviewKeyBoard);
        doneBtn = findViewById(R.id.done_btn);
        textView.setKeyListener(null);

        doneBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!textView.getText().toString().isEmpty()){
                    if (secret != null){
                        boolean tmp = false;
                        if (secretIsMd5 && secret.equals(FonctionUtils.md5Hash(textView.getText().toString().trim()))){
                            tmp = true;
                            new SessionManager().setUseFingerprint(true);
                            onResultAuth.onAuthSucceeded(textView.getText().toString().trim(), FonctionUtils.md5Hash(textView.getText().toString().trim()));
                            onResultAuth.onDoneClicked(textView.getText().toString().trim(), FonctionUtils.md5Hash(textView.getText().toString().trim()), tmp);

                        } else if (!secretIsMd5 && secret.equals(textView.getText().toString().trim())){
                            tmp = true;
                            new SessionManager().setUseFingerprint(true);
                            onResultAuth.onAuthSucceeded(textView.getText().toString().trim(), FonctionUtils.md5Hash(textView.getText().toString().trim()));
                            onResultAuth.onDoneClicked(textView.getText().toString().trim(), FonctionUtils.md5Hash(textView.getText().toString().trim()), tmp);

                        }else{
                            onResultAuth.onAuthFailed(CODED);
                            onResultAuth.onDoneClicked(null, null, tmp);
                        }

                    }else
                        onResultAuth.onDoneClicked(textView.getText().toString(), FonctionUtils.md5Hash(textView.getText().toString()), false);

                    onResultAuth.onAttempts(increment());
                }else {
                    textView.setError(errorMsg != null ? errorMsg : "Entrez votre le code secret");
                    textView.requestFocus();
                }
            }
        });

        notifyChange();
    }

    private void notifyChange(){

        keyBoardAdapter = new KeyBoardAdapter(activity, textView, isShuffle, colorKey, iconBackSpace, iconFingerPrint, iconNoFingerPrint, userFingerPrint);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 4));
        recyclerView.setAdapter(keyBoardAdapter);

        //keyBoardAdapter.shuffleKey();
        keyBoardAdapter.notifyDataSetChanged();
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
