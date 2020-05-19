package org.codewrite.teceme.model.rest;

import androidx.annotation.NonNull;

import org.codewrite.teceme.model.holder.Category;

public class CategoryJson extends Category {
    public CategoryJson(@NonNull Integer category_id, String category_name, Integer category_level, Integer category_parent_id, Boolean category_access, String category_date_created) {
        super(category_id, category_name, category_level, category_parent_id, category_access, category_date_created);
    }
}
