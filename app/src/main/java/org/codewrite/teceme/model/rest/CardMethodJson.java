package org.codewrite.teceme.model.rest;

public class CardMethodJson {
    private String pan;
    private String cvv;
    private String exp_month;
    private String exp_year;
    private String issuer;
    private String card_holder;
    private String currency;

    public CardMethodJson(String pan, String cvv, String exp_month, String exp_year, String issuer,
                          String card_holder, String currency) {
        this.pan = pan;
        this.cvv = cvv;
        this.exp_month = exp_month;
        this.exp_year = exp_year;
        this.issuer = issuer;
        this.card_holder = card_holder;
        this.currency = currency;
    }

    public String getPan() {
        return pan;
    }

    public String getCvv() {
        return cvv;
    }

    public String getExp_month() {
        return exp_month;
    }

    public String getExp_year() {
        return exp_year;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getCard_holder() {
        return card_holder;
    }

    public String getCurrency() {
        return currency;
    }
}
