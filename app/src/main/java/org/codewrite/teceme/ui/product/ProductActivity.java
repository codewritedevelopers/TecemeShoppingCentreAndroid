package org.codewrite.teceme.ui.product;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.ProductAdapter;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.viewmodel.ProductViewModel;

public class ProductActivity extends AppCompatActivity {

    private ProductViewModel productViewModel;
    private ProductAdapter productAdapter;
    private SearchBox searchBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productViewModel = ViewModelProviders.of(ProductActivity.this).get(ProductViewModel.class);

        RecyclerView mProductRv = findViewById(R.id.id_rv_product_list);
        productAdapter = new ProductAdapter(this);
        mProductRv.setAdapter(productAdapter);
        handleIntent(getIntent());
        setupSearchView();
    }


    private void handleIntent(Intent intent) {
        String group = intent.getStringExtra("GROUP"),
                child = intent.getStringExtra("CHILD");
        Integer childCategoryId = intent.getIntExtra("CHILD_CATEGORY_ID",-1);
        if (group!=null && child!=null) {
            productViewModel.getProducts(childCategoryId).observe(this, 
                    new Observer<PagedList<ProductEntity>>() {
                @Override
                public void onChanged(PagedList<ProductEntity> productEntities) {
                    if (productEntities==null){
                        return;
                    }
                    productAdapter.submitList(productEntities);
                }
            });
        }
    }

    private void setupSearchView() {
        // we find search view
        searchBox = findViewById(R.id.id_search_box);
        searchBox.setDrawerLogo(R.drawable.arrow_back_with_bg);
        ImageView drawLogo = searchBox.findViewById(R.id.drawer_logo);

        drawLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchBox.setLogoTextColor(R.color.colorAccent);
        searchBox.setLogoText(getResources().getString(R.string.search_your_product_text));
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
                Toast.makeText(ProductActivity.this, searchTerm + " Searched", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ProductActivity.this, ProductActivity.class);
                i.setAction(Intent.ACTION_SEARCH);
                i.putExtra("SEARCH_ITEM", searchTerm);
                startActivity(i);
            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to a result being clicked
                Intent i = new Intent(ProductActivity.this,ProductActivity.class);
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
