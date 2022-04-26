package edu.gatech.cs6310;

public enum Instruction {

    MENU {
        @Override
        public String getDescription() {
            return "menu";
        }
    },
    MAKE_STORE {
        @Override
        public String getDescription() {
            return "make_store [store_name(string)] [revenue(int)]";
        }
    },
    DISPLAY_STORES {
        @Override
        public String getDescription() {
            return "display_stores";
        }
    },
    SELL_ITEM{
        @Override
        public String getDescription() {
            return "sell_item [store_name(string) item_name(string) weight(int) category(string)]";
        }
    },
    DISPLAY_ITEMS{
        @Override
        public String getDescription() {
            return "display_items [store_name(string)]";
        }
    },
    DISPLAY_PILOTS{
        @Override
        public String getDescription() {
            return "display_pilots";
        }
    },
    MAKE_DRONE {
        @Override
        public String getDescription() {
            return "make_drone [store_name(string) drone_id(string) capacity(int) fuel(int)]";
        }
    },
    DISPLAY_DRONES{
       @Override
        public String getDescription() {
            return "display_drones [store_name(string)]";
        }
    },
    FLY_DRONE{
        @Override
        public String getDescription() {
            return "fly_drone [store_name(string) drone_id(string) pilot_id(string)]";
        }
    },
    DISPLAY_CUSTOMERS{
        @Override
        public String getDescription() {
            return "display_customers";
        }
    },
    START_ORDER{
        @Override
        public String getDescription() {
            return "start_order [store_name(string) order_id(string) drone_id(string)]";
        }
    },
    DISPLAY_ORDERS{
        @Override
        public String getDescription() {
            return "display_orders [store_name(string)]";
        }
    },
    REQUEST_ITEM{
        @Override
        public String getDescription() {
            return "request_item [store_name(string) order_id(string) item_name(string) quantity(int) unit_price(int)]";
        }
    },
    PURCHASE_ORDER{
        @Override
        public String getDescription() {
            return "purchase_order [store_name(string) order_id(string)]";
        }
    },
    CANCEL_ORDER{
        @Override
        public String getDescription() {
            return "cancel_order [store_name(string) order_id(string)]";
        }
    },
    CREATE_ACCOUNT {
        @Override
        public String getDescription() {
            return "create_account";
        }
    },
    LOGIN{
        @Override
        public String getDescription() {
            return "login";
        }
    },
    LOGOUT{
        @Override
        public String getDescription() {
            return "logout";
        }
    },
    VIEW_STORE_STATS{
        @Override
        public String getDescription() {
            return "view_store_stats [store_name(string)]";
        }
    },
    VIEW_PURCHASE_HISTORY{
        @Override
        public String getDescription() {
            return "view_purchase_history";
        }
    },
    ADD_CONFIGURATION_TO_STORE{
        @Override
        public String getDescription() {
            return "add_configuration_to_store [config_name(string) store_name(string)]";
        }
    },
    ADD_CONFIGURATION_TO_ITEM{
        @Override
        public String getDescription() {
            return "add_configuration_to_item [config_name(string) store_name(string) item_name(string)]";
        }
    },
    REMOVE_CONFIGURATION_FROM_STORE{
        @Override
        public String getDescription() {
            return "remove_configuration_from_store [config_name(string) store_name(string)]";
        }
    },
    REMOVE_CONFIGURATION_FROM_ITEM{
        @Override
        public String getDescription() {
            return "remove_configuration_from_item [config_name(string) store_name(string) item_name(string)]";
        }
    },
    LIST_CONFIGURATION_TYPES {
        @Override
        public String getDescription() {
            return "list_configuration_types";
        }
    },
    LIST_ALL_CONFIGURATIONS {
        @Override
        public String getDescription() {
            return "list_all_configurations-list all configurations in system";
        }
    },
    VIEW_SYSTEM_CONFIGURATIONS {
        @Override
        public String getDescription() {
            return "view_system_configurations-view configurations that are applied globally";
        }
    },
    VIEW_STORE_CONFIGURATIONS {
        @Override
        public String getDescription() {
            return "view_store_configurations [store_name(string)]";
        }
    },
    VIEW_ITEM_CONFIGURATIONS {
        @Override
        public String getDescription() {
            return "view_item_configurations [store_name(string) item_name(string)]";
        }
    },
    CREATE_CATEGORY_CONFIGURATION_FOR_SYSTEM{
        @Override
        public String getDescription() {
            return "create_category_configuration_for_system [configuration_name(string) category(string)]";
        }
    },
    CREATE_CATEGORY_CONFIGURATION_FOR_STORE{
        @Override
        public String getDescription() {
            return "create_category_configuration_for_store [configuration_name(string) store_name(string) category(string)]";
        }
    },
    CREATE_QUOTA_CONFIGURATION_FOR_STORE{
        @Override
        public String getDescription() {
            return "create_quota_configuration_for_store [configuration_name(string) store_name(string) limit(int)]";
        }
    },
    DELETE_CONFIGURATION{
        @Override
        public String getDescription() {
            return "delete_configuration [configuration_name(string)]";
        }
    },
    ENABLE_CONFIGURATION{
        @Override
        public String getDescription() {
            return "enable_configuration [configuration_name(string)]";
        }
    },
    DISABLE_CONFIGURATION{
        @Override
        public String getDescription() {
            return "disable_configuration [configuration_name(string)]";
        }
    },
    QUERY_LOGS {
        @Override
        public String getDescription() {
            return "query_logs [command_type(string)-if applicable level(string)-if applicable pattern(string)]";
        }

        @Override
        public String[] getExtraDetails() {
            return new String[] {
                    "command_type can be CREATE_ACCOUNT, LOGIN, HAS_PERMISSION, etc. or Wildcard *",
                    "level can be: INFO, ERROR, WARN, DEBUG or Wildcard *",
                    "pattern can be any string or Wildcard *"
            };
        }

        @Override
        public String[] getExamples() {
            return new String[] {
                    "Query ALL CREATE_ACCOUNT logs: query_logs,CREATE_ACCOUNT,*,*",
                    "Query ALL INFO logs: query_logs,*,INFO,*",
                    "Query ALL logs with with \"success\" in the message: query_logs,*,*,*success*",
                    "Query ALL logs starting with \"success*\" in the message: query_logs,*,*,success*",
                    "Query ALL logs ending with \"*success\" in the message: query_logs,*,*,*success"
            };
        }
    },
    DISPLAY_PROFILE {
        @Override
        public String getDescription() {
            return "display_profile - display current user profile";
        }
    },
    DISPLAY_PROFILES {
        @Override
        public String getDescription() {
            return "display_profiles";
        }
    },
    STOP{
        @Override
        public String getDescription() {
            return "stop";
        }
    };

    public abstract String getDescription();

    public String[] getExtraDetails() {
        return new String[0];
    }

    public String[] getExamples() {
        return new String[0];
    }
}
