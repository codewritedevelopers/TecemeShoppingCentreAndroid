package org.codewrite.teceme.model.holder;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "product_table")
public class Product {
    @PrimaryKey
    private Integer product_id;
    private String product_name;
    private String product_color;
    private String product_weight;
    private String product_size;
    private String product_code;
    private String product_desc;
    private Integer product_category_id;
    private String product_date_created;
    private Boolean product_access;
    private String product_img_uri;

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_color() {
        return product_color;
    }

    public void setProduct_color(String product_color) {
        this.product_color = product_color;
    }

    public String getProduct_weight() {
        return product_weight;
    }

    public void setProduct_weight(String product_weight) {
        this.product_weight = product_weight;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public Integer getProduct_category_id() {
        return product_category_id;
    }

    public void setProduct_category_id(Integer product_category_id) {
        this.product_category_id = product_category_id;
    }

    public String getProduct_date_created() {
        return product_date_created;
    }

    public void setProduct_date_created(String product_date_created) {
        this.product_date_created = product_date_created;
    }

    public Boolean getProduct_access() {
        return product_access;
    }

    public void setProduct_access(Boolean product_access) {
        this.product_access = product_access;
    }

    public String getProduct_img_uri() {
        return product_img_uri;
    }

    public void setProduct_img_uri(String product_img_uri) {
        this.product_img_uri = product_img_uri;
    }
}
