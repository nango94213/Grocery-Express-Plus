package edu.gatech.cs6310.configuration.dataManager;

import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.configuration.models.ItemConfiguration;

import java.util.List;

public class ItemConfigurationDataManager {

    private final PostgresClientImpl ps;
    private final Logger logger;

    public ItemConfigurationDataManager(PostgresClientImpl ps, Logger logger) {
        this.ps = ps;
        this.logger = logger;
    }

    public List<ItemConfiguration> getItemConfigurationsByName(String storeName, String itemName) {
        return ps.selectWhere("ItemConfiguration", "storename", storeName, "itemname", itemName);
    }

    public boolean create(ItemConfiguration configDao) {
        try{
            ps.save(configDao);
            logger.info("ITEM_CONFIGURATION", "Configuration %s created successfully.".formatted(configDao.getConfigurationname()));
            return true;
        } catch (Exception e){
            logger.error("ITEM_CONFIGURATION", "Configuration %s fail to create.".formatted(configDao.getConfigurationname()));
            return false;
        }
    }

    public boolean delete(ItemConfiguration configDao) {
        try{
            ps.deleteWhere("ItemConfiguration", "storename", configDao.getStorename(), "itemname", configDao.getItemname(), "configurationname", configDao.getConfigurationname());
            logger.info("ITEM_CONFIGURATION", "Configuration %s deleted successfully.".formatted(configDao.getConfigurationname()));
            return true;
        } catch (Exception e){
            logger.error("ITEM_CONFIGURATION", "Configuration %s fail to delete.".formatted(configDao.getConfigurationname()));
            return false;
        }
    }

    public boolean delete(String storeName, String configurationName) {
        try{
            ps.deleteWhere("ItemConfiguration", "storename", storeName, "configurationname", configurationName);
            logger.info("ITEM_CONFIGURATION", "Configuration %s deleted successfully for store %s.".formatted(configurationName, storeName));
            return true;
        } catch (Exception e){
            logger.error("ITEM_CONFIGURATION", "Configuration %s fail to delete.".formatted(configurationName));
            return false;
        }
    }

    public boolean delete(String configurationName) {
        try{
            ps.deleteWhere("ItemConfiguration", "configurationname", configurationName);
            logger.info("ITEM_CONFIGURATION", "Configuration %s deleted successfully.".formatted(configurationName));
            return true;
        } catch (Exception e){
            logger.error("ITEM_CONFIGURATION", "Configuration %s fail to delete.".formatted(configurationName));
            return false;
        }
    }
}
