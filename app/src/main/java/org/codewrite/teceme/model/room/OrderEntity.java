package org.codewrite.teceme.model.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.codewrite.teceme.model.rest.Result;

@Entity(tableName = "order_table")
public class OrderEntity extends Result {
    @NonNull
    @PrimaryKey
    private Integer order_id;
    private String order_owner;
    private Integer order_quantity;
    private Integer order_product_id;
    private String product_color;
    private String product_weight;
    private String product_size;
    private Boolean order_access;
    private String order_date_created;


    @NonNull
    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(@NonNull Integer order_id) {
        this.order_id = order_id;
    }

    public String getOrder_owner() {
        return order_owner;
    }

    public void setOrder_owner(String order_owner) {
        this.order_owner = order_owner;
    }

    public Integer getOrder_quantity() {
        return order_quantity;
    }

    public void setOrder_quantity(Integer order_quantity) {
        this.order_quantity = order_quantity;
    }

    public Integer getOrder_product_id() {
        return order_product_id;
    }

    public void setOrder_product_id(Integer order_product_id) {
        this.order_product_id = order_product_id;
    }

    public Boolean getOrder_access() {
        return order_access;
    }

    public void setOrder_access(Boolean order_access) {
        this.order_access = order_access;
    }

    public String getOrder_date_created() {
        return order_date_created;
    }

    public void setOrder_date_created(String order_date_created) {
        this.order_date_created = order_date_created;
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
