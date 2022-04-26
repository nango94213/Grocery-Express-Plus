package edu.gatech.cs6310;

public class DronePilot extends Employee {

    String licenseId;
    String accountId;
    int experience;

    public DronePilot(String accountId, String firstName, String lastName, String phoneNumber, String taxIdentifier, String licenseId, int experience) {
        super(firstName, lastName, phoneNumber, taxIdentifier);
        this.accountId = accountId;
        this.licenseId = licenseId;
        this.experience = experience;
    }
}