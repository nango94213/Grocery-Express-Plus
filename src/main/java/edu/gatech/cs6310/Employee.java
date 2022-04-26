package edu.gatech.cs6310;

class Employee extends User {

    String taxIdentifier;
    int monthsOfEmployment;
    int salary;

    Employee(String firstName, String lastName, String phoneNumber, String taxIdentifier) {
        super(firstName, lastName, phoneNumber);
        this.taxIdentifier = taxIdentifier;
    }
}