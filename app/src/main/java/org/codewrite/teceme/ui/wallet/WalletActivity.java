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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_accounts);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        walletViewModel = ViewModelProviders.of(WalletActivity.this).get(WalletViewModel.class);


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

        walletViewModel.getWalletResult().observe(this, new Observer<WalletJson>() {
            @Override
            public void onChanged(@Nullable WalletJson walletResult) {
                if (walletResult == null) {
                    return;
                }

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
