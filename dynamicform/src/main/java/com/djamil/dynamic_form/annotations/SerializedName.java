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
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface SerializedName {

    String value();

    String[] alternate() default {};
}
