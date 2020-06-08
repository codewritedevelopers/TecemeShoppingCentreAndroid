package org.codewrite.teceme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import org.codewrite.teceme.model.room.CartEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.ui.account.AccountsActivity;
import org.codewrite.teceme.ui.product.ProductActivity;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.CartViewModel;
import org.codewrite.teceme.viewmodel.CustomerViewModel;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private NestedScrollView nestedScrollView;
    private SearchBox searchBox;
    private NavController navController;
    private BottomNavigationView navView;
    private CartViewModel cartViewModel;
    private AccountViewModel accountViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);

        navView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_category,
                        R.id.navigation_stores, R.id.navigation_cart)
                        .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        accountViewModel.getLoggedInCustomer()
                .observe(this, new Observer<CustomerEntity>() {
                    @Override
                    public void onChanged(CustomerEntity customerEntity) {
                        if (customerEntity == null) {
                            return;
                        }
                        setBadges(customerEntity);
                    }
                });
    }

    private void setBadges(CustomerEntity customerEntity) {
        cartViewModel.getCartsEntity(customerEntity.getCustomer_id())
                .observe(this, new Observer<List<CartEntity>>() {
                    @Override
                    public void onChanged(List<CartEntity> cartEntities) {
                        if (cartEntities == null) {
                            return;
                        }
                        BadgeDrawable badge = navView.getOrCreateBadge(R.id.navigation_cart);
                        assert badge != null;
                        badge.setNumber(cartEntities.size());
                    }
                });
    }

    @Override
    public void onResume() {
        MenuItem item = navView.getMenu().getItem(4);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, AccountsActivity.class));
                return false;
            }
        });
        super.onResume();
    }

    private void setupSearchView() {
        // we find search view
        searchBox = findViewById(R.id.id_search_box);
        searchBox.setDrawerLogo(R.drawable.icons8_search);
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
                Toast.makeText(MainActivity.this, searchTerm + " Searched", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, ProductActivity.class);
                i.setAction(Intent.ACTION_SEARCH);
                i.putExtra("SEARCH_ITEM", searchTerm);
                startActivity(i);
            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to a result being clicked
                Intent i = new Intent(MainActivity.this, ProductActivity.class);
                i.setAction(Intent.ACTION_SEARCH);
                i.putExtra("SEARCH_ITEM", result.title);
                startActivity(i);
            }

            @Override
            public void onSearchCleared() {

            }

        });
    }

    @SuppressLint("CheckResult")
    private void checkInternetConnection() {
        ReactiveNetwork
                .observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnectedToInternet) throws Exception {
                        if (isConnectedToInternet) {
                            Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
                            single.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Boolean>() {
                                        @Override
                                        public void accept(Boolean isConnectedToInternet) throws Exception {
                                            if (!isConnectedToInternet) {
                                                Snackbar.make(findViewById(R.id.main_container),
                                                        "No Internet Connection!", Snackbar.LENGTH_SHORT)
                                                .setAction("Retry", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        checkInternetConnection();
                                                    }
                                                }).show();
                                            }
                                        }
                                    });
                        } else {
                            Snackbar.make(findViewById(R.id.main_container),
                                    "No Network Available", Snackbar.LENGTH_LONG)
                                    .setAction("Retry", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            checkInternetConnection();
                                        }
                                    }).show();
                        }
                    }
                });
    }
}
