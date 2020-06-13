package org.codewrite.teceme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.room.WalletLogEntity;

public class WalletLogAdapter extends ListAdapter<WalletLogEntity, WalletLogAdapter.WalletLogViewHolder> {

    private static final DiffUtil.ItemCallback<WalletLogEntity>
            DIFF_CALLBACK = new DiffUtil.ItemCallback<WalletLogEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull WalletLogEntity oldItem,
                                       @NonNull WalletLogEntity newItem) {
            return oldItem.getWallet_log_id().equals(newItem.getWallet_log_id());
        }

        @Override
        public boolean areContentsTheSame(@NonNull WalletLogEntity oldItem,
                                          @NonNull WalletLogEntity newItem) {
            return oldItem.getWallet_log_amount().equals(newItem.getWallet_log_amount())
                    && oldItem.getWallet_log_owner().equals(newItem.getWallet_log_owner())
                    && oldItem.getWallet_log_transaction_to().equals(newItem.getWallet_log_transaction_to())
                    && oldItem.getWallet_log_transaction_type().equals(newItem.getWallet_log_transaction_type())
                    && oldItem.getWallet_log_access().equals(newItem.getWallet_log_access());
        }
    };

    // member variable or objects
    private Context activityContext;
    private WalletLogViewListener productViewListener;

    /**
     * @class: CategoryWalletLogAdapter
     */
    public WalletLogAdapter(Context activityContext) {
        super(DIFF_CALLBACK);
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public WalletLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_log_list_content, parent, false);
        return new WalletLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WalletLogViewHolder holder, final int position) {
        // get a category product from list
        WalletLogEntity entity = getItem(position);
        if (entity == null) {
            return;
        }

        if (holder.transcationAmount != null) {
            String cedis = "GH₵ ";
            holder.transcationAmount.setText(cedis.concat(String.valueOf(entity.getWallet_log_amount())));
        }
    }

    public void setWalletLogViewListener(WalletLogViewListener productViewListener) {
        this.productViewListener = productViewListener;
    }

    static class WalletLogViewHolder extends RecyclerView.ViewHolder {
        private TextView transcationAmount;

        WalletLogViewHolder(@NonNull View itemView) {
            super(itemView);
            transcationAmount = itemView.findViewById(R.id.id_price);
        }
    }

    public interface WalletLogViewListener {
        void onWalletLogClicked(View v, int position);
    }
}
