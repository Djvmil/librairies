package com.djamil.authenticate_utils.fingerprint;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;

import com.djamil.authenticate_utils.Authenticate;
import com.djamil.authenticate_utils.SessionManager;
import com.djamil.authenticate_utils.interfaces.OnResultAuth;
import com.djamil.utils.FonctionUtils;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 7/28/20
 */

public class FingerPrintHandler extends BiometricPrompt.AuthenticationCallback{

    private OnResultAuth onResultAuth;
    private int nbAttempts = 0;
    private String secret;
    private boolean isMd5;

    public FingerPrintHandler(OnResultAuth onResultAuth, String secret, boolean isMd5) {
        this.onResultAuth = onResultAuth;
        this.secret = secret;
        this.isMd5 = isMd5;
    }

    @Override
    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
            // user clicked negative button
        } else {
            //TODO: Called when an unrecoverable error has been encountered and the operation is complete.
            onResultAuth.onAuthError(Authenticate.TO_MANY_ATTEMPTS);
            onResultAuth.onAttempts(increment());
        }
    }

    @Override
    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        //TODO: Called when a biometric is recognized.
        if (isMd5)
            onResultAuth.onAuthSucceeded(null, secret);
        else
            onResultAuth.onAuthSucceeded(secret, FonctionUtils.md5Hash(secret));

        onResultAuth.onAttempts(increment());
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        //TODO: Called when a biometric is valid but not recognized.
        onResultAuth.onAuthError(Authenticate.AUTH_FAIL);
        onResultAuth.onAuthFailed(Authenticate.IMPRIMED);
        onResultAuth.onAttempts(increment());
    }
    public int increment(){
        if (nbAttempts >= 3)
            new SessionManager().setUseFingerprint(false);
        return nbAttempts++;
    }
}
