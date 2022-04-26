package edu.gatech.cs6310;

import edu.gatech.cs6310.auth.Authentication;
import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.configuration.repository.ConfigurationRepository;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// ExpressConfiguration service to include all business logic
// Configurations created by Admin can only be managed by Admin
// Configurations are created active
// Admin can only create category restriction, and it will be automatically applied to all stores and all items
// Store owner can create both configuration types.
// All configurations changes will be applied automatically

public class ConfigurationService {

    private final ConfigurationRepository configRepository;
    private final PropertyChangeSupport support;
    private final TreeMap<String, Configuration> systemConfigurationTreeMap = new TreeMap<String, Configuration>();
    private final StoreService storeService;
    private final UserService userService;

    ConfigurationService(PostgresClientImpl ps, Logger logger, StoreService storeService, UserService userService) {
        this.configRepository = new ConfigurationRepository(ps, logger);
        this.support = new PropertyChangeSupport(this);
        this.storeService = storeService;
        this.userService = userService;

        this.loadSytemConfigurations();
        this.addListenerToStores();
    }

    //================================================================================
    // Listener
    //================================================================================

    public void addConfigurationChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removeConfigurationChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void notifyForConfigChange(ConfigurationNotification notif) {
        support.firePropertyChange("config_update", null, notif);
    }

    //================================================================================
    // Load / Get configurations
    //================================================================================

    void loadSytemConfigurations() {
        List<Configuration> configs = this.configRepository.getSystemConfigurations();
        for (Configuration config : configs) {
            this.systemConfigurationTreeMap.put(config.name, config);
        }
        return;
    }

    void addListenerToStores() {
        for (Map.Entry<String, Store> entry : this.storeService.getAllStores().entrySet()) {
            Store store = entry.getValue();
            this.addConfigurationChangeListener(store);
            List<Item> items = store.getItems();
            for (Item item : items) {
                this.addConfigurationChangeListener(item);
            }
        }
    }

    ArrayList<Configuration> getSystemConfigurationsByType(ConfigurationType type) {
        ArrayList<Configuration> configs = new ArrayList<Configuration>();
        for (Map.Entry<String, Configuration> entry : this.systemConfigurationTreeMap.entrySet()) {
            Configuration config = entry.getValue();
            if (config.type == type) {
                configs.add(config);
            }
        }
        return configs;
    }

    void printAllConfigurations() {
        List<Configuration> configs = this.configRepository.getAllConfigurations();
        if (configs.size() == 0) {
            System.out.println("INFO:no_configurations_found");
        } else {
            for (Configuration config : configs) {
                config.print();
            }
        }
        System.out.println("OK:display_completed");
    }

    void printConfigurationTypes() {
        System.out.println(java.util.Arrays.asList(ConfigurationType.values()));
    }

    void printSystemConfigurations() {
        if (this.systemConfigurationTreeMap.values().size() == 0) {
            System.out.println("INFO:no_system_configurations_found");
            return;
        }

        for (Map.Entry<String, Configuration> entry : this.systemConfigurationTreeMap.entrySet()) {
            Configuration config = entry.getValue();
            config.print();
        }
        System.out.println("OK:display_completed");
    }

    void printStoreConfigurations(String storeName) {
        Store store = this.storeService.getStoreByName(storeName);
        if (store == null) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            return;
        }

        if (!this.userService.isAdmin() && !this.userService.isCurrentUser(store.storeOwnerUsername)) {
            System.out.println("ERROR:only_store_owner_or_admin_can_see_store_configurations");
            return;
        }

