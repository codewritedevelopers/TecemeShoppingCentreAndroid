package org.codewrite.teceme.model.holder;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "access_token_table")
public class AccessToken {
    @PrimaryKey
    private Integer id;
    private String token;
    private String date_created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
