package edu.gatech.cs6310.dataManager;

import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.models.Customer;
import edu.gatech.cs6310.models.Pilot;

import edu.gatech.cs6310.DronePilot;

import java.util.ArrayList;
import java.util.List;

public class UserDataManager {

    private final PostgresClientImpl ps;
    private final Logger logger;

    public UserDataManager(PostgresClientImpl ps, Logger logger) {
        this.ps = ps;
        this.logger = logger;
    }

    public List<edu.gatech.cs6310.Customer> getAllCustomers() {
        List<Customer> models = ps.selectAll("Customer");

        ArrayList<edu.gatech.cs6310.Customer> customers = new ArrayList<>();
        for (Customer model : models) {
            edu.gatech.cs6310.models.AppUser user = this.getUser(model.getAuthUserId());
            if (user != null) {
                edu.gatech.cs6310.Customer customer = new edu.gatech.cs6310.Customer(model.getAuthUserId(), user.getFirstname(),
                        user.getLastname(), user.getPhonenumber(), model.getRating(), model.getCredit());
                customers.add(customer);
            }
        }
        return customers;
    }

    public edu.gatech.cs6310.Customer getCustomerByUsername(String username) {
        List<Customer> customers = ps.selectWhere("Customer", "authUserId", username);
        boolean exists = customers.size() > 0;
        if (!exists) {
            logger.info("CUSTOMER", "Customer %s does not exist.".formatted(username));
            return null;
        }
        if (customers.size() > 1) {
            logger.error("UNIQUE_KEY_ERROR", "Customer username %s should be unqiue".formatted(username));
            return null;
        }
        Customer model = customers.get(0);
        edu.gatech.cs6310.models.AppUser user = this.getUser(model.getAuthUserId());
        edu.gatech.cs6310.Customer customer = new edu.gatech.cs6310.Customer(model.getAuthUserId(), user.getFirstname(),
                user.getLastname(), user.getPhonenumber(), model.getRating(), model.getCredit());
        return customer;
    }

    public List<DronePilot> getAllPilot() {
        List<Pilot> models = ps.selectAll("Pilot");

        ArrayList<edu.gatech.cs6310.DronePilot> pilots = new ArrayList<>();
        for (Pilot model : models) {
            edu.gatech.cs6310.models.AppUser user = this.getUser(model.getAuthUserId());
            if (user != null) {
                edu.gatech.cs6310.DronePilot dronePilot = new edu.gatech.cs6310.DronePilot(model.getAccountId(), user.getFirstname(),
                        user.getLastname(), user.getPhonenumber(), model.getTaxIdentifier(), model.getLicenseId(), model.getExperience());
                pilots.add(dronePilot);
            }
        }
        return pilots;
    }

    public DronePilot getPilotByUsername(String username) {
        List<Pilot> models = ps.selectWhere("Pilot", "authUserId", username);
        boolean exists = models.size() > 0;
        if (!exists) {
            logger.info("PILOT", "Pilot %s does not exist.".formatted(username));
            return null;
        }
        if (models.size() > 1) {
            logger.error("UNIQUE_KEY_ERROR", "Pilot username %s should be unqiue".formatted(username));
            return null;
        }
        Pilot model = models.get(0);
        edu.gatech.cs6310.models.AppUser user = this.getUser(model.getAuthUserId());
        edu.gatech.cs6310.DronePilot dronePilot = new edu.gatech.cs6310.DronePilot(model.getAccountId(), user.getFirstname(),
                user.getLastname(), user.getPhonenumber(), model.getTaxIdentifier(), model.getLicenseId(), model.getExperience());
        return dronePilot;
    }

    public Boolean updateCustomerCredit(String username, int credit) {
        try{
            ps.updateWhere("Customer", "credit", Integer.toString(credit), "authUserId", username);
            logger.info("CUSTOMER", "Customer %s updated successfully".formatted(username));
            return true;
        } catch (Exception e){
            logger.error("CUSTOMER", "Customer %s fail to update.".formatted(username));
            return false;
        }
    }

    public Boolean updatePilotExperience(String accountId, int experience) {
        try{
            ps.updateWhere("Pilot", "experience", Integer.toString(experience), "accountId", accountId);
            logger.info("PILOT", "Pilot %s updated successfully".formatted(accountId));
            return true;
        } catch (Exception e){
            logger.error("PILOT", "Pilot %s fail to update.".formatted(accountId));
            return false;
        }
    }

    public Boolean isAccountIdAvailableForCustomer(String accountId) {
        List<edu.gatech.cs6310.models.Customer> customers = ps.selectWhere("Customer", "accountId", accountId);
        boolean exists = customers.size() > 0;
        if(exists) {
            logger.info("CREATE_ACCOUNT", "Customer accountId %s already exist.".formatted(accountId));
            return false;
        }
        return true;
    }

    public Boolean isAccountIdAvailableForPilot(String accountId) {
        List<edu.gatech.cs6310.models.Pilot> pilots = ps.selectWhere("Pilot", "accountId", accountId);
        boolean exists = pilots.size() > 0;
        if(exists) {
            logger.info("CREATE_ACCOUNT", "Pilots accountId %s already exist.".formatted(accountId));
            return false;
        }
        return true;
    }

    public Pilot getPilotByAccountId(String accountId) {
        List<edu.gatech.cs6310.models.Pilot> pilots = ps.selectWhere("Pilot", "accountId", accountId);
        boolean exists = pilots.size() > 0;
        if (!exists) {
            logger.info("PILOT", "Pilots accountId %s does not exist.".formatted(accountId));
            return null;
        }
        if (pilots.size() > 1) {
            logger.error("UNIQUE_KEY_ERROR", "Pilots accountId %s should be unqiue".formatted(accountId));
            return null;
        }
        return pilots.get(0);
    }

    public Boolean isLicenseIdAvailableForPilot(String licenseId) {
        List<edu.gatech.cs6310.models.Pilot> pilots = ps.selectWhere("Pilot", "licenseId", licenseId);
        boolean exists = pilots.size() > 0;
        if (exists) {
            logger.info("CREATE_ACCOUNT", "Pilots licenseId %s already exist.".formatted(licenseId));
            return false;
        }
        return true;
    }

    private edu.gatech.cs6310.models.AppUser getUser(String username) {
        List<edu.gatech.cs6310.models.AppUser> appUsers = ps.selectWhere("AppUser", "name", username);
        boolean exists = appUsers.size() > 0;
        if (!exists) {
            logger.info("GET_USER", "AppUser %s does not exist.".formatted(username));
            return null;
        }
        if (appUsers.size() > 1) {
            logger.error("UNIQUE_KEY_ERROR", "AppUser username %s should be unqiue".formatted(username));
            return null;
        }
        return appUsers.get(0);
    }
}
