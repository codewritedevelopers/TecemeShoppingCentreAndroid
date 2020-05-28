package org.codewrite.teceme.adapter;

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
import org.codewrite.teceme.model.ProductSize;

public class ProductSizeAdapter extends ListAdapter<ProductSize, ProductSizeAdapter.ProductViewHolder> {

    private static final DiffUtil.ItemCallback<ProductSize>
            DIFF_CALLBACK = new DiffUtil.ItemCallback<ProductSize>() {
        @Override
        public boolean areItemsTheSame(@NonNull ProductSize oldItem,
                                       @NonNull ProductSize newItem) {
            return oldItem.getSize().equals(newItem.getSize())
                    && oldItem.isSelected()==newItem.isSelected();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ProductSize oldItem,
                                          @NonNull ProductSize newItem) {
            return true;
        }
    };

    // member variable or objects
    private ProductViewListener productViewListener;
    private static int selectPosition;

    /**
     * @class: CategoryProductAdapter
     */
    public ProductSizeAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_design_sizes, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
        // get a category product from list
        ProductSize entity = getItem(position);
        assert entity != null;

        if (entity.isSelected()){
            selectPosition = position;
        }
        if (productViewListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productViewListener.onChangeSize(v, selectPosition, position);
                    getItem(selectPosition).setSelected(false);
                    getItem(position).setSelected(true);
                    notifyItemChanged(selectPosition);
                    selectPosition = position;
                    notifyItemChanged(position);
                }
            });
        }

        Resources resources = holder.itemView.getContext().getResources();
        if (entity.isSelected() && holder.size!=null) {
            holder.size.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark));
            holder.size.setTextColor(resources.getColor(R.color.colorWhite));
        } else if(holder.size!=null) {
            holder.size.setBackgroundColor(resources.getColor(R.color.colorWhite));
            holder.size.setTextColor(resources.getColor(R.color.colorBlack));
        }

        if (holder.size!=null){
            holder.size.setText(entity.getSize());
        }
    }

    public void setProductViewListener(ProductViewListener productViewListener) {
        this.productViewListener = productViewListener;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView size;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            size = itemView.findViewById(R.id.product_size);
        }
    }

    public interface ProductViewListener {
        void onChangeSize(View v, int from, int to);
    }
}
