package org.codewrite.teceme.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.quinny898.library.persistentsearch.SearchBox;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.ProductAdapter;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.model.room.WishListEntity;
import org.codewrite.teceme.ui.account.LoginActivity;
import org.codewrite.teceme.utils.AutoFitGridRecyclerView;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.ProductViewModel;
import org.codewrite.teceme.viewmodel.WishListViewModel;

import java.util.List;
import java.util.Objects;

public class ProductActivity extends AppCompatActivity {

    private ProductViewModel productViewModel;
    private AccountViewModel accountViewModel;
    private WishListViewModel wishListViewModel;

    private ProductAdapter productAdapter;
    private SearchBox searchBox;
    private TextView categoryLevel0,categoryLevel1;
    private AccessTokenEntity accessToken;
    private CustomerEntity loggedInCustomer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productViewModel = ViewModelProviders.of(ProductActivity.this).get(ProductViewModel.class);
        accountViewModel = ViewModelProviders.of(ProductActivity.this).get(AccountViewModel.class);
        wishListViewModel = ViewModelProviders.of(ProductActivity.this).get(WishListViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AutoFitGridRecyclerView mProductRv = findViewById(R.id.id_rv_product_list);
        categoryLevel0 = findViewById(R.id.id_category_level_0);
        categoryLevel1 = findViewById(R.id.id_category_level_1);

        accountViewModel.getAccessToken().observe(ProductActivity.this, new Observer<AccessTokenEntity>() {
            @Override
            public void onChanged(AccessTokenEntity accessTokenEntity) {
                if (accessTokenEntity == null) {
                    return;
                }
                accessToken = accessTokenEntity;
            }
        });

        accountViewModel.getLoggedInCustomer().observe(ProductActivity.this, new Observer<CustomerEntity>() {
            @Override
            public void onChanged(CustomerEntity customerEntity) {
                if (customerEntity == null) {
                    return;
                }
                loggedInCustomer = customerEntity;
            }
        });

        productAdapter = new ProductAdapter(this);
        mProductRv.setAdapter(productAdapter);
        handleIntent(getIntent());
    }


    private void handleIntent(Intent intent) {
        String group = intent.getStringExtra("GROUP"),
                child = intent.getStringExtra("CHILD");

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Integer childCategoryId = intent.getIntExtra("CHILD_CATEGORY_ID",-1);
        if (group!=null && child!=null) {

            assert actionBar != null;
            actionBar.setTitle(child);

            productViewModel.getProducts(childCategoryId).observe(this, 
                    new Observer<List<ProductEntity>>() {
                @Override
                public void onChanged(List<ProductEntity> productEntities) {
                    if (productEntities==null){
                        return;
                    }
                    productAdapter.submitList(productEntities);
                }
            });

            categoryLevel0.setText(group);
            categoryLevel1.setText(child);

            productAdapter.setProductViewListener(new ProductAdapter.ProductViewListener() {
                @Override
                public void onProductClicked(View v, int position) {
                    Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
                    intent.putExtra("PRODUCT_ID",
                            Objects.requireNonNull(Objects.requireNonNull(
                                    productAdapter.getCurrentList()).get(position)).getProduct_id());
                    startActivity(intent);
                }

                @Override
                public LiveData<WishListEntity> isInWishList(Integer id) {
                    if (loggedInCustomer == null||accessToken==null) {
                        MutableLiveData<WishListEntity> data= new MutableLiveData<>();
                        data.setValue(null);
                        return data;
                    }
                    return wishListViewModel.isWishList(id,loggedInCustomer.getCustomer_id());
                }
                @Override
                public void onToggleWishList(View v, Integer product_id, String id, boolean added) {
                    if (loggedInCustomer == null||accessToken==null) {
                        startActivity(new Intent(ProductActivity.this, LoginActivity.class));
                        return;
                    }
                    if (added) {
                        wishListViewModel.removeWishList(id,accessToken.getToken());
                    } else {
                        wishListViewModel.addWishList(product_id, loggedInCustomer.getCustomer_id(),accessToken.getToken());
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
