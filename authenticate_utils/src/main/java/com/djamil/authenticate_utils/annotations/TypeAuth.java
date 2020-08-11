package com.djamil.authenticate_utils.annotations;

import androidx.annotation.IntDef;

import com.djamil.authenticate_utils.Authenticate;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */
@Documented
@IntDef({Authenticate.IMPRIMED, Authenticate.CODED})
@Retention(RetentionPolicy.SOURCE)
@Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE})
public @interface TypeAuth {
}
