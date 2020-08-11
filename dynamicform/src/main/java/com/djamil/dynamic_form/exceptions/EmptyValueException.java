package com.djamil.dynamic_form.exceptions;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */
public class EmptyValueException extends RuntimeException {
    /**
     *
     */
    public EmptyValueException(String value){
        throw new RuntimeException("Le champ "+value+" \nest obligatoire");
    }
}