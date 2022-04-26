package edu.gatech.cs6310;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Item implements PropertyChangeListener {
    int weight;
    String name;
    String storeName;
    ItemStatus status;
    String category;
    // Key: Config name, Value: ExpressConfiguration
    private final TreeMap<String, Configuration> configurationTreeMap = new TreeMap<String, Configuration>();

    public int getWeight() { return this.weight; }
    public String getName() { return this.name; }
    public String getStoreName() { return this.storeName; }
    public String getCategory() { return this.category; }
    public List<Configuration> getConfigurations() {
        return this.configurationTreeMap.values().stream().toList();
    }

    public Item(String name, String storeName, int weight, String category, ArrayList<Configuration> configurations) {
        this.name = name;
        this.storeName = storeName;
        this.weight = weight;
        this.category = category;
        this.status = ItemStatus.READY_FOR_SALE;

        for (Configuration config : configurations) {
            if (config instanceof CategoryRestrictionConfiguration) {
                CategoryRestrictionConfiguration crConfig = (CategoryRestrictionConfiguration)config;
                if (crConfig.category.equals(this.category)) {
                    this.configurationTreeMap.put(config.name, config);
                    this.applyConfiguration(crConfig);
                }
            } else {
                this.configurationTreeMap.put(config.name, config);
            }
        }
    }

    //================================================================================
    // Listener
    //================================================================================

    public void propertyChange(PropertyChangeEvent evt) {
        ConfigurationNotification notif = (ConfigurationNotification) evt.getNewValue();
        if (notif != null) {
            String configName = notif.config.name;
            if (this.configurationTreeMap.containsKey(configName)) {
                if (notif.shouldBeRemoved && notif.shouldRemoveConfiguration(this.storeName, this.name)) {
                    this.removeConfiguration(notif.config);
                } else {
                    if (notif.shouldUpdateConfiguration(this.storeName, this.name)) {
                        // Update config with the latest version
                        this.applyConfiguration(notif.config);
                    }
                }
            } else {
                if (notif.shouldAddConfiguration(this.storeName, this.name)) {
                    // Update system configuration cache. Item should update itself through listener
                    this.applyConfiguration(notif.config);
                }
            }
        }
    }

    //================================================================================
    // Util
    //================================================================================

    boolean hasConfiguration(String configName) { return this.configurationTreeMap.containsKey(configName); }

    Boolean isForSale() {
        return this.status == ItemStatus.READY_FOR_SALE;
    }

    ArrayList<Configuration> getQuotaLimitConfigurations() {
        ArrayList<Configuration> configs = new ArrayList<Configuration>();
        for (Map.Entry<String, Configuration> entry : this.configurationTreeMap.entrySet()) {
            Configuration config = entry.getValue();
            if (config.type == ConfigurationType.QUOTA_LIMIT && config.getClass() == QuotaLimitConfiguration.class) {
                QuotaLimitConfiguration crConfig = (QuotaLimitConfiguration)config;
                configs.add(crConfig);
            }
        }
        return configs;
    }

    Boolean confirmQuantityRestriction(int quantity) {
        ArrayList<Configuration> configs = this.getQuotaLimitConfigurations();
        // If there is more than one quota limit restriction, take the min quota
        int quota = Integer.MAX_VALUE;
        for (Configuration config : configs) {
            if (config.type == ConfigurationType.QUOTA_LIMIT && config.getClass() == QuotaLimitConfiguration.class) {
                QuotaLimitConfiguration qlConfig = (QuotaLimitConfiguration) config;
                if (qlConfig.active == true) {
                    quota = Math.min(quota, qlConfig.limit);
                }
            }
        }
        return quantity <= quota;
    }

    void printConfigurations(){
        List<Configuration> configs = this.configurationTreeMap.values().stream().toList();
        if (configs.size() == 0) {
            System.out.println("INFO:there_is_no_configurations_for_item: " + this.name);
        } else {
            for (Configuration config : configs) {
                config.print();
            }
        }
    }


    private void applyConfiguration(Configuration config) {
        configurationTreeMap.put(config.name, config);

        if (config.type == ConfigurationType.CATEGORY_RESTRICTION && config.getClass() == CategoryRestrictionConfiguration.class) {
            CategoryRestrictionConfiguration crConfig = (CategoryRestrictionConfiguration)config;
            if (crConfig.active == false) {
                this.status = ItemStatus.READY_FOR_SALE;
                return;
            }

            if (crConfig.category.equals(this.category) && crConfig.active) {
                this.status = ItemStatus.NOT_FOR_SALE;
            }
        }
    }

    private void removeConfiguration(Configuration config) {
        if (config.type == ConfigurationType.CATEGORY_RESTRICTION && config.getClass() == CategoryRestrictionConfiguration.class) {
            CategoryRestrictionConfiguration crConfig = (CategoryRestrictionConfiguration)config;
            if (crConfig.active == false) {
                this.status = ItemStatus.READY_FOR_SALE;
            }
        }
        this.configurationTreeMap.remove(config.name);
    }
}