package edu.gatech.cs6310;

import edu.gatech.cs6310.auth.Authentication;
import edu.gatech.cs6310.auth.Authorization;
import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.LogQueryManager;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.logger.TransactionLogger;
import edu.gatech.cs6310.userInputFlow.AuthUserFlow;
import edu.gatech.cs6310.userInputFlow.LoginFlow;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

// Class to centralized shared modules and process user interface input
public class AppDelegate {

    private final ConfigurationService configService;
    private final StoreService storeService;
    private final DeliveryService deliveryServiceSystem;
    private final OrderService orderService;
    private final UserService userService;
    private final PostgresClientImpl ps = new PostgresClientImpl();
    private final Logger logger;
    private final TransactionLogger transactionLogger;
    private final Authentication authentication;
    private final Authorization authorization;
    private final LogQueryManager logQueryManager;
    private final Menu menu;

    public AppDelegate() {
        // initialize
        this.logger = new Logger(this.ps);
        this.transactionLogger = new TransactionLogger(this.ps);
        this.logQueryManager = new LogQueryManager(this.ps);
        this.authentication = new Authentication(this.ps, this.logger);
        this.authorization = new Authorization(this.ps, this.logger);
        this.menu = new Menu(this.authorization);
        this.userService = new UserService(this.ps, this.logger, this.authentication);
        this.storeService = new StoreService(this.ps, this.logger, this.userService);
        this.configService = new ConfigurationService(this.ps, this.logger, this.storeService, this.userService);
        this.deliveryServiceSystem = new DeliveryService(this.ps, this.logger, this.storeService, this.configService, this.authentication);
        this.orderService = new OrderService(this.ps, this.logger, this.transactionLogger, this.storeService, this.userService);
    }

    public String getDisplayName() {
        return this.authentication.currentAppUser.getFirstname();
    }

