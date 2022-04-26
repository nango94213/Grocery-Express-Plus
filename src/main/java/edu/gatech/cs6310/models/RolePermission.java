package edu.gatech.cs6310.models;

public class RolePermission {
    private int id;
    private String roleName;
    private String instName;

    public RolePermission(){}

    public RolePermission(String roleName, String instName) {
        this.roleName = roleName;
        this.instName = instName;
    }

    public RolePermission(int id, String roleName, String instName) {
        this.id = id;
        this.roleName = roleName;
        this.instName = instName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }
}
