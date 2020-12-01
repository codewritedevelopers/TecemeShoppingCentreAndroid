package org.codewrite.teceme.ui.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.snackbar.Snackbar;

import org.codewrite.teceme.MainActivity;
import org.codewrite.teceme.R;
import org.codewrite.teceme.model.form.LoginFormState;
import org.codewrite.teceme.model.rest.CustomerJson;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.viewmodel.AccountViewModel;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private AccountViewModel accountViewModel;
    private boolean finish_without_launching_another;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accountViewModel = ViewModelProviders.of(LoginActivity.this).get(AccountViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        finish_without_launching_another = getIntent()
                .getBooleanExtra("FINISH_WITHOUT_LAUNCHING_ANOTHER", false);
        if (!finish_without_launching_another
                ||getIntent().getBooleanExtra("LAUNCHED_FIRST_TIME", false)) {
                toolbar.setVisibility(View.GONE);
        }

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.action_login);
        TextView toSignup = findViewById(R.id.id_sign_up_from_login);
        TextView resetPassword = findViewById(R.id.id_forgot_password);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        // logout
        accountViewModel.logoutCustomer();

        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordEditText.getRight() -
                            passwordEditText.getCompoundDrawablesRelative()[DRAWABLE_RIGHT].getBounds().width())) {

                        Drawable[] compoundDrawables = passwordEditText.getCompoundDrawablesRelative();

                        if (accountViewModel.isPasswordMasked()) {
                            passwordEditText.setCompoundDrawablesRelative(null, null, getDrawable(R.drawable.icon_eye), null);
                            accountViewModel.setPasswordMasked(false);
                        } else {
                            passwordEditText.setCompoundDrawablesRelative(null, null, getDrawable(R.drawable.icon_eye_close), null);
                            accountViewModel.setPasswordMasked(true);
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        accountViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        accountViewModel.getLoginResult().observe(this, new Observer<CustomerJson>() {
            @Override
            public void onChanged(@Nullable CustomerJson loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.isStatus() == null) {
                    replaceWithLoggedInCustomer(loginResult);
                } else if (!loginResult.isStatus()) {
                    loginErrorAlert(loginResult.getMessage());
                }
            }
        });


        // observe if login user available
        accountViewModel.getLoggedInCustomer().observe(this, new Observer<CustomerEntity>() {
            @Override
            public void onChanged(CustomerEntity customerEntity) {
                if (customerEntity == null) {
                    return;
                }
                lunchMainActivity();
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
                accountViewModel.loginFormDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    accountViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                accountViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                checkInternetConnection();
            }
        });

        toSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

       resetPassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(LoginActivity.this, ForgottenPasswordActivity.class);
               startActivity(intent);
           }
       });
    }

    private void lunchMainActivity() {
        if (!finish_without_launching_another) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    private void loginErrorAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void replaceWithLoggedInCustomer(CustomerJson loginResult) {
        accountViewModel.replaceWIthLoggedInCustomer(loginResult);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                            Snackbar.make(findViewById(R.id.main_container),
                                    "No Internet Connection!", Snackbar.LENGTH_LONG)
                                    .setAction("Retry", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();
                        }
                    }
                });
    }
}
