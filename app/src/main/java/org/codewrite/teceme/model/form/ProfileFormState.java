package org.codewrite.teceme.model.form;

import androidx.annotation.Nullable;

/**
 * Data validation state of the profile form.
 */
public class ProfileFormState {

    @Nullable
    private Integer nameError;
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer confirmPasswordError;
    @Nullable
    private Integer phoneError;
    private boolean isDataValid;

    public ProfileFormState(@Nullable Integer nameError, @Nullable Integer phoneError,
                            @Nullable Integer usernameError, @Nullable Integer passwordError,
                            @Nullable Integer confirmPasswordError) {
        this.nameError = nameError;
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.confirmPasswordError = confirmPasswordError;
        this.phoneError = phoneError;
    }

    public ProfileFormState(boolean isDataValid) {
        this.nameError = null;
        this.phoneError = null;
        this.usernameError = null;
        this.passwordError = null;
        this.confirmPasswordError = null;
        this.isDataValid = isDataValid;
    }


    @Nullable
    public Integer getNameError() {
        return nameError;
    }

    @Nullable
    public Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    public Integer getPhoneError() {
        return phoneError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    @Nullable
    public Integer getConfirmPasswordError() {
        return confirmPasswordError;
    }
}
