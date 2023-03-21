package com.ipc2.convenience_stores.models.requisitions;

public enum RequisitionState {
    REQUESTED("Requested"),
    PENDING_REVIEW("Pending review"),
    REJECTED("Rejected"),
    SENT("Sent"),
    COMPLETED("Completed");

    private String nameDB;

    RequisitionState(String nameDB){
        this.nameDB = nameDB;
    }

    public String getNameDB(){
        return nameDB;
    }

}
