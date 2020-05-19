package org.codewrite.teceme.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.room.CategoryEntity;

public class HomeCategoryAdapter extends ListAdapter<CategoryEntity, HomeCategoryAdapter.CategoryViewHolder> {

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
    private Context activityContext;
    private int selectedItem;
    private CategoryViewListener categoryViewListener;

    /**
     * @class: CategoryProductAdapter
     */
    public HomeCategoryAdapter(Context activityContext) {
        super(DIFF_CALLBACK);
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_category_list_content, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {
        // get a category product from list
        final CategoryEntity entity = getItem(position);
        assert entity != null;

        // get resources reference, we use it when needed
        Resources resources = activityContext.getResources();

        // set group product name
        if (holder.categoryName != null) {
            holder.categoryName.setText(entity.getCategory_name());
            if (getSelectedItem() == position) {
                holder.categoryName.setTextColor(resources.getColor(R.color.colorPrimaryDark));
            } else {
                holder.categoryName.setTextColor(resources.getColor(R.color.colorBlack));
            }
        }

        // set on item clicked listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryViewListener != null)
                    categoryViewListener.onCategoryClicked(v, position);
            }
        });
    }

    private int getSelectedItem() {
        // check if categoryViewLister is set
        if (categoryViewListener != null)
            selectedItem = categoryViewListener.onSelectItem();
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        // notify change for previous item
        notifyItemChanged(this.selectedItem);

        // update current item selected
        this.selectedItem = selectedItem;

        // notify change for current item
        notifyItemChanged(selectedItem);
    }

    public void setCategoryViewListener(CategoryViewListener categoryViewListener) {
        this.categoryViewListener = categoryViewListener;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.id_home_category_name);
        }
    }

    public interface CategoryViewListener {
        int onSelectItem();

        void onCategoryClicked(View v, int position);
    }
}
