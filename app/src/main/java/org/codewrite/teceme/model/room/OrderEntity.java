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
    private String product_name;
    private String product_price;
    private String product_color;
    private String product_weight;
    private String product_size;
    private String product_code;
    private String product_desc;
    private int order_status;
    private Integer product_category_id;
    private String product_img_uri;
    private String cart_date_created;

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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
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

    public String getProduct_img_uri() {
        return product_img_uri;
    }

    public void setProduct_img_uri(String product_img_uri) {
        this.product_img_uri = product_img_uri;
    }

    public String getCart_date_created() {
        return cart_date_created;
    }

    public void setCart_date_created(String cart_date_created) {
        this.cart_date_created = cart_date_created;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }
}
