package org.codewrite.teceme.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.CartAdapter;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CartEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.ui.account.LoginActivity;
import org.codewrite.teceme.utils.ContentLoadingDialog;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.CartViewModel;

import java.util.List;

public class CartFragment extends Fragment {

    private static final int NUM_PAGES = 5;
    private CartViewModel productViewModel;
    private AccountViewModel accountViewModel;
    private RecyclerView mCartRv;
    private FragmentActivity mActivity;
    private CartAdapter cartAdapter;
    private AccessTokenEntity accessToken;
    private CustomerEntity loggedInCustomer;
    private Button buyNow;
    private ImageView imageNoCart;
    private AlertDialog alertDialog;

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

        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        // get product view model
        productViewModel = ViewModelProviders.of(this).get(CartViewModel.class);

        // get account view model
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);

        // find recycler view for all product
        mCartRv = root.findViewById(R.id.id_rv_cart_list);
        buyNow = root.findViewById(R.id.buy_now);
        imageNoCart = root.findViewById(R.id.no_cart);

        alertDialog = ContentLoadingDialog.create(mActivity, "Loading ...");
        alertDialog.show();
        accountViewModel.getAccessToken().observe(this.getViewLifecycleOwner(), new Observer<AccessTokenEntity>() {
            @Override
            public void onChanged(AccessTokenEntity accessTokenEntity) {
                if (accessTokenEntity == null) {
                    alertDialog.cancel();
                    launchLogin();
                }
                accessToken = accessTokenEntity;
            }
        });

        accountViewModel.getLoggedInCustomer().observe(this.getViewLifecycleOwner(), new Observer<CustomerEntity>() {
            @Override
            public void onChanged(CustomerEntity customerEntity) {
                if (customerEntity == null) {
                    launchLogin();
                    return;
                }
                alertDialog.hide();
                loggedInCustomer = customerEntity;
                setupCartRv(mCartRv, customerEntity);
            }
        });

        // return root view
        return root;
    }

    private void launchLogin() {
        Intent i = new Intent(mActivity, LoginActivity.class);
        startActivity(i);
        mActivity.finish();
    }

    private void setupCartRv(@NonNull RecyclerView recyclerView, CustomerEntity loggedInCustomer) {
        // create CartAdapter
        cartAdapter = new CartAdapter(mActivity);
        // we set recycler view adapter
        recyclerView.setAdapter(cartAdapter);

        productViewModel.getCartsEntity(loggedInCustomer.getCustomer_id())
                .observe(mActivity, new Observer<List<CartEntity>>() {
                    @Override
                    public void onChanged(List<CartEntity> cartEntities) {
                        if (cartEntities == null) {
                            return;
                        }
                        cartAdapter.submitList(cartEntities);
                        if (cartEntities.size() > 0) {
                            buyNow.setVisibility(View.VISIBLE);
                            imageNoCart.setVisibility(View.GONE);
                        } else {
                            buyNow.setVisibility(View.GONE);
                            imageNoCart.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

}
