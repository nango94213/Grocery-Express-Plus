package edu.gatech.cs6310.dataManager;

import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.models.ExpressStoreItem;

import java.util.List;

public class ExpressStoreItemDataManager {

    private final PostgresClientImpl ps;
    private final Logger logger;

    public ExpressStoreItemDataManager(PostgresClientImpl ps, Logger logger) {
        this.ps = ps;
        this.logger = logger;
    }

    public List<ExpressStoreItem> getItemsByStoreName(String storeName) {
        List<ExpressStoreItem> items = ps.selectWhere("ExpressStoreItem", "storename", storeName);
        return items;
    }

    public List<ExpressStoreItem> getItems(String storeName, String category) {
        List<ExpressStoreItem> items = ps.selectWhere("ExpressStoreItem", "storename", storeName, "category", category);
        return items;
    }

    public boolean create(ExpressStoreItem expressStoreItem) {
        try{
            ps.save(expressStoreItem);
            logger.info("ITEM", "ITEM %s created successfully.".formatted(expressStoreItem.getName()));
            return true;
        } catch (Exception e){
            logger.error("STORE", "Store %s fail to create.".formatted(expressStoreItem.getName()));
            return false;
        }
    }
}
