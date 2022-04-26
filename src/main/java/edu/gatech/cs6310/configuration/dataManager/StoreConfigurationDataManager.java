package edu.gatech.cs6310.configuration.dataManager;

import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.configuration.models.StoreConfiguration;

import java.util.List;

public class StoreConfigurationDataManager {

    private final PostgresClientImpl ps;
    private final Logger logger;

    public StoreConfigurationDataManager(PostgresClientImpl ps, Logger logger) {
        this.ps = ps;
        this.logger = logger;
    }

    public List<StoreConfiguration> getStoreConfigurationsByName(String storeName) {
        return ps.selectWhere("StoreConfiguration", "storename", storeName);
    }

    public boolean create(StoreConfiguration configDao) {
        try{
            ps.save(configDao);
            logger.info("STORE_CONFIGURATION", "Configuration %s created successfully.".formatted(configDao.getConfigurationname()));
            return true;
        } catch (Exception e){
            logger.error("STORE_CONFIGURATION", "Configuration %s fail to create.".formatted(configDao.getConfigurationname()));
            return false;
        }
    }

    public boolean delete(StoreConfiguration configDao) {
        try{
            ps.deleteWhere("StoreConfiguration", "storename", configDao.getStorename(), "configurationname", configDao.getConfigurationname());
            logger.info("STORE_CONFIGURATION", "Configuration %s deleted successfully.".formatted(configDao.getConfigurationname()));
            return true;
        } catch (Exception e){
            logger.error("STORE_CONFIGURATION", "Configuration %s fail to delete.".formatted(configDao.getConfigurationname()));
            return false;
        }
    }

    public boolean delete(String configurationName) {
        try{
            ps.deleteWhere("StoreConfiguration", "configurationname", configurationName);
            logger.info("STORE_CONFIGURATION", "Configuration %s deleted successfully.".formatted(configurationName));
            return true;
        } catch (Exception e){
            logger.error("STORE_CONFIGURATION", "Configuration %s fail to delete.".formatted(configurationName));
            return false;
        }
    }
}
