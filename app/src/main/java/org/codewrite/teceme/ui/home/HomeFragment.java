package org.codewrite.teceme.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import org.codewrite.teceme.adapter.ProductAdapter;
import org.codewrite.teceme.datasource.ProductDataSource;
import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.utils.BackToTopFabBehavior;
import org.codewrite.teceme.utils.SliderTimer;
import org.codewrite.teceme.utils.ZoomOutPageTransformer;
import org.codewrite.teceme.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executor;

public class HomeFragment extends Fragment {

    private static final int NUM_PAGES = 5;
    private List<CategoryEntity> categoryLevel1Entities;

    // adapters
    private PagerAdapter mSliderPagerAdapter;
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

    // reference to main activity
    private FragmentActivity mActivity;

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

        // slider timing
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(getActivity(), mPager, NUM_PAGES), 3000, 4000);

        // return root view
        return root;
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

        ProductDataSource productDataSource = new ProductDataSource();

        final PagedList<ProductEntity> productEntities =
                new PagedList.Builder<>(productDataSource,
                        new PagedList.Config.Builder().setPageSize(10).build())
                        .setInitialKey(0)
                        .setNotifyExecutor(new Executor() {
                            @Override
                            public void execute(Runnable command) {
                                Log.d("HomeFragment", "execute: setNotifyExecutor");
                            }
                        })
                        .setFetchExecutor(new Executor() {
                            @Override
                            public void execute(Runnable command) {
                                Log.d("HomeFragment", "execute: setFetchExecutor");
                            }
                        })
                        .setBoundaryCallback(new PagedList.BoundaryCallback() {
                            @Override
                            public void onZeroItemsLoaded() {
                                super.onZeroItemsLoaded();
                            }

                            @Override
                            public void onItemAtFrontLoaded(@NonNull Object itemAtFront) {
                                super.onItemAtFrontLoaded(itemAtFront);
                            }

                            @Override
                            public void onItemAtEndLoaded(@NonNull Object itemAtEnd) {
                                super.onItemAtEndLoaded(itemAtEnd);
                            }
                        }).build();


        // set product recycler loader
        categoryProductAdapter.setProductRvLoader(new CategoryProductAdapter.ProductRecyclerViewLoader() {
            @Override
            public void loadData(int position, View itemView) {

                CategoryEntity categoryLevel_1 = categoryProductAdapter.getCurrentList().get(position);

                // create recycler view for horizontal products
                RecyclerView groupProductsRv = itemView.findViewById(R.id.id_rv_group_product_list);
                // create product page adapter
                HomeProductAdapter productAdapter = new HomeProductAdapter(mActivity);
                // set adapter for recycler view
                groupProductsRv.setAdapter(productAdapter);

                productAdapter.submitList(productEntities);
                //TODO: implementation for loading products from db
//              observe products from db
//              productAdapter.submitList(null);
//                productViewModel.getProductResults(categoryLevel_1.getCategory_id())
//                .observe(mActivity, new Observer<List<ProductEntity>>() {
//                    @Override
//                    public void onChanged(List<ProductEntity> entities) {
//                        if (entities == null) {
//                            isLoaded.postValue(null);
//                            return;
//                        }
//                        if (entities.size() > 0) {
//                            productAdapter.submitList(entities);
//                            isLoaded.postValue(true);
//                        } else {
//                            productAdapter.submitList(entities);
//                            isLoaded.postValue(false);
//                        }
//                    }
//                });
            }

            @Override
            public void invalidate() {

            }

            @Override
            public boolean stop(int id) {
                return false;
            }
        });


        categoryLevel1Entities = new ArrayList<>();
        categoryLevel1Entities.add(new CategoryEntity(1, "Computers",
                1, 1, null, null));
        categoryLevel1Entities.add(new CategoryEntity(2, "Phones",
                1, 1, null, null));
        categoryLevel1Entities.add(new CategoryEntity(3, "Nestle",
                1, 2, null, null));

        categoryLevel1Entities.add(new CategoryEntity(4, "Qua-Bel",
                1, 2, null, null));

        categoryLevel1Entities.add(new CategoryEntity(5, "Bel-Aqua",
                1, 2, null, null));

        categoryLevel1Entities.add(new CategoryEntity(6, "T-shirts",
                1, 2, null, null));
        categoryLevel1Entities.add(new CategoryEntity(7, "Printers",
                1, 1, null, null));
        categoryLevel1Entities.add(new CategoryEntity(8, "Suits",
                1, 2, null, null));
    }

    private void setupCategoryRv(@NonNull RecyclerView recyclerView) {
        final List<CategoryEntity> categoryEntities = new ArrayList<>();
        categoryEntities.add(new CategoryEntity(1, "Electronics",
                0, null, null, null));
        categoryEntities.add(new CategoryEntity(2, "Glories & Beverages",
                0, null, null, null));
        categoryEntities.add(new CategoryEntity(3, "Ladies Dress",
                0, null, null, null));

        categoryEntities.add(new CategoryEntity(4, "Home appliance",
                0, null, null, null));

        categoryEntities.add(new CategoryEntity(5, "Phones & Accessories",
                0, null, null, null));

        categoryEntities.add(new CategoryEntity(6, "Men's Wear",
                0, null, null, null));
        categoryEntities.add(new CategoryEntity(7, "Shores",
                0, null, null, null));

        // create HomeCategoryAdapter
        homeCategoryAdapter = new HomeCategoryAdapter(getActivity());
        // we set category view listener
        homeCategoryAdapter.setCategoryViewListener(new HomeCategoryAdapter.CategoryViewListener() {
            @Override
            public int onSelectItem() {
                int i = categoryViewModel.getSelectedHomeCategory();
                // TODO: implementation for select sub level categories
                // CategoryEntity categoryEntity = homeCategoryAdapter.getCurrentList().get(i);
                // categoryViewModel.getCategories(categoryEntity.getCategory_level(),categoryEntity.getCategory_id());

                // test submit list
                categoryProductAdapter.submitList(categoryLevel1Entities);
                return i;
            }

            @Override
            public void onCategoryClicked(View v, int position) {
                // set selected category in view model to retain it state even on config change
                categoryViewModel.setSelectedHomeCategory(position);

                // set selected item at adapter to notify changes
                homeCategoryAdapter.setSelectedItem(position);
            }
        });

        // we set recycler view adapter
        recyclerView.setAdapter(homeCategoryAdapter);

        // we submit the list
        homeCategoryAdapter.submitList(categoryEntities);
    }

}