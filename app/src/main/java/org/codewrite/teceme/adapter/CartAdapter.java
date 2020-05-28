package org.codewrite.teceme.adapter;

import android.app.Activity;
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
import org.codewrite.teceme.model.room.CartEntity;

public class CartAdapter extends ListAdapter<CartEntity, CartAdapter.ProductViewHolder> {

    private static final DiffUtil.ItemCallback<CartEntity>
            DIFF_CALLBACK = new DiffUtil.ItemCallback<CartEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull CartEntity oldItem,
                                       @NonNull CartEntity newItem) {
            return oldItem.getCart_owner().equals(newItem.getCart_owner());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartEntity oldItem,
                                          @NonNull CartEntity newItem) {
            return oldItem.getProduct_name().equals(newItem.getProduct_name())
                    && oldItem.getProduct_img_uri().equals(newItem.getProduct_img_uri())
                    && oldItem.getProduct_category_id().equals(newItem.getProduct_category_id())
                    && oldItem.getProduct_weight().equals(newItem.getProduct_weight())
                    && oldItem.getProduct_price().equals(newItem.getProduct_price())
                    && oldItem.getProduct_ordered() == newItem.getProduct_ordered()
                    && oldItem.getCart_product_id().equals(newItem.getCart_product_id())
                    && oldItem.getCart_quantity().equals(newItem.getCart_quantity())
                    && oldItem.getProduct_color().equals(newItem.getProduct_color());
        }
    };

    // member variable or objects
    private Activity activityContext;
    private ProductViewListener productViewListener;

    /**
     * @class: CategoryProductAdapter
     */
    public CartAdapter(Activity activityContext) {
        super(DIFF_CALLBACK);
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_content, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
        // get a category product from list
        CartEntity entity = getItem(position);
        assert entity != null;

        // set image
        try {
            Picasso.get()
                    .load(activityContext.getResources().getString(R.string.api_base_url)
                            + entity.getProduct_img_uri())
                    .resize(60, 60)
                    .placeholder(R.drawable.loading_image)
                    .error(R.drawable.no_product_image)
                    .into(holder.productImage);
        } catch (Exception e) {
            holder.productImage.setImageResource(R.drawable.no_product_image);
        }

        // set product view lister
        if (productViewListener != null) {
            // set item clicked
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productViewListener.onProductClicked(v, position);
                }
            });

            // set wish list indicator
            productViewListener.isInWishList(entity.getCart_product_id())
                    .observeForever(new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean isInWishList) {
                            if (isInWishList == null)
                                return;
                            if (isInWishList) {
                                holder.wishListProduct.setColorFilter(R.color.colorPrimaryDark,
                                        android.graphics.PorterDuff.Mode.MULTIPLY);
                                holder.wishListProduct.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        productViewListener.onToggleWishList(v, position);
                                    }
                                });
                            } else {
                                holder.wishListProduct.setColorFilter(R.color.colorAccent,
                                        android.graphics.PorterDuff.Mode.MULTIPLY);
                            }
                        }
                    });

            // set group product name
            if (holder.productName != null)
                holder.productName.setText(entity.getProduct_name());

            if (holder.productPrice != null) {
                String cedis = "GH₵ ";
                holder.productPrice.setText(cedis.concat(entity.getProduct_price()));
            }

            if (holder.productWeight != null && entity.getProduct_weight() != null) {
                if (!entity.getProduct_weight().isEmpty()) {
                    holder.productWeight.setVisibility(View.VISIBLE);
                    holder.productWeight.setText(entity.getProduct_weight());
                }else{
                    holder.productWeight.setVisibility(View.GONE);
                }
            } else if (holder.productWeight != null) {
                holder.productWeight.setVisibility(View.GONE);
            }

            if (holder.productDiscount != null && entity.getProduct_discount() != null) {
                if (!entity.getProduct_discount().isEmpty()) {
                    holder.productDiscount.setVisibility(View.VISIBLE);
                    holder.productDiscount.setText(entity.getProduct_discount());
                }else{
                    holder.productDiscount.setVisibility(View.GONE);
                }
            } else if (holder.productDiscount != null) {
                holder.productDiscount.setVisibility(View.GONE);
            }

            if (holder.productOrdered != null) {
                String ordered =  entity.getProduct_ordered() + " ordered";
                holder.productOrdered.setText(ordered);
            }
        }
    }

    public void setProductViewListener(ProductViewListener productViewListener) {
        this.productViewListener = productViewListener;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView productPrice;
        private TextView productWeight;
        private TextView productDiscount;
        private ImageView wishListProduct;
        private ImageView productImage;
        private TextView productOrdered;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.id_product_name);
            productPrice = itemView.findViewById(R.id.id_price);
            productOrdered = itemView.findViewById(R.id.id_orders);
            productDiscount = itemView.findViewById(R.id.id_discount);
            productWeight = itemView.findViewById(R.id.id_weight);
            wishListProduct = itemView.findViewById(R.id.id_wish_list_indicator);
            productImage = itemView.findViewById(R.id.id_product_image);
        }
    }

    public interface ProductViewListener {
        void onProductClicked(View v, int position);

        LiveData<Boolean> isInWishList(int id);

        void onToggleWishList(View v, int position);
    }
}