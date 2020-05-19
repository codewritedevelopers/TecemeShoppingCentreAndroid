package org.codewrite.teceme.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.room.ProductEntity;

public class ProductAdapter extends PagedListAdapter<ProductEntity, ProductAdapter.ProductViewHolder> {

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
            return oldItem.getProduct_name().equals(newItem.getProduct_name());
        }
    };

    // member variable or objects
    private Activity activityContext;

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
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, int position) {
        // get a category product from list
        ProductEntity entity = getItem(position);
        assert entity != null;

        // set group product name
        if (holder.productName != null)
            holder.productName.setText(entity.getProduct_name());
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.id_product_name);
        }
    }

    public interface ProductViewListener {
        void onProdutClicked(View v, int position);
    }
}
