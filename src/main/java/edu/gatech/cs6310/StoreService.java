package edu.gatech.cs6310;

import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.repository.ItemRepository;
import edu.gatech.cs6310.repository.StoreRepository;
import edu.gatech.cs6310.logger.TransactionLogQueryManager;
import edu.gatech.cs6310.logger.models.TransactionLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StoreService {

    private final Logger logger;

    private final TreeMap<String, Store> storeTreeMap = new TreeMap<>();
    private final StoreRepository storeRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final TransactionLogQueryManager queryManager;

    public StoreService(PostgresClientImpl ps, Logger logger, UserService userService) {
        this.logger = logger;
        this.userService = userService;
        this.storeRepository = new StoreRepository(ps, logger);
        this.itemRepository = new ItemRepository(ps, logger);
        this.queryManager = new TransactionLogQueryManager(ps);

        this.loadData();
    }

    //================================================================================
    // Core features
    //================================================================================

    public void createDrone(String storeName, String droneId, int capacity, int fuel) {
        if (!this.isStoreExist(storeName)) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            return;
        }
        Store store = this.getStoreByName(storeName);
        boolean success = store.purchaseDrone(droneId, capacity, fuel);
        if (success) {
            System.out.println("OK:change_completed");
        }
    }

    public void displayDrones(String storeName) {
        if (!this.isStoreExist(storeName)) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            return;
        }
        Store store = this.getStoreByName(storeName);
        store.displayDrones();
        System.out.println("OK:display_completed");
    }

    public void viewStoreStats(String storeName) {
        if (!this.isStoreExist(storeName)) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            return;
        }

        Store store = this.getStoreByName(storeName);
        if (!this.userService.isAdmin() && !this.userService.isCurrentUser(store.storeOwnerUsername)) {
            System.out.println("ERROR:only_admin_and_store_owner_can_see_store_stats");
            return;
        }

        System.out.println("name:" + store.name + ",revenue:" + store.getRevenue());

        List<TransactionLog> logs = queryManager.getTransactionLogByStoreName(storeName);

        TreeMap<String, Integer> itemQuantityMap = new TreeMap<>();
        TreeMap<String, Integer> itemRevMap = new TreeMap<>();
        for (TransactionLog log : logs) {
            String itemName = log.getItemname();
            Integer quantity = log.getQuantity();
            Integer totalRevenue = log.getUnitprice() * log.getQuantity();
            if (itemQuantityMap.containsKey(itemName)) {
                Integer quantitySum = quantity + itemQuantityMap.get(itemName);
                Integer revSum = totalRevenue + itemRevMap.get(itemName);
                itemQuantityMap.put(itemName, quantitySum);
                itemRevMap.put(itemName, revSum);
            } else {
                itemQuantityMap.put(itemName, quantity);
                itemRevMap.put(itemName, totalRevenue);
            }
        }

        for (Map.Entry<String, Integer> entry : itemQuantityMap.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();
            Integer rev = itemRevMap.get(itemName);
            System.out.println("item_name:" + itemName + ",quantity:" + quantity + ",revenue:" + rev);
        }

        System.out.println("OK:display_completed");
    }

    public boolean updateStoreRevenue(String storeName, int revenue) {
        return this.storeRepository.updateRevenue(storeName, revenue);
    }

    private void loadData() {
        ArrayList<Store> stores = this.storeRepository.getAll();
        for (Store store : stores) {
            this.storeTreeMap.put(store.name, store);
        }
    }

    void createStore(Store store, String username) {
        this.storeTreeMap.put(store.name, store);
        this.storeRepository.create(store, username);
    }

    Item addItem(String storeName, String itemName, int weight, String category) {
        Store store = this.getStoreByName(storeName);
        Item item = store.addItem(itemName, weight, category);
        if (item != null) {
            ArrayList<Item> createItems = new ArrayList<Item>();
            createItems.add(item);
            this.itemRepository.createItems(store.name, createItems);
        }
        return item;
    }

    TreeMap<String, Store> getAllStores() {
        return this.storeTreeMap;
    }

    Boolean isStoreExist(String storeName) {
        return this.storeTreeMap.containsKey(storeName);
    }

    Store getStoreByName(String storeName) {
        if (this.storeTreeMap.containsKey(storeName)) {
            return this.storeTreeMap.get(storeName);
        }
        return null;
    }
}
