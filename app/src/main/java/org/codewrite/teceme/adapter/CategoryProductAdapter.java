package org.codewrite.teceme.adapter;

import android.app.Activity;
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
            return oldItem.getCategory_name().equals(newItem.getCategory_name());
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
        assert entity != null;

        // handle fetch product into the sub recycler view
        handleProductFetch(holder, position);

        // set group product name
        if (holder.categoryName != null)
            holder.categoryName.setText(entity.getCategory_name());


    }

    private void handleProductFetch(final CategoryProductViewHolder holder, int position) {

        final CategoryEntity entity = getItem(position);

        // check if productRvLoader is attached to page list adapter
        if (productRvLoader == null) {
            return;
        }
        // loadData products into product recycler view & observe if loaded
        productRvLoader.loadData(entity)
                .observe((LifecycleOwner) activityContext, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean == null) {
                            return;
                        }
                        if (aBoolean.equals(true) && holder.viewAll != null) {
                            holder.viewAll.setVisibility(View.VISIBLE);
                            holder.viewAll.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (categoryProductViewListener != null)
                                        categoryProductViewListener.onViewAllClicked(v, entity);
                                }
                            });
                        } else if (holder.viewAll != null) {
                            holder.viewAll.setVisibility(View.VISIBLE);
                        }
                    }
                });
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

    interface ProductRecyclerViewLoader {
        LiveData<Boolean> loadData(CategoryEntity id);

        void invalidate();

        boolean stop(Integer id);
    }

    public interface CategoryProductViewListener {
        void onViewAllClicked(View v, CategoryEntity entity);
    }
}
