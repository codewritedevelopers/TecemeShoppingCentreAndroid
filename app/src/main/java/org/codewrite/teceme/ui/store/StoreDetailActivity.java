package org.codewrite.teceme.ui.store;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.HomeProductAdapter;
import org.codewrite.teceme.adapter.ProductSizeAdapter;
import org.codewrite.teceme.adapter.ProductSliderAdapter;
import org.codewrite.teceme.adapter.StoreAdapter;
import org.codewrite.teceme.model.ProductSize;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CartEntity;
import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.model.room.StoreEntity;
import org.codewrite.teceme.model.room.WishListEntity;
import org.codewrite.teceme.ui.account.LoginActivity;
import org.codewrite.teceme.ui.payment.PaymentActivity;
import org.codewrite.teceme.ui.product.ProductDetailActivity;
import org.codewrite.teceme.utils.AutoFitGridRecyclerView;
import org.codewrite.teceme.utils.SliderTimer;
import org.codewrite.teceme.utils.ViewAnimation;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.CartViewModel;
import org.codewrite.teceme.viewmodel.CategoryViewModel;
import org.codewrite.teceme.viewmodel.CustomerViewModel;
import org.codewrite.teceme.viewmodel.ProductViewModel;
import org.codewrite.teceme.viewmodel.StoreViewModel;

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
    private ProductSliderAdapter mSliderPagerAdapter;
    private HomeProductAdapter relatedProductAdapter;
    private StoreAdapter relatedStoreAdapter;

    private ProductViewModel productViewModel;
    private AccountViewModel accountViewModel;
    private CategoryViewModel categoryViewModel;
    private CustomerViewModel customerViewModel;
    private StoreViewModel storeViewModel;

    private AccessTokenEntity accessToken;
    private CustomerEntity loggedInCustomer;

    private ViewPager mPager;
    private NestedScrollView nestedScrollView;
    private TextView storeName;
    private TextView storeCategory;
    private boolean isInCart;
    private boolean isFollowing;
    private boolean isRotate;
    private FloatingActionButton fabMore;
    private FloatingActionButton fabFollow;
    private FloatingActionButton fabLocateStores;
    private AutoFitGridRecyclerView relatedProductsRv;
    private AutoFitGridRecyclerView relatedStoresRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        storeViewModel = ViewModelProviders.of(this).get(StoreViewModel.class);
        // get account view model
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        customerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // views
        storeName = findViewById(R.id.id_product_name);
        storeCategory = findViewById(R.id.id_category_name);
        fabMore = findViewById(R.id.id_fab_more);
        fabLocateStores = findViewById(R.id.id_fab_locate_stores);
        fabFollow =findViewById(R.id.id_toggle_wish_list);
        // recycler views
        relatedProductsRv = findViewById(R.id.id_rv_related_products);
        relatedStoresRv = findViewById(R.id.id_rv_related_stores);

        // hide fabLocateStores & fabWishList
        ViewAnimation.initUp(fabLocateStores);
        ViewAnimation.initUp(fabFollow);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.ads_view_flipper);
        TabLayout mSliderIndicator = findViewById(R.id.indicator);

        mSliderPagerAdapter = new ProductSliderAdapter(getSupportFragmentManager());
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
                    return;
                }
                loggedInCustomer = customerEntity;
            }
        });


        int product_id = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (product_id == -1) {
            Toast.makeText(this.getApplicationContext(), "Invalid Product Selected!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            setupProductDetail(product_id);
        }
    }

    private void setupProductDetail(final int store_id) {

        final LiveData<StoreEntity> storeLive = storeViewModel.getStore(store_id);
        storeLive.observe(this, new Observer<StoreEntity>() {
            @Override
            public void onChanged(StoreEntity storeEntity) {
                if (storeEntity == null)
                    return;

                storeLive.removeObserver(this);
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

                // new size list
                // iterate through sizes and add to imgUriList
                mSliderPagerAdapter.subList(new ArrayList<>(Arrays.asList(imgUris)));

                // slider timing
                Timer timer = new Timer();
                sliderTimer = new SliderTimer(StoreDetailActivity.this, mPager, imgUris.length);
                timer.scheduleAtFixedRate(sliderTimer, 3000, 4000);

                setupRelateItems(storeEntity);
            }
        });
    }

    private void setupRelateItems(StoreEntity storeEntity) {
        // create product list adapter
        relatedProductAdapter =
                new HomeProductAdapter(StoreDetailActivity.this,HomeProductAdapter.ALL_PRODUCT_VIEW);

        // create store list adapter
        relatedStoreAdapter = new StoreAdapter(StoreDetailActivity.this);

        relatedProductsRv.setAdapter(relatedProductAdapter);

        relatedStoresRv.setAdapter(relatedStoreAdapter);

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
            public LiveData<Boolean> isInWishList(int id) {
                if (loggedInCustomer == null) {
                    return new MutableLiveData<>();
                }
                return customerViewModel.isInWishList(loggedInCustomer.getCustomer_id(), id);
            }

            @Override
            public void onToggleWishList(View v, int position) {
                if (loggedInCustomer == null) {
                    startActivity(new Intent(StoreDetailActivity.this, LoginActivity.class));
                    return;
                }

//                        accountViewModel.addToWishList(Objects.requireNonNull(Objects.requireNonNull(
//                                productAdapter.getCurrentList()).get(position)).getProduct_id(),
//                                loggedInCustomer.getCustomer_id(), accessToken.getToken());
            }
        });

        relatedStoreAdapter.setStoreViewListener(new StoreAdapter.StoreViewListener() {
            @Override
            public void onViewClicked(View v, Integer store_id) {
                Intent intent = new Intent(StoreDetailActivity.this, StoreDetailActivity.class);
                intent.putExtra("STORE_ID",
                        Objects.requireNonNull(Objects.requireNonNull(
                                relatedStoreAdapter.getCurrentList()).get(store_id)).getStore_id());
                startActivity(intent);
            }

            @Override
            public LiveData<CategoryEntity> onLoadCategory(Integer category_id) {
                return categoryViewModel.getCategory(category_id);
            }
        });

        productViewModel.getProducts(storeEntity.getStore_category_id())
                .observe(StoreDetailActivity.this, new Observer<List<ProductEntity>>() {
                    @Override
                    public void onChanged(List<ProductEntity> productEntities) {
                        if (productEntities==null){
                            return;
                        }
                        relatedProductAdapter.submitList(productEntities);
                    }
                });

        storeViewModel.getStores(storeEntity.getStore_category_id())
                .observe(StoreDetailActivity.this, new Observer<PagedList<StoreEntity>>() {
                    @Override
                    public void onChanged(PagedList<StoreEntity> storeEntities) {
                        if (storeEntities==null){
                            return;
                        }
                        relatedStoreAdapter.submitList(storeEntities);
                    }
                });
    }

    void setupActions(final StoreEntity storeEntity) {

        isRotate = ViewAnimation.rotateFab(fabMore, !isRotate);
        ViewAnimation.showIn(fabLocateStores);
        ViewAnimation.showIn(fabFollow);

        fabMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRotate = ViewAnimation.rotateFab(v, !isRotate);
                if(isRotate){
                    ViewAnimation.showIn(fabLocateStores);
                    ViewAnimation.showIn(fabFollow);
                }else{
                    ViewAnimation.showOut(fabLocateStores);
                    ViewAnimation.showOut(fabFollow);
                }

            }
        });

        fabFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loggedInCustomer == null) {
                    launchLogin();
                    return;
                }
                checkInternetConnection();
                if (isFollowing) {
                    customerViewModel.stopFollowingStore(storeEntity.getStore_id(),
                            loggedInCustomer.getCustomer_id(),
                            accessToken.getToken());
                } else {
                    customerViewModel.startFollowingStore(storeEntity.getStore_id(),
                            loggedInCustomer.getCustomer_id(),
                            accessToken.getToken());
                }
            }
        });

        //hide for now
        fabFollow.setVisibility(View.GONE);
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
                                                Snackbar.make(findViewById(R.id.main_container), "No Internet Connection!", Snackbar.LENGTH_INDEFINITE).show();
                                            }
                                        }
                                    });
                        } else {
                            Snackbar.make(findViewById(R.id.main_container),
                                    "No Network Available", Snackbar.LENGTH_INDEFINITE)
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
