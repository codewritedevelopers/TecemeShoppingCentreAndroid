package org.codewrite.teceme.ui.account;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.snackbar.Snackbar;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.form.ProfileFormState;
import org.codewrite.teceme.model.form.SignupFormState;
import org.codewrite.teceme.model.rest.CustomerJson;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.ui.others.ConfirmationActivity;
import org.codewrite.teceme.utils.ContentLoadingDialog;
import org.codewrite.teceme.viewmodel.AccountViewModel;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProfileActivity extends AppCompatActivity {

    private AccountViewModel accountViewModel;
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private TextView nameViewText;
    private TextView phoneViewText;
    private TextView usernameViewText;
    private TextView passwordViewText;
    private ImageView nameEditAction;
    private ImageView phoneEditAction;
    private ImageView usernameEditAction;
    private ImageView passwordEditAction;

    private Button updateButton;
    private CustomerEntity loggedInCustomer;
    private AccessTokenEntity mAccessTokenEntity;
    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accountViewModel = ViewModelProviders.of(ProfileActivity.this).get(AccountViewModel.class);

        nameEditText = findViewById(R.id.name);
        phoneEditText = findViewById(R.id.phone);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        nameViewText = findViewById(R.id.name_view);
        phoneViewText = findViewById(R.id.phone_view);
        usernameViewText = findViewById(R.id.username_view);
        passwordViewText = findViewById(R.id.password_view);
        updateButton = findViewById(R.id.action_update);

        nameEditAction= findViewById(R.id.action_edit_name);
        phoneEditAction = findViewById(R.id.action_edit_phone);
        usernameEditAction = findViewById(R.id.action_edit_username);
        passwordEditAction = findViewById(R.id.action_edit_password);

        loadingProgressBar = findViewById(R.id.loading);

        accountViewModel.getAccessToken().observe(this, new Observer<AccessTokenEntity>() {
            @Override
            public void onChanged(AccessTokenEntity accessTokenEntity) {
                if (accessTokenEntity == null) {
                    return;
                }
                mAccessTokenEntity = accessTokenEntity;
            }
        });

        accountViewModel.getLoggedInCustomer().observe(this, new Observer<CustomerEntity>() {
            @Override
            public void onChanged(CustomerEntity customerEntity) {
                if (customerEntity == null) {
                    launchLogin();
                    return;
                }
                loggedInCustomer = customerEntity;
                setupProfile(customerEntity);
            }
        });

        accountViewModel.getProfileFormState().observe(this, new Observer<ProfileFormState>() {
            @Override
            public void onChanged(@Nullable ProfileFormState profileFormState) {
                if (profileFormState == null) {
                    return;
                }
                updateButton.setEnabled(profileFormState.isDataValid());
                if (profileFormState.getNameError() != null) {
                    nameEditText.setError(getString(profileFormState.getNameError()));
                }
                if (profileFormState.getPhoneError() != null) {
                    phoneEditText.setError(getString(profileFormState.getPhoneError()));
                }
                if (profileFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(profileFormState.getUsernameError()));
                }
                if (profileFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(profileFormState.getPasswordError()));
                }
            }
        });

        accountViewModel.getProfileResult().observe(this, new Observer<CustomerJson>() {
            @Override
            public void onChanged(@Nullable CustomerJson profileResult) {
                if (profileResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (profileResult.isStatus() == null) {
                    updateSuccessAlert(profileResult);
                } else if (!profileResult.isStatus()) {
                    updateErrorAlert(profileResult.getMessage());
                }
            }
        });

        // disable update
        updateButton.setEnabled(false);
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateButton.setEnabled(true);
                accountViewModel.profileFormDataChanged(nameEditText.getText().toString(),
                        phoneEditText.getText().toString(),
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString().trim().isEmpty()
                                ?"000000": passwordEditText.getText().toString());
            }
        };
        nameEditText.addTextChangedListener(afterTextChangedListener);
        phoneEditText.addTextChangedListener(afterTextChangedListener);
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    updateButton.setEnabled(false);
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    updateCustomer();
                }
                return false;
            }
        });

        setOnEditClick(nameEditText,nameViewText,nameEditAction);
        setOnEditClick(phoneEditText,phoneViewText,phoneEditAction);
        setOnEditClick(usernameEditText,usernameViewText,usernameEditAction);
        setOnEditClick(passwordEditText,passwordViewText,passwordEditAction);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButton.setEnabled(false);
                loadingProgressBar.setVisibility(View.VISIBLE);
                updateCustomer();
            }
        });
    }

    private void updateSuccessAlert(CustomerJson profileResult) {
        Toast.makeText(this, "Update was successful", Toast.LENGTH_LONG).show();
        accountViewModel.replaceWIthLoggedInCustomer(profileResult);
        resetAllEdit();
    }

    private void updateCustomer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Verify Current Password");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_verify_password, null);
        final EditText currentPassword = view.findViewById(R.id.current_password);
        builder.setView(view);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accountViewModel.update(nameEditText.getText().toString(),
                        phoneEditText.getText().toString(),
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString().length()>5
                                ?passwordEditText.getText().toString()
                                :currentPassword.getText().toString(),
                        currentPassword.getText().toString(),
                        loggedInCustomer.getCustomer_id(),
                        mAccessTokenEntity.getToken());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                loadingProgressBar.setVisibility(View.GONE);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setupProfile(CustomerEntity customerEntity) {
        usernameEditText.setText(customerEntity.getCustomer_username());
        nameEditText.setText(customerEntity.getCustomer_first_name()
                .concat(" ").concat(customerEntity.getCustomer_middle_name() == null
                        ? "" : customerEntity.getCustomer_middle_name()).trim()
                .concat(" ").concat(customerEntity.getCustomer_last_name() == null
                        ? "" : customerEntity.getCustomer_last_name()));
        phoneEditText.setText(customerEntity.getCustomer_phone());

        usernameViewText.setText(customerEntity.getCustomer_username());
        nameViewText.setText(customerEntity.getCustomer_first_name()
                .concat(" ").concat(customerEntity.getCustomer_middle_name() == null
                        ? "" : customerEntity.getCustomer_middle_name())
                .trim()
                .concat(" ").concat(customerEntity.getCustomer_last_name() == null
                        ? "" : customerEntity.getCustomer_last_name()));
        phoneViewText.setText(customerEntity.getCustomer_phone());

        accountViewModel.profileFormDataChanged(nameEditText.getText().toString(),
                phoneEditText.getText().toString(),
                usernameEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    private void launchLogin() {
        Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void updateErrorAlert(String message) {
        loadingProgressBar.setVisibility(View.GONE);
        updateButton.setEnabled(true);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void setOnEditClick(final EditText editText, final TextView textView, ImageView imageView){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
            }
        });
    }

    public void resetAllEdit(){
        phoneEditText.setVisibility(View.GONE);
        nameEditText.setVisibility(View.GONE);
        usernameEditText.setVisibility(View.GONE);
        passwordEditText.setVisibility(View.GONE);

        phoneViewText.setVisibility(View.VISIBLE);
        nameViewText.setVisibility(View.VISIBLE);
        usernameViewText.setVisibility(View.VISIBLE);
        passwordViewText.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("CheckResult")
    private void checkInternetConnection() {
        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnectedToInternet) throws Exception {
                        if (!isConnectedToInternet) {
                            Snackbar.make(findViewById(R.id.main_container), "No Internet Connection!", Snackbar.LENGTH_INDEFINITE).show();
                        }
                    }
                });
    }
}
