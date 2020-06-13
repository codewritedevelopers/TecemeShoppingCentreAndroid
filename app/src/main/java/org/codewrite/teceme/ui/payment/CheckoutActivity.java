package org.codewrite.teceme.ui.payment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.rest.CardMethodJson;
import org.codewrite.teceme.model.rest.MobileMethodJson;
import org.codewrite.teceme.model.rest.Result;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CartEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.ui.account.LoginActivity;
import org.codewrite.teceme.ui.others.ConfirmationActivity;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.CartViewModel;
import org.codewrite.teceme.viewmodel.CheckOutViewModel;
import org.codewrite.teceme.viewmodel.WalletViewModel;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    private CartViewModel cartViewModel;
    private ImageView methodImage;
    private AccountViewModel accountViewModel;
    private CheckOutViewModel checkOutViewModel;

    private AccessTokenEntity tokenEntity;
    private WalletViewModel walletViewModel;
    private CustomerEntity loggedInCustomer;
    private TextView subTotalView;
    private TextView totalView;
    private Button checkoutButton;
    private ProgressBar loadingProgressBar;
    private EditText panText, cvvText,
            expMonthText, expYearText, cardHolderText;
    private EditText accountNumber;
    TextView issuerCardView,issuerMobileView;
    private String issuer ="";
    AlertDialog alertDialog;


    public static final String VISA_PREFIX = "4";
    public static final String MASTERCARD_PREFIX = "51,52,53,54,55,";
    public static final String DISCOVER_PREFIX = "6011";
    public static final String AMEX_PREFIX = "34,37,";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        accountViewModel = ViewModelProviders.of(CheckoutActivity.this).get(AccountViewModel.class);
        checkOutViewModel = ViewModelProviders.of(CheckoutActivity.this).get(CheckOutViewModel.class);
        walletViewModel = ViewModelProviders.of(CheckoutActivity.this).get(WalletViewModel.class);

        methodImage = findViewById(R.id.id_payment_method_image);
        subTotalView = findViewById(R.id.id_sub_total);
        totalView = findViewById(R.id.id_total);
        checkoutButton = findViewById(R.id.id_checkout);
        loadingProgressBar = findViewById(R.id.loading);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView editPaymentInfo = findViewById(R.id.edit_payment);
        editPaymentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog!=null) {
                    alertDialog.show();
                }
            }
        });
        setSupportActionBar(toolbar);

        accountViewModel.getAccessToken().observe(this, new Observer<AccessTokenEntity>() {
            @Override
            public void onChanged(AccessTokenEntity accessTokenEntity) {
                if (accessTokenEntity == null) {
                    launchLogin();
                    return;
                }
                tokenEntity = accessTokenEntity;
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

                String method = getIntent().getStringExtra("METHOD");
                if (method == null) {
                    Toast.makeText(getApplicationContext(), "Invalid Method", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    setup(method);
                }
            }
        });


    }

    private void launchLogin() {
        Intent i = new Intent(CheckoutActivity.this, LoginActivity.class);
        i.putExtra("FINISH_WITHOUT_LAUNCHING_ANOTHER", true);
        startActivity(i);
    }

    @SuppressLint("SetTextI18n")
    private void setup(final String method) {
        checkoutButton.setEnabled(false);
         issuerCardView = findViewById(R.id.card_issuer);
        issuerMobileView = findViewById(R.id.mobile_issuer);

        switch (method) {
            case "teceme_pay":
                issuer = "1";
                issuerMobileView.setText("TECEME PAY");
                methodImage.setImageResource(R.drawable.oder_online);
                getMobileDetail();
                break;
            case "mtn_momo":
                issuer = "2";
                issuerMobileView.setText("MTN");
                methodImage.setImageResource(R.drawable.mtn_momo);
                getMobileDetail();
                break;
            case "airtel_tigo_money":
                issuer = "3";
                issuerMobileView.setText("AIRTEL TIGO");
                methodImage.setImageResource(R.drawable.tigo_airtel_money);
                getMobileDetail();
                break;
            case "vodafone_cash":
                issuer = "5";
                issuerMobileView.setText("VODAFONE");
                methodImage.setImageResource(R.drawable.vodafone_cash);
                getMobileDetail();
                break;
            case "bank_card":
                methodImage.setImageResource(R.drawable.bank_card);
                getCardDetail();
                break;
        }

        cartViewModel.getCartsTotal(loggedInCustomer.getCustomer_id())
                .observe(CheckoutActivity.this, new Observer<Long>() {
                    @Override
                    public void onChanged(Long total) {
                        if (total == null) {
                            checkoutButton.setEnabled(false);
                            Toast.makeText(CheckoutActivity.this, "Something went wrong - checkout error 1", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        checkoutButton.setEnabled(true);
                        String cedis = "GH₵ ";
                        subTotalView.setText(cedis.concat(String.valueOf(total)));
                        totalView.setText(cedis.concat(String.valueOf(total)));
                    }
                });

        cartViewModel.getCartsEntity(loggedInCustomer.getCustomer_id())
                .observe(CheckoutActivity.this, new Observer<List<CartEntity>>() {
                    @Override
                    public void onChanged(final List<CartEntity> cartEntities) {
                        if (cartEntities == null) {
                            checkoutButton.setEnabled(false);
                            Toast.makeText(CheckoutActivity.this, "Something went wrong - checkout error 2", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // show alert
                        alertDialog.show();

                        checkoutButton.setEnabled(true);
                        checkoutButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkoutButton.setEnabled(false);
                                loadingProgressBar.setVisibility(View.VISIBLE);
                                if (method.equals("bank_card")) {
                                    checkOutViewModel.checkoutCustomer(cartEntities,
                                            new CardMethodJson(panText.getText().toString(),
                                                    cvvText.getText().toString(),
                                                    expMonthText.getText().toString(),
                                                    expYearText.getText().toString(),
                                                    issuerCardView.getText().toString().substring(0,3),
                                                    cardHolderText.getText().toString(),
                                                    "GHS"), tokenEntity.getToken());
                                } else {
                                    checkOutViewModel.checkoutCustomer(cartEntities,
                                            new MobileMethodJson(accountNumber.getText().toString(),issuer), tokenEntity.getToken());
                                }

                            }
                        });
                    }
                });

        checkOutViewModel.getCheckOutResult()
                .observe(CheckoutActivity.this, new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        if (result == null) {
                            checkoutButton.setEnabled(true);
                            loadingProgressBar.setVisibility(View.GONE);
                            return;
                        }
                        Toast.makeText(CheckoutActivity.this.getApplicationContext(),
                                result.getMessage(), Toast.LENGTH_SHORT).show();

                        if (result.isStatus()) {
                            cartViewModel.clearOldCart("");
                            Intent intent = new Intent(CheckoutActivity.this, ConfirmationActivity.class);
                            intent.putExtra(ConfirmationActivity.LAUNCH_KEY, "CHECK_OUT_CONFIRMATION");
                            intent.putExtra(ConfirmationActivity.LAUNCH_EMAIL, loggedInCustomer.getCustomer_username());
                            startActivity(intent);
                            CheckoutActivity.this.finish();
                        } else {
                            checkoutButton.setEnabled(true);
                            loadingProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void getCardDetail() {
        findViewById(R.id.card_method_info).setVisibility(View.VISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("BANK CARD INFORMATION");
        View view = View.inflate(this, R.layout.card_information, null);
        panText = view.findViewById(R.id.card_number);
        cvvText = view.findViewById(R.id.cvv);
        expMonthText = view.findViewById(R.id.exp_month);
        expYearText = view.findViewById(R.id.exp_year);
        cardHolderText = view.findViewById(R.id.card_holder);
        TextView add = view.findViewById(R.id.add);

        builder.setView(view);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView cardNumber = findViewById(R.id.card_number);
                cardNumber.setText(panText.getText());
                TextView expire = findViewById(R.id.expire_date);
                expire.setText(expMonthText.getText().toString()
                        .concat("/").concat(expYearText.getText().toString()));
                TextView holder = findViewById(R.id.card_holder);
                TextView cvv = findViewById(R.id.cvv);
                cvv.setText(cvvText.getText());
                holder.setText(cardHolderText.getText());
                setCardIssuer(panText.getText().toString());

                if (panText.getText().toString().trim().length()<13){
                    panText.setError("invalid card number");
                }
                else if (cvv.getText().toString().trim().length()!=3){
                    cvvText.setError("cvv is incorrect!");
                }
                else if (cardHolderText.getText().toString().trim().isEmpty()){
                    cardHolderText.setError("card holder name can't be empty!");
                }
                else if (expMonthText.getText().toString().trim().isEmpty()){
                    expMonthText.setError("month can't be empty!");
                }
                else if (expYearText.getText().toString().trim().isEmpty()){
                    expYearText.setError("year can't be empty!");
                }else{
                    alertDialog.dismiss();
                }

            }
        });
         alertDialog = builder.create();
    }

    private void setCardIssuer(String cardNumber) {
        String type = "NOT SUPPORTED";

        if (cardNumber.substring(0, 1).equals(VISA_PREFIX))
            type = "VISA";
        else if (MASTERCARD_PREFIX.contains(cardNumber.substring(0, 2) + ","))
            type = "MASTERCARD";
//        else if (AMEX_PREFIX.contains(cardNumber.substring(0, 2) + ","))
//            type = "AMEX";
//        else if (cardNumber.substring(0, 4).equals(DISCOVER_PREFIX))
//            type = "DISCOVER";

        issuerCardView.setText(type);
    }

    private void getMobileDetail() {
        findViewById(R.id.mobile_method_info).setVisibility(View.VISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("MOBILE ACCOUNT INFORMATION");
        View view = View.inflate(this, R.layout.mobile_information, null);
        accountNumber = view.findViewById(R.id.account_number);
        TextView add = view.findViewById(R.id.add);
        builder.setView(view);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone= accountNumber.getText().toString();
                if (Patterns.PHONE.matcher(phone).matches() && phone.length()>= 10) {
                 alertDialog.dismiss();
                }else{
                   alertDialog.show();
                    accountNumber.setError(getString(R.string.invalid_phone));
                }
                TextView accountNun = findViewById(R.id.account_number);
                accountNun.setText(accountNumber.getText());
            }
        });

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
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
}
