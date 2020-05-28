package org.codewrite.teceme.model.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.codewrite.teceme.model.rest.Result;

@Entity(tableName = "category_table")
public class CategoryEntity extends Result {

    @PrimaryKey
    private Integer category_id;
    private String category_name;
    private Integer category_level;
    private Integer category_parent_id;
    private Boolean category_access;
    private String category_date_created;

    @NonNull
    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(@NonNull Integer category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Integer getCategory_level() {
        return category_level;
    }

    public void setCategory_level(Integer category_level) {
        this.category_level = category_level;
    }

    public Integer getCategory_parent_id() {
        return category_parent_id;
    }

    public void setCategory_parent_id(Integer category_parent_id) {
        this.category_parent_id = category_parent_id;
    }

    public Boolean getCategory_access() {
        return category_access;
    }

    public void setCategory_access(Boolean category_access) {
        this.category_access = category_access;
    }

    public String getCategory_date_created() {
        return category_date_created;
    }

    public void setCategory_date_created(String category_date_created) {
        this.category_date_created = category_date_created;
    }

}
