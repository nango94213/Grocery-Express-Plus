package edu.gatech.cs6310.auth;

import edu.gatech.cs6310.Instruction;
import edu.gatech.cs6310.Role;
import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.models.AppUser;
import edu.gatech.cs6310.models.Customer;
import edu.gatech.cs6310.models.Pilot;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Authentication {
    //indicate if user logged in
    public Boolean loggedIn;
    public AppUser currentAppUser;
    public edu.gatech.cs6310.User currentUser;
    private AppUser guestUser = new AppUser("defaultUserName", Role.GUEST.toString(),
            "", "guest", "guest", "");

    private PostgresClientImpl ps;
    private Logger logger;
    public Timestamp timestamp;
    public Boolean timeout;

    public Authentication (PostgresClientImpl ps, Logger logger) {
        this.ps = ps;
        this.logger = logger;
        this.currentAppUser = guestUser;
        this.loggedIn = false;
        this.timestamp = Timestamp.from(Instant.now());
        this.timeout = false;
    }

    public boolean isUsernameAvailable(String username) {
        List<edu.gatech.cs6310.models.AppUser> appUsers = ps.selectWhere("AppUser", "name", username);
        boolean exists = appUsers.size() > 0;
        if(exists) {
            logger.info("CREATE_ACCOUNT", "AppUser %s already exist.".formatted(username));
            return false;
        }
        return true;
    }

    public boolean isTimeout(){
        //check timeout
        long diff = Timestamp.from(Instant.now()).getTime() - timestamp.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        if(seconds<=10){
            return true;
        }else{
            return false;
        }
    }

    public boolean createCustomer(String username, String role, String hashedPassword, String firstname, String lastname, String phonenumber, String accountId, int rating, int credit) {
        AppUser newAppUser = new AppUser(username, role, hashedPassword, firstname, lastname, phonenumber);
        try {
            ps.save(newAppUser);
            logger.info(Instruction.CREATE_ACCOUNT.toString(), "%s user added.".formatted(username));
            createCustomer(accountId, username, rating, credit);
            currentUser = new edu.gatech.cs6310.Customer(username, firstname,
                    lastname, phonenumber, rating, credit);
            currentAppUser = newAppUser;
            loggedIn = true;
            System.out.println("OK:change_completed");
            return true;
        } catch (Exception e){
            logger.error(Instruction.CREATE_ACCOUNT.toString(), "Unable to add %s user.".formatted(username));
            ps.deleteWhere("AppUser", "name", username);
            System.out.println("ERROR:create_account_failed");
            return false;
        }
    }

    public boolean createPilot(String username, String role, String hashedPassword, String firstname, String lastname, String phonenumber, String accountId,
                               String licenseId, String taxId, int experience, int monthsOfEmployment, int salary) {
        AppUser newAppUser = new AppUser(username, role, hashedPassword, firstname, lastname, phonenumber);
        try {
            ps.save(newAppUser);
            logger.info(Instruction.CREATE_ACCOUNT.toString(), "%s user added.".formatted(username));
            createPilot(accountId, username, licenseId, taxId, experience, monthsOfEmployment, salary);
            currentUser = new edu.gatech.cs6310.DronePilot(accountId, firstname,
                    lastname, phonenumber, taxId, licenseId, experience);
            currentAppUser = newAppUser;
            loggedIn = true;
            System.out.println("OK:change_completed");
            return true;
        } catch (Exception e){
            logger.error(Instruction.CREATE_ACCOUNT.toString(), "Unable to add %s user.".formatted(username));
            ps.deleteWhere("AppUser", "name", username);
            System.out.println("ERROR:create_account_failed");
            return false;
        }
    }

    public boolean createUser(String username, String role, String hashedPassword, String firstname, String lastname, String phonenumber) {
        AppUser newAppUser = new AppUser(username, role, hashedPassword, firstname, lastname, phonenumber);
        try{
            ps.save(newAppUser);
            logger.info(Instruction.CREATE_ACCOUNT.toString(), "%s user added.".formatted(username));
            loggedIn = true;
            currentAppUser = newAppUser;
            System.out.println("OK:change_completed");
            return true;
        } catch (Exception e){
            logger.error(Instruction.CREATE_ACCOUNT.toString(), "Unable to add %s user.".formatted(username));
            ps.deleteWhere("AppUser", "name", username);
            System.out.println("ERROR:create_account_failed");
            return false;
        }
    }

    private boolean createCustomer(String accountId, String appUserId, int rating, int credit) {
        Customer newCustomer = new Customer(accountId, appUserId, rating, credit);
        try{
            ps.save(newCustomer);
            System.out.println("OK:customer_created");
            logger.info(Instruction.CREATE_ACCOUNT.toString(), "customer added.");
            return true;
        } catch (Exception e){
            System.out.println("ERROR:customer_create_failed");
            logger.error(Instruction.CREATE_ACCOUNT.toString().toString(), "Unable to add customer.");
            return false;
        }
    }

    private boolean createPilot(String accountId, String appUserId, String licenseId, String taxIdentifier, int experience, int monthsOfEmployment, int salary) {
        Pilot newAppUser = new Pilot(accountId, appUserId, licenseId, taxIdentifier, experience, monthsOfEmployment, salary);
        try{
            ps.save(newAppUser);
            System.out.println("OK:pilot_created");
            logger.info(Instruction.CREATE_ACCOUNT.toString(), "pilot added.");
            return true;
        } catch (Exception e){
            System.out.println("ERROR:pilot_create_failed");
            logger.error(Instruction.CREATE_ACCOUNT.toString().toString(), "Unable to add pilot.");
            return false;
        }
    }

    public boolean login(String username, String hashedPassword) {
        List<AppUser> appUsers = ps.selectWhere("AppUser", "name", username, "password", hashedPassword);
        Boolean appUserExists = appUsers.size() > 0;
        timeout = false;
        if(appUserExists){
            AppUser appUser = appUsers.get(0);
            if(appUser.getRole().equalsIgnoreCase(Role.CUSTOMER.toString())){
                List<Customer> customers = ps.selectWhere("Customer", "authUserId", appUser.getName());
                Boolean customerExists = customers.size() > 0;
                if(customerExists){
                    Customer customer = customers.get(0);
                    System.out.println("OK:login_success");
                    logger.info("LOGIN", "AppUser %s logged in successfully.".formatted(username));
                    loggedIn = true;
                    currentAppUser = appUser;
                    currentUser = new edu.gatech.cs6310.Customer(appUser.getName(), appUser.getFirstname(),
                            appUser.getLastname(), appUser.getPhonenumber(), customer.getRating(), customer.getCredit());
                    return true;
                } else {
                    System.out.println("ERROR:please_try_again");
                    logger.error("LOGIN", "AppUser %s log in failed.".formatted(username));
                    return false;
                }
            } else if (appUser.getRole().equalsIgnoreCase(Role.PILOT.toString())) {
                List<Pilot> pilots = ps.selectWhere("Pilot", "authUserId", appUser.getName());
                Boolean pilotExists = pilots.size() > 0;
                if(pilotExists){
                    Pilot pilot = pilots.get(0);
                    System.out.println("OK:login_success");
                    logger.info("LOGIN", "AppUser %s logged in successfully.".formatted(username));
                    loggedIn = true;
                    currentAppUser = appUser;
                    currentUser = new edu.gatech.cs6310.DronePilot(pilot.getAccountId(), appUser.getFirstname(),
                            appUser.getLastname(), appUser.getPhonenumber(), pilot.getTaxIdentifier(), pilot.getLicenseId(),
                            pilot.getExperience());
                    return true;
                } else {
                    System.out.println("ERROR:please_try_again");
                    logger.error("LOGIN", "AppUser %s log in failed.".formatted(username));
                    return false;
                }
            } else {
                System.out.println("OK:login_success");
                logger.info("LOGIN", "AppUser %s logged in successfully.".formatted(username));
                loggedIn = true;
                currentAppUser = appUser;
                return true;
            }
        }else{
            System.out.println("ERROR:please_try_again");
            logger.error("LOGIN", "AppUser %s log in failed.".formatted(username));
            return false;
        }
    }

    public void logout(){
        loggedIn = false;
        currentAppUser = guestUser;
        currentUser = null;
        System.out.println("OK:logout_success");
        logger.info("LOGOUT", "successfully sign out");
    }
}
