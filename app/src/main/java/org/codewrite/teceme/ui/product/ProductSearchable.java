package org.codewrite.teceme.ui.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.codewrite.teceme.MainActivity;
import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.ProductAdapter;
import org.codewrite.teceme.datasource.ProductDataSource;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.viewmodel.ProductViewModel;

import java.util.concurrent.Executor;

import static java.lang.Math.ceil;
import static java.lang.Math.round;

public class ProductSearchable extends AppCompatActivity {

    private ProductViewModel productViewModel;
    private RecyclerView mProductRv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_searchable);

        productViewModel = ViewModelProviders.of(ProductSearchable.this).get(ProductViewModel.class);

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

        productAdapter.submitList(productEntities);
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
