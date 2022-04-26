package edu.gatech.cs6310.auth;

import edu.gatech.cs6310.Instruction;
import edu.gatech.cs6310.Role;
import edu.gatech.cs6310.dao.PostgresClientImpl;
import edu.gatech.cs6310.logger.Logger;
import edu.gatech.cs6310.models.AppUser;
import edu.gatech.cs6310.models.RolePermission;

import java.util.ArrayList;
import java.util.List;

public class Authorization {

    private final PostgresClientImpl ps;
    private final Logger logger;

    public Authorization(PostgresClientImpl ps, Logger logger){
        this.ps = ps;
        this.logger = logger;
    }

    /**
     * Function to check whether a user is authorized to run a command
     * @param user AppUser to check
     * @param instruction Instruction to check against
     * @return True if authorized, false otherwise.
     */
    public Boolean hasPermission(AppUser user, Instruction instruction){
        String username = user.getName();
        List<AppUser> appUsers = ps.selectWhere("AppUser", "name", username);
        boolean exists = appUsers.size() > 0;
        if(!exists) {
            logger.error("HAS_PERMISSION", "AppUser %s does not exist.".formatted(username));
            return false;
        }

        AppUser appUser = appUsers.get(0);
        List<RolePermission> rpExists = ps.selectWhere("RolePermission", "roleName", appUser.getRole(),
                "instName", instruction.toString());
        exists = rpExists.size() > 0;

        if(!exists) {
            logger.error("HAS_PERMISSION", "AppUser %s does not have permission to %s."
                                                .formatted(username, instruction.toString()));
            return false;
        }
        logger.info("HAS_PERMISSION", "AppUser %s running %s command."
                .formatted(username, instruction.toString()));
        return true;
    }

    public List<RolePermission> getPermission(Role role) {
        List<RolePermission> permissions = ps.selectWhere("RolePermission", "roleName", role.toString());
        return permissions;
    }
}
