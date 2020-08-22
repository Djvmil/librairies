package com.suntelecoms.authenticate.fingerprint;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 3/12/20.
 */
public interface FingerPrintListener {

    void onSuccess();

    void onFailed();

    void onError(CharSequence errorString);

    void onHelp(CharSequence helpString);

}

