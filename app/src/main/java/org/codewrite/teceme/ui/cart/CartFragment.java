package org.codewrite.teceme.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.CartAdapter;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CartEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.ui.account.LoginActivity;
import org.codewrite.teceme.ui.payment.PaymentActivity;
import org.codewrite.teceme.ui.product.ProductActivity;
import org.codewrite.teceme.ui.product.ProductDetailActivity;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    private AccountViewModel accountViewModel;
    private RecyclerView mCartRv;
    private FragmentActivity mActivity;
    private CartAdapter cartAdapter;
    private AccessTokenEntity accessToken;
    private CustomerEntity loggedInCustomer;
    private Button buyNow;
    private View noCart;
    private SearchBox searchBox;
    private TextView totalCart;
    private ArrayList<SearchResult> searchResults = new ArrayList<>();

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
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);

        // get account view model
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);

        // find recycler view for all product
        mCartRv = root.findViewById(R.id.id_rv_cart_list);
        buyNow = root.findViewById(R.id.buy_now);
        noCart = root.findViewById(R.id.no_cart);
        totalCart = root.findViewById(R.id.id_sub_total);

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });

        accountViewModel.getAccessToken()
                .observe(this.getViewLifecycleOwner(), new Observer<AccessTokenEntity>() {
                    @Override
                    public void onChanged(AccessTokenEntity accessTokenEntity) {
                        if (accessTokenEntity == null) {
                          return;
                        }
                        accessToken = accessTokenEntity;
                    }
                });

        accountViewModel.getLoggedInCustomer().
                observe(this.getViewLifecycleOwner(), new Observer<CustomerEntity>() {
                    @Override
                    public void onChanged(CustomerEntity customerEntity) {
                        if (customerEntity == null) {
                            return;
                        }
                        loggedInCustomer = customerEntity;
                        setupCartRv(mCartRv, customerEntity);
                    }
                });

        // return root view
        return root;
    }

    private void launchLogin() {
        Intent i = new Intent(mActivity, LoginActivity.class);
        i.putExtra("FINISH_WITHOUT_LAUNCHING_ANOTHER", true);
        startActivity(i);
    }

    private void setupCartRv(@NonNull RecyclerView recyclerView, final CustomerEntity loggedInCustomer) {
        // create CartAdapter
        cartAdapter = new CartAdapter(mActivity);
        // we set recycler view adapter
        recyclerView.setAdapter(cartAdapter);

        cartViewModel.getCartsEntity(loggedInCustomer.getCustomer_id())
                .observe(mActivity, cartEntities -> {
                    if (cartEntities == null) {
                        return;
                    }
                    cartAdapter.submitList(cartEntities);
                    if (cartEntities.size() > 0) {
                        buyNow.setVisibility(View.VISIBLE);
                        noCart.setVisibility(View.GONE);
                    } else {
                        buyNow.setVisibility(View.GONE);
                        noCart.setVisibility(View.VISIBLE);
                    }
                });

        cartViewModel.getCartsTotal(loggedInCustomer.getCustomer_id())
                .observe(mActivity, total -> {
                    if (total==null){
                        return;
                    }
                    String cedis = "Total GH₵ ", subTotal = String.valueOf(total);
                    totalCart.setText(cedis.concat(subTotal));
                });

        cartAdapter.setProductViewListener(new CartAdapter.ProductViewListener() {
            @Override
            public void onCartClicked(View v, int position) {
                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                intent.putExtra("PRODUCT_ID", cartAdapter.getCurrentList().get(position).getCart_product_id());
                startActivity(intent);
            }

            @Override
            public void onAddProduct(View v, int position) {
                TextView productQuantity = v.findViewById(R.id.id_num_ordered);
                int count = Integer.parseInt(productQuantity.getText().toString());
                productQuantity.setText(String.valueOf(count + 1));
                CartEntity cartEntity = cartAdapter.getCurrentList().get(position);
                cartEntity.setCart_quantity(count + 1);
                cartViewModel.updateCart(cartEntity);
            }

            @Override
            public void onSubtract(View v, int position) {
                TextView productQuantity = v.findViewById(R.id.id_num_ordered);
                int count = Integer.parseInt(productQuantity.getText().toString());
                if (count >1) {
                    productQuantity.setText(String.valueOf(count - 1));
                    CartEntity cartEntity = cartAdapter.getCurrentList().get(position);
                    cartEntity.setCart_quantity(count - 1);
                    cartViewModel.updateCart(cartEntity);
                }else{
                    Toast.makeText(mActivity, "Quantity can't be zero!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDelete(View v, int position) {
                cartViewModel.removeFromCart(cartAdapter.getCurrentList().get(position).getCart_product_id());
                Toast.makeText(mActivity, "Product removed from Cart!", Toast.LENGTH_SHORT).show();
            }

        });

        buyNow.setOnClickListener(v -> startActivity(new Intent(mActivity, PaymentActivity.class)));
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
