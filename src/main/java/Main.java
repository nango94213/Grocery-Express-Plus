import edu.gatech.cs6310.*;
import edu.gatech.cs6310.auth.Authentication;
import edu.gatech.cs6310.auth.Authorization;
import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        AppDelegate delegate = new AppDelegate();
        System.out.println("======================================================");
        System.out.println("== Welcome to the Grocery Express Delivery Service! ==");
        System.out.println("======================================================");
        delegate.execute(Instruction.MENU, new String[0]);
        startCommandLoop(delegate);
    }

    public static void startCommandLoop(AppDelegate delegate) {
        Scanner commandLineInput = new Scanner(System.in);
        String wholeInputLine;
        String[] tokens;
        final String DELIMITER = ",";
        while (true) {
            try {
                String displayName = delegate.getDisplayName();
                System.out.print(displayName + "@Delivery Service System % ");

                // Determine the next command and echo it to the monitor for testing purposes
                wholeInputLine = commandLineInput.nextLine();
                tokens = wholeInputLine.split(DELIMITER);
                System.out.println("> " + wholeInputLine);

                if (tokens[0].startsWith("//")) {
                    // do nothing
                } else {
                    try {
                        Instruction instruction = Instruction.valueOf(tokens[0].toUpperCase());
                        if (instruction == Instruction.STOP) {
                            System.out.println("stop acknowledged");
                            break;
                        } else {
                            delegate.execute(instruction, tokens);
                        }
                    } catch (Exception e) {
                        System.out.println("error " + e.getMessage());
                        System.out.println("command " + tokens[0] + " NOT acknowledged");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println();
            }
        }
        System.out.println("simulation terminated");
        commandLineInput.close();
    }
}
