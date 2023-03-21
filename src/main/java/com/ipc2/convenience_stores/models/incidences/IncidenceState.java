package com.ipc2.convenience_stores.models.incidences;

public enum IncidenceState {

    ACTIVE("Active"),
    SOLVED("Solved");
    private final String nameDB;

    IncidenceState(String nameDB){
        this.nameDB = nameDB;
    }

    public String getNameDB(){
        return nameDB;
    }


}
