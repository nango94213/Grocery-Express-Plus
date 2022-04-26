package edu.gatech.cs6310.configuration.repository;

import edu.gatech.cs6310.CategoryRestrictionConfiguration;
import edu.gatech.cs6310.Configuration;
import edu.gatech.cs6310.ConfigurationType;
import edu.gatech.cs6310.QuotaLimitConfiguration;
import edu.gatech.cs6310.configuration.models.ExpressConfiguration;
import edu.gatech.cs6310.configuration.models.ItemConfiguration;
import edu.gatech.cs6310.configuration.models.StoreConfiguration;
import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.dataManager.ExpressStoreDataManager;
import edu.gatech.cs6310.dataManager.ExpressStoreItemDataManager;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.configuration.dataManager.*;
import edu.gatech.cs6310.models.*;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationRepository {

    private final PostgresClientImpl ps;
    private final Logger logger;

    private final ExpressConfigurationDataManager configDataManager;
    private final ItemConfigurationDataManager itemConfigDataManager;
    private final StoreConfigurationDataManager storeConfigDataManager;

    private final ExpressStoreDataManager storeDataManager;
    private final ExpressStoreItemDataManager itemDataManager;

    public ConfigurationRepository(PostgresClientImpl ps, Logger logger) {
        this.ps = ps;
        this.logger = logger;

        configDataManager = new ExpressConfigurationDataManager(ps, logger);
        itemConfigDataManager = new ItemConfigurationDataManager(ps, logger);
        storeConfigDataManager = new StoreConfigurationDataManager(ps, logger);

        storeDataManager = new ExpressStoreDataManager(ps, logger);
        itemDataManager = new ExpressStoreItemDataManager(ps, logger);
    }

    public ArrayList<Configuration> getAllConfigurations() {
        ArrayList<Configuration> configs = new ArrayList<Configuration>();
        List<ExpressConfiguration> configDaos = configDataManager.getAll();

        if (configDaos == null) {
            return configs;
        }

        for (ExpressConfiguration cDao : configDaos) {
            Boolean isCategoryType = ConfigurationType.valueOf(cDao.getType()) == ConfigurationType.CATEGORY_RESTRICTION;
            if (isCategoryType) {
                CategoryRestrictionConfiguration crConfig = new CategoryRestrictionConfiguration(cDao.getName(), cDao.getStorename(), cDao.getCategory(), cDao.getActive());
                configs.add(crConfig);
            } else {
                QuotaLimitConfiguration qlConfig = new QuotaLimitConfiguration(cDao.getName(), cDao.getStorename(), cDao.getQuota(), cDao.getActive());
                configs.add(qlConfig);
            }
        }
        return configs;
    }

    public ArrayList<Configuration> getSystemConfigurations() {
        ArrayList<Configuration> configs = new ArrayList<Configuration>();
        List<ExpressConfiguration> configDaos = configDataManager.getSystemConfigs();

        if (configDaos == null) {
            return configs;
        }

        for (ExpressConfiguration cDao : configDaos) {
            Boolean isCategoryType = ConfigurationType.valueOf(cDao.getType()) == ConfigurationType.CATEGORY_RESTRICTION;
            if (isCategoryType) {
                CategoryRestrictionConfiguration crConfig = new CategoryRestrictionConfiguration(cDao.getName(), cDao.getStorename(), cDao.getCategory(), cDao.getActive());
                configs.add(crConfig);
            } else {
                QuotaLimitConfiguration qlConfig = new QuotaLimitConfiguration(cDao.getName(), cDao.getStorename(), cDao.getQuota(), cDao.getActive());
                configs.add(qlConfig);
            }
        }
        return configs;
    }

    public Configuration getConfigurationByName(String configurationName) {
        ExpressConfiguration cDao = configDataManager.getConfigByName(configurationName);
        if (cDao != null) {
            Boolean isCategoryType = ConfigurationType.valueOf(cDao.getType()) == ConfigurationType.CATEGORY_RESTRICTION;
            if (isCategoryType) {
                Configuration config = new CategoryRestrictionConfiguration(cDao.getName(), cDao.getStorename(), cDao.getCategory(), cDao.getActive());
                return config;
            } else {
                Configuration config = new QuotaLimitConfiguration(cDao.getName(), cDao.getStorename(), cDao.getQuota(), cDao.getActive());
                return config;
            }
        }
        return null;
    }

    public ArrayList<Configuration> getConfigurationsByNames(String[] configurationName) {
        ArrayList<Configuration> configs = new ArrayList<Configuration>();
        for (String name : configurationName) {
            ExpressConfiguration cDao = configDataManager.getConfigByName(name);
            if (cDao != null) {

                Boolean isCategoryType = ConfigurationType.valueOf(cDao.getType()) == ConfigurationType.CATEGORY_RESTRICTION;
                if (isCategoryType) {
                    CategoryRestrictionConfiguration crConfig = new CategoryRestrictionConfiguration(cDao.getName(), cDao.getStorename(), cDao.getCategory(), cDao.getActive());
                    configs.add(crConfig);
                } else {
                    QuotaLimitConfiguration qlConfig = new QuotaLimitConfiguration(cDao.getName(), cDao.getStorename(), cDao.getQuota(), cDao.getActive());
                    configs.add(qlConfig);
                }
            }
        }
        return configs;
    }

    public void createConfiguration(Configuration config) {
        Boolean isCategoryType = config.type == ConfigurationType.CATEGORY_RESTRICTION;
        int limit = !isCategoryType ? ((QuotaLimitConfiguration)config).limit: 0;
        String category = isCategoryType ? ((CategoryRestrictionConfiguration)config).category: "";
        ExpressConfiguration dao = new ExpressConfiguration(config.name, config.type.toString(), config.storeName, config.active, limit, category);
        configDataManager.create(dao);

        if (config.storeName == null) {
            if (isCategoryType) {
                List<ExpressStore> expressStores = storeDataManager.getAll();
                for (ExpressStore expressStore : expressStores) {
                    this.createConfigurationForStoreAndItems(expressStore.getName(), config);
                }
            }
        } else {
            this.createConfigurationForStoreAndItems(config.storeName, config);
        }

        return;
    }

    private void createConfigurationForStoreAndItems(String storeName, Configuration config) {
        Boolean isCategoryType = config.type == ConfigurationType.CATEGORY_RESTRICTION;
        String category = isCategoryType ? ((CategoryRestrictionConfiguration)config).category: "";
        StoreConfiguration storeConfiguration = new StoreConfiguration(storeName, config.name);
        storeConfigDataManager.create(storeConfiguration);

        if (isCategoryType) {
            // query item within the store with that category and create an item configuration
            List<ExpressStoreItem> expressStoreItems = itemDataManager.getItems(storeName, category);
            for (ExpressStoreItem expressStoreItem : expressStoreItems) {
                ItemConfiguration icDao = new ItemConfiguration(storeName, expressStoreItem.getName(), config.name);
                itemConfigDataManager.create(icDao);
            }
        } else {
            // and query item within the store and create an item configuration
            List<ExpressStoreItem> expressStoreItems = itemDataManager.getItemsByStoreName(storeName);
            for (ExpressStoreItem expressStoreItem : expressStoreItems) {
                ItemConfiguration icDao = new ItemConfiguration(storeName, expressStoreItem.getName(), config.name);
                itemConfigDataManager.create(icDao);
            }
        }
    }

    public void updateConfiguration(Configuration config) {
        Boolean isCategoryType = config.type == ConfigurationType.CATEGORY_RESTRICTION;
        int limit = !isCategoryType ? ((QuotaLimitConfiguration)config).limit: 0;
        String category = isCategoryType ? ((CategoryRestrictionConfiguration)config).category: "";
        ExpressConfiguration dao = new ExpressConfiguration(config.name, config.type.toString(), config.storeName, config.active, limit, category);
        configDataManager.update(dao);
        return;
    }

    public void deleteConfiguration(Configuration config) {
        Boolean isCategoryType = config.type == ConfigurationType.CATEGORY_RESTRICTION;
        int limit = !isCategoryType ? ((QuotaLimitConfiguration)config).limit: 0;
        String category = isCategoryType ? ((CategoryRestrictionConfiguration)config).category: "";
        ExpressConfiguration dao = new ExpressConfiguration(config.name, config.type.toString(), config.storeName, config.active, limit, category);
        configDataManager.delete(dao);
        return;
    }

    public void createStoreConfigurations(String storeName, List<Configuration> configs) {
        for (Configuration config : configs) {
            StoreConfiguration dao = new StoreConfiguration(storeName, config.name);
            storeConfigDataManager.create(dao);
        }
        return;
    }

    public void createItemConfigurations(String storeName, String itemName, List<Configuration> configs) {
        for (Configuration config : configs) {
            ItemConfiguration dao = new ItemConfiguration(storeName, itemName, config.name);
            itemConfigDataManager.create(dao);
        }
        return;
    }

    public void deleteStoreConfigurations(Configuration config) {
        storeConfigDataManager.delete(config.name);
        return;
    }

    public void deleteItemConfigurations(Configuration config) {
        itemConfigDataManager.delete(config.name);
        return;
    }

    public void deleteStoreConfiguration(String storeName, Configuration config) {
        StoreConfiguration dao = new StoreConfiguration(storeName, config.name);
        storeConfigDataManager.delete(dao);
        return;
    }

    public void deleteItemConfigurations(String storeName, Configuration config) {
        itemConfigDataManager.delete(storeName, config.name);
        return;
    }

    public void deleteItemConfiguration(String storeName, String itemName, Configuration config) {
        ItemConfiguration dao = new ItemConfiguration(storeName, itemName, config.name);
        itemConfigDataManager.delete(dao);
        return;
    }
}
