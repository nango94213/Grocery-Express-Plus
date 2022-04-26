-- START AUTHORIZATION
\echo INSERT default roles
INSERT INTO role(role_name) VALUES ('ADMIN'),
                                   ('STORE_OWNER'),
                                   ('CUSTOMER'),
                                   ('GUEST'),
                                   ('PILOT');

\echo INSERT default instructions
INSERT INTO instruction(inst_name) VALUES ('MENU'),
                                            ('MAKE_STORE'),
                                            ('DISPLAY_STORES'),
                                            ('SELL_ITEM'),
                                            ('DISPLAY_ITEMS'),
                                            ('DISPLAY_PILOTS'),
                                            ('MAKE_DRONE'),
                                            ('DISPLAY_DRONES'),
                                            ('FLY_DRONE'),
                                            ('DISPLAY_CUSTOMERS'),
                                            ('START_ORDER'),
                                            ('DISPLAY_ORDERS'),
                                            ('REQUEST_ITEM'),
                                            ('PURCHASE_ORDER'),
                                            ('CANCEL_ORDER'),
                                            ('CREATE_ACCOUNT'),
                                            ('LOGIN'),
                                            ('LOGOUT'),
                                            ('DISPLAY_PROFILE'),
                                            ('DISPLAY_PROFILES'),
                                            ('VIEW_STORE_STATS'),
                                            ('VIEW_PURCHASE_HISTORY'),
                                            ('ADD_CONFIGURATION_TO_STORE'),
                                            ('ADD_CONFIGURATION_TO_ITEM'),
                                            ('REMOVE_CONFIGURATION_FROM_STORE'),
                                            ('REMOVE_CONFIGURATION_FROM_ITEM'),
                                            ('LIST_CONFIGURATION_TYPES'),
                                            ('LIST_ALL_CONFIGURATIONS'),
                                            ('VIEW_SYSTEM_CONFIGURATIONS'),
                                            ('VIEW_STORE_CONFIGURATIONS'),
                                            ('VIEW_ITEM_CONFIGURATIONS'),
                                            ('CREATE_CATEGORY_CONFIGURATION_FOR_SYSTEM'),
                                            ('CREATE_CATEGORY_CONFIGURATION_FOR_STORE'),
                                            ('CREATE_QUOTA_CONFIGURATION_FOR_STORE'),
                                            ('DELETE_CONFIGURATION'),
                                            ('ENABLE_CONFIGURATION'),
                                            ('DISABLE_CONFIGURATION'),
                                            ('QUERY_LOGS'),
                                            ('STOP');

\echo INSERT default role ADMIN permissions
INSERT INTO rolepermission(role_name, inst_name) VALUES ('ADMIN', 'MENU'),
                                                        ('ADMIN', 'DISPLAY_STORES'),
                                                        ('ADMIN', 'DISPLAY_ITEMS'),
                                                        ('ADMIN', 'DISPLAY_PILOTS'),
                                                        ('ADMIN', 'DISPLAY_DRONES'),
                                                        ('ADMIN', 'FLY_DRONE'),
                                                        ('ADMIN', 'DISPLAY_CUSTOMERS'),
                                                        ('ADMIN', 'LOGOUT'),
                                                        ('ADMIN', 'DISPLAY_PROFILE'),
                                                        ('ADMIN', 'DISPLAY_PROFILES'),
                                                        ('ADMIN', 'VIEW_STORE_STATS'),
                                                        ('ADMIN', 'ADD_CONFIGURATION_TO_STORE'),
                                                        ('ADMIN', 'ADD_CONFIGURATION_TO_ITEM'),
                                                        ('ADMIN', 'REMOVE_CONFIGURATION_FROM_STORE'),
                                                        ('ADMIN', 'REMOVE_CONFIGURATION_FROM_ITEM'),
                                                        ('ADMIN', 'LIST_CONFIGURATION_TYPES'),
                                                        ('ADMIN', 'LIST_ALL_CONFIGURATIONS'),
                                                        ('ADMIN', 'VIEW_SYSTEM_CONFIGURATIONS'),
                                                        ('ADMIN', 'VIEW_STORE_CONFIGURATIONS'),
                                                        ('ADMIN', 'VIEW_ITEM_CONFIGURATIONS'),
                                                        ('ADMIN', 'CREATE_CATEGORY_CONFIGURATION_FOR_SYSTEM'),
                                                        ('ADMIN', 'CREATE_CATEGORY_CONFIGURATION_FOR_STORE'),
                                                        ('ADMIN', 'DELETE_CONFIGURATION'),
                                                        ('ADMIN', 'ENABLE_CONFIGURATION'),
                                                        ('ADMIN', 'DISABLE_CONFIGURATION'),
                                                        ('ADMIN', 'QUERY_LOGS'),
                                                        ('ADMIN', 'STOP');
                                                        
