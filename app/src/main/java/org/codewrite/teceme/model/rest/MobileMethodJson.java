package org.codewrite.teceme.model.rest;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mobile_method_table")
public class MobileMethodJson {

    @NonNull
    @PrimaryKey
   private String account_number;
   private String account_issuer;
   private String voucher_code;

    public MobileMethodJson(@NonNull String account_number, String account_issuer, String voucher_code) {
        this.account_number = account_number;
        this.account_issuer = account_issuer;
        this.voucher_code = voucher_code;
    }

    @NonNull
    public String getAccount_number() {
        return account_number;
    }

    public String getAccount_issuer() {
        return account_issuer;
    }

    public String getVoucher_code() {
        return voucher_code;
    }
}
