package org.codewrite.teceme.model.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.codewrite.teceme.model.rest.Result;

@Entity(tableName = "store_table")
public class StoreEntity extends Result {

    @NonNull
    @PrimaryKey
    private String store_id;
    private String store_name;
    private String store_location;
    private String store_longitude;
    private String store_latitude;
    private String store_hours;
    private String store_desc;
    private String store_date_created;
    private Integer store_access;
    private String store_img_uri;
    private Integer store_category_id;
    private String store_email;
    private String store_phone;
    private int store_following;

    @NonNull
    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(@NonNull String store_name) {
        this.store_name = store_name;
    }

    public String getStore_location() {
        return store_location==null?"":store_location;
    }

    public void setStore_location(String store_location) {
        this.store_location = store_location;
    }

    public String getStore_desc() {
        return store_desc==null?"":store_desc;
    }

    public void setStore_desc(String store_desc) {
        this.store_desc = store_desc;
    }

    public String getStore_date_created() {
        return store_date_created;
    }

    public void setStore_date_created(String store_date_created) {
        this.store_date_created = store_date_created;
    }

    public Integer getStore_access() {
        return store_access;
    }

    public void setStore_access(Integer store_access) {
        this.store_access = store_access;
    }

    public String getStore_img_uri() {
        return store_img_uri==null?"":store_img_uri;
    }

    public void setStore_img_uri(String store_img_uri) {
        this.store_img_uri = store_img_uri;
    }

    public Integer getStore_category_id() {
        return store_category_id;
    }

    public void setStore_category_id(Integer store_category_id) {
        this.store_category_id = store_category_id;
    }

    public String getStore_email() {
        return store_email==null?"":store_email;
    }

    public void setStore_email(String store_email) {
        this.store_email = store_email;
    }

    public String getStore_phone() {
        return store_phone==null?"":store_phone;
    }

    public void setStore_phone(String store_phone) {
        this.store_phone = store_phone;
    }

    public int getStore_following() {
        return store_following;
    }

    public void setStore_following(int store_following) {
        this.store_following = store_following;
    }

    @NonNull
    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(@NonNull String store_id) {
        this.store_id = store_id;
    }

    public String getStore_longitude() {
        return store_longitude;
    }

    public void setStore_longitude(String store_longitude) {
        this.store_longitude = store_longitude;
    }

    public String getStore_latitude() {
        return store_latitude;
    }

    public void setStore_latitude(String store_latitude) {
        this.store_latitude = store_latitude;
    }

    public String getStore_hours() {
        return store_hours;
    }

    public void setStore_hours(String store_hours) {
        this.store_hours = store_hours;
    }
}
