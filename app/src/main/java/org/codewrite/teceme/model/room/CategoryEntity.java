package org.codewrite.teceme.model.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.codewrite.teceme.model.holder.Category;

@Entity(tableName = "category_table")
public class CategoryEntity extends Category {
    public CategoryEntity(@NonNull Integer category_id, String category_name, Integer category_level, Integer category_parent_id, Boolean category_access, String category_date_created) {
        super(category_id, category_name, category_level, category_parent_id, category_access, category_date_created);
    }
}
