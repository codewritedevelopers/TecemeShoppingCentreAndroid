package org.codewrite.teceme.ui.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.AdsSliderAdapter;
import org.codewrite.teceme.adapter.CategoryProductAdapter;
import org.codewrite.teceme.adapter.HomeCategoryAdapter;
import org.codewrite.teceme.adapter.StoreAdapter;
import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.model.room.StoreEntity;
import org.codewrite.teceme.ui.product.ProductActivity;
import org.codewrite.teceme.ui.product.ProductDetailActivity;
import org.codewrite.teceme.utils.BackToTopFabBehavior;
import org.codewrite.teceme.utils.HideOnScrollBehavior;
import org.codewrite.teceme.utils.ViewAnimation;
import org.codewrite.teceme.viewmodel.CategoryViewModel;
import org.codewrite.teceme.viewmodel.StoreViewModel;

import java.util.ArrayList;
import java.util.List;

public class StoresFragment extends Fragment {
    private StoreViewModel storeViewModel;
    private CategoryViewModel categoryViewModel;

    private RecyclerView mStoreRv;
    private FragmentActivity mActivity;
    private StoreAdapter storeAdapter;
    private SearchBox searchBox;
    ArrayList<SearchResult> searchResults = new ArrayList<>();

    // adapters
    private HomeCategoryAdapter homeCategoryAdapter;
    private CategoryProductAdapter categoryProductAdapter;
    private FloatingActionButton fab;
    private NestedScrollView nestedScrollView;

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
        // get category view model
        categoryViewModel = ViewModelProviders.of(mActivity).get(CategoryViewModel.class);
        // find recycler view for all store
        mStoreRv = root.findViewById(R.id.id_rv_store_list);
        // setup recycler view, should be called before setupCategoryRv()
        setupStoreRv(mStoreRv);
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

        storeAdapter.setStoreViewListener(new StoreAdapter.StoreViewListener() {
            @Override
            public void onViewClicked(View v, String store_id) {
                Intent intent = new Intent(mActivity,StoreDetailActivity.class);
                intent.putExtra("STORE_ID", store_id);
                startActivity(intent);
            }

            @Override
            public LiveData<CategoryEntity> onLoadCategory(Integer category_id) {
                return categoryViewModel.getCategory(category_id);
            }
        });

        storeViewModel.getStores().observe(mActivity, new Observer<List<StoreEntity>>() {
            @Override
            public void onChanged(List<StoreEntity> storeEntities) {
                if (storeEntities== null){
                    return;
                }
                storeAdapter.submitList(storeEntities);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSearchView(view);
        setupNestedScrollView(view);
    }

    private void setupNestedScrollView(View view) {
        nestedScrollView = view.findViewById(R.id.nestedScroll);
        fab = mActivity.findViewById(R.id.id_fab_locate_stores);
        if (nestedScrollView != null) {
            nestedScrollView.setSmoothScrollingEnabled(true);

            // set fab for back to top
            if (fab != null) {
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(mActivity,StoresMapActivity.class);
                        intent.putExtra(StoresMapActivity.KEY,"ALL_CLOSE_BY_STORES");
                        startActivity(intent);
                    }
                });
                // add scroll listener for back to top behavior in nested scroll
                nestedScrollView.setOnScrollChangeListener(new HideOnScrollBehavior(fab));
            }
        }
    }

    private void setupSearchView(View view) {
        // we find search view
        searchBox = view.findViewById(R.id.id_search_box);
        searchBox.setDrawerLogo(R.drawable.icons8_search);
        searchBox.setLogoTextColor(R.color.colorAccent);
        searchBox.setLogoText(getResources().getString(R.string.search_your_store_text));
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
                storeViewModel.searchStores(result.title)
                        .observe(mActivity, new Observer<List<StoreEntity>>() {
                            @Override
                            public void onChanged(List<StoreEntity> entities) {
                                if (entities == null) {
                                    return;
                                }
                                storeAdapter.submitList(entities, new Runnable() {
                                    @Override
                                    public void run() {
                                        storeAdapter.notifyDataSetChanged();
                                    }
                                });
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
        storeViewModel.searchStores(s)
                .observe(mActivity, new Observer<List<StoreEntity>>() {
                    @Override
                    public void onChanged(List<StoreEntity> entities) {
                        if (entities == null) {
                            return;
                        }
                        storeAdapter.submitList(entities, new Runnable() {
                            @Override
                            public void run() {
                                storeAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
    }

}
