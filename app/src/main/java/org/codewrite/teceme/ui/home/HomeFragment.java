package org.codewrite.teceme.ui.home;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.AdsSliderAdapter;
import org.codewrite.teceme.adapter.CategoryProductAdapter;
import org.codewrite.teceme.adapter.HomeCategoryAdapter;
import org.codewrite.teceme.adapter.HomeProductAdapter;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.ui.account.LoginActivity;
import org.codewrite.teceme.ui.product.ProductDetailActivity;
import org.codewrite.teceme.utils.BackToTopFabBehavior;
import org.codewrite.teceme.utils.SliderTimer;
import org.codewrite.teceme.utils.ZoomOutPageTransformer;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.CategoryViewModel;
import org.codewrite.teceme.viewmodel.CustomerViewModel;
import org.codewrite.teceme.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

public class HomeFragment extends Fragment {

    private SliderTimer sliderTimer;
    // adapters
    private AdsSliderAdapter mSliderPagerAdapter;
    private TabLayout mSliderIndicator;
    private HomeCategoryAdapter homeCategoryAdapter;
    private CategoryProductAdapter categoryProductAdapter;

    //views
    private View root;
    private RecyclerView mCategoryRv, mGroupProductRv;
    private ViewPager mPager;
    private NestedScrollView nestedScrollView;
    private FloatingActionButton fab;

    // view models
    private CategoryViewModel categoryViewModel;
    private ProductViewModel productViewModel;
    private CustomerViewModel customerViewModel;
    private AccountViewModel accountViewModel;

    // reference to main activity
    private FragmentActivity mActivity;
    private AccessTokenEntity accessToken;
    private CustomerEntity loggedInCustomer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        assert mActivity != null;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        // get category view model
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        // get product view model
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        // get customer view model
        customerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
        // get account view model
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);

        // find recycler view for all product by their group
        mGroupProductRv = root.findViewById(R.id.id_rv_category_product_list);
        // setup recycler view, should be called before setupCategoryRv()
        setupCategoryProductRv(mGroupProductRv);

        // find recycler view for all main categories
        mCategoryRv = root.findViewById(R.id.id_rv_category_list);
        // setup recycler view, should be called after setupCategoryProductRv()
        setupCategoryRv(mCategoryRv);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = root.findViewById(R.id.ads_view_flipper);
        mSliderIndicator = root.findViewById(R.id.indicator);

        mSliderPagerAdapter = new AdsSliderAdapter(mActivity.getSupportFragmentManager());
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setAdapter(mSliderPagerAdapter);

        mSliderIndicator.setupWithViewPager(mPager, true);

        loadAdsSlider();

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
        customerViewModel.getAdsResult().observe(mActivity, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> list) {
                if (list==null){
                    List<String>  noAds = new ArrayList<>();
                    noAds.add("noAds");
                    mSliderPagerAdapter.subList(noAds);
                    return;
                }
                mSliderPagerAdapter.subList(list);
                // slider timing
                Timer timer = new Timer();
                sliderTimer = new SliderTimer(mActivity, mPager, list.size());
                timer.scheduleAtFixedRate(sliderTimer, 3000, 4000);
            }
        });
        customerViewModel.getAds();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupNestedScrollView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
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
                final HomeProductAdapter productAdapter = new HomeProductAdapter(mActivity);

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
                    public LiveData<Boolean> isInWishList(int id) {
                        if (loggedInCustomer == null) {
                            return new MutableLiveData<>();
                        }
                        return customerViewModel.isInWishList(loggedInCustomer.getCustomer_id(), id);
                    }

                    @Override
                    public void onToggleWishList(View v, int position) {
                        if (loggedInCustomer == null) {
                            startActivity(new Intent(mActivity, LoginActivity.class));
                            return;
                        }
                        accountViewModel.addToWishList(Objects.requireNonNull(Objects.requireNonNull(
                                productAdapter.getCurrentList()).get(position)).getProduct_id(),
                                loggedInCustomer.getCustomer_id(), accessToken.getToken());
                    }
                });
                // set adapter for recycler view
                groupProductsRv.setAdapter(productAdapter);

//              observe products from db
                productViewModel.getProducts(categoryLevel_1.getCategory_id())
                        .observe(mActivity, new Observer<PagedList<ProductEntity>>() {
                            @Override
                            public void onChanged(PagedList<ProductEntity> entities) {
                                if (entities == null) {
                                    return;
                                }
                                productAdapter.submitList(entities);
                            }
                        });
            }
        });
    }

    private void setupCategoryRv(@NonNull RecyclerView recyclerView) {

        // create HomeCategoryAdapter
        homeCategoryAdapter = new HomeCategoryAdapter(getActivity());
        // we set category view listener
        homeCategoryAdapter.setCategoryViewListener(new HomeCategoryAdapter.CategoryViewListener() {
            @Override
            public int onSelectItem() {
                int i = categoryViewModel.getSelectedHomeCategory();
                CategoryEntity categoryEntity = homeCategoryAdapter.getCurrentList().get(i);

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
                        homeCategoryAdapter.submitList(categoryEntities);
                    }
                });
    }
}