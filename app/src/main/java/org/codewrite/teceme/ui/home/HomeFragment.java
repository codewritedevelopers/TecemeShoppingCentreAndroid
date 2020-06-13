package org.codewrite.teceme.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.AdsSliderAdapter;
import org.codewrite.teceme.adapter.CategoryProductAdapter;
import org.codewrite.teceme.adapter.HomeCategoryAdapter;
import org.codewrite.teceme.adapter.HomeProductAdapter;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.model.room.WishListEntity;
import org.codewrite.teceme.ui.account.LoginActivity;
import org.codewrite.teceme.ui.product.ProductActivity;
import org.codewrite.teceme.ui.product.ProductDetailActivity;
import org.codewrite.teceme.utils.AutoFitGridRecyclerView;
import org.codewrite.teceme.utils.BackToTopFabBehavior;
import org.codewrite.teceme.utils.SliderTimer;
import org.codewrite.teceme.utils.ViewAnimation;
import org.codewrite.teceme.utils.ZoomOutPageTransformer;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.AdsViewModel;
import org.codewrite.teceme.viewmodel.CategoryViewModel;
import org.codewrite.teceme.viewmodel.ProductViewModel;
import org.codewrite.teceme.viewmodel.WishListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SliderTimer sliderTimer;
    ArrayList<SearchResult> searchResults = new ArrayList<>();
    // adapters
    private AdsSliderAdapter mSliderPagerAdapter;
    private HomeCategoryAdapter homeCategoryAdapter;
    private CategoryProductAdapter categoryProductAdapter;

    //views
    private View root;
    private RecyclerView mCategoryRv, mGroupProductRv;
    private ViewPager mPager;
    private TabLayout mSliderIndicator;
    private NestedScrollView nestedScrollView;
    private FloatingActionButton fab;

    // view models
    private CategoryViewModel categoryViewModel;
    private ProductViewModel productViewModel;
    private AccountViewModel accountViewModel;
    private AdsViewModel adsViewModel;
    private WishListViewModel wishListViewModel;

    // reference to main activity
    private FragmentActivity mActivity;
    private AccessTokenEntity accessToken;
    private CustomerEntity loggedInCustomer;
    private SearchBox searchBox;
    private AutoFitGridRecyclerView mAllProductsRv;
    private boolean isRotate;
    private Paginate paginate;
    private Paginate.Callbacks callbacks;
    private SwipeRefreshLayout swipeLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        assert mActivity != null;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        swipeLayout = root.findViewById(R.id.swipe_refresh);
        swipeLayout.setProgressViewOffset(false, 0, 40);
        swipeLayout.setOnRefreshListener(this);

        setupNestedScrollView(root);
        setupSearchView(root);

        // get category view model
        categoryViewModel = ViewModelProviders.of(mActivity).get(CategoryViewModel.class);
        // get product view model
        productViewModel = ViewModelProviders.of(mActivity).get(ProductViewModel.class);
        // get account view model
        accountViewModel = ViewModelProviders.of(mActivity).get(AccountViewModel.class);
        // get ads view model
        adsViewModel = ViewModelProviders.of(mActivity).get(AdsViewModel.class);

        // find recycler view for all product by their group
        mGroupProductRv = root.findViewById(R.id.id_rv_category_product_list);
        mAllProductsRv = root.findViewById(R.id.id_rv_product_list);
        // setup recycler view, should be called before setupCategoryRv()
        setupCategoryProductRv(mGroupProductRv);

        // find recycler view for all main categories
        mCategoryRv = root.findViewById(R.id.id_rv_category_list);
        // setup recycler view, should be called after setupCategoryProductRv()
        setupCategoryRv(mCategoryRv);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = root.findViewById(R.id.image_view_flipper);
        mSliderIndicator = root.findViewById(R.id.indicator);

        wishListViewModel = ViewModelProviders.of(mActivity).get(WishListViewModel.class);

        accountViewModel.getAccessToken().observe(mActivity, new Observer<AccessTokenEntity>() {
            @Override
            public void onChanged(AccessTokenEntity accessTokenEntity) {
                if (accessTokenEntity == null) {
                    return;
                }
                accessToken = accessTokenEntity;
            }
        });

        accountViewModel.getLoggedInCustomer().observe(mActivity, new Observer<CustomerEntity>() {
            @Override
            public void onChanged(CustomerEntity customerEntity) {
                if (customerEntity == null) {
                    return;
                }
                loggedInCustomer = customerEntity;
            }
        });
        // return root view
        return root;
    }

    private void loadAdsSlider() {

            mSliderPagerAdapter = new AdsSliderAdapter(mActivity.getSupportFragmentManager());
            mPager.setPageTransformer(true, new ZoomOutPageTransformer());
            mPager.setAdapter(mSliderPagerAdapter);
        mSliderIndicator.setupWithViewPager(mPager, true);

        adsViewModel.getAdsResult().observe(mActivity, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> list) {
                if (sliderTimer != null) {
                    sliderTimer.cancel();
                }
                if (list == null) {
                    return;
                }
                mSliderPagerAdapter.subList(list);
                // slider timing
                Timer timer = new Timer();
                sliderTimer = new SliderTimer(mActivity, mPager, list.size());
                timer.scheduleAtFixedRate(sliderTimer, 4000, 4000);
            }
        });
        adsViewModel.getAds();
    }

    private void setupNestedScrollView(View view) {
        nestedScrollView = view.findViewById(R.id.nestedScrollHomePage);
        if (nestedScrollView != null) {
            nestedScrollView.setSmoothScrollingEnabled(true);

            // set fab for back to top
            fab = mActivity.findViewById(R.id.id_fab_back_to_top);
            if (fab != null) {
                fab.setVisibility(View.INVISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nestedScrollView.setScrollY(0);
                        fab.setVisibility(View.INVISIBLE);
                    }
                });

                // add scroll listener for back to top behavior in nested scroll
                nestedScrollView.setOnScrollChangeListener(new BackToTopFabBehavior(fab));
            }
        }
    }

    private void setupCategoryProductRv(@NonNull final RecyclerView recyclerView) {
        // create CategoryProductAdapter
        categoryProductAdapter = new CategoryProductAdapter(mActivity);
        // we set recycler view adapter
        recyclerView.setAdapter(categoryProductAdapter);

        // set product recycler loader
        categoryProductAdapter.setProductRvLoader(new CategoryProductAdapter.ProductRecyclerViewLoader() {
            @Override
            public void loadData(int position, View itemView) {

                CategoryEntity categoryLevel_1 = categoryProductAdapter.getCurrentList().get(position);

                // create recycler view for horizontal products
                RecyclerView groupProductsRv = itemView.findViewById(R.id.id_rv_group_product_list);
                // create product page adapter
                final HomeProductAdapter productAdapter = new HomeProductAdapter(mActivity, 0);

                // set product listener
                productAdapter.setProductViewListener(new HomeProductAdapter.ProductViewListener() {
                    @Override
                    public void onProductClicked(View v, int position) {
                        Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                        intent.putExtra("PRODUCT_ID",
                                Objects.requireNonNull(Objects.requireNonNull(
                                        productAdapter.getCurrentList()).get(position)).getProduct_id());
                        startActivity(intent);
                    }

                    @Override
                    public LiveData<WishListEntity> isInWishList(Integer id) {
                        if (loggedInCustomer == null || accessToken==null) {
                           MutableLiveData<WishListEntity> data= new MutableLiveData<>();
                           data.setValue(null);
                           return data;
                        }
                        return wishListViewModel.isWishList(id, loggedInCustomer.getCustomer_id());
                    }

                    @Override
                    public void onToggleWishList(View v, Integer product_id, String id, boolean added) {
                        if (loggedInCustomer == null || accessToken==null) {
                            startActivity(new Intent(mActivity, LoginActivity.class));
                            return;
                        }
                        if (added) {
                            wishListViewModel.removeWishList(id,accessToken.getToken());
                        } else {
                            wishListViewModel.addWishList(product_id, loggedInCustomer.getCustomer_id(),accessToken.getToken());
                        }
                    }
                });
                // set adapter for recycler view
                groupProductsRv.setAdapter(productAdapter);

//              observe products from db
                productViewModel.getProducts(categoryLevel_1.getCategory_id())
                        .observe(mActivity, new Observer<List<ProductEntity>>() {
                            @Override
                            public void onChanged(List<ProductEntity> entities) {
                                if (entities == null) {
                                    return;
                                }
                                mGroupProductRv.setVisibility(View.VISIBLE);
                                mAllProductsRv.setVisibility(View.GONE);
                                productAdapter.submitList(entities);
                            }
                        });
            }
        });

        categoryProductAdapter.setCategoryProductViewListener(
                new CategoryProductAdapter.CategoryProductViewListener() {
                    @Override
                    public void onViewAllClicked(View v, int position) {
                        CategoryEntity categoryEntity1 = categoryProductAdapter.getCurrentList().get(position);
                        CategoryEntity categoryEntity
                                = homeCategoryAdapter.getCurrentList().get(homeCategoryAdapter.getSelectedItem());
                        String child = categoryEntity1.getCategory_name();
                        String group = categoryEntity.getCategory_name();
                        Integer childCategoryId = categoryEntity1.getCategory_id();

                        Intent intent = new Intent(mActivity, ProductActivity.class);
                        intent.putExtra("GROUP", group);
                        intent.putExtra("CHILD", child);
                        intent.putExtra("CHILD_CATEGORY_ID", childCategoryId);
                        startActivity(intent);
                    }
                });
    }

    private void setupCategoryRv(@NonNull RecyclerView recyclerView) {

        // create HomeCategoryAdapter
        homeCategoryAdapter = new HomeCategoryAdapter(getActivity());
        // all products adapter
        final HomeProductAdapter productAdapter
                = new HomeProductAdapter(mActivity, HomeProductAdapter.ALL_PRODUCT_VIEW);
        mAllProductsRv.setAdapter(productAdapter);

        // set product listener
        productAdapter.setProductViewListener(new HomeProductAdapter.ProductViewListener() {
            @Override
            public void onProductClicked(View v, int position) {
                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                Integer i = Objects.requireNonNull(Objects.requireNonNull(
                        productAdapter.getCurrentList()).get(position)).getProduct_id();
                intent.putExtra("PRODUCT_ID", i);
                startActivity(intent);
            }

            @Override
            public LiveData<WishListEntity> isInWishList(Integer id) {
                if (loggedInCustomer == null||accessToken==null) {
                    MutableLiveData<WishListEntity> data= new MutableLiveData<>();
                    data.setValue(null);
                    return data;
                }
                return wishListViewModel.isWishList(id, loggedInCustomer.getCustomer_id());
            }
            @Override
            public void onToggleWishList(View v, Integer product_id, String id, boolean added) {
                if (loggedInCustomer == null||accessToken==null) {
                    startActivity(new Intent(mActivity, LoginActivity.class));
                    return;
                }
                if (added) {
                    wishListViewModel.removeWishList(id,accessToken.getToken());
                } else {
                    wishListViewModel.addWishList(product_id, loggedInCustomer.getCustomer_id(),accessToken.getToken());
                }
            }
        });

        // we set category view listener
        homeCategoryAdapter.setCategoryViewListener(new HomeCategoryAdapter.CategoryViewListener() {
            @Override
            public int onSelectItem() {
                int i = categoryViewModel.getSelectedHomeCategory();
                final CategoryEntity categoryEntity = homeCategoryAdapter.getCurrentList().get(i);

                if (categoryEntity.getCategory_id() != -1) {
                    mAllProductsRv.setVisibility(View.GONE);
                    mGroupProductRv.setVisibility(View.VISIBLE);
                    categoryViewModel.getCategoryByParentResult(categoryEntity.getCategory_id())
                            .observe(mActivity, new Observer<List<CategoryEntity>>() {
                                @Override
                                public void onChanged(List<CategoryEntity> categoryEntities) {
                                    if (categoryEntities == null) {
                                        return;
                                    }
                                    categoryProductAdapter.submitList(categoryEntities);
                                }
                            });
                } else {
                    mGroupProductRv.setVisibility(View.GONE);
                    mAllProductsRv.setVisibility(View.VISIBLE);

                    if (paginate != null) {
                        paginate.unbind();
                    }
                    productViewModel.getProductListResult()
                            .observe(mActivity, new Observer<List<ProductEntity>>() {
                                @Override
                                public void onChanged(List<ProductEntity> entities) {
                                    if (entities == null) {
                                        paginate.setHasMoreDataToLoad(false);
                                        return;
                                    }
                                    productAdapter.submitList(entities);
                                }
                            });

                    callbacks = new Paginate.Callbacks() {
                        @Override
                        public void onLoadMore() {
                            Log.d("ProductViewModel", "onLoadMore: ");
                            productViewModel.loadProducts(categoryEntity.getCategory_id()
                                    == -1 ? null : categoryEntity.getCategory_id());
                        }

                        @Override
                        public boolean isLoading() {
                            // Indicate whether new page loading is in progress or not
                            Log.d("ProductViewModel", "isLoading: ");
                            return productViewModel.isLoading();
                        }

                        @Override
                        public boolean hasLoadedAllItems() {
                            // Indicate whether all data (pages) are loaded or not
                            Log.d("ProductViewModel", "hasLoadedAllItems: ");
                            return !productViewModel.isMoreItem();
                        }
                    };
                    paginate = Paginate.with(mAllProductsRv, callbacks)
                            .addLoadingListItem(true)
                            .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
                                @Override
                                public int getSpanSize() {
                                    GridLayoutManager gridLayoutManager = (GridLayoutManager)
                                            mAllProductsRv.getLayoutManager();
                                    assert gridLayoutManager != null;
                                    return gridLayoutManager.getSpanCount();
                                }
                            })
                            .setLoadingTriggerThreshold(2).build();
                }

                return i;
            }

            @Override
            public void onCategoryClicked(View v, int position) {
                // set selected category in view model to retain it state even on config change
                categoryViewModel.setSelectedHomeCategory(position);
                categoryProductAdapter.submitList(new ArrayList<CategoryEntity>());
                // set selected item at adapter to notify changes
                homeCategoryAdapter.setSelectedItem(position);
            }
        });

        // we set recycler view adapter
        recyclerView.setAdapter(homeCategoryAdapter);

        // load from database
        categoryViewModel.getCategoryForHomeResult()
                .observe(mActivity, new Observer<List<CategoryEntity>>() {
                    @Override
                    public void onChanged(List<CategoryEntity> categoryEntities) {
                        if (categoryEntities == null) {
                            return;
                        }
                        CategoryEntity allCategoryEntity = new CategoryEntity();
                        allCategoryEntity.setCategory_id(-1);
                        allCategoryEntity.setCategory_access(1);
                        allCategoryEntity.setCategory_level(0);
                        allCategoryEntity.setCategory_name("All Categories");
                        categoryEntities.add(0, allCategoryEntity);
                        homeCategoryAdapter.submitList(categoryEntities, new Runnable() {
                            @Override
                            public void run() {
                                homeCategoryAdapter.setSelectedItem(0);
                            }
                        });

                        swipeLayout.setRefreshing(false);
                    }
                });
    }

    private void setupSearchView(View view) {
        isRotate = true;
        // we find search view
        searchBox = view.findViewById(R.id.id_search_box);
        FloatingActionButton fabSearch = view.findViewById(R.id.fab_search);
        ViewAnimation.initLeft(searchBox);

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRotate) {
                    ViewAnimation.showInLeft(searchBox);
                    isRotate = false;
                } else {
                    ViewAnimation.showOutLeft(searchBox);
                    isRotate = true;
                }

            }
        });

        searchBox.setDrawerLogo(R.drawable.icons8_search);
        searchBox.setLogoTextColor(R.color.colorAccent);
        searchBox.setLogoText(getResources().getString(R.string.search_your_product_text));
        // we set voice search
        searchBox.enableVoiceRecognition(this);
        searchBox.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                // add suggestions
                searchBox.addAllSearchables(searchResults);
            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
            }

            @Override
            public void onSearchTermChanged(String s) {
                setSearchResult(s);
            }

            @Override
            public void onSearch(final String searchTerm) {
                SearchResult searchResult = new SearchResult(searchTerm,
                        getResources().getDrawable(R.drawable.icons8_time_machine));
                onResultClick(searchResult);
            }

            @Override
            public void onResultClick(final SearchResult result) {
                final LiveData<ProductEntity> productLive = productViewModel.searchProduct(result.title);
                productLive.observe(mActivity, new Observer<ProductEntity>() {
                    @Override
                    public void onChanged(ProductEntity productEntity) {
                        if (productEntity == null) {
                            final LiveData<CategoryEntity> categoryLive = categoryViewModel.searchCategory(result.title);
                            categoryLive.observe(mActivity, new Observer<CategoryEntity>() {
                                @Override
                                public void onChanged(CategoryEntity categoryEntity) {
                                    if (categoryEntity == null) {
                                        return;
                                    }
                                    List<CategoryEntity> currentList = homeCategoryAdapter.getCurrentList();
                                    String group = "", child;
                                    for (CategoryEntity entity : currentList) {
                                        if (entity.getCategory_id().equals(categoryEntity.getCategory_parent_id())) {
                                            group = entity.getCategory_name();
                                            break;
                                        }
                                    }
                                    child = categoryEntity.getCategory_name();

                                    Intent intent = new Intent(mActivity, ProductActivity.class);
                                    intent.putExtra("GROUP", group);
                                    intent.putExtra("CHILD", child);
                                    intent.putExtra("CHILD_CATEGORY_ID", categoryEntity.getCategory_id());
                                    startActivity(intent);
                                    categoryLive.removeObserver(this);
                                }
                            });
                            return;
                        }
                        productLive.removeObserver(this);
                        Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                        intent.putExtra("PRODUCT_ID", productEntity.getProduct_id());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onSearchCleared() {
                searchResults.clear();
            }

        });
    }

    private void setSearchResult(final String s) {
        searchResults.clear();

        productViewModel.searchProducts(s)
                .observe(mActivity, new Observer<List<ProductEntity>>() {
                    @Override
                    public void onChanged(List<ProductEntity> entities) {
                        if (entities == null) {
                            return;
                        }
                        if (entities.size() == 0) {
                            productViewModel.searchOnlineProducts(s);
                        }
                        for (ProductEntity entity : entities) {
                            searchResults.add(new SearchResult(entity.getProduct_name(),
                                    mActivity.getResources().getDrawable(R.drawable.icons8_time_machine)));
                        }
                        searchBox.clearSearchable();
                        searchBox.addAllSearchables(searchResults);
                    }
                });

        final LiveData<List<CategoryEntity>> searchCategories = categoryViewModel.searchCategories(s);
        searchCategories.observe(mActivity, new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(List<CategoryEntity> categoryEntities) {
                if (categoryEntities == null) {
                    return;
                }
                searchCategories.removeObserver(this);
                for (CategoryEntity entity : categoryEntities) {
                    searchResults.add(new SearchResult(entity.getCategory_name(),
                            getResources().getDrawable(R.drawable.icons8_time_machine)));
                }

                searchBox.clearSearchable();
                searchBox.addAllSearchables(searchResults);
            }
        });
    }


    @Override
    public void onRefresh() {
        checkInternetConnection();
        productViewModel.invalidate();
        categoryViewModel.getCategoryList();
        if (accessToken != null) {
            wishListViewModel.getWisLists(accessToken.getToken());
        }
        loadAdsSlider();
    }

    @Override
    public void onResume() {
        checkInternetConnection();
        productViewModel.invalidate();
        categoryViewModel.getCategoryList();
        if (accessToken != null) {
            wishListViewModel.getWisLists(accessToken.getToken());
        }
        loadAdsSlider();
        super.onResume();
    }

    @SuppressLint("CheckResult")
    private void checkInternetConnection() {
        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
        Disposable retry = single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnectedToInternet) throws Exception {
                        if (!isConnectedToInternet) {
                            swipeLayout.setRefreshing(false);
                            Snackbar.make(mActivity.findViewById(R.id.main_container),
                                    "No Internet Connection!", Snackbar.LENGTH_SHORT)
                                    .setAction("Retry", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    }).show();
                        }
                    }
                });
    }

}