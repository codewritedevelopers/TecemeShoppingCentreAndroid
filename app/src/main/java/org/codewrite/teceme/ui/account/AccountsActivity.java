package org.codewrite.teceme.ui.account;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.snackbar.Snackbar;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.ui.orders.OrderActivity;
import org.codewrite.teceme.ui.others.HelpActivity;
import org.codewrite.teceme.ui.product.WishListActivity;
import org.codewrite.teceme.ui.wallet.WalletActivity;
import org.codewrite.teceme.viewmodel.AccountViewModel;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AccountsActivity extends AppCompatActivity {
    private AccountViewModel accountViewModel;
    private TextView nameView;
    private TextView walletIdView;
    private AccessTokenEntity accessToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        nameView = findViewById(R.id.name);
        walletIdView = findViewById(R.id.wallet_id);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accountViewModel = ViewModelProviders.of(AccountsActivity.this).get(AccountViewModel.class);
        accountViewModel.getAccessToken().observe(this, new Observer<AccessTokenEntity>() {
            @Override
            public void onChanged(AccessTokenEntity accessTokenEntity) {
                if (accessTokenEntity == null) {
                    launchLogin();
                }
                accessToken = accessTokenEntity;
            }
        });

        accountViewModel.getLoggedInCustomer().observe(this, new Observer<CustomerEntity>() {
            @Override
            public void onChanged(CustomerEntity customerEntity) {
                if (customerEntity == null) {
                    launchLogin();
                    return;
                }
                setupProfile(customerEntity);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchProfile();
            }
        });

        findViewById(R.id.wallet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchWalletActivity();
            }
        });

        findViewById(R.id.wish_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchWishListActivity();
            }
        });

        findViewById(R.id.orders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchOrdersActivity();
            }
        });


        findViewById(R.id.help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHelpActivity();
            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        findViewById(R.id.delete_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });

        findViewById(R.id.referral).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareReferralLink();
            }
        });
    }

    private void launchProfile() {
        startActivity(new Intent(AccountsActivity.this, ProfileActivity.class));
    }

    private void launchWalletActivity() {
        startActivity(new Intent(AccountsActivity.this, WalletActivity.class));
    }

    private void launchWishListActivity() {
        startActivity(new Intent(AccountsActivity.this, WishListActivity.class));
    }

    private void launchOrdersActivity() {
        startActivity(new Intent(AccountsActivity.this, OrderActivity.class));
    }


    private void launchHelpActivity() {
        startActivity(new Intent(AccountsActivity.this, HelpActivity.class));
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Account Logout!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accountViewModel.logoutCustomer();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setMessage("Do you want to logout ?");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Account Deletion!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accountViewModel.deleteAccount(accessToken.getToken());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setMessage("Are you sure you want to delete your account ? This process will log you out.");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void launchLogin() {
        Intent i = new Intent(AccountsActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void shareReferralLink() {

    }

    private void setupProfile(CustomerEntity customerEntity) {
        nameView.setText(customerEntity.getCustomer_first_name()
                .concat(" ").concat(customerEntity.getCustomer_middle_name() == null
                        ? "" : customerEntity.getCustomer_middle_name())
                .concat(" ").concat(customerEntity.getCustomer_last_name() == null
                        ? "" : customerEntity.getCustomer_last_name()));
        walletIdView.setText(customerEntity.getCustomer_phone());
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkInternetConnection();
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
        ReactiveNetwork
                .observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnectedToInternet) throws Exception {
                        if (isConnectedToInternet) {
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
                                                                checkInternetConnection();
                                                            }
                                                        }).show();
                                            }
                                        }
                                    });
                        } else {
                            Snackbar.make(findViewById(R.id.main_container),
                                    "No Network Available", Snackbar.LENGTH_LONG)
                                    .setAction("Retry", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            checkInternetConnection();
                                        }
                                    }).show();
                        }
                    }
                });


    }
}
