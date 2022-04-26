package edu.gatech.cs6310;

public class AppUser {
    private String username;
    private String password;
    private String userRole;
    private boolean signIn = false;

    public AppUser(String username, String password){
        this.username = username;
        this.password = password;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getRole(){
        return this.userRole;
    }
    public void setRole(String role){
        this.userRole = role;
    }
    public boolean isSignIn(){
        return signIn;
    }
    public void setSignIn(boolean signIn){
        this.signIn = signIn;
    }



}
