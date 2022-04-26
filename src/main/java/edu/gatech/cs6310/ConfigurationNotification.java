package edu.gatech.cs6310;

public class ConfigurationNotification {
    Configuration config;
    String storeName;
    String itemName;
    Boolean shouldBeRemoved;

    ConfigurationNotification(Configuration config, String storeName, String itemName) {
        this.config = config;
        this.storeName = storeName;
        this.itemName = itemName;
        this.shouldBeRemoved = false;
    }

    public Boolean shouldRemoveConfiguration(String storeName, String itemName) {
        // store != null and item == null -> store listener
        if (storeName != null && itemName == null) {
            // system config, remove for all
            if (this.storeName == null) {
                return true;
            }

            // Configuration is for current store
            if (this.storeName != null && storeName.equals(this.storeName)) {
                return true;
            }
            return false;
        }

        // store != null and item != null -> item listener
        if (storeName != null && itemName != null) {
            // system config, remove for all
            if (this.storeName == null) {
                return true;
            }

            // Configuration is for current store
            if (this.storeName != null && storeName.equals(this.storeName)) {
                // Remove for all items
                if (this.itemName == null) {
                    return true;
                }

                if (this.itemName != null && this.itemName.equals(itemName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean shouldUpdateConfiguration(String storeName, String itemName) {
        return false;
    }



    public Boolean shouldAddConfiguration(String storeName, String itemName) {
        Boolean isCategoryType = this.config.type == ConfigurationType.CATEGORY_RESTRICTION;

        // store != null and item == null -> store listener
        if (storeName != null && itemName == null) {
            if (this.storeName == null && isCategoryType) {
                return true;
            }
            if(this.storeName != null && this.storeName.equals(storeName)) {
                return true;
            }
            return false;
        }

        // store != null and item != null -> item listener
        if (storeName != null && itemName != null) {
            if (this.storeName == null && isCategoryType) {
                return true;
            }

            if (this.storeName != null && this.storeName.equals(storeName)) {
                if (this.itemName == null && isCategoryType) {
                    return true;
                }

                // If name matches, always added - quota limit
                if (this.itemName != null && itemName.equals(this.itemName)) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}
