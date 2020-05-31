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
    private EditText cPasswordEditText;
    private EditText passwordEditText;
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
        cPasswordEditText = findViewById(R.id.confirm_password);
        updateButton = findViewById(R.id.action_update);

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
                if (profileFormState.getConfirmPasswordError() != null) {
                    cPasswordEditText.setError(getString(profileFormState.getConfirmPasswordError()));
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
                accountViewModel.profileFormDataChanged(nameEditText.getText().toString(),
                        phoneEditText.getText().toString(),
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        cPasswordEditText.getText().toString());
            }
        };
        nameEditText.addTextChangedListener(afterTextChangedListener);
        phoneEditText.addTextChangedListener(afterTextChangedListener);
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        cPasswordEditText.addTextChangedListener(afterTextChangedListener);
        cPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                   updateCustomer();
                }
                return false;
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
               updateCustomer();
            }
        });
    }

    private void updateSuccessAlert(CustomerJson profileResult) {
        Toast.makeText(this, "Update successful, we're logout you out!", Toast.LENGTH_LONG).show();
        accountViewModel.replaceWIthLoggedInCustomer(profileResult);
    }

    private void updateCustomer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Verify Current Password");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_verify_password,null);
        final EditText currentPassword = view.findViewById(R.id.current_password);
        builder.setView(view);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accountViewModel.update(nameEditText.getText().toString(),
                        phoneEditText.getText().toString(),
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString().isEmpty()
                                ?null:passwordEditText.getText().toString(),
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
                        ?"":customerEntity.getCustomer_middle_name())
        .concat(" ").concat(customerEntity.getCustomer_last_name()==null
                        ?"":customerEntity.getCustomer_last_name()));
        phoneEditText.setText(customerEntity.getCustomer_phone());

        accountViewModel.profileFormDataChanged(nameEditText.getText().toString(),
                phoneEditText.getText().toString(),
                usernameEditText.getText().toString(),
                passwordEditText.getText().toString(),
                cPasswordEditText.getText().toString());
    }

    private void launchLogin() {
        Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void updateErrorAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onStart() {
        super.onStart();
        checkInternetConnection();
    }

    @SuppressLint("CheckResult")
    private  void checkInternetConnection(){
        ReactiveNetwork
                .observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnectedToInternet) throws Exception {
                        if (isConnectedToInternet){
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
                        }else{
                            Snackbar.make(findViewById(R.id.main_container),"No Network Available", Snackbar.LENGTH_INDEFINITE).show();
                        }
                    }
                });


    }
}
