package edu.gatech.cs6310.configuration.dataManager;

import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.configuration.models.ExpressConfiguration;

import java.util.List;

public class ExpressConfigurationDataManager {

    private final PostgresClientImpl ps;
    private final Logger logger;

    public ExpressConfigurationDataManager(PostgresClientImpl ps, Logger logger) {
        this.ps = ps;
        this.logger = logger;
    }

    public ExpressConfiguration getConfigByName(String configName) {
        List<ExpressConfiguration> configs = ps.selectWhere("ExpressConfiguration", "name", configName);
        boolean exists = configs.size() > 0;
        if (!exists) {
            logger.error("CONFIGURATION", "ExpressConfiguration %s does not exist.".formatted(configName));
            return null;
        }
        if (configs.size() > 1) {
            logger.error("UNIQUE_KEY_ERROR", "ExpressConfiguration name %s should be unqiue".formatted(configName));
            return null;
        }
        ExpressConfiguration config = configs.get(0);
        return config;
    }

    public List<ExpressConfiguration> getAll() {
        List<ExpressConfiguration> configs =  ps.selectAll("ExpressConfiguration");
        return configs;
    }

    public List<ExpressConfiguration> getSystemConfigs() {
        List<ExpressConfiguration> configs =  ps.selectWhereNull("ExpressConfiguration", "storename");
        return configs;
    }

    public boolean create(ExpressConfiguration configDao) {
        try{
            ps.save(configDao);
            logger.info("CONFIGURATION", "Configuration %s created successfully.".formatted(configDao.getName()));
            return true;
        } catch (Exception e){
            logger.error("CONFIGURATION", "Configuration %s fail to create.".formatted(configDao.getName()));
            return false;
        }
    }

    public boolean update(ExpressConfiguration configDao) {
        try{
            ps.updateWhere("ExpressConfiguration", "active", configDao.getActive().toString(), "name", configDao.getName());
            logger.info("CONFIGURATION", "Configuration %s updated successfully.".formatted(configDao.getName()));
            return true;
        } catch (Exception e){
            logger.error("CONFIGURATION", "Configuration %s fail to update.".formatted(configDao.getName()));
            return false;
        }
    }

    public boolean delete(ExpressConfiguration configDao) {
        try{
            ps.deleteWhere("ExpressConfiguration", "name", configDao.getName());
            logger.info("CONFIGURATION", "Configuration %s deleted successfully.".formatted(configDao.getName()));
            return true;
        } catch (Exception e){
            logger.error("CONFIGURATION", "Configuration %s fail to delete.".formatted(configDao.getName()));
            return false;
        }
    }
}
