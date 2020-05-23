package org.codewrite.teceme.model.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.codewrite.teceme.model.rest.Result;

@Entity(tableName = "wallet_log_table")
public class WalletLogEntity  extends Result {
    @NonNull
    @PrimaryKey
    private Integer wallet_log_id;
    private Integer wallet_log_owner;
    private String wallet_log_transaction_type;
    private Long wallet_log_amount;
    private String wallet_log_transaction_to;
    private Boolean wallet_log_access;
    private String wallet_log_date_created;

    @NonNull
    public Integer getWallet_log_id() {
        return wallet_log_id;
    }

    public void setWallet_log_id(@NonNull Integer wallet_log_id) {
        this.wallet_log_id = wallet_log_id;
    }

    public Integer getWallet_log_owner() {
        return wallet_log_owner;
    }

    public void setWallet_log_owner(Integer wallet_log_owner) {
        this.wallet_log_owner = wallet_log_owner;
    }

    public String getWallet_log_transaction_type() {
        return wallet_log_transaction_type;
    }

    public void setWallet_log_transaction_type(String wallet_log_transaction_type) {
        this.wallet_log_transaction_type = wallet_log_transaction_type;
    }

    public Long getWallet_log_amount() {
        return wallet_log_amount;
    }

    public void setWallet_log_amount(Long wallet_log_amount) {
        this.wallet_log_amount = wallet_log_amount;
    }

    public String getWallet_log_transaction_to() {
        return wallet_log_transaction_to;
    }

    public void setWallet_log_transaction_to(String wallet_log_transaction_to) {
        this.wallet_log_transaction_to = wallet_log_transaction_to;
    }

    public Boolean getWallet_log_access() {
        return wallet_log_access;
    }

    public void setWallet_log_access(Boolean wallet_log_access) {
        this.wallet_log_access = wallet_log_access;
    }

    public String getWallet_log_date_created() {
        return wallet_log_date_created;
    }

    public void setWallet_log_date_created(String wallet_log_date_created) {
        this.wallet_log_date_created = wallet_log_date_created;
    }
}
