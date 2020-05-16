package org.codewrite.teceme.model.holder;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_table")
public class Cart {
    @PrimaryKey
    private Integer cart_id;
    private Integer cart_product_id;
    private Integer cart_quantity;
    private Boolean cart_access;
    private String cart_date_created;

    public Integer getCart_id() {
        return cart_id;
    }

    public void setCart_id(Integer cart_id) {
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
}
