package org.codewrite.teceme.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.model.room.WishListEntity;

public class ProductAdapter extends ListAdapter<ProductEntity, ProductAdapter.ProductViewHolder> {

    private static final DiffUtil.ItemCallback<ProductEntity>
            DIFF_CALLBACK = new DiffUtil.ItemCallback<ProductEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull ProductEntity oldItem,
                                       @NonNull ProductEntity newItem) {
            return oldItem.getProduct_id().equals(newItem.getProduct_id());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ProductEntity oldItem,
                                          @NonNull ProductEntity newItem) {
            return oldItem.getProduct_name().equals(newItem.getProduct_name())
                    && oldItem.getProduct_img_uri().equals(newItem.getProduct_img_uri())
                    && oldItem.getProduct_category_id().equals(newItem.getProduct_category_id())
                    && oldItem.getProduct_weight().equals(newItem.getProduct_weight())
                    && oldItem.getProduct_price().equals(newItem.getProduct_price())
                    && oldItem.getProduct_ordered() == newItem.getProduct_ordered();
        }
    };

    // member variable or objects
    private Activity activityContext;
    private ProductViewListener productViewListener;

    /**
     * @class: CategoryProductAdapter
     */
    public ProductAdapter(Activity activityContext) {
        super(DIFF_CALLBACK);
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_content, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
        // get a category product from list
        final ProductEntity entity = getItem(position);
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
            final boolean[] wishListed = {false};

            // set wish list indicator
            productViewListener.isInWishList(entity.getProduct_id())
                    .observeForever(new Observer<WishListEntity>() {
                        @Override
                        public void onChanged(final WishListEntity isInWishList) {
                            if (isInWishList == null) {
                                holder.wishListProduct.setColorFilter(R.color.colorAccent,
                                        android.graphics.PorterDuff.Mode.MULTIPLY);

                                holder.wishListProduct.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (productViewListener != null) {
                                            holder.wishListProduct.clearColorFilter();
                                            productViewListener.onToggleWishList(v, entity.getProduct_id(),
                                                    null, false);
                                        }
                                    }
                                });
                                return;
                            }
                            holder.wishListProduct.clearColorFilter();

                            holder.wishListProduct.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (productViewListener != null) {
                                        holder.wishListProduct.setColorFilter(R.color.colorAccent,
                                                android.graphics.PorterDuff.Mode.MULTIPLY);
                                        productViewListener.onToggleWishList(v, entity.getProduct_id(),
                                                isInWishList.getWishlist_id(), true);
                                    }
                                }
                            });
                        }
                    });
        }
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
            } else {
                holder.productWeight.setVisibility(View.GONE);
            }
        } else if (holder.productWeight != null) {
            holder.productWeight.setVisibility(View.GONE);
        }

        if (holder.productDiscount != null && entity.getProduct_discount() != null) {
            if (!entity.getProduct_discount().isEmpty()) {
                holder.productDiscount.setVisibility(View.VISIBLE);
                holder.productDiscount.setText(entity.getProduct_discount());
            } else {
                holder.productDiscount.setVisibility(View.GONE);
            }
        } else if (holder.productDiscount != null) {
            holder.productDiscount.setVisibility(View.GONE);
        }

        if (holder.productOrdered != null) {
            String ordered = entity.getProduct_ordered() + " ordered";
            holder.productOrdered.setText(ordered);
            if (entity.getProduct_ordered()==0){
                holder.productOrdered.setVisibility(View.GONE);
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
        LiveData<WishListEntity> isInWishList(Integer id);
        void onToggleWishList(View v, Integer product_id,String id, boolean added);
    }
}
