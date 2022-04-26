package edu.gatech.cs6310;

public class CategoryRestrictionConfiguration extends Configuration {
    public String category;

    public CategoryRestrictionConfiguration(String name, String storeName, String category) {
        super(name, ConfigurationType.CATEGORY_RESTRICTION, storeName);
        this.category = category;
    }

    public CategoryRestrictionConfiguration(String name, String storeName, String category, Boolean active) {
        super(name, ConfigurationType.CATEGORY_RESTRICTION, storeName);
        this.category = category;
        this.active = active;
    }

    @Override
    public void print() {
        StringBuilder info = new StringBuilder();
        info.append("category_config," + "name:" + this.name + ",category:" + this.category + ",active:" + this.active);
        if (storeName != null) {
            info.append(",store name:" + this.storeName);
        }
        System.out.println(info.toString());
    }
}
