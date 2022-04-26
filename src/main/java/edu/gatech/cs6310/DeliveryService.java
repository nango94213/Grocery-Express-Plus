package edu.gatech.cs6310;

import edu.gatech.cs6310.auth.Authentication;
import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.dataManager.UserDataManager;
import edu.gatech.cs6310.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DeliveryService {

    private final StoreService storeService;
    private final ConfigurationService configService;
    private final PostgresClientImpl ps;
    private final Logger logger;

    private final TreeMap<String, DronePilot> pilotTreeMap = new TreeMap<String, DronePilot>();
    // Key: Pilot Id - auth user id / username, Value: drone Id
    private final TreeMap<String, String> pilotDroneTreeMap = new TreeMap<>();

    private final Authentication authentication;
    private final UserDataManager userDataManager;

    public DeliveryService(PostgresClientImpl ps, Logger logger, StoreService storeService, ConfigurationService service, Authentication authentication) {
        this.ps = ps;
        this.logger = logger;
        this.storeService = storeService;
        this.configService = service;
        this.userDataManager = new UserDataManager(ps, logger);
        this.authentication = authentication;

        this.loadPilots();
    }

    private void loadPilots() {
        List<DronePilot> pilots = this.userDataManager.getAllPilot();
        for (DronePilot pilot : pilots) {
            this.addPilot(pilot);
        }
    }

    //================================================================================
    // Core features
    //================================================================================

    public void createStore(String storeName, int revenue) {
        if (this.storeService.isStoreExist(storeName)) {
            System.out.println("ERROR:store_identifier_already_exists");
            return;
        }

        ArrayList<Configuration> configs = this.configService.getSystemConfigurationsByType(ConfigurationType.CATEGORY_RESTRICTION);
        Store store = new Store(storeName, revenue, configs, this.authentication.currentAppUser.getName());
        this.storeService.createStore(store, this.authentication.currentAppUser.getName());
        this.configService.addConfigurationChangeListener(store);
        System.out.println("OK:change_completed");
    }

    public void displayStores() {
        for (Map.Entry<String, Store> entry : this.storeService.getAllStores().entrySet()) {
            Store store = entry.getValue();
            System.out.println("name:" + store.name + ",revenue:" + store.getRevenue());
        }
        System.out.println("OK:display_completed");
    }

    public void addItem(String storeName, String itemName, int weight, String category) {
        if (!this.storeService.isStoreExist(storeName)) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            return;
        }

        Store store = this.storeService.getStoreByName(storeName);
        if(!store.storeOwnerUsername.equals(this.authentication.currentAppUser.getName())) {
            System.out.println("ERROR:only_store_owner_can_add_item_to_store");
            return;
        }

        Item item = this.storeService.addItem(storeName, itemName, weight, category);
        if (item != null) {
            this.configService.addConfigurationChangeListener(item);
            System.out.println("OK:change_completed");
        }
    }

    public void displayItems(String storeName) {
        if (!this.storeService.isStoreExist(storeName)) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            return;
        }

        Store store = this.storeService.getStoreByName(storeName);
        store.displayItems();
        System.out.println("OK:display_completed");
    }

    public void addPilot(DronePilot pilot) {
        boolean canAddPilot = this.canAddPilot(pilot.accountId, pilot.licenseId);
        if (!canAddPilot) {
            return;
        }

        pilotTreeMap.put(pilot.accountId, pilot);
    }

    public void flyDrone(String storeName, String droneId, String pilotId) {
        if (!this.storeService.isStoreExist(storeName)) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            return;
        }

        if(!this.pilotTreeMap.containsKey(pilotId)) {
            System.out.println("ERROR:pilot_identifier_does_not_exist");
            return;
        }

        // Unassign drone with the pilot
        if (this.pilotDroneTreeMap.containsKey(pilotId)) {
            Store store = this.storeService.getStoreByName(storeName);
            String currentDroneId = this.pilotDroneTreeMap.get(pilotId);
            boolean success = store.unassignPilot(currentDroneId);
            if (!success) {
                System.out.println("ERROR:cannot_unassign_drone_pilot");
                return;
            }
        }

        // Assign pilot to new drone
        DronePilot pilot = this.pilotTreeMap.get(pilotId);
        Store store = this.storeService.getStoreByName(storeName);
        boolean success = store.assignPilot(droneId, pilot);
        if (success) {
            this.pilotDroneTreeMap.put(pilotId, droneId);
            System.out.println("OK:change_completed");
        }
    }

    private boolean canAddPilot(String accountId, String licenseId) {
        for (Map.Entry<String, DronePilot> entry : this.pilotTreeMap.entrySet()) {
            DronePilot pilot = entry.getValue();
            if (pilot.accountId.equals(accountId)) {
                System.out.println("ERROR:pilot_identifier_already_exists");
                return false;
            }

            if (pilot.licenseId.equals(licenseId)) {
                System.out.println("ERROR:pilot_license_already_exists");
                return false;
            }
        }
        return true;
    }
}
