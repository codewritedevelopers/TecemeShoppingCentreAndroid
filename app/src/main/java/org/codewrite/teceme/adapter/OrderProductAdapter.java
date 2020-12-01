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
import org.codewrite.teceme.model.room.CustomerOrderEntity;

public class OrderProductAdapter extends ListAdapter<CustomerOrderEntity, OrderProductAdapter.OrderViewHolder> {

    private static final DiffUtil.ItemCallback<CustomerOrderEntity>
            DIFF_CALLBACK = new DiffUtil.ItemCallback<CustomerOrderEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull CustomerOrderEntity oldItem,
                                       @NonNull CustomerOrderEntity newItem) {
            return oldItem.getCustomer_order_id().equals(newItem.getCustomer_order_id());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CustomerOrderEntity oldItem,
                                          @NonNull CustomerOrderEntity newItem) {
            return oldItem.getCustomer_order_product_name().equals(newItem.getCustomer_order_product_name())
                    && oldItem.getCustomer_order_product_img_uri().equals(newItem.getCustomer_order_product_img_uri())
                    && oldItem.getCustomer_order_quantity().equals(newItem.getCustomer_order_quantity())
                    && oldItem.getCustomer_order_product_size().equals(newItem.getCustomer_order_product_size())
                    && oldItem.getCustomer_order_product_weight().equals(newItem.getCustomer_order_product_weight())
                    && oldItem.getCustomer_order_product_code().equals(newItem.getCustomer_order_product_code())
                    && oldItem.getCustomer_order_code().equals(newItem.getCustomer_order_product_code())
                    && oldItem.getCustomer_order_product_color().equals(newItem.getCustomer_order_product_color())
                    && oldItem.getCustomer_order_product_price().equals(newItem.getCustomer_order_product_price());
        }
    };

    // member variable or objects
    private Context activityContext;
    private OrderViewListener productViewListener;
    private Integer fragme_status;

    /**
     * @class: CategoryOrderAdapter
     */
    public OrderProductAdapter(Context activityContext,Integer fragme_status) {
        super(DIFF_CALLBACK);
        this.activityContext = activityContext;
        this.fragme_status = fragme_status;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_content, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position) {
        // get a category product from list
        CustomerOrderEntity entity = getItem(position);
        if (entity == null) {
            return;
        }
        // set image
        try {
            Picasso.get()
                    .load(activityContext.getResources().getString(R.string.api_base_url)
                            + "products/product-image/"
                            + entity.getCustomer_order_product_img_uri())
                    .placeholder(R.drawable.loading_image)
                    .into(holder.productImage);
        } catch (Exception e) {
            holder.productImage.setImageResource(R.drawable.no_product_image);
        }

        // set group product name
        if (holder.productName != null)
            holder.productName.setText(entity.getCustomer_order_product_name());

        if (holder.productPrice != null) {
            String cedis = "GH₵ ";
            holder.productPrice.setText(cedis.concat(entity.getCustomer_order_product_price()));
        }

        if (holder.productColor != null) {
            if (!entity.getCustomer_order_product_color().isEmpty()) {
                holder.productColor.setVisibility(View.VISIBLE);
                holder.productColor.setText(entity.getCustomer_order_product_color());
            } else {
                holder.productColor.setVisibility(View.GONE);
            }
        }

        if (holder.productSize != null) {
            if (!entity.getCustomer_order_product_size().isEmpty()) {
                holder.productSize.setVisibility(View.VISIBLE);
                holder.productSize.setText(entity.getCustomer_order_product_size());
                holder.productSize.setBackgroundColor(
                        activityContext.getResources().getColor(R.color.colorPrimary));
                holder.productSize.setTextColor(
                        activityContext.getResources().getColor(R.color.colorWhite));
            } else {
                holder.productSize.setVisibility(View.GONE);
            }
        }

        if (holder.productQuantity != null) {
                holder.productQuantity.setText(String.valueOf(entity.getCustomer_order_quantity()));
        }

        if (holder.orderStatus!=null){

            String status="";
            switch (fragme_status) {
                case -1:
                    if (entity.getCustomer_order_status()==0) {
                        status = entity.getCustomer_order_quantity()-entity.getCustomer_order_redeemed()+ " PENDING";
                    }else {
                        status = entity.getCustomer_order_redeemed()+ " REDEEMED";
                    }
                break;
                case 0:
                    status = entity.getCustomer_order_quantity()-entity.getCustomer_order_redeemed()+ " PENDING";
                    break;
                case 1:
                    status = entity.getCustomer_order_redeemed()+ " REDEEMED";
                    break;
            }
            holder.orderStatus.setText(status);
        }

        if (holder.productWeight != null && entity.getCustomer_order_product_weight() != null) {
            if (!entity.getCustomer_order_product_weight().isEmpty()) {
                holder.productWeight.setVisibility(View.VISIBLE);
                holder.productWeight.setText(entity.getCustomer_order_product_weight());
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
        private ImageView productImage;
        private TextView orderStatus;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.id_product_name);
            productPrice = itemView.findViewById(R.id.id_price);
            productQuantity = itemView.findViewById(R.id.id_num_ordered);
            productWeight = itemView.findViewById(R.id.id_weight);
            productColor = itemView.findViewById(R.id.id_color);
            productSize = itemView.findViewById(R.id.product_size);
            productImage = itemView.findViewById(R.id.id_product_image);
            orderStatus = itemView.findViewById(R.id.id_status);
        }
    }

    public interface OrderViewListener {
        void onOrderClicked(View v, int position);
    }
}
