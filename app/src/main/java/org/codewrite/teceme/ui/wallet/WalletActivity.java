package org.codewrite.teceme.ui.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.form.WalletFormState;
import org.codewrite.teceme.model.rest.WalletJson;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.ui.account.LoginActivity;
import org.codewrite.teceme.ui.others.ConfirmationActivity;
import org.codewrite.teceme.viewmodel.WalletViewModel;

import java.util.Objects;

public class WalletActivity extends AppCompatActivity {

   private WalletViewModel walletViewModel;
    private CustomerEntity loggedInCustomer;
    private AccessTokenEntity mAccessTokenEntity;
    private String secretQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        walletViewModel = ViewModelProviders.of(WalletActivity.this).get(WalletViewModel.class);

        //avoid null exception
        secretQuestion = "";
        final EditText phoneEditText = findViewById(R.id.phone);
        final EditText secretAnswerEditText = findViewById(R.id.secret_answer);
        final EditText pinCodeEditText = findViewById(R.id.pin_code);
        final EditText confirmPinCodeEditText = findViewById(R.id.confirm_pin_code);
        final Button createWalletButton = findViewById(R.id.create_wallet);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        Spinner questions = findViewById(R.id.secret_question);

        // We create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.wallet_secret_questions, android.R.layout.simple_spinner_item);
        // We specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // We apply the adapter to the questions spinner
        questions.setAdapter(adapter);

        // set default item
        secretQuestion = Objects.requireNonNull(adapter.getItem(0)).toString();

        questions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secretQuestion = Objects.requireNonNull(adapter.getItem(position)).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        walletViewModel.getAccessToken().observe(this, new Observer<AccessTokenEntity>() {
            @Override
            public void onChanged(AccessTokenEntity accessTokenEntity) {
                if (accessTokenEntity == null) {
                  launchLogin();
                    return;
                }
                mAccessTokenEntity = accessTokenEntity;
            }
        });

        walletViewModel.getLoggedInCustomer().observe(this, new Observer<CustomerEntity>() {
            @Override
            public void onChanged(CustomerEntity customerEntity) {
                if (customerEntity == null) {
                    launchLogin();
                    return;
                }
                loggedInCustomer = customerEntity;
            }
        });


        // We observe the wallet form state
        walletViewModel.getWalletFormState().observe(this, new Observer<WalletFormState>() {
            @Override
            public void onChanged(@Nullable WalletFormState walletFormState) {
                if (walletFormState == null) {
                    return;
                }
                createWalletButton.setEnabled(walletFormState.isDataValid());

                if (walletFormState.getPhoneError() != null) {
                    phoneEditText.setError(getString(walletFormState.getPhoneError()));
                }
                if (walletFormState.getSecretAnswerError() != null) {
                    secretAnswerEditText.setError(getString(walletFormState.getSecretAnswerError()));
                }
                if (walletFormState.getPinCodeError() != null) {
                    pinCodeEditText.setError(getString(walletFormState.getPinCodeError()));
                }
                if (walletFormState.getConfirmPinCodeError() != null) {
                    confirmPinCodeEditText.setError(getString(walletFormState.getConfirmPinCodeError()));
                }
            }
        });

        walletViewModel.getWalletResult().observe(this, new Observer<WalletJson>() {
            @Override
            public void onChanged(@Nullable WalletJson walletResult) {
                if (walletResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (walletResult.isStatus() == null
                        && !walletResult.getWallet_owner().isEmpty()) {
                    launchConfirmationActivity(loggedInCustomer.getCustomer_username(),
                            walletResult.getWallet_id());
                } else if (!walletResult.isStatus()) {
                    createWalletErrorAlert(walletResult.getMessage());
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
                walletViewModel.walletFormDataChanged(pinCodeEditText.getText().toString(),
                        confirmPinCodeEditText.getText().toString(),
                        secretAnswerEditText.getText().toString());
            }
        };
        phoneEditText.addTextChangedListener(afterTextChangedListener);
        secretAnswerEditText.addTextChangedListener(afterTextChangedListener);
        pinCodeEditText.addTextChangedListener(afterTextChangedListener);
        confirmPinCodeEditText.addTextChangedListener(afterTextChangedListener);
        confirmPinCodeEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    walletViewModel.create(loggedInCustomer.getCustomer_id(),
                            secretQuestion,
                            secretAnswerEditText.getText().toString(),
                            pinCodeEditText.getText().toString(),
                            mAccessTokenEntity.getToken());
                }
                return false;
            }
        });

        createWalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                walletViewModel.create(loggedInCustomer.getCustomer_id(),
                        secretQuestion,
                        secretAnswerEditText.getText().toString(),
                        pinCodeEditText.getText().toString(),
                        mAccessTokenEntity.getToken());
            }
        });
    }

    private void launchConfirmationActivity(String email,String wallet_id) {
        Intent i = new Intent(WalletActivity.this, ConfirmationActivity.class);
        i.putExtra(ConfirmationActivity.LAUNCH_KEY,"CREATE_WALLET_CONFIRMATION");
        i.putExtra(ConfirmationActivity.LAUNCH_EMAIL,email);
        i.putExtra(ConfirmationActivity.LAUNCH_WALLET_ID, wallet_id);
        startActivity(i);
        finish();
    }

    private void createWalletErrorAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void launchLogin() {
        Intent i = new Intent(WalletActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            finish();
    }
}
