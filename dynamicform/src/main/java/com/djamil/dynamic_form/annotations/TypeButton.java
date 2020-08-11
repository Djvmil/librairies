package com.djamil.dynamic_form.annotations;

import androidx.annotation.IntDef;

import com.djamil.dynamic_form.DynamicForm;

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
@IntDef({DynamicForm.BUTTON_BACK, DynamicForm.BUTTON_CANCEL, DynamicForm.BUTTON_DONE, DynamicForm.BUTTON_NEXT})
@Retention(RetentionPolicy.SOURCE)
@Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE})
public @interface TypeButton {
}
