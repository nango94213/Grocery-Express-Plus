package edu.gatech.cs6310;

import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.logger.TransactionLogger;


public class OrderService {

    private final StoreService storeService;
    private final PostgresClientImpl ps;
    private final Logger logger;
    private final TransactionLogger transactionLogger;
    private final UserService userService;

    public OrderService(PostgresClientImpl ps, Logger logger, TransactionLogger transactionLogger, StoreService storeService, UserService userService) {
        this.ps = ps;
        this.logger = logger;
        this.transactionLogger = transactionLogger;
        this.storeService = storeService;
        this.userService = userService;
    }

    public void startOrder(String storeName, String orderId, String droneId) {
        if (!this.userService.canPurchase()) {
            System.out.println("ERROR:only_customer_account_can_purchase");
            return;
        }

        if (!this.storeService.isStoreExist(storeName)) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            return;
        }

        Store store = this.storeService.getStoreByName(storeName);
        Customer cust = this.userService.getCustomer();
        boolean success = store.startOrder(orderId, droneId, cust);
        if (success) {
            System.out.println("OK:change_completed");
        }
    }

    public void displayOrders(String storeName) {
        if (!this.storeService.isStoreExist(storeName)) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            return;
        }

        Store store = this.storeService.getStoreByName(storeName);
        Customer customer = this.userService.getCustomer();
        // not customer
        if (customer == null) {
            store.displayOrders();
        } else {
            store.displayOrdersForCustomer(customer.accountId);
        }
        System.out.println("OK:display_completed");
    }

    public void purchaseOrder(String storeName, String orderId) {
        if (!this.storeService.isStoreExist(storeName)) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            return;
        }

        TransactionLogClosure onPurchaseSuccess = (String itemName, int price, int quantity) -> {
            this.transactionLogger.log(this.userService.getAppUser().getName(), storeName, itemName, price ,quantity);
        };

        DroneOrderCompletionClosure onDroneOrderCompleteSuccess = (String pilotAccountId, int experience) -> {
            this.userService.updatePilotExperience(pilotAccountId, experience);
        };

        Store store = this.storeService.getStoreByName(storeName);
        boolean success = store.purchaseOrder(orderId, onPurchaseSuccess, onDroneOrderCompleteSuccess);
        if (success) {
            this.userService.updateCustomerCredit();
            this.storeService.updateStoreRevenue(storeName, store.getRevenue());
            System.out.println("OK:change_completed");
        }
    }

    public void cancelOrder(String storeName, String orderId) {
        if (!this.storeService.isStoreExist(storeName)) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            return;
        }

        Store store = this.storeService.getStoreByName(storeName);
        boolean success = store.cancelOrder(orderId);
        if (success) {
            System.out.println("OK:change_completed");
        }
    }

    public void requestItem(String storeName, String orderId, String itemName, int quantity, int unitPrice) {
        if (!this.storeService.isStoreExist(storeName)) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            return;
        }
        Store store = this.storeService.getStoreByName(storeName);
        boolean success = store.requestItem(orderId, itemName, quantity, unitPrice);
        if (success) {
            System.out.println("OK:change_completed");
        }
    }
}
