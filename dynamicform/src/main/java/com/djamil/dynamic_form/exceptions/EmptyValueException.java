package com.djamil.dynamic_form.exceptions;

/**
 * Created by Djvmil_ on 5/20/20
 */

public class EmptyValueException extends RuntimeException {
    /**
     *
     */
    public EmptyValueException(String value){
        throw new RuntimeException("Le champ "+value+" \nest obligatoire");
    }
}