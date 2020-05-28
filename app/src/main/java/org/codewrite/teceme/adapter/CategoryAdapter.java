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

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CategoryAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<CategoryEntity> expandableListTitle;
    private Map<Integer,List<CategoryEntity>> expandableListDetail;

    public CategoryAdapter(Context context, List<CategoryEntity> expandableListTitle,
                                       Map<Integer,List<CategoryEntity>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public CategoryEntity getChild(int listPosition, int expandedListPosition) {
        return Objects.requireNonNull(
                this.expandableListDetail.get(expandableListTitle.get(listPosition).getCategory_id()))
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
                this.expandableListDetail.get(
                        this.expandableListTitle.get(listPosition).getCategory_id())).size();
    }

    @Override
    public CategoryEntity getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
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
}
