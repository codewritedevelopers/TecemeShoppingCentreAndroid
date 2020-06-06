package org.codewrite.teceme.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.codewrite.teceme.R;
import org.codewrite.teceme.model.room.CategoryEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CategoryAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<CategoryEntity> expandableListTitle;
    private List<CategoryEntity> filterListTitle = new ArrayList<>();
    private Map<Integer,List<CategoryEntity>> expandableListDetail;
    private Map<Integer,List<CategoryEntity>> filterListDetail = new HashMap<>();

    public CategoryAdapter(Context context, List<CategoryEntity> expandableListTitle,
                                       Map<Integer,List<CategoryEntity>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        filterListDetail.clear();
        filterListTitle.clear();
        filterListDetail.putAll(expandableListDetail);
        filterListTitle.addAll(expandableListTitle);
    }

    @Override
    public CategoryEntity getChild(int listPosition, int expandedListPosition) {
        return Objects.requireNonNull(
                this.filterListDetail.get(filterListTitle.get(listPosition).getCategory_id()))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText =  getChild(listPosition, expandedListPosition).getCategory_name();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.category_list_content, null);
        }
        TextView expandedListTextView =  convertView.findViewById(R.id.name);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return Objects.requireNonNull(
                this.filterListDetail.get(
                        this.filterListTitle.get(listPosition).getCategory_id())).size();
    }

    @Override
    public CategoryEntity getGroup(int listPosition) {
        return this.filterListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.filterListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle =  getGroup(listPosition).getCategory_name();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.category_list_content, null);
        }
        TextView listTitleTextView =  convertView.findViewById(R.id.name);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public void filterData(String query){

        query = query.toLowerCase();
        filterListTitle.clear();

        if(query.isEmpty()){
            filterListTitle.addAll(expandableListTitle);
        }
        else {

            for(CategoryEntity categoryEntity: expandableListTitle){

                List<CategoryEntity> entities = expandableListDetail.get(categoryEntity.getCategory_id());
                assert entities != null;
                List<CategoryEntity> newList = new ArrayList<>();
                for (CategoryEntity entity : entities) {
                    if(entity.getCategory_name().toLowerCase().contains(query)
                            || categoryEntity.getCategory_name().toLowerCase().contains(query)){
                        newList.add(entity);
                    }
                }
                if(categoryEntity.getCategory_name().toLowerCase().contains(query) || newList.size() > 0){
                    filterListTitle.add(categoryEntity);
                }
                if(newList.size() > 0){
                  filterListDetail.put(categoryEntity.getCategory_id(),newList);
                }
            }
        }
        notifyDataSetChanged();
    }
}
