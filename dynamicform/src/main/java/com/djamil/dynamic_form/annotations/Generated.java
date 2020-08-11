package com.djamil.dynamic_form.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.PARAMETER})
public @interface Generated {
    String[] value();

    String date() default "";

    String comments() default "";
}
