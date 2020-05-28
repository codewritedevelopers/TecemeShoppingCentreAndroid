package org.codewrite.teceme.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.codewrite.teceme.R;
import org.codewrite.teceme.adapter.CategoryAdapter;
import org.codewrite.teceme.model.room.CategoryEntity;
import org.codewrite.teceme.ui.product.ProductActivity;
import org.codewrite.teceme.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CategoryFragment extends Fragment {

    private CategoryViewModel categoryViewModel;
    private CategoryAdapter categoryAdapter;
    private FragmentActivity mActivity;
    private Map<Integer, List<CategoryEntity>> listMap;
    private MutableLiveData<Boolean> listLoaded = new MutableLiveData<>();

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
        listMap = new HashMap<>();
        // find recycler view for all main categories
        ExpandableListView mCategoryListView = root.findViewById(R.id.category_list);
        // setup recycler view
        setupCategoryRv(mCategoryListView);
        return root;
    }

    private void setupCategoryRv(@NonNull final ExpandableListView expandableListView) {
        // load from database
        categoryViewModel.getCategoryForHomeResult()
                .observe(mActivity, new Observer<List<CategoryEntity>>() {
                    @Override
                    public void onChanged(final List<CategoryEntity> pCategoryEntities) {
                        if (pCategoryEntities == null) {
                            return;
                        }
                        listLoaded.observeForever(new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if (aBoolean == null) {
                                    return;
                                }
                                categoryAdapter =
                                        new CategoryAdapter(mActivity, pCategoryEntities, listMap);
                                expandableListView.setAdapter(categoryAdapter);
                            }
                        });

                        int count = 1;
                        for (final CategoryEntity categoryEntity : pCategoryEntities) {
                            final int finalCount = count;
                            categoryViewModel.getCategoryByParentResult(categoryEntity.getCategory_id())
                                    .observe(mActivity, new Observer<List<CategoryEntity>>() {
                                        @Override
                                        public void onChanged(List<CategoryEntity> categoryEntities) {
                                            if (categoryEntities == null) {
                                                return;
                                            }
                                            listMap.put(categoryEntity.getCategory_id(), categoryEntities);

                                            if (finalCount == pCategoryEntities.size()) {
                                                listLoaded.postValue(true);
                                            }
                                        }
                                    });
                            count++;
                        }
                    }
                });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String group =categoryAdapter.getGroup(groupPosition).getCategory_name();
                CategoryEntity categoryEntity = categoryAdapter.getChild(groupPosition,childPosition);
                String child = categoryEntity.getCategory_name();
                Integer childCategoryId = categoryEntity.getCategory_id();

                Intent intent = new Intent(mActivity, ProductActivity.class);
                intent.putExtra("GROUP", group);
                intent.putExtra("CHILD", child);
                intent.putExtra("CHILD_CATEGORY_ID", childCategoryId);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
