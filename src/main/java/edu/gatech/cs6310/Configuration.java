package edu.gatech.cs6310;

public class Configuration {
    // configuration name is unqiue
    public String name;
    public ConfigurationType type;
    public Boolean active;
    public String storeName;

    public Configuration(String name, ConfigurationType type, String storeName) {
        this.name = name;
        this.type = type;
        this.storeName = storeName;
        this.active = true;
    }

    public void print() {
        StringBuilder info = new StringBuilder();
        info.append("config," + "name:" + this.name + ",active:" + this.active);
        System.out.println(info.toString());
    }
}
