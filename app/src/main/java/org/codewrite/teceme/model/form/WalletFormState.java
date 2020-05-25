package org.codewrite.teceme.model.form;

import androidx.annotation.Nullable;

/**
 * Data validation state of the wallet form.
 */
public class WalletFormState {
    @Nullable
    private Integer phoneError;
    @Nullable
    private Integer pinCodeError;
    @Nullable
    private Integer confirmPinCodeError;
    @Nullable
    private Integer secretAnswerError;
    private boolean isDataValid;

    public WalletFormState(@Nullable Integer phoneError,  @Nullable Integer pinCodeError,
                           @Nullable Integer confirmPinCodeError,@Nullable Integer secretAnswerError) {
        this.phoneError = phoneError;
        this.pinCodeError = pinCodeError;
        this.confirmPinCodeError = confirmPinCodeError;
        this.secretAnswerError = secretAnswerError;
        this.isDataValid = false;
    }

    public WalletFormState(boolean isDataValid) {
        this.pinCodeError = null;
        this.phoneError = null;
        this.confirmPinCodeError=null;
        this.secretAnswerError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getSecretAnswerError() {
        return secretAnswerError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    @Nullable
    public Integer getPhoneError() {
        return phoneError;
    }

    @Nullable
    public Integer getConfirmPinCodeError() {
        return confirmPinCodeError;
    }

    @Nullable
    public Integer getPinCodeError() {
        return pinCodeError;
    }
}
