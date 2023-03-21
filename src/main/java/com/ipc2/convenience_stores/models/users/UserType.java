package com.ipc2.convenience_stores.models.users;

public enum UserType {

    STORE_USER("store_user"),
    SUPERVISOR_USER("supervisor_user"),
    WAREHOUSE_USER("warehouse_user"),
    ADMIN_USER("admin_user");

    private String dbName;

    UserType(String dbName){
        this.dbName = dbName;
    }

    public String getName(){
        return dbName;
    }

}
