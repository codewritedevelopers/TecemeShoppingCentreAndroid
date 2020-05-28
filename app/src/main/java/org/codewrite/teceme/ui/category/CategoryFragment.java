package org.codewrite.teceme.ui.category;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.AdsSliderAdapter;
import org.codewrite.teceme.adapter.CategoryAdapter;
import org.codewrite.teceme.adapter.HomeCategoryAdapter;
import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.utils.SliderTimer;
import org.codewrite.teceme.utils.ZoomOutPageTransformer;
import org.codewrite.teceme.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class CategoryFragment extends Fragment {

    private CategoryViewModel categoryViewModel;
    private CategoryAdapter categoryAdapter;
    private RecyclerView mCategoryRv;
    private FragmentActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        assert mActivity != null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

       View root = inflater.inflate(R.layout.fragment_category, container, false);
        // get category view model
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        // find recycler view for all main categories
        mCategoryRv = root.findViewById(R.id.id_rv_category_list);
        // setup recycler view, should be called after setupCategoryProductRv()
        setupCategoryRv(mCategoryRv);
        return root;
    }

    private void setupCategoryRv(@NonNull RecyclerView recyclerView) {

        // create HomeCategoryAdapter
        categoryAdapter = new CategoryAdapter();
        // we set category view listener
        categoryAdapter.setCategoryViewListener(new CategoryAdapter.CategoryViewListener() {
            @Override
            public void onViewClicked(View v, int position) {

            }

            @Override
            public LiveData<List<CategoryEntity>> onSubCategory(int id) {
                return categoryViewModel.getCategoryByParentResult(id);
            }

        });

        // we set recycler view adapter
        recyclerView.setAdapter(categoryAdapter);

        // load from database
        categoryViewModel.getCategoryForHomeResult()
                .observe(mActivity, new Observer<List<CategoryEntity>>() {
                    @Override
                    public void onChanged(List<CategoryEntity> categoryEntities) {
                        if (categoryEntities == null) {
                            return;
                        }
                        categoryAdapter.submitList(categoryEntities);
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
