package org.codewrite.teceme.ui.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
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
import org.codewrite.teceme.model.rest.Result;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.ui.others.ConfirmationActivity;
import org.codewrite.teceme.viewmodel.AccountViewModel;

import java.util.regex.Pattern;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ForgottenPasswordActivity extends AppCompatActivity {

    private AccountViewModel accountViewModel;
    private boolean finish_without_launching_another;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);
        accountViewModel = ViewModelProviders.of(ForgottenPasswordActivity.this).get(AccountViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        finish_without_launching_another = getIntent()
                .getBooleanExtra("FINISH_WITHOUT_LAUNCHING_ANOTHER", false);
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("LAUNCHED_FIRST_TIME")
                    && !finish_without_launching_another) {
                toolbar.setVisibility(View.GONE);
            }
        }
        if (!finish_without_launching_another) {
            toolbar.setVisibility(View.GONE);
        }

        final EditText usernameEditText = findViewById(R.id.username);
        ;
        final Button sendButton = findViewById(R.id.action_send);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Patterns.EMAIL_ADDRESS.matcher(usernameEditText.getText().toString()).matches()) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    accountViewModel.resetPassword(usernameEditText.getText().toString());
                }else{
                    usernameEditText.setError(getString(R.string.invalid_username));
                }
                checkInternetConnection();
            }
        });

        accountViewModel.getResetPasswordResult()
                .observe(this, new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        loadingProgressBar.setVisibility(View.GONE);
                        if (result == null) {
                            return;
                        }
                        Toast.makeText(ForgottenPasswordActivity.this.getApplicationContext(),
                                result.getMessage(), Toast.LENGTH_SHORT).show();

                        if (result.isStatus()) {
                            Intent intent = new Intent(ForgottenPasswordActivity.this, ConfirmationActivity.class);
                            intent.putExtra(ConfirmationActivity.LAUNCH_KEY, "RESET_PASSWORD_CONFIRMATION");
                            intent.putExtra(ConfirmationActivity.LAUNCH_EMAIL, usernameEditText.getText().toString());
                            startActivity(intent);
                            finish();
                        }
                    }
                });
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
