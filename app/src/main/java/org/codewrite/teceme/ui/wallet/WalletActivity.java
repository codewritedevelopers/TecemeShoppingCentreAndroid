package org.codewrite.teceme.ui.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
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
import org.codewrite.teceme.model.rest.WalletLogJson;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.ui.account.LoginActivity;
import org.codewrite.teceme.ui.others.ConfirmationActivity;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.WalletViewModel;

import java.util.Objects;

public class WalletActivity extends AppCompatActivity {

   private WalletViewModel walletViewModel;
   private AccountViewModel accountViewModel;
    private CustomerEntity loggedInCustomer;
    private AccessTokenEntity mAccessTokenEntity;
    private String mTransType;
    private TextView mTransactionToTextView;
    private TextView mAmount;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_accounts);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        accountViewModel = ViewModelProviders.of(WalletActivity.this).get(AccountViewModel.class);

        accountViewModel.getAccessToken().observe(this, new Observer<AccessTokenEntity>() {
            @Override
            public void onChanged(AccessTokenEntity accessTokenEntity) {
                if (accessTokenEntity == null) {
                  launchLogin();
                    return;
                }
                mAccessTokenEntity = accessTokenEntity;
                walletViewModel = ViewModelProviders.of(WalletActivity.this).get(WalletViewModel.class);
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

                setupWalletTransaction();
            }
        });

        walletViewModel.getWalletResult().observe(this, new Observer<WalletJson>() {
            @Override
            public void onChanged(@Nullable WalletJson walletResult) {
                if (walletResult == null) {
                    return;
                }

            }
        });

    }

    private void setupWalletTransaction() {

        mTransactionToTextView = findViewById(R.id.transaction_to);
        mAmount= findViewById(R.id.amount);
        submitButton = findViewById(R.id.submit);
        Spinner transactionType = findViewById(R.id.transaction_type);

        // We create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.wallet_transaction_type, android.R.layout.simple_spinner_item);
        // We specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // We apply the adapter to the questions spinner
        transactionType.setAdapter(adapter);

        // set default item
        mTransType = "Deposit";

        transactionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    mTransType = "Deposit";
                    mTransactionToTextView.setVisibility(View.GONE);
                }else{
                    mTransType = "Transfer";
                    mTransactionToTextView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(mAmount.getText().toString()) <= 0){
                    Toast.makeText(WalletActivity.this, "Amount can't be negative!", Toast.LENGTH_SHORT).show();
                    return;
                }
                walletViewModel.submitTransaction(loggedInCustomer.getCustomer_id(),
                        Long.parseLong(mAmount.getText().toString()),
                        mTransType,mTransactionToTextView.getText().toString(),mAccessTokenEntity.getToken());
            }
        });
        walletViewModel.getWalletLogResult()
                .observe(this, new Observer<WalletLogJson>() {
            @Override
            public void onChanged(WalletLogJson walletLogJson) {
                if (walletLogJson==null){
                    return;
                }
                if (!walletLogJson.isStatus()) {
                    mTransactionToTextView.setText(null);
                    mAmount.setText(null);
                }

                Toast.makeText(WalletActivity.this, walletLogJson.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void launchLogin() {
        Intent i = new Intent(WalletActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

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
}
