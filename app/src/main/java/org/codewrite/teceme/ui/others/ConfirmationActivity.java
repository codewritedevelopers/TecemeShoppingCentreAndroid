package org.codewrite.teceme.ui.others;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.codewrite.teceme.MainActivity;
import org.codewrite.teceme.R;
import org.codewrite.teceme.ui.account.LoginActivity;

public class ConfirmationActivity extends AppCompatActivity {

    public static final String LAUNCH_KEY = "org.codewrite.teceme.LAUNCH_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Button backToLogin = findViewById(R.id.back_to_login);
        Button backToHome = findViewById(R.id.back_to_home);
        Button goBack = findViewById(R.id.go_back);

        TextView message = findViewById(R.id.confirmation_message);

        if ("SIGN_UP_CONFIRMATION".equals(getIntent().getStringExtra(LAUNCH_KEY))){
            backToLogin.setVisibility(View.VISIBLE);
            String text = "Account created successfully. Please check your email (i.e "+
                    getIntent().getStringExtra("EMAIL")+") to activate your account.";
            message.setText(text);
            backToLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ConfirmationActivity.this, LoginActivity.class));
                    finish();
                }
            });
        }else if ("CHECK_OUT_CONFIRMATION".equals(getIntent().getStringExtra(LAUNCH_KEY))){
            String text = "We have successfully created your order. Please check your email (i.e "+
                    getIntent().getStringExtra("EMAIL")+") for detail as to how to retrieve" +
                    " your order from any of our vendor.";
            message.setText(text);
            backToHome.setVisibility(View.VISIBLE);
            backToHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ConfirmationActivity.this, MainActivity.class));
                    finish();
                }
            });
        } else if ("CREATE_WALLET_CONFIRMATION".equals(getIntent().getStringExtra(LAUNCH_KEY))){
            String text = "Wallet created successfully. Please check your email (i.e "+
                    getIntent().getStringExtra("EMAIL")+") to activate your wallet.";
            message.setText(text);
            goBack.setVisibility(View.VISIBLE);
            goBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