\echo INSERT default role STORE_OWNER permissions
INSERT INTO rolepermission(role_name, inst_name) VALUES ('STORE_OWNER', 'MENU'),
                                                        ('STORE_OWNER', 'MAKE_STORE'),
                                                        ('STORE_OWNER', 'DISPLAY_STORES'),
                                                        ('STORE_OWNER', 'SELL_ITEM'),
                                                        ('STORE_OWNER', 'DISPLAY_ITEMS'),
                                                        ('STORE_OWNER', 'DISPLAY_PILOTS'),
                                                        ('STORE_OWNER', 'MAKE_DRONE'),
                                                        ('STORE_OWNER', 'DISPLAY_DRONES'),
                                                        ('STORE_OWNER', 'DISPLAY_CUSTOMERS'),
                                                        ('STORE_OWNER', 'LOGOUT'),
                                                        ('STORE_OWNER', 'CANCEL_ORDER'),
                                                        ('STORE_OWNER', 'DISPLAY_ORDERS'),
                                                        ('STORE_OWNER', 'DISPLAY_PROFILE'),
                                                        ('STORE_OWNER', 'VIEW_STORE_STATS'),
                                                        ('STORE_OWNER', 'DELETE_CONFIGURATION'),
                                                        ('STORE_OWNER', 'DISABLE_CONFIGURATION'),
                                                        ('STORE_OWNER', 'ENABLE_CONFIGURATION'),
                                                        ('STORE_OWNER', 'VIEW_STORE_CONFIGURATIONS'),
                                                        ('STORE_OWNER', 'VIEW_ITEM_CONFIGURATIONS'),
                                                        ('STORE_OWNER', 'ADD_CONFIGURATION_TO_STORE'),
                                                        ('STORE_OWNER', 'ADD_CONFIGURATION_TO_ITEM'),
                                                        ('STORE_OWNER', 'REMOVE_CONFIGURATION_FROM_STORE'),
                                                        ('STORE_OWNER', 'REMOVE_CONFIGURATION_FROM_ITEM'),
                                                        ('STORE_OWNER', 'LIST_CONFIGURATION_TYPES'),
                                                        ('STORE_OWNER', 'LIST_ALL_CONFIGURATIONS'),
                                                        ('STORE_OWNER', 'CREATE_CATEGORY_CONFIGURATION_FOR_STORE'),
                                                        ('STORE_OWNER', 'CREATE_QUOTA_CONFIGURATION_FOR_STORE'),
                                                        ('STORE_OWNER', 'QUERY_LOGS'),
                                                        ('STORE_OWNER', 'STOP');
                                                        
\echo INSERT default role CUSTOMER permissions
INSERT INTO rolepermission(role_name, inst_name) VALUES ('CUSTOMER', 'MENU'),
                                                        ('CUSTOMER', 'DISPLAY_STORES'),
                                                        ('CUSTOMER', 'DISPLAY_ITEMS'),
                                                        ('CUSTOMER', 'START_ORDER'),
                                                        ('CUSTOMER', 'PURCHASE_ORDER'),
                                                        ('CUSTOMER', 'CANCEL_ORDER'),
                                                        ('CUSTOMER', 'DISPLAY_ORDERS'),
                                                        ('CUSTOMER', 'REQUEST_ITEM'),
                                                        ('CUSTOMER', 'LOGOUT'),
                                                        ('CUSTOMER', 'DISPLAY_PROFILE'),
                                                        ('CUSTOMER', 'VIEW_PURCHASE_HISTORY'),
                                                        ('CUSTOMER', 'STOP');
                                                        
\echo INSERT default role GUEST permissions
INSERT INTO rolepermission(role_name, inst_name) VALUES ('GUEST', 'MENU'),
                                                        ('GUEST', 'DISPLAY_ITEMS'),
                                                        ('GUEST', 'DISPLAY_STORES'),
                                                        ('GUEST', 'CREATE_ACCOUNT'),
                                                        ('GUEST', 'LOGIN'),
                                                        ('GUEST', 'STOP');
                                                        
\echo INSERT default role PILOT permissions
INSERT INTO rolepermission(role_name, inst_name) VALUES ('PILOT', 'MENU'),
                                                        ('PILOT', 'DISPLAY_ITEMS'),
                                                        ('PILOT', 'DISPLAY_DRONES'),
                                                        ('PILOT', 'DISPLAY_PROFILE'),
                                                        ('PILOT', 'LOGOUT'),
                                                        ('PILOT', 'STOP');

-- END AUTHORIZATION

-- START AUTHENTICATION

\echo INSERT default appusers
INSERT INTO appuser(user_id, role_name, password, first_name, last_name, phone_number) VALUES ('admin', 'ADMIN', '$argon2id$v=19$m=12,t=20,p=2$$m/+Ddr/17GchlrwY2aZYY1+c+oAQ4LVKd1bbN8xS1as', 'admin', 'admin', '000-0000');
INSERT INTO appuser(user_id, role_name, password, first_name, last_name, phone_number) VALUES ('defaultUserName', 'GUEST', '$argon2id$v=19$m=12,t=20,p=2$$m/+Ddr/17GchlrwY2aZYY1+c+oAQ4LVKd1bbN8xS1as', 'guest', 'guest', '000-0000');
INSERT INTO appuser(user_id, role_name, password, first_name, last_name, phone_number) VALUES ('test', 'STORE_OWNER', '$argon2id$v=19$m=12,t=20,p=2$$m/+Ddr/17GchlrwY2aZYY1+c+oAQ4LVKd1bbN8xS1as', 'test', 'test', '000-0000');

-- END AUTHENTICATION