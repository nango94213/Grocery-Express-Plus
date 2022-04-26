package edu.gatech.cs6310;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Store implements PropertyChangeListener {
    private int revenue;
    public String name;
    public String storeOwnerUsername;
    private final TreeMap<String, Item> itemTreeMap = new TreeMap<>();
    private final TreeMap<String, Drone> droneTreeMap = new TreeMap<>();
    private final TreeMap<String, Order> orderTreeMap = new TreeMap<>();
    // key: order id, order: drone Id
    private final TreeMap<String, String> orderDroneTreeMap = new TreeMap<>();
    // Key: Config name, Value: ExpressConfiguration
    private final TreeMap<String, Configuration> configurationTreeMap = new TreeMap<String, Configuration>();

    Store(String storeName, int revenue, ArrayList<Configuration> configurations, String storeOwnerUsername) {
        this.name = storeName;
        this.revenue = revenue;
        this.storeOwnerUsername = storeOwnerUsername;
        for (Configuration config : configurations) {
            this.configurationTreeMap.put(config.name, config);
        }
    }

    public Store(String storeName, int revenue, ArrayList<Item> items, ArrayList<Configuration> configurations, String storeOwnerUsername) {
        this.name = storeName;
        this.revenue = revenue;
        this.storeOwnerUsername = storeOwnerUsername;
        for (Configuration config : configurations) {
            this.configurationTreeMap.put(config.name, config);
        }
        for (Item item : items) {
            this.itemTreeMap.put(item.name, item);
        }
    }

    //================================================================================
    // Listener
    //================================================================================

    public void propertyChange(PropertyChangeEvent evt) {

        ConfigurationNotification notif = (ConfigurationNotification) evt.getNewValue();
        if (notif != null) {
            String configName = notif.config.name;
            if (this.configurationTreeMap.containsKey(configName)) {
                if (notif.shouldBeRemoved) {
                    if (notif.shouldRemoveConfiguration(this.name, null)) {
                        configurationTreeMap.remove(configName);
                    }
                } else {
                    if (notif.shouldUpdateConfiguration(this.name, null)) {
                        // Update config with the latest version
                        configurationTreeMap.put(configName, notif.config);
                    }
                }
            } else {
                if (notif.shouldAddConfiguration(this.name, null)) {
                    // Update system configuration cache. Item should update itself through listener
                    configurationTreeMap.put(configName, notif.config);
                }
            }
        }
    }

    //================================================================================
    // Utils
    //================================================================================

    boolean hasConfiguration(String configName) { return this.configurationTreeMap.containsKey(configName); }

    Item getItem(String name) {
        return itemTreeMap.get(name);
    }

    public int getRevenue() { return this.revenue; }

    boolean isItemExist(String name) {
        return itemTreeMap.containsKey(name);
    }

    public List<Configuration> getConfigurations() {
        return this.configurationTreeMap.values().stream().toList();
    }

    public List<Item> getItems() {
        return this.itemTreeMap.values().stream().toList();
    }

    ArrayList<Configuration> getConfigurationsForNewItem(String category) {
        ArrayList<Configuration> configs = new ArrayList<Configuration>();
        for (Map.Entry<String, Configuration> entry : this.configurationTreeMap.entrySet()) {
            Configuration config = entry.getValue();
            if (config.type == ConfigurationType.CATEGORY_RESTRICTION) {
                CategoryRestrictionConfiguration crConfig = (CategoryRestrictionConfiguration)config;
                if (category.equals(crConfig.category)) {
                    configs.add(crConfig);
                }
            } else {
                configs.add(config);
            }
        }
        return configs;
    }

    void printStoreConfigurations(){
        List<Configuration> configs = this.getConfigurations();
        if (configs.size() == 0) {
            System.out.println("INFO:there_is_no_configurations_for_store: " + this.name);
        } else {
            for (Configuration config : configs) {
                config.print();
            }
        }
    }

    void printStoreItemConfigurations(String itemName){
        Item item = this.itemTreeMap.get(itemName);
        item.printConfigurations();
    }

    //================================================================================
    // Core features
    //================================================================================

    Item addItem(String name, int weight, String category) {
        if (itemTreeMap.containsKey(name)) {
            System.out.println("ERROR:item_identifier_already_exists");
            return null;
        }

        // Apple existing category configurations in store for newly added item
        // Quota limit need to be explicitly added
        ArrayList<Configuration> configs = this.getConfigurationsForNewItem(category);
        Item item = new Item(name, this.name, weight, category, configs);
        itemTreeMap.put(name, item);
        return item;
    }

    void displayItems() {
        for (Map.Entry<String, Item> entry : this.itemTreeMap.entrySet()) {
            Item item = entry.getValue();
            System.out.println(item.name + "," + item.weight);
        }
    }

    boolean purchaseDrone(String droneId, int capacity, int fuel) {
        if (droneTreeMap.containsKey(droneId)) {
            System.out.println("ERROR:drone_identifier_already_exists");
            return false;
        }

        Drone drone = new Drone(droneId, capacity, fuel);
        droneTreeMap.put(droneId, drone);
        return true;
    }

    void displayDrones() {
        for (Map.Entry<String, Drone> entry : this.droneTreeMap.entrySet()) {
            Drone drone = entry.getValue();
            if (drone.pilot != null) {
                System.out.println("droneID:" + drone.id +
                        ",total_cap:" + drone.maxCapacity +
                        ",num_orders:" + drone.getTotalNumberOfOrders() +
                        ",remaining_cap:" + drone.remainingCapacity +
                        ",trips_left:" + drone.remainingTrip +
                        ",flown_by:" + drone.pilot.firstName + "_" + drone.pilot.lastName);
            } else {
                System.out.println("droneID:" + drone.id +
                        ",total_cap:" + drone.maxCapacity +
                        ",num_orders:" + drone.getTotalNumberOfOrders() +
                        ",remaining_cap:" + drone.remainingCapacity +
                        ",trips_left:" + drone.remainingTrip);
            }
        }
    }

    boolean assignPilot(String droneId, DronePilot pilot) {
        if (!droneTreeMap.containsKey(droneId)) {
            System.out.println("ERROR:drone_identifier_does_not_exist");
            return false;
        }
        Drone drone = droneTreeMap.get(droneId);
        drone.pilot = pilot;
        return true;
    }

    boolean unassignPilot(String droneId) {
        if (!droneTreeMap.containsKey(droneId)) {
            System.out.println("ERROR:drone_identifier_does_not_exist");
            return false;
        }
        Drone drone = droneTreeMap.get(droneId);
        drone.pilot = null;
        return true;
    }

    boolean startOrder(String orderId, String droneId, Customer customer) {
        if (this.orderTreeMap.containsKey(orderId)) {
            System.out.println("ERROR:order_identifier_already_exists");
            return false;
        }

        if (!this.droneTreeMap.containsKey(droneId)) {
            System.out.println("ERROR:drone_identifier_does_not_exist");
            return false;
        }

        if (customer == null) {
            System.out.println("ERROR:customer_identifier_does_not_exist");
            return false;
        }

        // Create order
        Order order = new Order(orderId, customer);

        // Add order to Drone
        Drone drone = droneTreeMap.get(droneId);
        drone.addOrder(order);

        // Track order in customer
        customer.addNewOrder(order);

        // Track order in Store
        this.orderTreeMap.put(orderId, order);
        this.orderDroneTreeMap.put(orderId, droneId);
        return true;
    }

    boolean requestItem(String orderId, String itemName, int quantity, int unitPrice) {
        if (!this.orderTreeMap.containsKey(orderId)) {
            System.out.println("ERROR:order_identifier_does_not_exist");
            return false;
        }

        if (!this.itemTreeMap.containsKey(itemName)) {
            System.out.println("ERROR:item_identifier_does_not_exist");
            return false;
        }

        Order order = this.orderTreeMap.get(orderId);
        Item item = this.itemTreeMap.get(itemName);

        if (!item.isForSale()) {
            System.out.println("ERROR:category_of_this_item_is_not_for_sale");
            return false;
        }

        if (!item.confirmQuantityRestriction(quantity)) {
            System.out.println("ERROR:quantity_quota_limit_exceed");
            return false;
        }

        String droneId = this.orderDroneTreeMap.get(orderId);
        Drone drone = this.droneTreeMap.get(droneId);

        Line line = new Line(item, quantity, unitPrice);
        if (!order.canAddNewLine(line)) {
            return false;
        }

        boolean canCarry = drone.canAddNewItem(line);
        if (!canCarry) {
            System.out.println("ERROR:drone_cant_carry_new_item");
            return false;
        }

        order.addLine(line);
        drone.addLine(line);
        return true;
    }

    void displayOrders() {
        for (Map.Entry<String, Order> entry : this.orderTreeMap.entrySet()) {
            Order order = entry.getValue();
            System.out.println("orderID:" + order.orderId);
            order.displayItems();
        }
    }

    void displayOrdersForCustomer(String accountId) {
        for (Map.Entry<String, Order> entry : this.orderTreeMap.entrySet()) {
            Order order = entry.getValue();
            if (order.customer.accountId.equals(accountId)) {
                System.out.println("orderID:" + order.orderId);
                order.displayItems();
            }
        }
    }

    boolean purchaseOrder(String orderId, TransactionLogClosure onPurchaseSuccess, DroneOrderCompletionClosure onDroneOrderCompleteSuccess) {
        if (!this.orderTreeMap.containsKey(orderId)) {
            System.out.println("ERROR:order_identifier_does_not_exist");
            return false;
        }

        // number of remaining deliveries must be reduced by one
        // experience must be increased by one
        String droneId = this.orderDroneTreeMap.get(orderId);
        Drone drone = this.droneTreeMap.get(droneId);

        if (drone.pilot == null) {
            System.out.println("ERROR:drone_needs_pilot");
            return false;
        }

        boolean success = drone.completeOrder(orderId, onDroneOrderCompleteSuccess);
        if (!success) {
            System.out.println("ERROR:drone_needs_fuel");
            return false;
        }

        Order order = this.orderTreeMap.get(orderId);
        List<Line> lines = order.getLines();
        for (Line line : lines) {
            onPurchaseSuccess.logTransaction(line.item.name, line.unitPrice, line.quantity);
        }
        // cost of order must be deducted from customer account
        order.complete();
        // cost of order must be added to the store revenue
        this.revenue += order.getTotalCost();

        // order must be removed from the system
        this.orderTreeMap.remove(orderId);
        this.orderDroneTreeMap.remove(orderId);
        return true;
    }

    boolean cancelOrder(String orderId) {
        if (!this.orderTreeMap.containsKey(orderId)) {
            System.out.println("ERROR:order_identifier_does_not_exist");
            return false;
        }

        String droneId = this.orderDroneTreeMap.get(orderId);
        Drone drone = this.droneTreeMap.get(droneId);
        // cancel order for drone
        drone.cancelOrder(orderId);

        Order order = this.orderTreeMap.get(orderId);
        // cancel order for customer
        order.cancel();
        // cancel order from store
        this.orderTreeMap.remove(orderId);
        this.orderDroneTreeMap.remove(orderId);
        return true;
    }

    int getIncomingRevenue() {
        int totalIncomingRevenue = 0;
        for (Map.Entry<String, Order> entry : this.orderTreeMap.entrySet()) {
            Order order = entry.getValue();
            totalIncomingRevenue += order.getTotalCost();
        }
        return totalIncomingRevenue;
    }

    void displayIncomingRevenue() {
        int incomingRevenue = this.getIncomingRevenue();
        System.out.println("incoming_revenue:" + incomingRevenue);
    }
}