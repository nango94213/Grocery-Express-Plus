package edu.gatech.cs6310.models;

public class Customer {

    String authUserId;
    String accountId;
    int customerId;
    int rating;
    int credit;

    public Customer() {
    }

    public Customer(String accountId, String authUserId, int rating, int credit) {
        this.accountId = accountId;
        this.authUserId = authUserId;
        this.rating = rating;
        this.credit = credit;
    }

    public String getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(String authUserId) {
        this.authUserId = authUserId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
