package org.codewrite.teceme.model.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "access_token_table")
public class AccessTokenEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String token;

    public AccessTokenEntity(String token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
