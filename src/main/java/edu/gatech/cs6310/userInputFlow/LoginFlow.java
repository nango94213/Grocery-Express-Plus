package edu.gatech.cs6310.userInputFlow;

import com.password4j.Hash;
import com.password4j.Password;
import edu.gatech.cs6310.auth.Authentication;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class LoginFlow {
    private final Authentication authentication;

    public LoginFlow(Authentication authentication) {
        this.authentication = authentication;
    }

    public boolean loginCommandLoop(){

        int count = 5;
        Boolean loggedIn = false;

        while (!loggedIn && count > 0) {
            count --;
            String username = this.getUsernameCommandLoop();
            if (username == null) {
                return false;
            }
            String hashedPassword = this.getHashedPasswordCommandLoop();
            if (hashedPassword == null) {
                return false;
            }

            loggedIn = this.authentication.login(username, hashedPassword);
        }

        if (loggedIn) {
            return true;
        }
        if (!loggedIn && count == 0) {
            authentication.timeout = true;
            authentication.timestamp = Timestamp.from(Instant.now());
            System.out.println("INFO:too_many_attempts_please_try_again_later");
        }
        return false;
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
}
