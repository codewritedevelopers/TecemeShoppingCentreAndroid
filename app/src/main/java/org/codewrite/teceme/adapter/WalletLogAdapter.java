package org.codewrite.teceme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

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
                .inflate(R.layout.product_list_content, parent, false);
        return new WalletLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WalletLogViewHolder holder, final int position) {
        // get a category product from list
        WalletLogEntity entity = getItem(position);
        if (entity == null) {
            return;
        }
        // set group product name
        if (holder.productName != null)
            holder.productName.setText(entity.getProduct_name());

        if (holder.productPrice != null) {
            String cedis = "GH₵ ";
            holder.productPrice.setText(cedis.concat(entity.getProduct_price()));
        }

        if (holder.productColor != null) {
            if (!entity.getProduct_color().isEmpty()) {
                holder.productColor.setVisibility(View.VISIBLE);
                holder.productColor.setText(entity.getProduct_color());
            } else {
                holder.productColor.setVisibility(View.GONE);
            }
        }

        if (holder.productSize != null) {
            if (!entity.getProduct_size().isEmpty()) {
                holder.productSize.setVisibility(View.VISIBLE);
                holder.productSize.setText(entity.getProduct_size());
                holder.productSize.setBackgroundColor(
                        activityContext.getResources().getColor(R.color.colorPrimary));
                holder.productSize.setTextColor(
                        activityContext.getResources().getColor(R.color.colorWhite));
            } else {
                holder.productSize.setVisibility(View.GONE);
            }
        }

        if (holder.productQuantity != null) {
            holder.productQuantity.setText(String.valueOf(entity.getWalletLog_quantity()));
        }

        if (holder.productWeight != null && entity.getProduct_weight() != null) {
            if (!entity.getProduct_weight().isEmpty()) {
                holder.productWeight.setVisibility(View.VISIBLE);
                holder.productWeight.setText(entity.getProduct_weight());
            } else {
                holder.productWeight.setVisibility(View.GONE);
            }
        } else if (holder.productWeight != null) {
            holder.productWeight.setVisibility(View.GONE);
        }
        // set product view lister
        if (productViewListener != null) {
            // set item clicked
            if (holder.productImage!=null) {
                holder.productImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productViewListener.onWalletLogClicked(holder.itemView, position);
                    }
                });
            }
        }
    }

    public void setWalletLogViewListener(WalletLogViewListener productViewListener) {
        this.productViewListener = productViewListener;
    }

    static class WalletLogViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView productPrice;
        private TextView productColor;
        private TextView productSize;
        private TextView productWeight;
        private TextView productQuantity;
        private TextView productDiscount;
        private ImageView productImage;
        private TextView productWalletLoged;

        WalletLogViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.id_product_name);
            productPrice = itemView.findViewById(R.id.id_price);
            productWalletLoged = itemView.findViewById(R.id.id_walletLogs);
            productQuantity = itemView.findViewById(R.id.id_num_walletLoged);
            productDiscount = itemView.findViewById(R.id.id_discount);
            productWeight = itemView.findViewById(R.id.id_weight);
            productColor = itemView.findViewById(R.id.id_color);
            productSize = itemView.findViewById(R.id.product_size);
            productImage = itemView.findViewById(R.id.id_product_image);
        }
    }

    public interface WalletLogViewListener {
        void onWalletLogClicked(View v, int position);

        void onToggleWishList(View v, int position);
    }
}
