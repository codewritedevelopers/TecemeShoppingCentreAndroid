package org.codewrite.teceme.model.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.codewrite.teceme.model.rest.Result;

@Entity(tableName = "customer_order_table")
public class CustomerOrderEntity extends Result {

    @PrimaryKey
    private Integer customer_order_id;
    private Integer customer_order_quantity;
    private String customer_order_product_color;
    private String customer_order_product_size;
    private String customer_order_product_weight;
    private String customer_order_code;
    private String customer_product_code;
    private String customer_order_customer_id;
    private int customer_order_status;
    private int customer_order_redeemed;
    private String customer_order_date_created;
    private String customer_order_product_img_uri;
    private String customer_order_product_name;
    private String customer_order_product_price;
    private String customer_order_product_desc;
    private Integer customer_order_product_category_id;


    public Integer getCustomer_order_id() {
        return customer_order_id;
    }

    public void setCustomer_order_id(Integer customer_order_id) {
        this.customer_order_id = customer_order_id;
    }

    public Integer getCustomer_order_quantity() {
        return customer_order_quantity;
    }

    public void setCustomer_order_quantity(Integer customer_order_quantity) {
        this.customer_order_quantity = customer_order_quantity;
    }

    public String getCustomer_order_product_color() {
        return customer_order_product_color;
    }

    public void setCustomer_order_product_color(String customer_order_product_color) {
        this.customer_order_product_color = customer_order_product_color;
    }

    public String getCustomer_order_product_size() {
        return customer_order_product_size;
    }

    public void setCustomer_order_product_size(String customer_order_product_size) {
        this.customer_order_product_size = customer_order_product_size;
    }

    public String getCustomer_order_product_weight() {
        return customer_order_product_weight;
    }

    public void setCustomer_order_product_weight(String customer_order_product_weight) {
        this.customer_order_product_weight = customer_order_product_weight;
    }

    public String getCustomer_order_code() {
        return customer_order_code;
    }

    public void setCustomer_order_code(String customer_order_code) {
        this.customer_order_code = customer_order_code;
    }

    public String getCustomer_product_code() {
        return customer_product_code;
    }

    public void setCustomer_product_code(String customer_product_code) {
        this.customer_product_code = customer_product_code;
    }

    public String getCustomer_order_customer_id() {
        return customer_order_customer_id;
    }

    public void setCustomer_order_customer_id(String customer_order_customer_id) {
        this.customer_order_customer_id = customer_order_customer_id;
    }

    public int getCustomer_order_status() {
        return customer_order_status;
    }

    public void setCustomer_order_status(int customer_order_status) {
        this.customer_order_status = customer_order_status;
    }

    public String getCustomer_order_date_created() {
        return customer_order_date_created;
    }

    public void setCustomer_order_date_created(String customer_order_date_created) {
        this.customer_order_date_created = customer_order_date_created;
    }

    public String getCustomer_order_product_img_uri() {
        return customer_order_product_img_uri;
    }

    public void setCustomer_order_product_img_uri(String customer_order_product_img_uri) {
        this.customer_order_product_img_uri = customer_order_product_img_uri;
    }

    public String getCustomer_order_product_name() {
        return customer_order_product_name;
    }

    public void setCustomer_order_product_name(String customer_order_product_name) {
        this.customer_order_product_name = customer_order_product_name;
    }

    public String getCustomer_order_product_price() {
        return customer_order_product_price;
    }

    public void setCustomer_order_product_price(String customer_order_product_price) {
        this.customer_order_product_price = customer_order_product_price;
    }

    public String getCustomer_order_product_desc() {
        return customer_order_product_desc;
    }

    public void setCustomer_order_product_desc(String customer_order_product_desc) {
        this.customer_order_product_desc = customer_order_product_desc;
    }

    public Integer getCustomer_order_product_category_id() {
        return customer_order_product_category_id;
    }

    public void setCustomer_order_product_category_id(Integer customer_order_product_category_id) {
        this.customer_order_product_category_id = customer_order_product_category_id;
    }

    public int getCustomer_order_redeemed() {
        return customer_order_redeemed;
    }

    public void setCustomer_order_redeemed(int customer_order_redeemed) {
        this.customer_order_redeemed = customer_order_redeemed;
    }
}
