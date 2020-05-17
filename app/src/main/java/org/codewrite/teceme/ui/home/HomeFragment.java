package org.codewrite.teceme.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.arch.core.util.Function;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.codewrite.teceme.adapter.CategoryProductAdapter;
import org.codewrite.teceme.datasource.CategoryDataSource;
import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.AdsSliderAdapter;
import org.codewrite.teceme.utils.ZoomOutPageTransformer;
import org.codewrite.teceme.utils.SliderTimer;

import java.util.Timer;

public class HomeFragment extends Fragment {

    private Toolbar mToolbar;
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mSliderPagerAdapter;
    private TabLayout mSliderIndicator;
    private static final int NUM_PAGES = 5;
    private RecyclerView mCategoryRv;
    private RecyclerView mGroupProductRv;

    private FloatingActionButton fab;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // find recycler view for all product by their group
        mGroupProductRv = root.findViewById(R.id.id_rv_category_product_list);
        // setup recycler view
        setupCategoryProductRv(mGroupProductRv);


        // find recycler view for all main categories
        mCategoryRv = root.findViewById(R.id.id_rv_category_list);
        setupCategoryRv(mCategoryRv);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = root.findViewById(R.id.ads_view_flipper);
        mSliderIndicator =root.findViewById(R.id.indicator);

        assert  getActivity() != null;
        mSliderPagerAdapter = new AdsSliderAdapter(getActivity().getSupportFragmentManager());
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setAdapter(mSliderPagerAdapter);
        
        mSliderIndicator.setupWithViewPager(mPager, true);

        // slider timing
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(getActivity(),mPager,NUM_PAGES), 3000, 4000);

        // setup floating action button for back to top
        fab = getActivity().findViewById(R.id.id_fab_back_to_top);
        setupFab(fab);

        // return view
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void setupFab(FloatingActionButton fab){

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGroupProductRv.scrollToPosition(0);
                }
            });
    }

    private void setupCategoryProductRv(@NonNull RecyclerView recyclerView) {
        CategoryProductAdapter categoryProductAdapter = new CategoryProductAdapter(getActivity());
        recyclerView.setAdapter(categoryProductAdapter);
        CategoryDataSource dataSource = new CategoryDataSource();
        dataSource.map(new Function<CategoryEntity, Object>() {
            @Override
            public Object apply(CategoryEntity input) {
                return null;
            }
        });
        PagedList<CategoryEntity> groupProductPagedList = null;
        //categoryProductAdapter.submitList(groupProductPagedList);

    }
    private void setupCategoryRv(@NonNull RecyclerView recyclerView) {
    }

}