package com.ipc2.convenience_stores.models.incidences;

public enum IncidenceReason {
    WRONG_PRODUCT("Wrong product"),
    DAMAGED_PRODUCT("Damaged product"),
    UNSOLICITED_PRODUCT("Unsolicited product"),
    MISSING_PRODUCT("Missing product"),
    EXCESS_PRODUCT("Excess product");
    private final String nameDB;

    IncidenceReason(String nameDB){
        this.nameDB = nameDB;
    }

    public String getNameDB(){
        return nameDB;
    }

}
