package edu.gatech.cs6310.dataManager;

import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.models.ExpressStore;

import java.util.List;

public class ExpressStoreDataManager {

    private final PostgresClientImpl ps;
    private final Logger logger;

    public ExpressStoreDataManager(PostgresClientImpl ps, Logger logger) {
        this.ps = ps;
        this.logger = logger;
    }

    public List<ExpressStore> getAll() {
        List<ExpressStore> stores = ps.selectAll("ExpressStore");
        return stores;
    }

    public ExpressStore getStoreByName(String storeName) {
        List<ExpressStore> stores = ps.selectWhere("ExpressStore", "name", storeName);
        boolean exists = stores.size() > 0;
        if (!exists) {
            logger.error("STORE", "Store %s does not exist.".formatted(storeName));
            return null;
        }
        if (stores.size() > 1) {
            logger.error("UNIQUE_KEY_ERROR", "Store name %s should be unqiue".formatted(storeName));
            return null;
        }
        ExpressStore store = stores.get(0);
        return store;
    }

    public boolean create(ExpressStore expressStore) {
        try{
            ps.save(expressStore);
            logger.info("STORE", "Store %s created successfully.".formatted(expressStore.getName()));
            return true;
        } catch (Exception e){
            logger.error("STORE", "Store %s fail to create.".formatted(expressStore.getName()));
            return false;
        }
    }

    public Boolean updateStoreRevenue(String storeName, int revenue) {
        try{
            ps.updateWhere("ExpressStore", "revenue", Integer.toString(revenue), "name", storeName);
            logger.info("STORE", "Store %s updated successfully.".formatted(storeName));
            return true;
        } catch (Exception e){
            logger.error("STORE", "Store %s fail to update.".formatted(storeName));
            return false;
        }
    }
}
