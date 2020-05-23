package org.codewrite.teceme.model.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.codewrite.teceme.model.rest.Result;

@Entity(tableName = "customer_table")
public class CustomerEntity extends Result {
    @NonNull
    @PrimaryKey
    private String customer_id;
    private String customer_username;
    private String customer_password;
    private String customer_first_name;
    private String customer_middle_name;
    private String customer_last_name;
    private String customer_phone;
    private String customer_referral_id;
    private String customer_referred_by;
    private Boolean customer_access;
    private String customer_date_created;

    @NonNull
    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(@NonNull String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_username() {
        return customer_username;
    }

    public void setCustomer_username(String customer_username) {
        this.customer_username = customer_username;
    }

    public String getCustomer_password() {
        return customer_password;
    }

    public void setCustomer_password(String customer_password) {
        this.customer_password = customer_password;
    }


    public String getCustomer_middle_name() {
        return customer_middle_name;
    }

    public void setCustomer_middle_name(String customer_middle_name) {
        this.customer_middle_name = customer_middle_name;
    }

    public String getCustomer_last_name() {
        return customer_last_name;
    }

    public void setCustomer_last_name(String customer_last_name) {
        this.customer_last_name = customer_last_name;
    }

    public String getCustomer_referral_id() {
        return customer_referral_id;
    }

    public void setCustomer_referral_id(String customer_referral_id) {
        this.customer_referral_id = customer_referral_id;
    }

    public String getCustomer_referred_by() {
        return customer_referred_by;
    }

    public void setCustomer_referred_by(String customer_referred_by) {
        this.customer_referred_by = customer_referred_by;
    }

    public Boolean getCustomer_access() {
        return customer_access;
    }

    public void setCustomer_access(Boolean customer_access) {
        this.customer_access = customer_access;
    }

    public String getCustomer_date_created() {
        return customer_date_created;
    }

    public void setCustomer_date_created(String customer_date_created) {
        this.customer_date_created = customer_date_created;
    }

    public String getCustomer_first_name() {
        return customer_first_name;
    }

    public void setCustomer_first_name(String customer_first_name) {
        this.customer_first_name = customer_first_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }
}