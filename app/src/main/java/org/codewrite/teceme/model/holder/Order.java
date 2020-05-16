package org.codewrite.teceme.model.holder;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_table")
public class Order {
    @PrimaryKey
    private Integer order_id;
    private String order_owner;
    private Integer order_quantity;
    private Integer order_product_id;
    private Boolean order_access;
    private String order_date_created;


    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
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
}
