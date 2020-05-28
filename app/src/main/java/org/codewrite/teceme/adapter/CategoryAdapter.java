package org.codewrite.teceme.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.room.CategoryEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryAdapter extends ListAdapter<CategoryEntity, CategoryAdapter.CategoryViewHolder> {

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
                    && oldItem.getState().equals(newItem.getState())
                    && oldItem.getCategory_level().equals(newItem.getCategory_level());
        }
    };

    private CategoryViewListener categoryViewListener;
    private Map<Integer, List<CategoryEntity>> listMap = new HashMap<>();
    private List<CategoryEntity> defaultList = new ArrayList<>();
    private boolean isFirstSubmit;
    private int previouslyOpenedPosition;

    /**
     * @class: CategoryProductAdapter
     */
    public CategoryAdapter() {
        super(DIFF_CALLBACK);
        isFirstSubmit = true;
        previouslyOpenedPosition = -1;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_content, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {
        // get a category product from list
        final CategoryEntity entity = getItem(position);
        assert entity != null;

        holder.categoryName.setText(entity.getCategory_name());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(holder.container.getLayoutParams());
        layoutParams.setMargins(((int) convertDpToPixel(24, holder.itemView.getContext())) * entity.getCategory_level(),
                layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);

        switch (entity.getState()) {

            case CLOSED:
                holder.imgArrow.setImageResource(R.drawable.svg_arrow_right_filled);
                break;
            case OPENED:
                holder.imgArrow.setImageResource(R.drawable.svg_arrow_down_filled);
                break;
        }

        if (categoryViewListener != null) {

            categoryViewListener.onSubCategory(entity.getCategory_id())
                    .observeForever(new Observer<List<CategoryEntity>>() {
                        @Override
                        public void onChanged(List<CategoryEntity> categoryEntities) {
                            if (categoryEntities == null) {
                                return;
                            }
                            if (categoryEntities.size() > 0) {
                                listMap.put(position, categoryEntities);
                                holder.imgArrow.setVisibility(View.VISIBLE);
                                holder.viewDashed.setVisibility(View.INVISIBLE);
                            } else {
                                holder.imgArrow.setVisibility(View.INVISIBLE);
                                holder.viewDashed.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<CategoryEntity> mCategoryEntities = listMap.get(position);
                if(mCategoryEntities==null)
                    return;
                if (mCategoryEntities.size() == 0) {
                    return;
                }
                final List<CategoryEntity> entities = new ArrayList<>(defaultList);
                switch (entity.getState()) {
                    case CLOSED:
                        if (previouslyOpenedPosition != -1 && position != previouslyOpenedPosition) {
                            entities.get(previouslyOpenedPosition).setState(CategoryEntity.STATE.CLOSED);
                        }
                        entities.addAll(position + 1, mCategoryEntities);
                        entities.get(position).setState(CategoryEntity.STATE.OPENED);
                        break;
                    case OPENED:
                        entities.get(position).setState(CategoryEntity.STATE.CLOSED);
                        break;
                }
                submitList(entities, new Runnable() {
                    @Override
                    public void run() {
                        notifyItemChanged(position);
                        if (previouslyOpenedPosition != -1 && position != previouslyOpenedPosition) {
                            notifyItemChanged(previouslyOpenedPosition);
                        }
                        previouslyOpenedPosition = position;
                    }
                });
                categoryViewListener.onViewClicked(v, position);
            }
        });
    }

    @Override
    public void submitList(@Nullable List<CategoryEntity> list) {
        super.submitList(list);
        if (isFirstSubmit) {
            assert list != null;
            defaultList.addAll(list);
            isFirstSubmit = false;
        }
    }

    public void setCategoryViewListener(CategoryViewListener categoryViewListener) {
        this.categoryViewListener = categoryViewListener;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView imgArrow;
        View viewDashed;
        LinearLayout container;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.id_category_name);
            imgArrow = itemView.findViewById(R.id.imgArrow);
            viewDashed = itemView.findViewById(R.id.viewDashed);
            container = itemView.findViewById(R.id.container);
        }
    }

    private static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    private static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public interface CategoryViewListener {
        void onViewClicked(View v, int position);

        LiveData<List<CategoryEntity>> onSubCategory(int id);
    }


}
