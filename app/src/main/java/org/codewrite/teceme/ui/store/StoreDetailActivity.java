package org.codewrite.teceme.ui.store;

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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.HomeProductAdapter;
import org.codewrite.teceme.adapter.StoreAdapter;
import org.codewrite.teceme.adapter.StoreSliderAdapter;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.model.room.StoreEntity;
import org.codewrite.teceme.model.room.WishListEntity;
import org.codewrite.teceme.ui.account.LoginActivity;
import org.codewrite.teceme.ui.product.ProductDetailActivity;
import org.codewrite.teceme.utils.AutoFitGridRecyclerView;
import org.codewrite.teceme.utils.SliderTimer;
import org.codewrite.teceme.utils.ViewAnimation;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.CategoryViewModel;
import org.codewrite.teceme.viewmodel.ProductViewModel;
import org.codewrite.teceme.viewmodel.StoreViewModel;
import org.codewrite.teceme.viewmodel.WishListViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class StoreDetailActivity extends AppCompatActivity {
    private SliderTimer sliderTimer;
    // adapters
    private StoreSliderAdapter mSliderPagerAdapter;
    private HomeProductAdapter relatedProductAdapter;

    private ProductViewModel productViewModel;
    private AccountViewModel accountViewModel;
    private CategoryViewModel categoryViewModel;
    private WishListViewModel wishListViewModel;
    private StoreViewModel storeViewModel;

    private AccessTokenEntity accessToken;
    private CustomerEntity loggedInCustomer;

    private ViewPager mPager;
    private TextView storeName;
    private TextView storeCategory;
    private boolean isFollowing;
    private boolean isRotate;
    private FloatingActionButton fabMore;
    private FloatingActionButton fabFollow;
    private FloatingActionButton fabLocateStores;
    private AutoFitGridRecyclerView relatedProductsRv;
    private TextView storeLocation;
    private TextView storeDesc;
    private TextView storePhone;
    private TextView storeHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        storeViewModel = ViewModelProviders.of(this).get(StoreViewModel.class);
        // get account view model
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        wishListViewModel = ViewModelProviders.of(this).get(WishListViewModel.class);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // views
        storeName = findViewById(R.id.id_store_name);
        storeLocation = findViewById(R.id.id_store_location);
        storeDesc = findViewById(R.id.id_store_desc);
        storePhone = findViewById(R.id.id_phone);
        storeHours = findViewById(R.id.id_hours);
        storeCategory = findViewById(R.id.id_store_category);
      ///  fabMore = findViewById(R.id.id_fab_more);
        fabLocateStores = findViewById(R.id.id_fab_locate_stores);
       // fabFollow = findViewById(R.id.id_follow);
        // recycler views
        relatedProductsRv = findViewById(R.id.id_rv_related_products);

        // hide fabLocateStores & fabWishList
//        ViewAnimation.initUp(fabLocateStores);
//        ViewAnimation.initUp(fabFollow);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.image_view_flipper);
        TabLayout mSliderIndicator = findViewById(R.id.indicator);

        mSliderPagerAdapter = new StoreSliderAdapter(getSupportFragmentManager());
        mPager.setAdapter(mSliderPagerAdapter);

        mSliderIndicator.setupWithViewPager(mPager, true);

        accountViewModel.getAccessToken().observe(this, new Observer<AccessTokenEntity>() {
            @Override
            public void onChanged(AccessTokenEntity accessTokenEntity) {
                if (accessTokenEntity == null) {
                    return;
                }
                accessToken = accessTokenEntity;
            }
        });

        accountViewModel.getLoggedInCustomer().observe(this, new Observer<CustomerEntity>() {
            @Override
            public void onChanged(CustomerEntity customerEntity) {
                if (customerEntity == null) {
                    String store_id = getIntent().getStringExtra("STORE_ID");
                    if (store_id==null) {
                        Toast.makeText(StoreDetailActivity.this.getApplicationContext(), "Invalid Store Selected!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        setupProductDetail(store_id);
                    }
                    return;
                }
                loggedInCustomer = customerEntity;
                String store_id = getIntent().getStringExtra("STORE_ID");
                if (store_id == null) {
                    Toast.makeText(StoreDetailActivity.this.getApplicationContext(), "Invalid Store Selected!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    setupProductDetail(store_id);
                }
            }
        });
    }

    private void setupProductDetail(final String store_id) {

        final LiveData<StoreEntity> storeLive = storeViewModel.getStore(store_id);
        storeLive.observe(this, new Observer<StoreEntity>() {
            @Override
            public void onChanged(StoreEntity storeEntity) {
                if (storeEntity == null)
                    return;

                // Show the Up button in the action bar.
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(storeEntity.getStore_name());
                }

                storeName.setText(storeEntity.getStore_name());

                categoryViewModel.getCategory(storeEntity.getStore_category_id())
                        .observe(StoreDetailActivity.this, new Observer<CategoryEntity>() {
                            @Override
                            public void onChanged(CategoryEntity categoryEntity) {
                                if (categoryEntity == null) {
                                    return;
                                }
                                storeCategory.setText(categoryEntity.getCategory_name());
                            }
                        });

                String[] imgUris = storeEntity.getStore_img_uri().trim().split(",");
                // setup action later
                setupActions(storeEntity);

                if (!storeEntity.getStore_desc().trim().isEmpty()) {
                    storeDesc.setText(Html.fromHtml(storeEntity.getStore_desc()));
                } else {
                    storeDesc.setVisibility(View.GONE);
                }

                if (!storeEntity.getStore_hours().trim().isEmpty()) {
                    storeHours.setText(storeEntity.getStore_hours());
                } else {
                    storeHours.setVisibility(View.GONE);
                }

                if (!storeEntity.getStore_location().trim().isEmpty()) {
                    storeLocation.setText(Html.fromHtml(storeEntity.getStore_location()));
                } else {
                    storeLocation.setVisibility(View.GONE);
                }

                if (!storeEntity.getStore_phone().trim().isEmpty()) {
                    storePhone.setText(Html.fromHtml(storeEntity.getStore_phone()));
                } else {
                    storePhone.setVisibility(View.GONE);
                }

                // new size list
                // iterate through sizes and add to imgUriList
                mSliderPagerAdapter.subList(new ArrayList<>(Arrays.asList(imgUris)));

                // slider timing
                Timer timer = new Timer();
                sliderTimer = new SliderTimer(StoreDetailActivity.this, mPager, imgUris.length);
                timer.scheduleAtFixedRate(sliderTimer, 4000, 4000);

                setupRelateItems(storeEntity);
            }
        });
    }

    private void setupRelateItems(StoreEntity storeEntity) {
        // create product list adapter
        relatedProductAdapter =
                new HomeProductAdapter(StoreDetailActivity.this, HomeProductAdapter.ALL_PRODUCT_VIEW);
        relatedProductsRv.setAdapter(relatedProductAdapter);

        relatedProductAdapter.setProductViewListener(new HomeProductAdapter.ProductViewListener() {
            @Override
            public void onProductClicked(View v, int position) {
                Intent intent = new Intent(StoreDetailActivity.this, ProductDetailActivity.class);
                intent.putExtra("PRODUCT_ID",
                        Objects.requireNonNull(Objects.requireNonNull(
                                relatedProductAdapter.getCurrentList()).get(position)).getProduct_id());
                startActivity(intent);
            }

            @Override
            public LiveData<WishListEntity> isInWishList(Integer id) {
                if (loggedInCustomer == null || wishListViewModel == null) {
                    MutableLiveData<WishListEntity> data = new MutableLiveData<>();
                    data.setValue(null);
                    return data;
                }
                return wishListViewModel.isWishList(id, loggedInCustomer.getCustomer_id());
            }

            @Override
            public void onToggleWishList(View v, Integer product_id, String id, boolean added) {
                if (loggedInCustomer == null || wishListViewModel == null) {
                    startActivity(new Intent(StoreDetailActivity.this, LoginActivity.class));
                    return;
                }
                if (added) {
                    wishListViewModel.removeWishList(id, accessToken.getToken());
                } else {
                    wishListViewModel.addWishList(product_id, loggedInCustomer.getCustomer_id(), accessToken.getToken());
                }
            }
        });

        productViewModel.getProducts(storeEntity.getStore_category_id())
                .observe(StoreDetailActivity.this, new Observer<List<ProductEntity>>() {
                    @Override
                    public void onChanged(List<ProductEntity> productEntities) {
                        if (productEntities == null) {
                            return;
                        }
                        relatedProductAdapter.submitList(productEntities);
                    }
                });


    }

    void setupActions(final StoreEntity storeEntity) {

//        isRotate = ViewAnimation.rotateFab(fabMore, !isRotate);
//        ViewAnimation.showIn(fabLocateStores);
//        ViewAnimation.showIn(fabFollow);
//
//        fabMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isRotate = ViewAnimation.rotateFab(v, !isRotate);
//                if (isRotate) {
//                    ViewAnimation.showIn(fabLocateStores);
//                    ViewAnimation.showIn(fabFollow);
//                } else {
//                    ViewAnimation.showOut(fabLocateStores);
//                    ViewAnimation.showOut(fabFollow);
//                }
//
//            }
//        });

//        fabFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (loggedInCustomer == null) {
//                    launchLogin();
//                    return;
//                }
//                checkInternetConnection();
//                if (isFollowing) {
//                    accountViewModel.stopFollowingStore(storeEntity.getStore_id(),
//                            loggedInCustomer.getCustomer_id(),
//                            accessToken.getToken());
//                } else {
//                    accountViewModel.startFollowingStore(storeEntity.getStore_id(),
//                            loggedInCustomer.getCustomer_id(),
//                            accessToken.getToken());
//                }
//            }
//        });

        //hide for now
//        fabFollow.setVisibility(View.GONE);
    }

    private void launchLogin() {
        sliderTimer.cancel();
        Intent intent = new Intent(StoreDetailActivity.this, LoginActivity.class);
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
                            Snackbar.make(findViewById(R.id.main_container), "No Internet Connection!", Snackbar.LENGTH_INDEFINITE).show();
                        }
                    }
                });

    }
}
