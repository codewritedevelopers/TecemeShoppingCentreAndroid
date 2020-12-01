package org.codewrite.teceme.ui.orders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.ProductSizeAdapter;
import org.codewrite.teceme.adapter.ProductSliderAdapter;
import org.codewrite.teceme.adapter.StoreAdapter;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.CustomerOrderEntity;
import org.codewrite.teceme.model.room.StoreEntity;
import org.codewrite.teceme.ui.account.LoginActivity;
import org.codewrite.teceme.ui.store.StoreDetailActivity;
import org.codewrite.teceme.utils.AutoFitGridRecyclerView;
import org.codewrite.teceme.utils.SliderTimer;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.CategoryViewModel;
import org.codewrite.teceme.viewmodel.CheckOutViewModel;
import org.codewrite.teceme.viewmodel.StoreViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailActivity extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener {

    private SliderTimer sliderTimer;
    // adapters
    private ProductSliderAdapter mSliderPagerAdapter;
    private ProductSizeAdapter orderSizeAdapter;
    private StoreAdapter relatedStoreAdapter;

    private CheckOutViewModel orderViewModel;
    private CategoryViewModel categoryViewModel;
    private AccessTokenEntity accessToken;
    private CustomerEntity loggedInCustomer;

    private ViewPager mPager;
    private TextView orderName;
    private TextView orderPrice;
    private TextView orderWeight;
    private TextView orderStatus;
    private TextView orderQuantity;
    private TextView orderRedeemed;
    private TextView orderDesc;
    private AutoFitGridRecyclerView relatedStoresRv;
    private StoreViewModel storeViewModel;
    private TextView orderSize;
    private TextView orderColor;
    private int order_id;
    private SwipeRefreshLayout swipe_refresh;
    private TextView orderCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderViewModel = ViewModelProviders.of(this).get(CheckOutViewModel.class);
        storeViewModel = ViewModelProviders.of(this).get(StoreViewModel.class);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        // get account view model
        AccountViewModel accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         swipe_refresh = findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(this);
        swipe_refresh.setProgressViewOffset(false, 0, 100);
        swipe_refresh.setRefreshing(true);
        // views
        orderPrice = findViewById(R.id.id_order_price);
        orderName = findViewById(R.id.id_order_name);
        orderStatus = findViewById(R.id.id_status);
        orderWeight = findViewById(R.id.id_weight);
        orderDesc = findViewById(R.id.id_order_desc);
        orderCode = findViewById(R.id.id_order_code);
        orderQuantity = findViewById(R.id.id_num_ordered);
        orderRedeemed = findViewById(R.id.id_num_redeemed);
        orderSize = findViewById(R.id.product_size);
        orderColor = findViewById(R.id.id_color);

        // recycler views
        relatedStoresRv = findViewById(R.id.id_rv_store_list);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.image_view_flipper);
        TabLayout mSliderIndicator = findViewById(R.id.indicator);

        mSliderPagerAdapter = new ProductSliderAdapter(getSupportFragmentManager());
        mPager.setAdapter(mSliderPagerAdapter);

        mSliderIndicator.setupWithViewPager(mPager, true);

        accountViewModel.getAccessToken().observe(this, new Observer<AccessTokenEntity>() {
            @Override
            public void onChanged(AccessTokenEntity accessTokenEntity) {
                if (accessTokenEntity == null) {
                    launchLogin();
                    return;
                }
                accessToken = accessTokenEntity;
            }
        });
        accountViewModel.getLoggedInCustomer().observe(this, new Observer<CustomerEntity>() {
            @Override
            public void onChanged(CustomerEntity customerEntity) {
                 order_id = getIntent().getIntExtra("ORDER_ID", -1);
                if (customerEntity == null) {
                    if (order_id == -1) {
                        Toast.makeText(OrderDetailActivity.this.getApplicationContext(), "Invalid Product Selected!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        checkInternetConnection();
                        setupProductDetail(order_id);
                    }
                    return;
                }
                loggedInCustomer = customerEntity;

                if (order_id == -1) {
                    Toast.makeText(OrderDetailActivity.this.getApplicationContext(), "Invalid Product Selected!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    setupProductDetail(order_id);
                }
            }
        });
    }

    private void setupProductDetail(final int order_id) {

        final LiveData<CustomerOrderEntity> orderLive
                = orderViewModel.getOrderResult(accessToken.getToken(), order_id);
        orderLive.observe(this, new Observer<CustomerOrderEntity>() {
            @Override
            public void onChanged(CustomerOrderEntity orderEntity) {

                if (orderEntity == null)
                    return;
                // Show the Up button in the action bar.
                swipe_refresh.setRefreshing(false);

                if (orderEntity.isStatus()!=null) {
                    if (!orderEntity.isStatus()){
                        Toast.makeText(OrderDetailActivity.this, orderEntity.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(orderEntity.getCustomer_order_product_name());
                }
                orderName.setVisibility(View.VISIBLE);
                orderName.setText(orderEntity.getCustomer_order_product_name());

                orderCode.setVisibility(View.VISIBLE);
                orderCode.setText(orderEntity.getCustomer_order_code());
                String cedis = "GH₵ ";
                orderPrice.setVisibility(View.VISIBLE);
                orderPrice.setText(cedis.concat(orderEntity.getCustomer_order_product_price()));
                orderStatus.setText((orderEntity.getCustomer_order_status()==1?"REDEEMED":"PENDING"));
                if (!orderEntity.getCustomer_order_product_weight().isEmpty()) {
                    orderWeight.setVisibility(View.VISIBLE);
                    orderWeight.setText(orderEntity.getCustomer_order_product_weight());
                } else {
                    orderWeight.setVisibility(View.GONE);
                }
                if (!orderEntity.getCustomer_order_product_desc().trim().isEmpty()) {
                    orderDesc.setVisibility(View.VISIBLE);
                    orderDesc.setText(Html.fromHtml(orderEntity.getCustomer_order_product_desc()));
                } else {
                    orderDesc.setVisibility(View.GONE);
                }
               if (orderEntity.getCustomer_order_product_color().trim().isEmpty()){
                   orderColor.setVisibility(View.GONE);
               }else{
                   orderColor.setVisibility(View.VISIBLE);
                   orderColor.setText(orderEntity.getCustomer_order_product_color());
               }
                if (orderEntity.getCustomer_order_product_size().trim().isEmpty()) {
                    orderSize.setVisibility(View.VISIBLE);
                } else {
                    orderSize.setVisibility(View.GONE);
                }

                orderQuantity.setVisibility(View.VISIBLE);
                orderRedeemed.setVisibility(View.VISIBLE);
                orderQuantity.setText(String.valueOf(orderEntity.getCustomer_order_quantity()));
                orderRedeemed.setText(String.valueOf(orderEntity.getCustomer_order_redeemed()));

                String[] imgUris = orderEntity.getCustomer_order_product_img_uri().split(",");
                // new size list
                // iterate through sizes and add to imgUriList
                mSliderPagerAdapter.subList(new ArrayList<>(Arrays.asList(imgUris)));

                // slider timing
                Timer timer = new Timer();
                sliderTimer = new SliderTimer(OrderDetailActivity.this, mPager, imgUris.length);
                timer.scheduleAtFixedRate(sliderTimer, 4000, 8000);
                setupRelateItems(orderEntity);
            }
        });
    }

    private void setupRelateItems(CustomerOrderEntity orderEntity) {
        // create store list adapter
        relatedStoreAdapter = new StoreAdapter(OrderDetailActivity.this);
        relatedStoresRv.setAdapter(relatedStoreAdapter);
        relatedStoreAdapter.setStoreViewListener(new StoreAdapter.StoreViewListener() {
            @Override
            public void onViewClicked(View v, String store_id) {
                Intent intent = new Intent(OrderDetailActivity.this, StoreDetailActivity.class);
                intent.putExtra("STORE_ID", store_id);
                startActivity(intent);
            }

            @Override
            public LiveData<CategoryEntity> onLoadCategory(Integer category_id) {
                return categoryViewModel.getCategory(category_id);
            }
        });

        storeViewModel.getStoresByProduct(orderEntity.getCustomer_order_product_code())
                .observe(OrderDetailActivity.this, new Observer<List<StoreEntity>>() {
                    @Override
                    public void onChanged(List<StoreEntity> storeEntities) {
                        if (storeEntities == null) {
                            return;
                        }
                        relatedStoreAdapter.submitList(storeEntities);
                    }
                });
    }

    private void launchLogin() {
        sliderTimer.cancel();
        Intent intent = new Intent(OrderDetailActivity.this, LoginActivity.class);
        intent.putExtra("FINISH_WITHOUT_LAUNCHING_ANOTHER", true);
        startActivity(intent);
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

    @SuppressLint("CheckResult")
    private void checkInternetConnection() {
        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnectedToInternet) throws Exception {
                        if (!isConnectedToInternet) {
                            Snackbar.make(findViewById(R.id.main_container),
                                    "No Internet Connection!", Snackbar.LENGTH_INDEFINITE).show();
                        }
                    }
                });

    }

    @Override
    public void onRefresh() {
        swipe_refresh.setRefreshing(true);
        setupProductDetail(order_id);
    }
}
