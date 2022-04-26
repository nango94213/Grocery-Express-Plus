package edu.gatech.cs6310;

public abstract class User {
    String firstName;
    String lastName;
    String phoneNumber;

    User(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
}