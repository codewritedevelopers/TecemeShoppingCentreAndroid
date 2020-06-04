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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.StoreAdapter;
import org.codewrite.teceme.model.room.StoreEntity;
import org.codewrite.teceme.ui.product.ProductActivity;
import org.codewrite.teceme.viewmodel.StoreViewModel;

public class StoresFragment extends Fragment {
    private StoreViewModel storeViewModel;
    private RecyclerView mStoreRv;
    private FragmentActivity mActivity;
    private StoreAdapter storeAdapter;
    private SearchBox searchBox;

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

        storeViewModel.getStores(null).observe(mActivity, new Observer<PagedList<StoreEntity>>() {
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSearchView(view);
    }

    private void setupSearchView(View view) {
        // we find search view
        searchBox = view.findViewById(R.id.id_search_box);
        searchBox.setDrawerLogo(R.drawable.arrow_back_with_bg);
        searchBox.setLogoTextColor(R.color.colorAccent);
        searchBox.setLogoText(getResources().getString(R.string.search_your_store_text));
        ImageView drawLogo = searchBox.findViewById(R.id.drawer_logo);
        drawLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        // we set voice search
        searchBox.enableVoiceRecognition(this);

        searchBox.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen
                for (String s : getResources().getStringArray(R.array.search_suggestions)) {
                    SearchResult result = new SearchResult(s);
                    // add suggestions
                    searchBox.addSearchable(result);
                }
            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
                searchBox.clearResults();
            }

            @Override
            public void onSearchTermChanged(String s) {
            }

            @Override
            public void onSearch(String searchTerm) {
                Toast.makeText(mActivity, searchTerm + " Searched", Toast.LENGTH_LONG).show();
                Intent i = new Intent(mActivity, ProductActivity.class);
                i.setAction(Intent.ACTION_SEARCH);
                i.putExtra("SEARCH_ITEM", searchTerm);
                startActivity(i);
            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to a result being clicked
                Intent i = new Intent(mActivity, ProductActivity.class);
                i.setAction(Intent.ACTION_SEARCH);
                i.putExtra("SEARCH_ITEM", result.title);
                startActivity(i);
            }

            @Override
            public void onSearchCleared() {

            }

        });
    }
}
