package org.codewrite.teceme.ui.payment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.codewrite.teceme.R;
import org.codewrite.teceme.ui.wallet.CreateWalletActivity;

public class PaymentActivity extends AppCompatActivity {

    Button toPayment;
    TextView toCreateWallet;
    RadioGroup paymentMethods;
    private boolean isMethodChecked;
    private String method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toPayment = findViewById(R.id.to_payment);
        toCreateWallet = findViewById(R.id.create_wallet);
        paymentMethods = findViewById(R.id.payment_methods);
        paymentMethods.setOnCheckedChangeListener((group, checkedId) -> {
            if (group==null)
                return;
           switch (checkedId){

               case R.id.teceme_pay:
                   method = "teceme_pay";
                   break;
               case R.id.mtn_momo:
                   method="mtn_momo";
                   break;
               case R.id.airtel_tigo_cash:
                   method="airtel_tigo_money";
                   break;
               case R.id.vodafone_cash:
                   method="vodafone_cash";
                   break;
               case R.id.bank_card:
                   method= "bank_card";
                   break;
           }
           isMethodChecked =true;
        });
        toPayment.setOnClickListener(v -> {
            if (isMethodChecked){
                Intent intent = new Intent(PaymentActivity.this, CheckoutActivity.class);
                intent.putExtra("METHOD", method);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(PaymentActivity.this, "Please, choose a method!", Toast.LENGTH_SHORT).show();
            }
        });

        toCreateWallet.setOnClickListener(v -> startActivity(new Intent(PaymentActivity.this, CreateWalletActivity.class)));
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
