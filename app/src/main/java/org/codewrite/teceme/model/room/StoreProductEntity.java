package org.codewrite.teceme.model.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.codewrite.teceme.model.rest.Result;


@Entity(tableName = "store_product_table")
public class StoreProductEntity extends Result {

    @NonNull
    @PrimaryKey
    private String store_product_id;
    private Integer store_product_product_id;
    private String store_product_store_id;
    private Boolean store_product_access;
    private String store_product_date_created;
    private Integer product_id;
    private String product_name;
    private String product_price;
    private String product_color;
    private String product_weight;
    private String product_size;
    private String product_code;
    private String product_desc;
    private String product_discount;
    private Integer product_category_id;
    private int product_ordered;
    private String product_date_created;
    private Boolean product_access;
    private String product_img_uri;

    @NonNull
    public String getStore_product_id() {
        return store_product_id;
    }

    public void setStore_product_id(@NonNull String store_product_id) {
        this.store_product_id = store_product_id;
    }

    public Integer getStore_product_product_id() {
        return store_product_product_id;
    }

    public void setStore_product_product_id(Integer store_product_product_id) {
        this.store_product_product_id = store_product_product_id;
    }

    public String getStore_product_store_id() {
        return store_product_store_id;
    }

    public void setStore_product_store_id(String store_product_store_id) {
        this.store_product_store_id = store_product_store_id;
    }

    public Boolean getStore_product_access() {
        return store_product_access;
    }

    public void setStore_product_access(Boolean store_product_access) {
        this.store_product_access = store_product_access;
    }

    public String getStore_product_date_created() {
        return store_product_date_created;
    }

    public void setStore_product_date_created(String store_product_date_created) {
        this.store_product_date_created = store_product_date_created;
    }

    @NonNull
    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(@NonNull Integer product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_color() {
        return product_color==null?"":product_color;
    }

    public void setProduct_color(String product_color) {
        this.product_color = product_color;
    }

    public String getProduct_weight() {
        return product_weight==null?"":product_weight;
    }

    public void setProduct_weight(String product_weight) {
        this.product_weight = product_weight;
    }

    public String getProduct_size() {
        return product_size==null?"":product_size;
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
        return product_desc==null?"":product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    @NonNull
    public Integer getProduct_category_id() {
        return product_category_id;
    }

    public void setProduct_category_id(@NonNull Integer product_category_id) {
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
        return product_img_uri==null?"":product_img_uri;
    }

    public void setProduct_img_uri(String product_img_uri) {
        this.product_img_uri = product_img_uri;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_discount() {
        return product_discount==null?"":product_discount;
    }

    public void setProduct_discount(String product_discount) {
        this.product_discount = product_discount;
    }

    public int getProduct_ordered() {
        return product_ordered;
    }

    public void setProduct_ordered(int product_ordered) {
        this.product_ordered = product_ordered;
    }
}
