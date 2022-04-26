package edu.gatech.cs6310.userInputFlow;

import com.password4j.Hash;
import com.password4j.Password;
import edu.gatech.cs6310.Role;
import edu.gatech.cs6310.auth.Authentication;
import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.dataManager.UserDataManager;
import edu.gatech.cs6310.logger.Logger;

import java.util.Scanner;

public class AuthUserFlow {

    private final PostgresClientImpl ps;
    private final Logger logger;
    private final Authentication authentication;
    private final UserDataManager userDataManager;

    public AuthUserFlow(PostgresClientImpl ps, Logger logger, Authentication authentication) {
        this.ps = ps;
        this.logger = logger;
        this.authentication = authentication;
        this.userDataManager = new UserDataManager(ps, logger);
    }

    private enum InputType {
        FIRSTNAME {
            public String getDescription() {
                return "first name";
            }
        },
        LASTNAME {
            public String getDescription() {
                return "last name";
            }
        },
        PHONENUMBER {
            public String getDescription() {
                return "phone number";
            }
        },
        RATING {
            public String getDescription() {
                return "customer rating (num)";
            }
        },
        CREDIT {
            public String getDescription() {
                return "customer credit (num)";
            }
        },
        TAX_ID {
            public String getDescription() {
                return "pilot tax id";
            }
        },
        EXPERIENCE {
            public String getDescription() {
                return "pilot experience (num)";
            }
        },
        MONTHS_EMPLOYMENT {
            public String getDescription() {
                return "pilot months of employment (num)";
            }
        },
        SALARY {
            public String getDescription() {
                return "pilot salary (num)";
            }
        };
        public abstract String getDescription();
    }

    public boolean registerCommandLoop(){
        String username = this.getUsernameCommandLoop();
        if (username == null) {
            return false;
        }
        String hashedPassword = this.getHashedPasswordCommandLoop();
        if (hashedPassword == null) {
            return false;
        }
        String roleString = this.getRoleCommandLoop();
        if (roleString == null) {
            return false;
        }
        String firstname = getInputCommandLoop(InputType.FIRSTNAME);
        if (firstname == null) {
            return false;
        }
        String lastname = getInputCommandLoop(InputType.LASTNAME);
        if (lastname == null) {
            return false;
        }
        String phonenumber = getInputCommandLoop(InputType.PHONENUMBER);
        if (phonenumber == null) {
            return false;
        }

        return this.createUser(username, roleString, hashedPassword, firstname, lastname, phonenumber);
    }

    private boolean createUser(String username, String role, String hashedPassword, String firstname, String lastname, String phonenumber) {
        if(role.equalsIgnoreCase(Role.CUSTOMER.toString())){
            String accountId = getCustomerAccountIdCommandLoop();
            int rating = Integer.parseInt(getInputCommandLoop(AuthUserFlow.InputType.RATING));
            int credit = Integer.parseInt(getInputCommandLoop(AuthUserFlow.InputType.CREDIT));
            return this.authentication.createCustomer(username, role, hashedPassword, firstname, lastname, phonenumber, accountId, rating, credit);
        } else if (role.equalsIgnoreCase(Role.PILOT.toString())) {
            String accountId = getPilotAccountIdCommandLoop();
            String licenseId = getPilotLicenseIdCommandLoop();
            String taxId = getInputCommandLoop(AuthUserFlow.InputType.TAX_ID);
            int experience = Integer.parseInt(getInputCommandLoop(AuthUserFlow.InputType.EXPERIENCE));
            int monthsOfEmployment = Integer.parseInt(getInputCommandLoop(AuthUserFlow.InputType.MONTHS_EMPLOYMENT));
            int salary = Integer.parseInt(getInputCommandLoop(AuthUserFlow.InputType.SALARY));
            return this.authentication.createPilot(username, role, hashedPassword, firstname, lastname, phonenumber, accountId, licenseId, taxId, experience, monthsOfEmployment, salary);
        } else{
            return this.authentication.createUser(username, role, hashedPassword, firstname, lastname, phonenumber);
        }
    }

