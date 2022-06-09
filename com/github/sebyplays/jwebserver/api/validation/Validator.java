package com.github.sebyplays.jwebserver.api.validation;

public abstract class Validator {

    private String message;

    public abstract boolean isValid(Object... args);

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = this.getClass().getSimpleName() + ": " + message;
    }

}
