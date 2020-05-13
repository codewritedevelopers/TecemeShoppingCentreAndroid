package org.codewrite.teceme.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.codewrite.teceme.ProductDetailActivity;
import org.codewrite.teceme.ProductDetailFragment;
import org.codewrite.teceme.ProductListActivity;
import org.codewrite.teceme.R;
import org.codewrite.teceme.ScreenSlidePagerAdapter;
import org.codewrite.teceme.ZoomOutPageTransformer;
import org.codewrite.teceme.dummy.DummyContent;
import org.codewrite.teceme.ui.SliderTimer;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Toolbar toolbar;
    private boolean mTwoPane;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;
    private TabLayout indicator;
    private static final int NUM_PAGES = 5;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mTwoPane = false;
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
      //  final TextView textView = root.findViewById(R.id.text_home);
        View recyclerView = root.findViewById(R.id.id_group_product_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        View recyclerView2 = root.findViewById(R.id.home_category_list);
        assert recyclerView2 != null;
        setupRecyclerView2((RecyclerView) recyclerView2);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = root.findViewById(R.id.ads_view_flipper);
        indicator=root.findViewById(R.id.indicator);

        assert  getActivity() != null;
        pagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mPager.setAdapter(pagerAdapter);
        indicator.setupWithViewPager(mPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(getActivity(),mPager,NUM_PAGES), 3000, 4000);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new HomeFragment.SimpleItemRecyclerViewAdapter( DummyContent.ITEMS));
    }
    private void setupRecyclerView2(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new HomeFragment.SimpleItem2RecyclerViewAdapter( DummyContent.ITEMS));
    }

    public static class SimpleItem2RecyclerViewAdapter
            extends RecyclerView.Adapter<HomeFragment.SimpleItem2RecyclerViewAdapter.ViewHolder> {

        private List<DummyContent.DummyItem> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();

                    Context context = view.getContext();
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra(ProductDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
            }
        };

        SimpleItem2RecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @NonNull
        @Override
        public HomeFragment.SimpleItem2RecyclerViewAdapter.ViewHolder
        onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_category_list_content, parent, false);
            return new HomeFragment.SimpleItem2RecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final HomeFragment.SimpleItem2RecyclerViewAdapter.ViewHolder holder, int position) {
           // holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

         class ViewHolder extends RecyclerView.ViewHolder {

            final TextView mContentView;

             ViewHolder(View view) {
                super(view);
                mContentView = view.findViewById(R.id.id_home_category_name);
            }
        }
    }


    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<HomeFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private List<DummyContent.DummyItem> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();

                Context context = view.getContext();
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra(ProductDetailFragment.ARG_ITEM_ID, item.id);

                context.startActivity(intent);
            }
        };

        SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @NonNull
        @Override
        public HomeFragment.SimpleItemRecyclerViewAdapter.ViewHolder
        onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.group_product_list_content, parent, false);
            return new HomeFragment.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final HomeFragment.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            // holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mContentView = view.findViewById(R.id.id_group_name);
            }
        }
    }
}