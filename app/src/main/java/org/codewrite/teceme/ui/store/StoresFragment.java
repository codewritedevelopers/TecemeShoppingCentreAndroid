package org.codewrite.teceme.ui.store;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.AdsSliderAdapter;
import org.codewrite.teceme.adapter.CategoryProductAdapter;
import org.codewrite.teceme.adapter.HomeCategoryAdapter;
import org.codewrite.teceme.adapter.HomeProductAdapter;
import org.codewrite.teceme.adapter.StoreAdapter;
import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.model.room.StoreEntity;
import org.codewrite.teceme.utils.BackToTopFabBehavior;
import org.codewrite.teceme.utils.SliderTimer;
import org.codewrite.teceme.utils.ZoomOutPageTransformer;
import org.codewrite.teceme.viewmodel.ProductViewModel;
import org.codewrite.teceme.viewmodel.StoreViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class StoresFragment extends Fragment {
    private static final int NUM_PAGES = 5;
    private StoreViewModel storeViewModel;
    private RecyclerView mStoreRv;
    private ViewPager mPager;
    private TabLayout mSliderIndicator;
    private AdsSliderAdapter mSliderPagerAdapter;
    private FragmentActivity mActivity;
    private SliderTimer sliderTimer;
    private StoreAdapter storeAdapter;

     @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        assert mActivity != null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_stores, container, false);

          // get product view model
        storeViewModel = ViewModelProviders.of(this).get(StoreViewModel.class);

        // find recycler view for all store
        mStoreRv = root.findViewById(R.id.id_rv_store_list);
        // setup recycler view, should be called before setupCategoryRv()
        setupStoreRv(mStoreRv);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = root.findViewById(R.id.ads_view_flipper);
        mSliderIndicator = root.findViewById(R.id.indicator);

        mSliderPagerAdapter = new AdsSliderAdapter(mActivity.getSupportFragmentManager());
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setAdapter(mSliderPagerAdapter);

        mSliderIndicator.setupWithViewPager(mPager, true);

        // slider timing
        Timer timer = new Timer();
        sliderTimer = new SliderTimer(getActivity(), mPager, NUM_PAGES);
        timer.scheduleAtFixedRate(sliderTimer, 3000, 4000);

        // return root view
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupStoreRv(@NonNull final RecyclerView recyclerView) {
        // create StoreAdapter
        storeAdapter = new StoreAdapter(mActivity);
        // we set recycler view adapter
        recyclerView.setAdapter(storeAdapter);

        storeViewModel.getStores().observe(mActivity, new Observer<PagedList<StoreEntity>>() {
            @Override
            public void onChanged(PagedList<StoreEntity> storeEntities) {
                if (storeEntities== null){
                    return;
                }
                storeAdapter.submitList(storeEntities);
            }
        });
    }

    @Override
    public void onDestroy() {
        sliderTimer.cancel();
        super.onDestroy();
    }
}
