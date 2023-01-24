package com.djamil.authenticate_utils.interfaces;

import androidx.annotation.Nullable;

import com.djamil.authenticate_utils.annotations.ErrorAuth;
import com.djamil.authenticate_utils.annotations.TypeAuth;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 7/28/20
 */

public interface OnResultAuth {
    void onAuthError(@ErrorAuth int errorCode);
    void onAuthFailed(@TypeAuth int typeAuth);
    void onAuthSucceeded(@Nullable String pwd, @Nullable String pwdMd5);
    void onAttempts(int nbAttempts);
    void onDoneClicked(@Nullable String pwd, @Nullable String pwdMd5, boolean isSuccess);
}
