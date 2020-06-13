package org.codewrite.teceme.model.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.codewrite.teceme.model.rest.Result;

@Entity(tableName = "cart_table")
public class CartEntity extends Result {

    private String cart_owner;
    @NonNull
    @PrimaryKey
    private Integer cart_product_id;
    private Integer cart_quantity;
    private Integer cart_access;
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
    private String product_img_uri;
    private String cart_date_created;

    @NonNull
    public Integer getCart_product_id() {
        return cart_product_id;
    }

    public void setCart_product_id(@NonNull Integer cart_product_id) {
        this.cart_product_id = cart_product_id;
    }

    public Integer getCart_quantity() {
        return cart_quantity;
    }

    public void setCart_quantity(Integer cart_quantity) {
        this.cart_quantity = cart_quantity;
    }

    public Integer getCart_access() {
        return cart_access;
    }

    public void setCart_access(Integer cart_access) {
        this.cart_access = cart_access;
    }

    public String getCart_date_created() {
        return cart_date_created==null?"":cart_date_created;
    }

    public void setCart_date_created(String cart_date_created) {
        this.cart_date_created = cart_date_created;
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

    @NonNull
    public String getCart_owner() {
        return cart_owner;
    }

    public void setCart_owner(@NonNull String cart_owner) {
        this.cart_owner = cart_owner;
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

    public String getProduct_code() {
        return product_code==null?"":product_code;
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

    public String getProduct_discount() {
        return product_discount==null?"":product_discount;
    }

    public void setProduct_discount(String product_discount) {
        this.product_discount = product_discount;
    }

    public Integer getProduct_category_id() {
        return product_category_id;
    }

    public void setProduct_category_id(Integer product_category_id) {
        this.product_category_id = product_category_id;
    }

    public int getProduct_ordered() {
        return product_ordered;
    }

    public void setProduct_ordered(int product_ordered) {
        this.product_ordered = product_ordered;
    }

    public String getProduct_img_uri() {
        return product_img_uri==null?"":product_img_uri;
    }

    public void setProduct_img_uri(String product_img_uri) {
        this.product_img_uri = product_img_uri;
    }
}
