package org.codewrite.teceme.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.room.CategoryEntity;

public class CategoryProductAdapter extends
        ListAdapter<CategoryEntity, CategoryProductAdapter.CategoryProductViewHolder> {

    // static constant
    private static final DiffUtil.ItemCallback<CategoryEntity>
            DIFF_CALLBACK = new DiffUtil.ItemCallback<CategoryEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull CategoryEntity oldItem,
                                       @NonNull CategoryEntity newItem) {
            return oldItem.getCategory_id().equals(newItem.getCategory_id());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CategoryEntity oldItem,
                                          @NonNull CategoryEntity newItem) {
            return oldItem.getCategory_name().equals(newItem.getCategory_name())
                    && oldItem.getCategory_access().equals(newItem.getCategory_access())
                    && oldItem.getCategory_date_created().equals(newItem.getCategory_date_created())
                    && oldItem.getCategory_level().equals(newItem.getCategory_level())
                    && oldItem.getCategory_parent_id().equals(newItem.getCategory_parent_id());
        }
    };

    // member variable or objects
    private Activity activityContext;
    private ProductRecyclerViewLoader productRvLoader;
    private CategoryProductViewListener categoryProductViewListener;

    /**
     * @class: CategoryProductAdapter
     */
    public CategoryProductAdapter(Activity activityContext) {
        super(DIFF_CALLBACK);
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public CategoryProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_product_list_content, parent, false);

        return new CategoryProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryProductViewHolder holder, int position) {
        // get a category product from list
        CategoryEntity entity = getItem(position);
        // if entity null don't bind view
        if (entity == null) {
            return;
        }

        // set group product name
        if (holder.categoryName != null) {
            holder.categoryName.setText(entity.getCategory_name());
        }

        // handle fetching of products into view
       handleProductFetch(holder, position);
    }

    /**
     * Handling fetching of products into each recycler view
     *
     * @param holder   view holder for CategoryProductAdapter
     * @param position position or index of each item in CategoryProduct, needed for loading
     *                 products into recycler view
     */
    private void handleProductFetch(CategoryProductViewHolder holder, int position) {

        // check if productRvLoader is attached to page list adapter
        if (productRvLoader == null) {
            return;
        }
        // loadData products into product recycler view & observe if loaded
        productRvLoader.loadData(position, holder.itemView);
        Log.d("CategoryProductAdapter", "handleProductFetch: "+position);
    }

    public void setProductRvLoader(ProductRecyclerViewLoader productRvLoader) {
        this.productRvLoader = productRvLoader;
    }

    public void setCategoryProductViewListener(CategoryProductViewListener
                                                       categoryProductViewListener) {
        this.categoryProductViewListener = categoryProductViewListener;
    }

    class CategoryProductViewHolder extends RecyclerView.ViewHolder {
        private TextView viewAll;
        private TextView categoryName;

        CategoryProductViewHolder(@NonNull View itemView) {
            super(itemView);
            viewAll = itemView.findViewById(R.id.id_view_all);
            categoryName = itemView.findViewById(R.id.id_category_name);
        }
    }

    public interface ProductRecyclerViewLoader {
       void loadData(int position, View itemView);

        void invalidate();

        boolean stop(int position);
    }

    public interface CategoryProductViewListener {
        void onViewAllClicked(View v, CategoryEntity entity);
    }
}
