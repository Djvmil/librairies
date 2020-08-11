package com.djamil.dynamic_form.annotations;

import androidx.annotation.StringDef;

import com.djamil.dynamic_form.INPUT_TYPE_DF;

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
@StringDef({INPUT_TYPE_DF.Double, INPUT_TYPE_DF.Select, INPUT_TYPE_DF.String, INPUT_TYPE_DF.Integer, INPUT_TYPE_DF.Separator,
        INPUT_TYPE_DF.Int, INPUT_TYPE_DF.Number, INPUT_TYPE_DF.Phone, INPUT_TYPE_DF.Email, INPUT_TYPE_DF.Text,
        INPUT_TYPE_DF.Password, INPUT_TYPE_DF.CheckBox, INPUT_TYPE_DF.Radio, INPUT_TYPE_DF.Spinner, INPUT_TYPE_DF.Time,
        INPUT_TYPE_DF.Country, INPUT_TYPE_DF.Date, INPUT_TYPE_DF.Label, INPUT_TYPE_DF.Sign, INPUT_TYPE_DF.Photo, INPUT_TYPE_DF.ImageView})
@Retention(RetentionPolicy.SOURCE)
@Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE})
public @interface InputTypeDF {
}
