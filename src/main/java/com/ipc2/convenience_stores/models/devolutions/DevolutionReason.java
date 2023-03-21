package com.ipc2.convenience_stores.models.devolutions;

public enum DevolutionReason {

    WRONG_PRODUCT("Wrong product"),
    DAMAGED_PRODUCT("Damaged product"),
    UNSOLICITED_PRODUCT("Unsolicited product"),
    EXCESS_PRODUCT("Excess product");
    private String nameDB;

    DevolutionReason(String nameDB){
        this.nameDB = nameDB;
    }

    public String getNameDB(){
        return nameDB;
    }
}
