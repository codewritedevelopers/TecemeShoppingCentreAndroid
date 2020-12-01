package org.codewrite.teceme.ui.orders;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.snackbar.Snackbar;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.OrderProductAdapter;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.CustomerOrderEntity;
import org.codewrite.teceme.utils.AutoFitGridRecyclerView;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.CheckOutViewModel;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class RedeemedOrderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private CheckOutViewModel checkOutViewModel;
    private AccountViewModel accountViewModel;

    private FragmentActivity activityContext;

    private OrderProductAdapter orderProductAdapter;
    private AutoFitGridRecyclerView orderRecyclerView;
    private AccessTokenEntity tokenEntity;
    private CustomerEntity loggedInCustomer;
    private View noOrders;
    private SwipeRefreshLayout swipeRefresh;

    public RedeemedOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        checkOutViewModel = ViewModelProviders.of(this).get(CheckOutViewModel.class);

         swipeRefresh = view.findViewById(R.id.swipe_refresh);
         swipeRefresh.setOnRefreshListener(this);

        orderRecyclerView = view.findViewById(R.id.id_rv_order_list);
        noOrders = view.findViewById(R.id.no_orders);
        orderProductAdapter = new OrderProductAdapter(activityContext,1);
        orderRecyclerView.setAdapter(orderProductAdapter);
    }

    private void setupViewModel(){
        accountViewModel.getAccessToken().observe(activityContext, new Observer<AccessTokenEntity>() {
            @Override
            public void onChanged(AccessTokenEntity accessTokenEntity) {

                if (accessTokenEntity == null) {
                    return;
                }
                tokenEntity = accessTokenEntity;
                checkOutViewModel.getRedeemedOrders(tokenEntity.getToken());

                accountViewModel.getLoggedInCustomer()
                        .observe(activityContext, new Observer<CustomerEntity>() {
                            @Override
                            public void onChanged(CustomerEntity customerEntity) {
                                if (customerEntity == null) {
                                    return;
                                }
                                loggedInCustomer = customerEntity;
                                setupOrderRv();
                            }
                        });
            }
        });
    }

    private void setupOrderRv() {

        orderProductAdapter.setOrderViewListener(new OrderProductAdapter.OrderViewListener() {
            @Override
            public void onOrderClicked(View v, int position) {
                Intent intent = new Intent(activityContext, OrderDetailActivity.class);
                intent.putExtra("ORDER_ID",
                        orderProductAdapter.getCurrentList().get(position).getCustomer_order_id());
                startActivity(intent);
            }
        });

        checkOutViewModel.getOrderRedeemedResult()
                .observe(activityContext, new Observer<List<CustomerOrderEntity>>() {
                    @Override
                    public void onChanged(List<CustomerOrderEntity> orderEntities) {
                        swipeRefresh.setRefreshing(false);
                        if (orderEntities == null) {
                            noOrders.setVisibility(View.VISIBLE);
                            return;
                        }

                        if (orderEntities.isEmpty()) {
                            noOrders.setVisibility(View.VISIBLE);
                        } else {
                            noOrders.setVisibility(View.GONE);
                            orderProductAdapter.submitList(orderEntities);
                        }
                    }
                });
       // checkInternetConnection();
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
                            swipeRefresh.setRefreshing(false);
                            Snackbar.make(activityContext.findViewById(R.id.main_container),
                                    "No Internet Connection!", Snackbar.LENGTH_INDEFINITE)
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

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        swipeRefresh.setRefreshing(true);
        setupViewModel();
    }

    @Override
    public void onRefresh() {
        swipeRefresh.setRefreshing(true);
        if (tokenEntity!=null){
            checkOutViewModel.getRedeemedOrders(tokenEntity.getToken());
        }
    }
}
