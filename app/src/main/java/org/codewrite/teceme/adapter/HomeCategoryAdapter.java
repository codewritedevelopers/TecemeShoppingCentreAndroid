package org.codewrite.teceme.adapter;

import android.app.Activity;
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
            return oldItem.getCategory_name().equals(newItem.getCategory_name());
        }
    };

    // member variable or objects
    private Activity activityContext;

    /**
     * @class: CategoryProductAdapter
     */
    public HomeCategoryAdapter(Activity activityContext) {
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
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position) {
        // get a category product from list
        CategoryEntity entity = getItem(position);
        assert entity != null;

        // set group product name
        if (holder.categoryName != null)
            holder.categoryName.setText(entity.getCategory_id());
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.id_category_name);
        }
    }

    public interface CategoryViewListener {
        void onViewAllClicked(View v, CategoryEntity entity);
    }
}