        store.printStoreConfigurations();
        System.out.println("OK:display_completed");
    }

    void printStoreItemConfigurations(String storeName, String itemName) {
        Store store = this.storeService.getStoreByName(storeName);
        if (store == null) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            return;
        }

        if (!this.userService.isAdmin() && !this.userService.isCurrentUser(store.storeOwnerUsername)) {
            System.out.println("ERROR:only_store_owner_or_admin_can_see_store_configurations");
            return;
        }

        if (!store.isItemExist(itemName)) {
            System.out.println("ERROR:item_identifier_does_not_exist");
            return;
        }

        store.printStoreItemConfigurations(itemName);
        System.out.println("OK:display_completed");
    }

    //================================================================================
    // Create / Update / Delete / Add / Remove Configurations
    //================================================================================

    void createConfiguration(String configName, ConfigurationType type, String storeName, int limit, String category) {
        Configuration config = this.configRepository.getConfigurationByName(configName);
        if (config != null) {
            System.out.println("ERROR:configuration_identifier_already_exist");
            return;
        }

        Boolean isAdmin = this.userService.isAdmin();
        if(isAdmin && storeName == null && type == ConfigurationType.QUOTA_LIMIT) {
            System.out.println("ERROR:admin_can_only_create_category_restriction_configuration");
            return;
        }

        if (!isAdmin && storeName == null) {
            System.out.println("ERROR:store_name_must_be_provided_for_creating_configuration");
            return;
        }

        if (storeName != null) {
            Store store = this.storeService.getStoreByName(storeName);
            if (store == null) {
                System.out.println("ERROR:store_identifier_does_not_exist");
                return;
            }

            if (!isAdmin && !this.userService.isCurrentUser(store.storeOwnerUsername)) {
                System.out.println("ERROR:this_configuration_can_only_be_updated_by_admin_or_store_owner");
                return;
            }
        }

        switch (type) {
            case CATEGORY_RESTRICTION:
                if (category == null) {
                    System.out.println("ERROR:category_identifier_is_required_for_category_restriction");
                    return;
                }
                config = new CategoryRestrictionConfiguration(configName, storeName, category);
                break;
            case QUOTA_LIMIT:
                if (limit == 0) {
                    System.out.println("ERROR:limit_must_be_larger_than_zero_for_quota_limit");
                    return;
                }
                config = new QuotaLimitConfiguration(configName, storeName, limit);
                break;
            default:
                System.out.println("ERROR:configuration_Type_not_supported");
                return;
        }
        if (storeName == null) {
            this.systemConfigurationTreeMap.put(configName, config);
        }
        ConfigurationNotification notification = new ConfigurationNotification(config, config.storeName, null);
        this.notifyForConfigChange(notification);
        this.configRepository.createConfiguration(config);
        System.out.println("OK:change_completed");
        return;
    }

    void activateConfiguration(String configurationName) {
        Configuration config = this.configRepository.getConfigurationByName(configurationName);
        if (config == null) {
            System.out.println("ERROR:configuration_identifier_does_not_exist");
            return;
        }
        config.active = true;
        this.updateConfiguration(config);
        System.out.println("OK:change_completed");
    }

    void deactivateConfiguration(String configurationName) {
        Configuration config = this.configRepository.getConfigurationByName(configurationName);
        if (config == null) {
            System.out.println("ERROR:configuration_identifier_does_not_exist");
            return;
        }
        config.active = false;
        this.updateConfiguration(config);
        System.out.println("OK:change_completed");
    }

    // This is to activate or deactivate a configuration
    private void updateConfiguration(Configuration config) {
        Boolean hasPermission = this.hasStorePermission(config, "updated");
        if (!hasPermission) {
            return;
        }

        if (config.storeName == null) {
            this.systemConfigurationTreeMap.put(config.name, config);
        }
        ConfigurationNotification notification = new ConfigurationNotification(config, config.storeName, null);
        this.notifyForConfigChange(notification);
        this.configRepository.updateConfiguration(config);
        return;
    }

    void deleteConfiguration(String configurationName) {
        Configuration config = this.configRepository.getConfigurationByName(configurationName);
        if (config == null) {
            System.out.println("ERROR:configuration_identifier_does_not_exist");
            return;
        }

        Boolean hasPermission = this.hasStorePermission(config, "deleted");
        if (!hasPermission) {
            return;
        }

        ConfigurationNotification notification = new ConfigurationNotification(config, config.storeName, null);
        notification.shouldBeRemoved = true;
        this.notifyForConfigChange(notification);
        if (config.storeName == null) {
            this.systemConfigurationTreeMap.remove(configurationName);
        }
        this.configRepository.deleteConfiguration(config);
        this.configRepository.deleteStoreConfigurations(config);
        this.configRepository.deleteItemConfigurations(config);
        System.out.println("OK:change_completed");
        return;
    }

    //================================================================================
    // Add configuration to store/item
    //================================================================================

    void addConfiguration(String configName, String storeName) {
        Configuration config = this.configRepository.getConfigurationByName(configName);
        if (config == null) {
            System.out.println("ERROR:configuration_identifier_does_not_exist");
            return;
        }

        Boolean hasPermission = this.hasStorePermission(config, "added");
        if (!hasPermission) {
            return;
        }

        Store store = this.storeService.getStoreByName(storeName);
        Boolean exist = store.hasConfiguration(configName);
        if (exist) {
            System.out.println("ERROR:configuration_already_exist_in_store");
            return;
        }
        ConfigurationNotification notification = new ConfigurationNotification(config, storeName, null);
        this.notifyForConfigChange(notification);
        List<Configuration> configs = new ArrayList<Configuration>();
        configs.add(config);
        this.configRepository.createStoreConfigurations(storeName, configs);
        List<Item> items = store.getItems();
        for (Item item : items) {
            this.configRepository.createItemConfigurations(storeName, item.name, configs);
        }
        System.out.println("OK:change_completed");
    }

    void addConfiguration(String configName, String storeName, String itemName) {
        Configuration config = this.configRepository.getConfigurationByName(configName);
        if (config == null) {
            System.out.println("ERROR:configuration_identifier_does_not_exist");
            return;
        }

        Boolean hasPermission = this.hasStorePermission(config, "added");
        if (!hasPermission) {
            return;
        }

        if (config.storeName != null && !config.storeName.equals(storeName)) {
            System.out.println("ERROR:configuration_does_not_belong_to_system_or_to_this_store");
            return;
        }

        Store store = this.storeService.getStoreByName(storeName);
        Boolean exist = store.hasConfiguration(configName);
        if (!exist) {
            System.out.println("ERROR:configuration_does_not_exist_in_store");
            return;
        }

        if (!store.isItemExist(itemName)) {
            System.out.println("ERROR:item_does_not_exist_in_store");
            return;
        }

        ConfigurationNotification notification = new ConfigurationNotification(config, storeName, itemName);
        this.notifyForConfigChange(notification);
        List<Configuration> configs = new ArrayList<Configuration>();
        configs.add(config);
        // if admin configuraiton - add to store and item
        if (config.storeName == null) {
            this.configRepository.createStoreConfigurations(storeName, configs);
            this.configRepository.createItemConfigurations(storeName, itemName, configs);
        } else {
            // if only store config - store should still have it and then add to item
            this.configRepository.createItemConfigurations(storeName, itemName, configs);
        }
        System.out.println("OK:change_completed");
    }

    //================================================================================
    // Remove configuration from store/item
    //================================================================================

    void removeConfiguration(String configName, String storeName, String itemName) {
        Configuration config = this.configRepository.getConfigurationByName(configName);
        if (config == null) {
            System.out.println("ERROR:configuration_identifier_does_not_exist");
            return;
        }

        if (!this.storeService.isStoreExist(storeName)) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            return;
        }

        Store store = this.storeService.getStoreByName(storeName);
        if (!store.isItemExist(itemName)) {
            System.out.println("ERROR:item_identifier_does_not_exists_in_store");
            return;
        }

        Boolean hasPermission = this.hasStorePermission(config, "removed");
        if (!hasPermission) {
            return;
        }

        Item item = store.getItem(itemName);
        if (!item.hasConfiguration(configName)) {
            System.out.println("ERROR:configuration_does_not_exist_in_item");
            return;
        }

        ConfigurationNotification notification = new ConfigurationNotification(config, storeName, itemName);
        notification.shouldBeRemoved = true;
        this.notifyForConfigChange(notification);
        this.configRepository.deleteItemConfiguration(storeName, itemName, config);
        System.out.println("OK:change_completed");
        return;
    }

    void removeConfiguration(String configName, String storeName) {
        Configuration config = this.configRepository.getConfigurationByName(configName);
        if (config == null) {
            System.out.println("ERROR:configuration_identifier_does_not_exist");
            return;
        }

        if (!this.storeService.isStoreExist(storeName)) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            return;
        }

        Boolean hasPermission = this.hasStorePermission(config, "removed");
        if (!hasPermission) {
            return;
        }

        if (config.storeName != null) {
            Store store = this.storeService.getStoreByName(storeName);
            if (!store.hasConfiguration(configName)) {
                System.out.println("ERROR:configuration_does_not_exist_in_store");
                return;
            }
        }

        ConfigurationNotification notification = new ConfigurationNotification(config, storeName, null);
        notification.shouldBeRemoved = true;
        this.notifyForConfigChange(notification);
        if (config.storeName != null) {
            this.configRepository.deleteConfiguration(config);
        }
        this.configRepository.deleteStoreConfiguration(storeName, config);
        this.configRepository.deleteItemConfigurations(storeName, config);
        System.out.println("OK:change_completed");
        return;
    }

    //================================================================================
    // Util
    //================================================================================

    private Boolean hasStorePermission(Configuration config, String action) {
        Boolean isAdmin = this.userService.isAdmin();

        if (config.storeName == null && !isAdmin) {
            System.out.println("ERROR:this_configuration_can_only_be_" + action + "_by_admin");
            return false;
        }

        if (config.storeName != null) {
            Store store = this.storeService.getStoreByName(config.storeName);
            if(!this.userService.isCurrentUser(store.storeOwnerUsername)) {
                System.out.println("ERROR:this_configuration_can_only_be_\" + action + \"_by_admin_or_store_owner");
                return false;
            }
        }
        return true;
    }
}
