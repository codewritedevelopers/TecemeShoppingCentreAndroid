package org.codewrite.teceme.ui.product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.HomeProductAdapter;
import org.codewrite.teceme.adapter.ProductAdapter;
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
import org.codewrite.teceme.ui.store.StoreDetailActivity;
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

public class ProductDetailActivity extends AppCompatActivity {

    private SliderTimer sliderTimer;
    // adapters
    private ProductSliderAdapter mSliderPagerAdapter;
    private ProductSizeAdapter productSizeAdapter;
    private HomeProductAdapter relatedProductAdapter;
    private StoreAdapter relatedStoreAdapter;

    private ProductViewModel productViewModel;
    private AccountViewModel accountViewModel;
    private CategoryViewModel categoryViewModel;
    private CartViewModel cartViewModel;
    private CustomerViewModel customerViewModel;

    private AccessTokenEntity accessToken;
    private CustomerEntity loggedInCustomer;
    private CartEntity mCartEntity = new CartEntity();

    private ViewPager mPager;
    private NestedScrollView nestedScrollView;
    private TextView productName;
    private TextView productPrice;
    private TextView productWeight;
    private TextView productDiscount;
    private TextView productOrdered;
    private TextView addToCart;
    private TextView buyNow;
    private ImageView addProductView, subtractProductView;
    private RecyclerView productSizeRv;
    private Spinner spinnerProductColor;
    private TextView productQuantity;
    private TextView productCategory;
    private TextView productDesc;
    private boolean isInCart;
    private boolean isInWishList;
    private String[] colors;
    private List<ProductSize> sizeList;
    private String[] imgUris;
    private boolean isRotate;
    private FloatingActionButton fabMore;
    private FloatingActionButton fabWishList;
    private FloatingActionButton fabLocateStores;
    private AutoFitGridRecyclerView relatedProductsRv;
    private AutoFitGridRecyclerView relatedStoresRv;
    private StoreViewModel storeViewModel;
    private View productSizeContainer;
    private View productColorContainer;

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
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // views
        productPrice = findViewById(R.id.id_product_price);
        productName = findViewById(R.id.id_product_name);
        productOrdered = findViewById(R.id.id_orders);
        productDiscount = findViewById(R.id.id_discount);
        productWeight = findViewById(R.id.id_weight);
        productCategory = findViewById(R.id.id_category_name);
        productDesc = findViewById(R.id.id_product_desc);
        productQuantity = findViewById(R.id.id_num_ordered);
        fabMore = findViewById(R.id.id_fab_more);
        fabLocateStores = findViewById(R.id.id_fab_locate_stores);
        fabWishList =findViewById(R.id.id_toggle_wish_list);
        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);
        addProductView = findViewById(R.id.id_add_product);
        subtractProductView = findViewById(R.id.id_subtract_product);
        spinnerProductColor = findViewById(R.id.spinner_colors);

        productSizeContainer = findViewById(R.id.size_container);
        productColorContainer = findViewById(R.id.colors_container);

        // recycler views
        productSizeRv = findViewById(R.id.rv_product_size);
        relatedProductsRv = findViewById(R.id.id_rv_related_products);
        relatedStoresRv = findViewById(R.id.id_rv_related_stores);

        // hide fabLocateStores & fabWishList
        ViewAnimation.initUp(fabLocateStores);
        ViewAnimation.initUp(fabWishList);

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

    private void setupProductDetail(final int product_id) {

        // create product size adapter
        productSizeAdapter = new ProductSizeAdapter();
        // set product size adapter
        productSizeRv.setAdapter(productSizeAdapter);

        final LiveData<ProductEntity> productLive = productViewModel.getProduct(product_id);
                productLive.observe(this, new Observer<ProductEntity>() {
                    @Override
                    public void onChanged(ProductEntity productEntity) {
                        if (productEntity == null)
                            return;

                        productLive.removeObserver(this);
                        // Show the Up button in the action bar.
                        ActionBar actionBar = getSupportActionBar();
                        if (actionBar != null) {
                            actionBar.setTitle(productEntity.getProduct_name());
                        }
                        // set default quantity
                        mCartEntity.setCart_quantity(1);

                        productName.setText(productEntity.getProduct_name());

                        String cedis = "GH₵ ";
                        productPrice.setText(cedis.concat(productEntity.getProduct_price()));
                        productOrdered.setText(String.valueOf(productEntity.getProduct_ordered()));
                        if (!productEntity.getProduct_discount().isEmpty()) {
                            productDiscount.setText(productEntity.getProduct_discount());
                        } else {
                            productDiscount.setVisibility(View.GONE);
                        }
                        if (!productEntity.getProduct_weight().isEmpty()) {
                            productWeight.setText(productEntity.getProduct_weight());
                        } else {
                            productWeight.setVisibility(View.GONE);
                        }
                        if (!productEntity.getProduct_desc().trim().isEmpty()) {
                            productDesc.setText(Html.fromHtml(productEntity.getProduct_desc()));
                        } else {
                            productDesc.setVisibility(View.GONE);
                        }
                        colors = productEntity.getProduct_color().trim().split(",");
                        //set default color
                        if (!colors[0].isEmpty()) {
                            productColorContainer.setVisibility(View.VISIBLE);
                            mCartEntity.setProduct_color(colors[0]);
                        } else {
                            productColorContainer.setVisibility(View.GONE);
                        }
                        // Creating an ArrayAdapter using the string array and a default spinner layout
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                ProductDetailActivity.this, android.R.layout.simple_spinner_dropdown_item, colors);

                        //setting adapter for spinner
                        spinnerProductColor.setAdapter(adapter);
                        spinnerProductColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mCartEntity.setProduct_color(colors[position]);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                //ignore
                            }
                        });

                        String[] sizes = productEntity.getProduct_size().trim().split(",");
                        // new size list
                        sizeList = new ArrayList<>();
                        // iterate through sizes and add to sizeList
                        for (String size : sizes) {
                            if (!size.isEmpty()) {
                                sizeList.add(new ProductSize(size));
                            }
                        }
                        // set default
                        if (sizeList.size() > 0) {
                            productSizeContainer.setVisibility(View.VISIBLE);
                            sizeList.get(0).setSelected(true);
                            mCartEntity.setProduct_size(sizeList.get(0).getSize());
                        } else {
                            productSizeContainer.setVisibility(View.GONE);
                        }
                        // set default to zero
                        productQuantity.setText("1");

                        productSizeAdapter.setProductViewListener(new ProductSizeAdapter.ProductViewListener() {
                            @Override
                            public void onChangeSize(View v, int from, int to) {
                                mCartEntity.setProduct_size(sizeList.get(to).getSize());
                            }
                        });
                        productSizeAdapter.submitList(sizeList);

                        categoryViewModel.getCategory(productEntity.getProduct_category_id())
                                .observe(ProductDetailActivity.this, new Observer<CategoryEntity>() {
                                    @Override
                                    public void onChanged(CategoryEntity categoryEntity) {
                                        if (categoryEntity == null) {
                                            return;
                                        }
                                        productCategory.setText(categoryEntity.getCategory_name());
                                    }
                                });

                       imgUris = productEntity.getProduct_img_uri().trim().split(",");
                        if (imgUris.length > 0) {
                            mCartEntity.setProduct_img_uri(imgUris[0]);
                        }
                        // setup action later
                        setupActions(productEntity);

                        // new size list
                        // iterate through sizes and add to imgUriList
                        mSliderPagerAdapter.subList(new ArrayList<>(Arrays.asList(imgUris)));

                        // slider timing
                        Timer timer = new Timer();
                        sliderTimer = new SliderTimer(ProductDetailActivity.this, mPager, imgUris.length);
                        timer.scheduleAtFixedRate(sliderTimer, 3000, 4000);

                        setupRelateItems(productEntity);
                    }
                });
    }

    private void setupRelateItems(ProductEntity productEntity) {
        // create product list adapter
        relatedProductAdapter = new HomeProductAdapter(ProductDetailActivity.this,HomeProductAdapter.ALL_PRODUCT_VIEW);

        // create store list adapter
        relatedStoreAdapter = new StoreAdapter(ProductDetailActivity.this);

        relatedProductsRv.setAdapter(relatedProductAdapter);

        relatedStoresRv.setAdapter(relatedStoreAdapter);

        relatedProductAdapter.setProductViewListener(new HomeProductAdapter.ProductViewListener() {
            @Override
            public void onProductClicked(View v, int position) {
                Intent intent = new Intent(ProductDetailActivity.this, ProductDetailActivity.class);
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
                    startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
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
                Intent intent = new Intent(ProductDetailActivity.this, StoreDetailActivity.class);
                intent.putExtra("STORE_ID", store_id);
                startActivity(intent);
            }

            @Override
            public LiveData<CategoryEntity> onLoadCategory(Integer category_id) {
                return categoryViewModel.getCategory(category_id);
            }
        });

        productViewModel.getProducts(productEntity.getProduct_category_id())
                .observe(ProductDetailActivity.this, new Observer<List<ProductEntity>>() {
                    @Override
                    public void onChanged(List<ProductEntity> productEntities) {
                        if (productEntities==null){
                            return;
                        }
                        relatedProductAdapter.submitList(productEntities);
                    }
                });

        storeViewModel.getStores(productEntity.getProduct_category_id())
                .observe(ProductDetailActivity.this, new Observer<PagedList<StoreEntity>>() {
                    @Override
                    public void onChanged(PagedList<StoreEntity> storeEntities) {
                        if (storeEntities==null){
                            return;
                        }
                        relatedStoreAdapter.submitList(storeEntities);
                    }
                });
    }

    void setupActions(final ProductEntity productEntity) {

        isRotate = ViewAnimation.rotateFab(fabMore, !isRotate);
            ViewAnimation.showIn(fabLocateStores);
            ViewAnimation.showIn(fabWishList);

        fabMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRotate = ViewAnimation.rotateFab(v, !isRotate);
                if(isRotate){
                    ViewAnimation.showIn(fabLocateStores);
                    ViewAnimation.showIn(fabWishList);
                }else{
                    ViewAnimation.showOut(fabLocateStores);
                    ViewAnimation.showOut(fabWishList);
                }

            }
        });
        addProductView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(productQuantity.getText().toString());
                productQuantity.setText(String.valueOf(count + 1));
            }
        });

        subtractProductView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(productQuantity.getText().toString());
                if (count != 0) {
                    productQuantity.setText(String.valueOf(count - 1));
                }
            }
        });
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInCart) {
                    cartViewModel.removeFromCart(productEntity.getProduct_id());
                } else {
                    if (loggedInCustomer == null) {
                        launchLogin();
                        return;
                    }

                    mCartEntity.setCart_product_id(productEntity.getProduct_id());
                    mCartEntity.setProduct_category_id(productEntity.getProduct_category_id());
                    mCartEntity.setProduct_weight(productEntity.getProduct_weight());
                    mCartEntity.setProduct_price(productEntity.getProduct_price());
                    mCartEntity.setCart_access(productEntity.getProduct_access());
                    mCartEntity.setCart_quantity(Integer.valueOf(productQuantity.getText().toString()));
                    mCartEntity.setCart_owner(loggedInCustomer.getCustomer_id());
                    mCartEntity.setProduct_desc(productEntity.getProduct_desc());
                    mCartEntity.setProduct_code(productEntity.getProduct_code());
                    mCartEntity.setCart_date_created(productEntity.getProduct_date_created());
                    mCartEntity.setProduct_ordered(productEntity.getProduct_ordered());
                    mCartEntity.setProduct_name(productEntity.getProduct_name());
                    mCartEntity.setProduct_img_uri(productEntity.getProduct_img_uri());
                    mCartEntity.setProduct_discount(productEntity.getProduct_discount());
                    cartViewModel.addToCart(mCartEntity);

                    Toast.makeText(ProductDetailActivity.this, "Product added to Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loggedInCustomer == null) {
                    launchLogin();
                    return;
                }
                checkInternetConnection();

                if (isInWishList) {
                    accountViewModel.removeWishList(productEntity.getProduct_id(),
                            loggedInCustomer.getCustomer_id(),
                            accessToken.getToken());
                } else {
                    accountViewModel.addToWishList(productEntity.getProduct_id(),
                            loggedInCustomer.getCustomer_id(),
                            accessToken.getToken());
                }
            }
        });
        accountViewModel.isWishList(productEntity.getProduct_id())
                .observe(ProductDetailActivity.this, new Observer<WishListEntity>() {
                    @Override
                    public void onChanged(WishListEntity wishListEntity) {
                        if (wishListEntity == null) {
                            fabWishList.getDrawable().mutate().setTint(getResources().getColor(R.color.colorAccent));
                            return;
                        }
                        fabWishList.getDrawable().mutate().setTint(getResources().getColor(R.color.colorPrimaryDark));
                    }
                });

        final LiveData<CartEntity> inCart = cartViewModel.isInCart(productEntity.getProduct_id());
        inCart.observe(ProductDetailActivity.this, new Observer<CartEntity>() {
                            @Override
                            public void onChanged(CartEntity cartEntity) {
                                if (cartEntity == null) {
                                    isInCart = false;
                                    addToCart.setText(getString(R.string.add_to_cart_text));
                                } else {
                                    isInCart = true;

                                    addToCart.setText(getString(R.string.remove_from_cart_text));
                                    Integer quantity = cartEntity.getCart_quantity();
                                    productQuantity.setText(String.valueOf(quantity == null ? 0 : quantity));

                                    // create an new list for changes
                                    List<ProductSize> sizeList2 = new ArrayList<>();
                                    // check if any size selected
                                    for (ProductSize productSize : sizeList) {
                                        if (productSize.getSize().equals(cartEntity.getProduct_size())) {
                                            productSize.setSelected(true);
                                        } else {
                                            productSize.setSelected(false);
                                        }
                                        sizeList2.add(productSize);
                                    }
                                    productSizeAdapter.submitList(sizeList2);
                                    productSizeAdapter.notifyDataSetChanged();

                                    for (int i = 0; i < colors.length; i++) {
                                        if (colors[i].equals(cartEntity.getProduct_color())) {
                                            spinnerProductColor.setSelection(i, true);
                                        }
                                    }

                                    mCartEntity = cartEntity;
                                }
                            }
                        }
                );

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loggedInCustomer == null) {
                    launchLogin();
                    return;
                }

                if (productQuantity.getText().toString().equals("0")){
                    Toast.makeText(ProductDetailActivity.this, "Please, choose quantity!", Toast.LENGTH_LONG).show();
                return;
                }
                mCartEntity.setCart_product_id(productEntity.getProduct_id());
                mCartEntity.setProduct_category_id(productEntity.getProduct_category_id());
                mCartEntity.setProduct_weight(productEntity.getProduct_weight());
                mCartEntity.setProduct_price(productEntity.getProduct_price());
                mCartEntity.setCart_access(productEntity.getProduct_access());
                mCartEntity.setCart_quantity(Integer.valueOf(productQuantity.getText().toString()));
                mCartEntity.setCart_owner(loggedInCustomer.getCustomer_id());
                mCartEntity.setProduct_desc(productEntity.getProduct_desc());
                mCartEntity.setProduct_code(productEntity.getProduct_code());
                mCartEntity.setCart_date_created(productEntity.getProduct_date_created());
                mCartEntity.setProduct_ordered(productEntity.getProduct_ordered());
                mCartEntity.setProduct_name(productEntity.getProduct_name());
                mCartEntity.setProduct_img_uri(productEntity.getProduct_img_uri());
                mCartEntity.setProduct_discount(productEntity.getProduct_discount());
                inCart.removeObservers(ProductDetailActivity.this);

                cartViewModel.addToCart(mCartEntity);
                launchPayment();
            }
        });
    }

    private void launchPayment() {
        sliderTimer.cancel();
        Intent intent = new Intent(ProductDetailActivity.this, PaymentActivity.class);
        intent.putExtra("FINISH_WITHOUT_LAUNCHING_ANOTHER", true);
        startActivity(intent);
    }

    private void launchLogin() {
        sliderTimer.cancel();
        Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
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
