package edu.gatech.cs6310.models;

public class Instruction {
    private String name;

    public Instruction() {}

    public Instruction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