    public void execute(Instruction instruction, String[] tokens) {
        if(!authorization.hasPermission(authentication.currentAppUser, instruction)) {
            System.out.println("ERROR:does_not_have_permission_for_this_action");
            return;
        }

        switch (instruction) {
            case CREATE_ACCOUNT:
                if(!authentication.loggedIn){
                    AuthUserFlow flow = new AuthUserFlow(this.ps, this.logger, this.authentication);
                    flow.registerCommandLoop();
                    if (Role.valueOf(this.authentication.currentAppUser.getRole()) == Role.PILOT) {
                        this.deliveryServiceSystem.addPilot((DronePilot)this.authentication.currentUser);
                    }
                    this.execute(Instruction.MENU, new String[0]);
                } else {
                    System.out.println("ERROR:please_log_out_before_register");
                }
                return;
            case LOGIN:
                if(!authentication.loggedIn){
                    if (authentication.timeout&&authentication.isTimeout()){
                        System.out.println("ERROR:login_timeout_please_wait");
                        return;
                    }
                    LoginFlow flow = new LoginFlow(this.authentication);
                    flow.loginCommandLoop();
                    this.execute(Instruction.MENU, new String[0]);
                } else {
                    System.out.println("ERROR:it_is_already_logged_in");
                }
                return;

            case LOGOUT:
                if(authentication.loggedIn){
                    authentication.logout();
                    this.execute(Instruction.MENU, new String[0]);
                } else {
                    System.out.println("ERROR:it_is_not_logged_in");
                }
                return;
            case MENU:
                this.menu.printMenu(Role.valueOf(this.authentication.currentAppUser.getRole()));
                return;
            case QUERY_LOGS:
                String commandType = tokens[1];
                String level = tokens[2];
                String pattern = tokens[3];
                logQueryManager.queryLogs(commandType, level, pattern);
                System.out.println("OK:finished_querying_logs\n");
                return;
            case LIST_CONFIGURATION_TYPES:
                this.configService.printConfigurationTypes();
                return;
            case VIEW_SYSTEM_CONFIGURATIONS:
                this.configService.printSystemConfigurations();
                return;
            case VIEW_STORE_CONFIGURATIONS:
                this.configService.printStoreConfigurations(tokens[1]);
                return;
            case VIEW_ITEM_CONFIGURATIONS:
                this.configService.printStoreItemConfigurations(tokens[1], tokens[2]);
                return;
            case LIST_ALL_CONFIGURATIONS:
                this.configService.printAllConfigurations();
                return;
            case ENABLE_CONFIGURATION:
                this.configService.activateConfiguration(tokens[1]);
                return;
            case DISABLE_CONFIGURATION:
                this.configService.deactivateConfiguration(tokens[1]);
                return;
            case DELETE_CONFIGURATION:
                this.configService.deleteConfiguration(tokens[1]);
                return;
            case REMOVE_CONFIGURATION_FROM_STORE:
                this.configService.removeConfiguration(tokens[1], tokens[2]);
                return;
            case REMOVE_CONFIGURATION_FROM_ITEM:
                this.configService.removeConfiguration(tokens[1], tokens[2], tokens[3]);
                return;
            case ADD_CONFIGURATION_TO_STORE:
                this.configService.addConfiguration(tokens[1], tokens[2]);
                return;
            case ADD_CONFIGURATION_TO_ITEM:
                this.configService.addConfiguration(tokens[1], tokens[2], tokens[3]);
                return;
            case CREATE_CATEGORY_CONFIGURATION_FOR_SYSTEM:
                this.configService.createConfiguration(tokens[1], ConfigurationType.CATEGORY_RESTRICTION, null, 0, tokens[2]);
                return;
            case CREATE_CATEGORY_CONFIGURATION_FOR_STORE:
                this.configService.createConfiguration(tokens[1], ConfigurationType.CATEGORY_RESTRICTION, tokens[2], 0, tokens[3]);
                return;
            case CREATE_QUOTA_CONFIGURATION_FOR_STORE:
                this.configService.createConfiguration(tokens[1], ConfigurationType.QUOTA_LIMIT, tokens[2], Integer.parseInt(tokens[3]), null);
                return;
            case MAKE_STORE:
                this.deliveryServiceSystem.createStore(tokens[1], Integer.parseInt(tokens[2]));
                return;
            case DISPLAY_STORES:
                this.deliveryServiceSystem.displayStores();
                return;
            case SELL_ITEM:
                this.deliveryServiceSystem.addItem(tokens[1], tokens[2], Integer.parseInt(tokens[3]), tokens[4]);
                return;
            case DISPLAY_ITEMS:
                this.deliveryServiceSystem.displayItems(tokens[1]);
                return;
            case DISPLAY_PROFILE:
                this.userService.displayProfile();
                return;
            case DISPLAY_PROFILES:
                this.userService.displayProfiles();
                return;
            case VIEW_PURCHASE_HISTORY:
                this.userService.printUserPurchaseHistory();
                return;
            case VIEW_STORE_STATS:
                this.storeService.viewStoreStats(tokens[1]);
                return;
            case DISPLAY_CUSTOMERS:
                this.userService.displayCustomers();
                return;
            case START_ORDER:
                this.orderService.startOrder(tokens[1], tokens[2], tokens[3]);
                return;
            case DISPLAY_ORDERS:
                this.orderService.displayOrders(tokens[1]);
                return;
            case REQUEST_ITEM:
                this.orderService.requestItem(tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]));
                return;
            case PURCHASE_ORDER:
                this.orderService.purchaseOrder(tokens[1], tokens[2]);
                return;
            case CANCEL_ORDER:
                this.orderService.cancelOrder(tokens[1], tokens[2]);
                return;
            case FLY_DRONE:
                this.deliveryServiceSystem.flyDrone(tokens[1], tokens[2], tokens[3]);
                return;
            case DISPLAY_PILOTS:
                this.userService.displayPilots();
                return;
            case DISPLAY_DRONES:
                this.storeService.displayDrones(tokens[1]);
                return;
            case MAKE_DRONE:
                this.storeService.createDrone(tokens[1], tokens[2], Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
                return;
            default:
                return;
        }
    }
}
