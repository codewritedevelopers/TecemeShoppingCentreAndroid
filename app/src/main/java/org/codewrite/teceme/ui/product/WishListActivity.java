package org.codewrite.teceme.ui.product;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.ProductAdapter;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.viewmodel.ProductViewModel;

import java.util.concurrent.Executor;

import static java.lang.Math.round;

public class WishListActivity extends AppCompatActivity {

    private ProductViewModel productViewModel;
    private RecyclerView mProductRv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        productViewModel = ViewModelProviders.of(WishListActivity.this).get(ProductViewModel.class);

        mProductRv = findViewById(R.id.id_rv_product_list);
        setupProductRecyclerView(mProductRv);
        handleIntent(getIntent());
    }

    private void setupProductRecyclerView(RecyclerView mProductRv) {

        int colSpan = round(getResources().getConfiguration().screenWidthDp/
                getResources().getDimension(R.dimen.product_card_width)) + 1;
       // GridLayoutManager gridLayoutManager = new GridLayoutManager(this,colSpan);

//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return  round(ProductSearchable.this.getResources().getConfiguration().screenWidthDp/
//                        getResources().getDimension(R.dimen.product_card_width)) + 1;
//            }
//        });
      //  mProductRv.setLayoutManager(gridLayoutManager);
        mProductRv.setHasFixedSize(true);
        mProductRv.setItemAnimator(new DefaultItemAnimator());

        ProductAdapter productAdapter = new ProductAdapter(this);
        mProductRv.setAdapter(productAdapter);


        //productAdapter.submitList(productEntities);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            performSearch(query);
        }
    }

    private void performSearch(String query) {

    }
}
