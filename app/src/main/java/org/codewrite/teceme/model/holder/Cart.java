package org.codewrite.teceme.model.holder;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


public class Cart {
    @NonNull
    @PrimaryKey
    private Integer cart_id;
    private Integer cart_product_id;
    private Integer cart_quantity;
    private Boolean cart_access;
    private String cart_date_created;
    private String product_color;
    private String product_weight;
    private String product_size;

    @NonNull
    public Integer getCart_id() {
        return cart_id;
    }

    public void setCart_id(@NonNull Integer cart_id) {
        this.cart_id = cart_id;
    }

    public Integer getCart_product_id() {
        return cart_product_id;
    }

    public void setCart_product_id(Integer cart_product_id) {
        this.cart_product_id = cart_product_id;
    }

    public Integer getCart_quantity() {
        return cart_quantity;
    }

    public void setCart_quantity(Integer cart_quantity) {
        this.cart_quantity = cart_quantity;
    }

    public Boolean getCart_access() {
        return cart_access;
    }

    public void setCart_access(Boolean cart_access) {
        this.cart_access = cart_access;
    }

    public String getCart_date_created() {
        return cart_date_created;
    }

    public void setCart_date_created(String cart_date_created) {
        this.cart_date_created = cart_date_created;
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
}