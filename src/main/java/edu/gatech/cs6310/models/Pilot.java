package edu.gatech.cs6310.models;

public class Pilot {

    int pilotId;
    String accountId;
    String authUserId;
    String licenseId;
    String taxIdentifier;
    int monthsOfEmployment;
    int salary;
    int experience;

    public Pilot() {

    }

    public Pilot(String accountId, String authUserId, String licenseId, String taxIdentifier, int monthsOfEmployment, int salary, int experience) {
        this.accountId = accountId;
        this.authUserId = authUserId;
        this.licenseId = licenseId;
        this.taxIdentifier = taxIdentifier;
        this.monthsOfEmployment = monthsOfEmployment;
        this.salary = salary;
        this.experience = experience;
    }

    public int getPilotId() {
        return pilotId;
    }

    public void setPilotId(int pilotId) {
        this.pilotId = pilotId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(String authUserId) {
        this.authUserId = authUserId;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getTaxIdentifier() {
        return taxIdentifier;
    }

    public void setTaxIdentifier(String taxIdentifier) {
        this.taxIdentifier = taxIdentifier;
    }

    public int getMonthsOfEmployment() {
        return monthsOfEmployment;
    }

    public void setMonthsOfEmployment(int monthsOfEmployment) {
        this.monthsOfEmployment = monthsOfEmployment;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}
