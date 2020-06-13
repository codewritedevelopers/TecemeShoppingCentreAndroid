package org.codewrite.teceme.ui.others;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import org.codewrite.teceme.R;
import org.codewrite.teceme.viewmodel.CartViewModel;
import org.codewrite.teceme.viewmodel.CategoryViewModel;
import org.codewrite.teceme.viewmodel.ProductViewModel;

import java.util.Date;


public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000;
    private Animation animFadeIn;
    private RelativeLayout splashContainer;
    private CategoryViewModel categoryViewModel;
    private CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // get category view model
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        View decorView = getWindow().getDecorView();

        // Hide the status bar.
        int uiOptions = 0;
        uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        // loadData the animation
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.animation_fade_in);
        // animation for image
        splashContainer = findViewById(R.id.splash_container);
        // start the animation
        splashContainer.setVisibility(View.VISIBLE);
        splashContainer.startAnimation(animFadeIn);

        cartViewModel.clearOldCart(java.text.DateFormat.getDateInstance().format(new Date()));
        // get data from network
        categoryViewModel.getCategoryList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
                startActivity(i);
                SplashActivity.this.finish();
            }
        },SPLASH_DISPLAY_LENGTH);

    }



    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}

