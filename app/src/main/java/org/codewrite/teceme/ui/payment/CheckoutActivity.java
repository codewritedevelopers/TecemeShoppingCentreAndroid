package org.codewrite.teceme.ui.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import org.codewrite.teceme.R;
import org.codewrite.teceme.viewmodel.CartViewModel;

public class CheckoutActivity extends AppCompatActivity {
    private CartViewModel cartViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);

    }

    private void setup () {

    }
}
