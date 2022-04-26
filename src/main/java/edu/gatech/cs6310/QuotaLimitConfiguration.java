package edu.gatech.cs6310;

public class QuotaLimitConfiguration extends Configuration {
    public int limit;

    public QuotaLimitConfiguration(String name, String storeName, int limit) {
        super(name, ConfigurationType.QUOTA_LIMIT, storeName);
        this.limit = limit;
    }

    public QuotaLimitConfiguration(String name, String storeName, int limit, boolean active) {
        super(name, ConfigurationType.QUOTA_LIMIT, storeName);
        this.limit = limit;
        this.active = active;
    }

    @Override
    public void print() {
        StringBuilder info = new StringBuilder();
        info.append("quota_config," + "name:" + this.name + ",quota:" + this.limit + ",active:" + this.active);
        if (storeName != null) {
            info.append(",store_name:" + this.storeName);
        }
        System.out.println(info.toString());
    }
}
