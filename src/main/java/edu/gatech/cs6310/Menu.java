package edu.gatech.cs6310;

import edu.gatech.cs6310.auth.Authorization;
import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.models.RolePermission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Menu {

    private final Authorization authorization;

    Menu(Authorization authorization) {
        this.authorization = authorization;
    }

    public void printMenu(Role role) {
        List<Instruction> instructions = this.getMenu(role);

        StringBuilder menu = new StringBuilder();
        menu.append("These are the instructions that you are allowed to perform:\n");
        menu.append("====== Start of Menu ======\n");
        for (Instruction inst : instructions) {
            menu.append("--- " + inst.getDescription());

            String[] extraDetails = inst.getExtraDetails();

            menu.append(extraDetails.length == 0 ? "" : "\n      Where:");

            for (int i = 0; i < extraDetails.length; i++) {
                menu.append("\n          > " + extraDetails[i]);
            }

            String[] examples = inst.getExamples();

            menu.append(examples.length == 0 ? "" : "\n\n      Examples:");

            for (int i = 0; i < examples.length; i++) {
                menu.append("\n          " + (i + 1) + ") " + examples[i]);
            }

            menu.append("\n");
        }
        menu.append("====== End of Menu ======\n");
        System.out.println(menu.toString());
        System.out.println("OK:display_completed");
    }

    private List<Instruction> getMenu(Role role) {
        List<RolePermission> permissions = this.authorization.getPermission(role);
        Collections.sort(permissions, (s1, s2) -> { return s1.getInstName().compareTo(s2.getInstName()); });
        List<Instruction> menu = new ArrayList<Instruction>();
        for (RolePermission perm : permissions) {
            Instruction inst = Instruction.valueOf(perm.getInstName());
            menu.add(inst);
        }
        return menu;
    }
}
