package org.codewrite.teceme.ui.wallet;

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

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.OrderProductAdapter;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.CustomerEntity;
import org.codewrite.teceme.model.room.CustomerOrderEntity;
import org.codewrite.teceme.utils.AutoFitGridRecyclerView;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.CheckOutViewModel;

import java.util.List;

public class OutgoingFragment extends Fragment {

    private OrderProductAdapter orderProductAdapter;
    private AutoFitGridRecyclerView pendingOrderRv;
    private FragmentActivity mActivity;
    private CheckOutViewModel checkOutViewModel;
    private AccountViewModel accountViewModel;
    private CustomerEntity loggedInCustomer;
    private AccessTokenEntity accessToken;

    public OutgoingFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mActivity = getActivity();
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

        pendingOrderRv = view.findViewById(R.id.id_rv_order_list);
            checkOutViewModel = ViewModelProviders.of(mActivity).get(CheckOutViewModel.class);

        // get account view model
        accountViewModel = ViewModelProviders.of(mActivity).get(AccountViewModel.class);

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
                            mActivity.onBackPressed();
                            return;
                        }
                        loggedInCustomer = customerEntity;
                    }
                });


    }

    private void setOrderAdapter(final OrderProductAdapter orderProductAdapter, CustomerEntity loggedInCustomer) {
        pendingOrderRv.setAdapter(orderProductAdapter);

    }
}
