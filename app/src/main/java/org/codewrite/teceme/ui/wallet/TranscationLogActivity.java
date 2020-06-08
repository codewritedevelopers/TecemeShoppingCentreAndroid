package org.codewrite.teceme.ui.wallet;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.WalletLogPagerAdapter;

public class TranscationLogActivity extends AppCompatActivity {

    private ViewPager walletLogsViewPager;
    private TabLayout walletLogsTitleTab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_log);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        walletLogsViewPager = findViewById(R.id.transaction_view_pager);
        walletLogsTitleTab = findViewById(R.id.transaction_title_tab);

        WalletLogPagerAdapter walletLogAdapter = new WalletLogPagerAdapter(getSupportFragmentManager());
        walletLogAdapter.addFrag(new IncomingFragment(),"Incoming Transactions");
        walletLogAdapter.addFrag(new OutgoingFragment(), "Outgoing Transactions");
        walletLogsViewPager.setAdapter(walletLogAdapter);
        // set up tabs indicators
        walletLogsTitleTab.setupWithViewPager(walletLogsViewPager);
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
