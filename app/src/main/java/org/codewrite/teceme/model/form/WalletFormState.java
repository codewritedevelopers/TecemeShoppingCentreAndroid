package org.codewrite.teceme.model.form;

import androidx.annotation.Nullable;

/**
 * Data validation state of the wallet form.
 */
public class WalletFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer secretAnswerError;
    private boolean isDataValid;

    public WalletFormState(@Nullable Integer usernameError,  @Nullable Integer passwordError,
                           @Nullable Integer secretAnswerError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.secretAnswerError = secretAnswerError;
        this.isDataValid = false;
    }

    public WalletFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.secretAnswerError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    public Integer getSecretAnswerError() {
        return secretAnswerError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
