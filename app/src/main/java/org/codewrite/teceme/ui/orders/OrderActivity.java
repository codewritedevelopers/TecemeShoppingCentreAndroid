package org.codewrite.teceme.ui.orders;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.OrderPagerAdapter;

public class OrderActivity extends AppCompatActivity {

    private ViewPager ordersViewPager;
    private TabLayout ordersTitleTab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ordersViewPager = findViewById(R.id.orders_view_pager);
        ordersTitleTab = findViewById(R.id.orders_title_tab);

        OrderPagerAdapter orderAdapter = new OrderPagerAdapter(getSupportFragmentManager());
        orderAdapter.addFrag(new OrderFragment(),"All Orders");
        orderAdapter.addFrag(new PendingOrderFragment(), "Pending Orders");
        orderAdapter.addFrag(new RedeemedOrderFragment(), "Redeemed Orders");
        ordersViewPager.setAdapter(orderAdapter);
        // set up tabs indicators
        ordersTitleTab.setupWithViewPager(ordersViewPager);
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
