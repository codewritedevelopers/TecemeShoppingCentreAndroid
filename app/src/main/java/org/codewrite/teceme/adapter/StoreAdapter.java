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
import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.model.room.StoreEntity;

public class StoreAdapter extends ListAdapter<StoreEntity, StoreAdapter.StoreViewHolder> {

    private static final DiffUtil.ItemCallback<StoreEntity>
            DIFF_CALLBACK = new DiffUtil.ItemCallback<StoreEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull StoreEntity oldItem,
                                       @NonNull StoreEntity newItem) {
            return oldItem.getStore_id().equals(newItem.getStore_id());
        }

        @Override
        public boolean areContentsTheSame(@NonNull StoreEntity oldItem,
                                          @NonNull StoreEntity newItem) {
            return oldItem.getStore_name().equals(newItem.getStore_name())
                    && oldItem.getStore_img_uri().equals(newItem.getStore_img_uri())
                    && oldItem.getStore_location().equals(newItem.getStore_location())
                    && oldItem.getStore_phone().equals(newItem.getStore_phone())
                    && oldItem.getStore_category_id().equals(newItem.getStore_category_id());
        }
    };

    // member variable or objects
    private Activity activityContext;
    private StoreViewListener storeViewListener;

    /**
     * @class: CategoryStoreAdapter
     */
    public StoreAdapter(Activity activityContext) {
        super(DIFF_CALLBACK);
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_list_content, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoreViewHolder holder, int position) {
        // get a category product from list
        final StoreEntity entity = getItem(position);
        assert entity != null;

        // set image
        try {
            Picasso.get()
                    .load(activityContext.getResources().getString(R.string.api_base_url)
                            +"stores/store-image/"
                            + entity.getStore_img_uri().split(",")[0])
                    .placeholder(R.drawable.loading_image)
                    .into(holder.storeImage);
        } catch (Exception e) {
            holder.storeImage.setImageResource(R.drawable.no_store_image);
        }

        // set group store name
        if (holder.storeName != null)
            holder.storeName.setText(entity.getStore_name());

        // set group store location
        if (holder.storeLocation != null) {
            String location = "Location: ".concat(entity.getStore_location());
            holder.storeLocation.setText(location);
        }

        if (holder.storePhone != null) {
            String phone = "Contact: ".concat(entity.getStore_phone());
            holder.storePhone.setText(phone);
        }

        // set group store view
        if (holder.storeFollowing != null) {
            String viewed = entity.getStore_following() + " following";
            holder.storeFollowing.setText(viewed);

            if (entity.getStore_following()==0){
                holder.storeFollowing.setVisibility(View.GONE);
            }
        }

        // set group store category
        if (storeViewListener != null) {
            storeViewListener.onLoadCategory(entity.getStore_category_id())
                    .observeForever(new Observer<CategoryEntity>() {
                        @Override
                        public void onChanged(CategoryEntity categoryEntity) {
                            if (categoryEntity == null) {
                                holder.storeCategory.setVisibility(View.GONE);
                                return;
                            }
                            if (holder.storeCategory != null) {
                                holder.storeCategory.setVisibility(View.VISIBLE);
                                holder.storeCategory.setText(categoryEntity.getCategory_name());
                            }
                        }
                    });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storeViewListener.onViewClicked(v, entity.getStore_id());
                }
            });
        }

    }

    public void setStoreViewListener(StoreViewListener storeViewListener) {
        this.storeViewListener = storeViewListener;
    }

    static class StoreViewHolder extends RecyclerView.ViewHolder {
        private TextView storeName;
        private ImageView storeImage;
        private TextView storeCategory;
        private TextView storeLocation;
        private TextView storePhone;
        private TextView storeFollowing;

        StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            storeName = itemView.findViewById(R.id.id_store_name);
            storeImage = itemView.findViewById(R.id.id_store_image);
            storeLocation = itemView.findViewById(R.id.id_store_location);
            storePhone = itemView.findViewById(R.id.id_phone);
            storeCategory = itemView.findViewById(R.id.id_store_category);
            storeFollowing = itemView.findViewById(R.id.id_store_viewed);
        }
    }

    public interface StoreViewListener {
        void onViewClicked(View v, String store_id);

        LiveData<CategoryEntity> onLoadCategory(Integer category_id);
    }
}
