package edu.gatech.cs6310.repository;

import edu.gatech.cs6310.Item;
import edu.gatech.cs6310.Store;
import edu.gatech.cs6310.Configuration;
import edu.gatech.cs6310.configuration.repository.ConfigurationRepository;
import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.dataManager.ExpressStoreDataManager;
import edu.gatech.cs6310.configuration.dataManager.StoreConfigurationDataManager;
import edu.gatech.cs6310.configuration.models.StoreConfiguration;
import edu.gatech.cs6310.models.ExpressStore;

import java.util.ArrayList;
import java.util.List;

public class StoreRepository {

    private final PostgresClientImpl ps;
    private final Logger logger;
    private final StoreConfigurationDataManager storeConfigDataManager;
    private final ExpressStoreDataManager storeDataManager;

    public StoreRepository(PostgresClientImpl ps, Logger logger) {
        this.ps = ps;
        this.logger = logger;
        storeConfigDataManager = new StoreConfigurationDataManager(ps, logger);
        storeDataManager = new ExpressStoreDataManager(ps, logger);
    }

    //================================================================================
    // Get
    //================================================================================

    public ArrayList<Store> getAll() {
        ArrayList<Store> stores = new ArrayList<Store>();
        List<ExpressStore> expressStores = storeDataManager.getAll();
        for (ExpressStore dao : expressStores) {
            Store store = this.getStoreInfo(dao);
            stores.add(store);
        }
        return stores;
    }

    public Store getStoreByName(String storeName) {
        ExpressStore expressStore = storeDataManager.getStoreByName(storeName);
        if (expressStore == null) {
            return null;
        }
        return this.getStoreInfo(expressStore);
    }

    //================================================================================
    // Create
    //================================================================================

    public void create(Store store, String username) {
        ExpressStore dao = new ExpressStore(store.name, username, store.getRevenue());
        // Create store configs
        List<Configuration> storeConfigs = store.getConfigurations();
        ConfigurationRepository rep = new ConfigurationRepository(this.ps, this.logger);
        rep.createStoreConfigurations(store.name, storeConfigs);

        // Create item
        List<Item> items = store.getItems();
        ItemRepository itemRep = new ItemRepository(this.ps, this.logger);
        itemRep.createItems(store.name, items);

        storeDataManager.create(dao);
        return;
    }

    //================================================================================
    // Get
    //================================================================================

    public boolean updateRevenue(String storeName, int revenue) {
        return storeDataManager.updateStoreRevenue(storeName, revenue);
    }

    //================================================================================
    // Util
    //================================================================================

    private Store getStoreInfo(ExpressStore expressStore) {
        String storeName = expressStore.getName();
        // Get configuration for store
        List<StoreConfiguration> storeConfigDaos = storeConfigDataManager.getStoreConfigurationsByName(storeName);
        String[] configNames = storeConfigDaos.stream().map(c -> c.getConfigurationname()).toArray(String[]::new);
        ConfigurationRepository rep = new ConfigurationRepository(this.ps, this.logger);
        ArrayList<Configuration> storeConfigs = rep.getConfigurationsByNames(configNames);

        // Get items for store
        ItemRepository itemRep = new ItemRepository(this.ps, this.logger);
        ArrayList<Item> items = itemRep.getItemsByStoreName(storeName);

        return new Store(storeName, expressStore.getRevenue(), items, storeConfigs, expressStore.getStoreownerusername());
    }
}


