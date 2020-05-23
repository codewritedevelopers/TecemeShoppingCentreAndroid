package org.codewrite.teceme.model.rest;

import org.codewrite.teceme.model.room.CustomerEntity;

public class CustomerJson extends CustomerEntity {

    public CustomerJson(){

    }
    public CustomerJson(String customer_username, String customer_password, String customer_first_name,
                        String customer_middle_name, String customer_last_name, String customer_phone,
                        String customer_referred_by) {
        super(customer_username, customer_password, customer_first_name, customer_middle_name,
                customer_last_name, customer_phone, customer_referred_by);
    }
}
