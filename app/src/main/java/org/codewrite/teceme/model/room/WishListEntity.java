package org.codewrite.teceme.model.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.codewrite.teceme.model.rest.Result;

@Entity(tableName = "wish_list_table")
public class WishListEntity extends Result {
    @NonNull
    @PrimaryKey
    private Integer wishlist_id;
    private Integer wishlist_product_id;
    private String wishlist_customer_id;
    private String wishlist_date_created;
    private Boolean wishlist_access;

    @NonNull
    public Integer getWishlist_id() {
        return wishlist_id;
    }

    public void setWishlist_id(@NonNull Integer wishlist_id) {
        this.wishlist_id = wishlist_id;
    }

    public Integer getWishlist_product_id() {
        return wishlist_product_id;
    }

    public void setWishlist_product_id(Integer wishlist_product_id) {
        this.wishlist_product_id = wishlist_product_id;
    }

    public String getWishlist_customer_id() {
        return wishlist_customer_id;
    }

    public void setWishlist_customer_id(String wishlist_customer_id) {
        this.wishlist_customer_id = wishlist_customer_id;
    }

    public String getWishlist_date_created() {
        return wishlist_date_created;
    }

    public void setWishlist_date_created(String wishlist_date_created) {
        this.wishlist_date_created = wishlist_date_created;
    }

    public Boolean getWishlist_access() {
        return wishlist_access;
    }

    public void setWishlist_access(Boolean wishlist_access) {
        this.wishlist_access = wishlist_access;
    }
}
