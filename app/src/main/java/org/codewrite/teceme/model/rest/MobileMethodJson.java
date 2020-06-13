package org.codewrite.teceme.model.rest;

public class MobileMethodJson {

   private String account_number;
   private String account_issuer;
   private String voucher_code;

    public MobileMethodJson(String account_number, String account_issuer) {
        this.account_number = account_number;
        this.account_issuer = account_issuer;
        this.voucher_code = "";
    }

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
