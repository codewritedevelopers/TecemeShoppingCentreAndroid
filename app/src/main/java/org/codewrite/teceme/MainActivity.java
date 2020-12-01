package org.codewrite.teceme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import org.codewrite.teceme.model.room.CartEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.ui.account.AccountsActivity;
import org.codewrite.teceme.ui.product.ProductActivity;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.CartViewModel;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

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
                        BadgeDrawable badge = navView.getOrCreateBadge(R.id.navigation_cart);
                        assert badge != null;
                        if (cartEntities == null) {
                            badge.clearNumber();
                            return;
                        }
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

    @SuppressLint("CheckResult")
    private void checkInternetConnection() {

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
    }
}
