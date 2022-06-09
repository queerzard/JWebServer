package com.github.sebyplays.jwebserver.utils.exceptions;

/**
 * This class is used to throw an exception when a context is already assigned to a variable
 */
public class ContextAlreadyAssignedException extends Exception{

    public ContextAlreadyAssignedException(String message){
        super(message);
    }
}
