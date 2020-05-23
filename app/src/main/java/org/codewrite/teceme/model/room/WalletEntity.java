package org.codewrite.teceme.model.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.codewrite.teceme.model.rest.Result;

@Entity(tableName = "wallet_table")
public class WalletEntity extends Result {
    @NonNull
    @PrimaryKey
    private String wallet_owner;
    private Long wallet_balance;
    private Integer wallet_pin_code;
    private String wallet_secret_question;
    private String wallet_secret_answer;
    private Boolean wallet_access;
    private String wallet_date_created;

    @NonNull
    public String getWallet_owner() {
        return wallet_owner;
    }

    public void setWallet_owner(@NonNull String wallet_owner) {
        this.wallet_owner = wallet_owner;
    }

    public Long getWallet_balance() {
        return wallet_balance;
    }

    public void setWallet_balance(Long wallet_balance) {
        this.wallet_balance = wallet_balance;
    }

    public Integer getWallet_pin_code() {
        return wallet_pin_code;
    }

    public void setWallet_pin_code(Integer wallet_pin_code) {
        this.wallet_pin_code = wallet_pin_code;
    }

    public String getWallet_secret_question() {
        return wallet_secret_question;
    }

    public void setWallet_secret_question(String wallet_secret_question) {
        this.wallet_secret_question = wallet_secret_question;
    }

    public String getWallet_secret_answer() {
        return wallet_secret_answer;
    }

    public void setWallet_secret_answer(String wallet_secret_answer) {
        this.wallet_secret_answer = wallet_secret_answer;
    }

    public Boolean getWallet_access() {
        return wallet_access;
    }

    public void setWallet_access(Boolean wallet_access) {
        this.wallet_access = wallet_access;
    }

    public String getWallet_date_created() {
        return wallet_date_created;
    }

    public void setWallet_date_created(String wallet_date_created) {
        this.wallet_date_created = wallet_date_created;
    }
}
