package org.codewrite.teceme.ui.product;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.WishListAdapter;
import org.codewrite.teceme.adapter.ProductAdapter;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CartEntity;
import org.codewrite.teceme.model.room.WishListEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.ui.account.LoginActivity;
import org.codewrite.teceme.ui.payment.PaymentActivity;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.CartViewModel;
import org.codewrite.teceme.viewmodel.ProductViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import static java.lang.Math.log;
import static java.lang.Math.round;

public class WishListActivity extends AppCompatActivity {

    private ProductViewModel productViewModel;
    private CartViewModel cartViewModel;
    private AccountViewModel accountViewModel;
    private RecyclerView mProductWishRv;
    private WishListAdapter wishListAdapter;
    private AccessTokenEntity mAccessTokenEntity;
    private View noWishList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        productViewModel = ViewModelProviders.of(WishListActivity.this).get(ProductViewModel.class);
        cartViewModel = ViewModelProviders.of(WishListActivity.this).get(CartViewModel.class);
        accountViewModel = ViewModelProviders.of(WishListActivity.this).get(AccountViewModel.class);
        mProductWishRv = findViewById(R.id.id_rv_wish_list);
        noWishList = findViewById(R.id.no_wish_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        accountViewModel.getAccessToken().observe(this, new Observer<AccessTokenEntity>() {
            @Override
            public void onChanged(AccessTokenEntity accessTokenEntity) {
                if (accessTokenEntity==null){
                    launchLogin();
                    return;
                }
                mAccessTokenEntity = accessTokenEntity;
            }
        });
        accountViewModel.getLoggedInCustomer()
                .observe(this, new Observer<CustomerEntity>() {
            @Override
            public void onChanged(CustomerEntity customerEntity) {
                if (customerEntity==null){
                    launchLogin();
                    return;
                }
                setupWishListRv(customerEntity);
            }
        });
    }

    private void launchLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra("FINISH_WITHOUT_LAUNCHING_ANOTHER", true);
        startActivity(i);
    }

    private void setupWishListRv(final CustomerEntity loggedInCustomer) {
        // create WishListAdapter
        wishListAdapter = new WishListAdapter(this);
        // we set recycler view adapter
        mProductWishRv.setAdapter(wishListAdapter);


        accountViewModel.getWishListResult(loggedInCustomer.getCustomer_id())
                .observe(this, new Observer<List<WishListEntity>>() {
                    @Override
                    public void onChanged(List<WishListEntity> cartEntities) {
                        if (cartEntities == null) {
                            return;
                        }
                        wishListAdapter.submitList(cartEntities);
                    }
                });

        wishListAdapter.setWishListViewListener(new WishListAdapter.WishListViewListener() {
            @Override
            public void onWishListClicked(View v, Integer product_id) {
                Intent intent = new Intent(WishListActivity.this, ProductDetailActivity.class);
                intent.putExtra("PRODUCT_ID",product_id);
                startActivity(intent);
            }

            @Override
            public void onDelete(int position, Integer product_id) {
                accountViewModel.removeWishList(product_id,
                        loggedInCustomer.getCustomer_id(),
                        mAccessTokenEntity.getToken());
            }

            @Override
            public LiveData<CartEntity> isInCart(Integer product_id) {
                return cartViewModel.isInCart(product_id);
            }

            @Override
            public void onToggleCart(boolean remove, CartEntity cartEntity, Integer product_id) {
                if (remove){
                    cartViewModel.removeFromCart(product_id);
                }else{
                    cartViewModel.addToCart(cartEntity);
                }
            }

            @Override
            public LiveData<ProductEntity> onLoadProduct(Integer product_id) {
                return productViewModel.getProduct(product_id);
            }
        });

        accountViewModel.getWishListResult(loggedInCustomer.getCustomer_id())
                .observe(WishListActivity.this, new Observer<List<WishListEntity>>() {
            @Override
            public void onChanged(List<WishListEntity> wishListEntities) {
                if (wishListEntities==null){
                    noWishList.setVisibility(View.VISIBLE);
                    return;
                }
                noWishList.setVisibility(View.VISIBLE);
                wishListAdapter.submitList(wishListEntities);
            }
        });
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