    private String getInputCommandLoop(InputType input) {
        String query = null;
        while (query == null) {
            Scanner s = new Scanner(System.in);
            System.out.print("Enter %s (or type exit to quit): ".formatted(input.getDescription()));
            query = s.nextLine();
            query = query.toLowerCase();
            if (query.equals("exit")) {
                query = null;
                break;
            }
        }
        return query;
    }

    private String getCustomerAccountIdCommandLoop() {
        String accountId = null;
        while (accountId == null) {
            Scanner s = new Scanner(System.in);
            System.out.print("Enter accountId (or type exit to quit): ");
            accountId = s.nextLine();
            accountId = accountId.toLowerCase();
            if (accountId.equals("exit")) {
                accountId = null;
                break;
            }

            if (userDataManager.isAccountIdAvailableForCustomer(accountId)) {
                break;
            } else {
                System.out.println("ERROR:accountId_is_not_available");
                accountId = null;
            }
        }
        return accountId;
    }

    private String getPilotAccountIdCommandLoop() {
        String accountId = null;
        while (accountId == null) {
            Scanner s = new Scanner(System.in);
            System.out.print("Enter accountId (or type exit to quit): ");
            accountId = s.nextLine();
            accountId = accountId.toLowerCase();
            if (accountId.equals("exit")) {
                accountId = null;
                break;
            }

            if (userDataManager.isAccountIdAvailableForPilot(accountId)) {
                break;
            } else {
                System.out.println("ERROR:accountId_is_not_available");
                accountId = null;
            }
        }
        return accountId;
    }

    private String getPilotLicenseIdCommandLoop() {
        String licenseId = null;
        while (licenseId == null) {
            Scanner s = new Scanner(System.in);
            System.out.print("Enter licenseId (or type exit to quit): ");
            licenseId = s.nextLine();
            licenseId = licenseId.toLowerCase();
            if (licenseId.equals("exit")) {
                licenseId = null;
                break;
            }

            if (userDataManager.isLicenseIdAvailableForPilot(licenseId)) {
                break;
            } else {
                System.out.println("ERROR:licenseId_is_not_available");
                licenseId = null;
            }
        }
        return licenseId;
    }

    private String getUsernameCommandLoop() {
        String username = null;
        while (username == null) {
            Scanner s = new Scanner(System.in);
            System.out.print("Enter username (or type exit to quit): ");
            username = s.nextLine();
            username = username.toLowerCase();
            if (username.equals("exit")) {
                username = null;
                break;
            }

            if (this.authentication.isUsernameAvailable(username)) {
                break;
            } else {
                System.out.println("ERROR:username_is_not_available");
                username = null;
            }
        }
        return username;
    }

    private String getHashedPasswordCommandLoop() {
        String password = null;
        while (password == null) {
            Scanner s = new Scanner(System.in);
            System.out.print("Enter password (or type exit to quit): ");
            password = s.nextLine();
            password = password.toLowerCase();

            if (password.equals("exit")) {
                password = null;
                break;
            }
        }
        if (password != null) {
            Hash hash = Password.hash(password).withArgon2();
            String hashed_password = hash.getResult();
            return hashed_password;
        } else {
            return password;
        }
    }

    private String getRoleCommandLoop() {
        String roleString = null;
        while (roleString == null) {
            Scanner s = new Scanner(System.in);
            System.out.print("Enter role (type list_role to show role types or exit to quit): ");
            roleString = s.nextLine();
            roleString = roleString.toLowerCase();

            if (roleString.equals("exit")) {
                roleString = null;
                break;
            } else if (roleString.equals("list_role")) {
                this.printRoles();
                roleString = null;
            } else {
                try {
                   Role role = Role.valueOf(roleString.toUpperCase());
                   if (role == Role.GUEST) {
                       System.out.println("ERROR:cannot_create_account_with_GUEST_role");
                       roleString = null;
                   } else {
                       roleString = role.toString();
                   }
                } catch (Exception e) {
                    System.out.println("role " + roleString + " NOT acknowledged");
                    roleString = null;
                }
            }
        }
        return roleString;
    }

    private void printRoles() {
        Role[] roles = new Role[] { Role.ADMIN, Role.STORE_OWNER, Role.CUSTOMER, Role.PILOT };
        System.out.println(java.util.Arrays.asList(roles));
    }
}
