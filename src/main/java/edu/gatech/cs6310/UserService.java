package edu.gatech.cs6310;

import edu.gatech.cs6310.auth.Authentication;
import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.dataManager.UserDataManager;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.logger.TransactionLogQueryManager;
import edu.gatech.cs6310.logger.models.TransactionLog;
import edu.gatech.cs6310.models.AppUser;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UserService {

    private final PostgresClientImpl ps;
    private final Logger logger;
    private final Authentication authentication;
    private final UserDataManager userDataManager;
    private final TransactionLogQueryManager queryManager;

    public UserService(PostgresClientImpl ps, Logger logger, Authentication authentication) {
        this.ps = ps;
        this.logger = logger;
        this.authentication = authentication;
        this.queryManager = new TransactionLogQueryManager(ps);
        this.userDataManager = new UserDataManager(ps, logger);
    }

    public void displayProfile() {
        this.print(this.authentication.currentAppUser);
        System.out.println("OK:display_completed");
    }

    public AppUser getAppUser() {
        return this.authentication.currentAppUser;
    }

    public Customer getCustomer() {
        if (this.authentication.currentUser instanceof Customer) {
            return (Customer) this.authentication.currentUser;
        } else {
            return null;
        }
    }

    public DronePilot getPilot() {
        if (this.authentication.currentUser instanceof DronePilot) {
            return (DronePilot) this.authentication.currentUser;
        } else {
            return null;
        }
    }

    public void displayProfiles() {
        List<edu.gatech.cs6310.models.AppUser> appUsers = ps.selectAll("AppUser");
        for (AppUser user : appUsers) {
            if (Role.valueOf(user.getRole()) != Role.GUEST) {
                this.print(user);
            }
        }
        System.out.println("OK:display_completed");
    }

    public Boolean updateCustomerCredit() {
        return this.userDataManager.updateCustomerCredit(this.getAppUser().getName(), this.getCustomer().credit);
    }

    public Boolean updatePilotExperience(String pilotAccountId, int experience) {
        return this.userDataManager.updatePilotExperience(pilotAccountId, experience);
    }

    public void printUserPurchaseHistory() {
        if (!canPurchase()) {
            System.out.println("ERROR:only_customer_has_purchase_history");
            return;
        }

        String username = this.authentication.currentAppUser.getName();

        System.out.println("name:" + this.authentication.currentAppUser.getFirstname() + "_" + this.authentication.currentAppUser.getLastname());
        List<TransactionLog> logs = queryManager.getTransactionLogByUsername(username);

        TreeMap<String, Integer> storeItemQuantityMap = new TreeMap<>();
        TreeMap<String, Integer> storeTotalCostMap = new TreeMap<>();
        for (TransactionLog log : logs) {
            String storeName = log.getStorename();
            int quantity = log.getQuantity();
            int totalRevenue = log.getUnitprice() * log.getQuantity();
            if (storeItemQuantityMap.containsKey(storeName)) {
                int quantitySum = quantity + storeItemQuantityMap.get(storeName);
                int revSum = totalRevenue + storeTotalCostMap.get(storeName);
                storeItemQuantityMap.put(storeName, quantitySum);
                storeTotalCostMap.put(storeName, revSum);
            } else {
                storeItemQuantityMap.put(storeName, quantity);
                storeTotalCostMap.put(storeName, totalRevenue);
            }
        }

        for (Map.Entry<String, Integer> entry : storeItemQuantityMap.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();
            Integer rev = storeTotalCostMap.get(itemName);
            System.out.println("store_name:" + itemName + ",total_item_bought:" + quantity + ",total_cost:" + rev);
        }

        System.out.println("OK:display_completed");
    }

    public Boolean isAdmin() {
        return Role.valueOf(this.authentication.currentAppUser.getRole()) == Role.ADMIN;
    }

    public Boolean isCurrentUser(String username) {
        return this.authentication.currentAppUser.getName().equals(username);
    }

    public Boolean canPurchase() {
        return Role.valueOf(this.authentication.currentAppUser.getRole()) == Role.CUSTOMER;
    }

    void displayCustomers() {
        List<Customer> customers = this.userDataManager.getAllCustomers();
        for (Customer cust : customers) {
            System.out.println("name:" + cust.firstName + "_" + cust.lastName +
                    ",phone:" + cust.phoneNumber +
                    ",rating:" + cust.rating +
                    ",credit:" + cust.credit);
        }
        System.out.println("OK:display_completed");
    }

    void displayPilots() {
        List<DronePilot> pilots = this.userDataManager.getAllPilot();
        for (DronePilot pilot : pilots) {
            System.out.println("name:" + pilot.firstName + "_" + pilot.lastName +
                    ",phone:" + pilot.phoneNumber +
                    ",taxID:" + pilot.taxIdentifier +
                    ",licenseID:" + pilot.licenseId +
                    ",experience:" + pilot.experience);
        }
        System.out.println("OK:display_completed");
    }

    private void print(AppUser user) {
        StringBuilder menu = new StringBuilder();
        System.out.println("name:" + user.getFirstname() + "_" + user.getLastname() +
                ",phone:" + user.getPhonenumber() +
                ",role:" + user.getRole());
        System.out.println(menu.toString());
    }
}
