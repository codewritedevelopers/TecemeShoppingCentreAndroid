package org.codewrite.teceme.model.form;

import androidx.annotation.Nullable;

/**
 * Data validation state of the sign up form.
 */
public class SignupFormState {
    @Nullable
    private Integer nameError;
    @Nullable
    private Integer phoneError;
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer confirmPasswordError;
    private boolean isDataValid;

    public SignupFormState(@Nullable Integer nameError, @Nullable Integer phoneError,
                    @Nullable Integer usernameError, @Nullable Integer passwordError,
                           @Nullable Integer cPasswordError) {
        this.nameError = nameError;
        this.phoneError = phoneError;
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.confirmPasswordError = cPasswordError;
        this.isDataValid = false;
    }

    public SignupFormState(boolean isDataValid) {
        this.nameError = null;
        this.phoneError = null;
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getNameError() {
        return nameError;
    }

    @Nullable
    public Integer getPhoneError() {
        return phoneError;
    }

    @Nullable
    public Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
   public Integer getPasswordError() {
        return passwordError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    @Nullable
    public Integer getConfirmPasswordError() {
        return confirmPasswordError;
    }
}
