package com.ipc2.convenience_stores.models.devolutions;

public enum DevolutionState {
    ACTIVE("Active"),
    REJECTED("Rejected"),
    ACCEPTED("Accepted");
    private final String nameDB;

    DevolutionState(String nameDB){
        this.nameDB = nameDB;
    }

    public String getNameDB(){
        return nameDB;
    }

}
