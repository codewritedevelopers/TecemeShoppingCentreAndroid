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
            return oldItem.getStore_name().equals(newItem.getStore_name());
        }
    };

    // member variable or objects
    private Activity activityContext;

    /**
     * @class: CategoryProductAdapter
     */
    public StoreAdapter(Activity activityContext) {
        super(DIFF_CALLBACK);
        this.activityContext = activityContext;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_list_content, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoreViewHolder holder, int position) {
        // get a category product from list
        StoreEntity entity = getItem(position);
        assert entity != null;

        // set group product name
        if (holder.categoryName != null)
            holder.categoryName.setText(entity.getStore_id());
    }

    class StoreViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;

        StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.id_category_name);
        }
    }

    public interface StoreViewListener {
        void onViewAllClicked(View v, StoreEntity entity);
    }
}
