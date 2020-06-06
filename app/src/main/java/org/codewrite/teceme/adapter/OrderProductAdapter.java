package org.codewrite.teceme.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.room.OrderEntity;

public class OrderProductAdapter extends ListAdapter<OrderEntity, OrderProductAdapter.OrderViewHolder> {

    private static final DiffUtil.ItemCallback<OrderEntity>
            DIFF_CALLBACK = new DiffUtil.ItemCallback<OrderEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull OrderEntity oldItem,
                                       @NonNull OrderEntity newItem) {
            return oldItem.getOrder_id().equals(newItem.getOrder_id());
        }

        @Override
        public boolean areContentsTheSame(@NonNull OrderEntity oldItem,
                                          @NonNull OrderEntity newItem) {
            return oldItem.getProduct_name().equals(newItem.getProduct_name())
                    && oldItem.getProduct_img_uri().equals(newItem.getProduct_img_uri())
                    && oldItem.getProduct_category_id().equals(newItem.getProduct_category_id())
                    && oldItem.getOrder_quantity().equals(newItem.getOrder_quantity())
                    && oldItem.getProduct_price().equals(newItem.getProduct_price());
        }
    };

    // member variable or objects
    private Context activityContext;
    private OrderViewListener productViewListener;

    /**
     * @class: CategoryOrderAdapter
     */
    public OrderProductAdapter(Context activityContext) {
        super(DIFF_CALLBACK);
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_content, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position) {
        // get a category product from list
        OrderEntity entity = getItem(position);
        if (entity == null) {
            return;
        }
        // set image
        try {
            Picasso.get()
                    .load(activityContext.getResources().getString(R.string.api_base_url)
                            + "products/product-image/"
                            + entity.getProduct_img_uri().split(",")[0])
                    .placeholder(R.drawable.loading_image)
                    .error(R.drawable.no_product_image)
                    .into(holder.productImage);
        } catch (Exception e) {
            holder.productImage.setImageResource(R.drawable.no_product_image);
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
            holder.productQuantity.setText(String.valueOf(entity.getOrder_quantity()));
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
                        productViewListener.onOrderClicked(holder.itemView, position);
                    }
                });
            }
        }
    }

    public void setOrderViewListener(OrderViewListener productViewListener) {
        this.productViewListener = productViewListener;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView productPrice;
        private TextView productColor;
        private TextView productSize;
        private TextView productWeight;
        private TextView productQuantity;
        private TextView productDiscount;
        private ImageView productImage;
        private TextView productOrdered;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.id_product_name);
            productPrice = itemView.findViewById(R.id.id_price);
            productOrdered = itemView.findViewById(R.id.id_orders);
            productQuantity = itemView.findViewById(R.id.id_num_ordered);
            productDiscount = itemView.findViewById(R.id.id_discount);
            productWeight = itemView.findViewById(R.id.id_weight);
            productColor = itemView.findViewById(R.id.id_color);
            productSize = itemView.findViewById(R.id.product_size);
            productImage = itemView.findViewById(R.id.id_product_image);
        }
    }

    public interface OrderViewListener {
        void onOrderClicked(View v, int position);

        void onToggleWishList(View v, int position);
    }
}
