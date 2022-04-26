package edu.gatech.cs6310.repository;

import edu.gatech.cs6310.Item;
import edu.gatech.cs6310.Configuration;
import edu.gatech.cs6310.configuration.models.ItemConfiguration;
import edu.gatech.cs6310.configuration.repository.ConfigurationRepository;
import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.dataManager.ExpressStoreItemDataManager;
import edu.gatech.cs6310.configuration.dataManager.ItemConfigurationDataManager;
import edu.gatech.cs6310.models.*;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {

    private final PostgresClientImpl ps;
    private final Logger logger;

    private final ItemConfigurationDataManager itemConfigDataManager;
    private final ExpressStoreItemDataManager itemDataManager;

    public ItemRepository(PostgresClientImpl ps, Logger logger) {
        this.ps = ps;
        this.logger = logger;

        itemConfigDataManager = new ItemConfigurationDataManager(ps, logger);
        itemDataManager = new ExpressStoreItemDataManager(ps, logger);
    }

    public ArrayList<Item> getItemsByStoreName(String storeName) {
        List<ExpressStoreItem> expressStoreItems = itemDataManager.getItemsByStoreName(storeName);
        ArrayList<Item> items = new ArrayList<Item>();
        for (ExpressStoreItem dao: expressStoreItems) {
            // Get item configurations for each item
            List<ItemConfiguration> configDaos = itemConfigDataManager.getItemConfigurationsByName(storeName, dao.getName());
            if (configDaos != null) {
                // convert all dao into configuration
                // Get configuration by name - return configuration
                // create item
                // add configuration to item
                String[] configNames = configDaos.stream().map(c -> c.getConfigurationname()).toArray(String[]::new);
                ConfigurationRepository rep = new ConfigurationRepository(this.ps, this.logger);
                ArrayList<Configuration> configs = rep.getConfigurationsByNames(configNames);
                Item item = new Item(dao.getName(), storeName, dao.getWeight(), dao.getCategory(), configs);
                items.add(item);
            } else {
                Item item = new Item(dao.getName(), storeName, dao.getWeight(), dao.getCategory(), new ArrayList<Configuration>());
                items.add(item);
            }
        }
        return items;
    }

    public void createItems(String storeName, List<Item> items) {
        for (Item item : items) {
            ExpressStoreItem dao = new ExpressStoreItem(item.getName(), storeName, item.getWeight(), item.getCategory());
            itemDataManager.create(dao);

            // create item configurations
            List<Configuration> itemConfigs = item.getConfigurations();
            ConfigurationRepository rep = new ConfigurationRepository(this.ps, this.logger);
            rep.createItemConfigurations(storeName, item.getName(), itemConfigs);
        }
        return;
    }
}
