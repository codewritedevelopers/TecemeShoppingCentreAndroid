package org.codewrite.teceme.model.rest;

import androidx.room.Ignore;

public class Result {
    @Ignore
    private Boolean status;
    @Ignore
    private String message;

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
