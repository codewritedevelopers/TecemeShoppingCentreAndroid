package org.codewrite.teceme.adapter;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.room.CartEntity;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.model.room.WishListEntity;
import org.codewrite.teceme.viewmodel.AccountViewModel;
import org.codewrite.teceme.viewmodel.CartViewModel;

public class WishListAdapter extends ListAdapter<WishListEntity, WishListAdapter.WishListViewHolder> {

    private static final DiffUtil.ItemCallback<WishListEntity>
            DIFF_CALLBACK = new DiffUtil.ItemCallback<WishListEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull WishListEntity oldItem,
                                       @NonNull WishListEntity newItem) {
            return oldItem.getWishlist_id().equals(newItem.getWishlist_id());
        }

        @Override
        public boolean areContentsTheSame(@NonNull WishListEntity oldItem,
                                          @NonNull WishListEntity newItem) {
            return oldItem.getWishlist_customer_id().equals(newItem.getWishlist_customer_id())
                    && oldItem.getWishlist_date_created().equals(newItem.getWishlist_date_created())
                    && oldItem.getWishlist_product_id().equals(newItem.getWishlist_product_id());
        }
    };

    // member variable or objects
    private AppCompatActivity activityContext;
    private WishListViewListener wishListViewListener;

    /**
     * @class: WishListAdapter
     */
    public WishListAdapter(AppCompatActivity activityContext) {
        super(DIFF_CALLBACK);
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wish_list_content, parent, false);
        return new WishListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WishListViewHolder holder, final int position) {
        // get a category product from list
        final WishListEntity wishListEntity = getItem(position);
        if (wishListEntity == null) {
            return;
        }

        if (wishListViewListener==null){
            return;
        }

        if (holder.productImage!=null) {
            holder.productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wishListViewListener.onWishListClicked(v,wishListEntity.getWishlist_product_id());
                }
            });
        }
        wishListViewListener.isInCart(wishListEntity.getWishlist_product_id())
                .observe(activityContext, new Observer<CartEntity>() {
            @Override
            public void onChanged(final CartEntity cartEntity) {
                String s;
                if (cartEntity==null) {
                     s = "Add To Cart";
                    if (holder.productCart!=null){
                        holder.productCart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                wishListViewListener.onToggleCart(false,null,
                                        wishListEntity.getWishlist_product_id());
                            }
                        });
                    }
                }else {
                    s = "Remove From Cart";
                    if (holder.productCart!=null){
                        holder.productCart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                wishListViewListener.onToggleCart(true,cartEntity,
                                        wishListEntity.getWishlist_product_id());
                            }
                        });
                    }
                }
                if (holder.productCart != null) {
                    holder.productCart.setText(s);
                }

            }
        });

        if (holder.deleteWishList!=null){
            holder.deleteWishList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wishListViewListener.onDelete(position,wishListEntity.getWishlist_product_id());
                }
            });
        }

        wishListViewListener.onLoadProduct(wishListEntity.getWishlist_product_id())
                .observe(activityContext, new Observer<ProductEntity>() {
                    @Override
                    public void onChanged(ProductEntity entity) {
                        if (entity==null){
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

                        // set product view lister
                            // set item clicked
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    wishListViewListener.onWishListClicked(v, position);
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
                        }
                    }
                });
    }

    public void setWishListViewListener(WishListViewListener wishListViewListener) {
        this.wishListViewListener = wishListViewListener;
    }

    static class WishListViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView productPrice;
        private TextView productWeight;
        private TextView productCart;
        private TextView buyNow;
        private TextView productDiscount;
        private ImageView productImage;
        private TextView productOrdered;
        private ImageView deleteWishList;

        WishListViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.id_product_name);
            productPrice = itemView.findViewById(R.id.id_price);
            productOrdered = itemView.findViewById(R.id.id_orders);
            productDiscount = itemView.findViewById(R.id.id_discount);
            productWeight = itemView.findViewById(R.id.id_weight);
            productCart = itemView.findViewById(R.id.id_toggle_cart);
            productImage = itemView.findViewById(R.id.id_product_image);
            deleteWishList = itemView.findViewById(R.id.id_delete);
        }
    }

    public interface WishListViewListener {
        void onWishListClicked(View v, Integer product_id);
        void onDelete(int position, Integer product_id);
        LiveData<CartEntity> isInCart(Integer product_id);
        void onToggleCart(boolean remove, CartEntity cartEntity, Integer product_id);
        LiveData<ProductEntity> onLoadProduct(Integer product_id);
    }
}