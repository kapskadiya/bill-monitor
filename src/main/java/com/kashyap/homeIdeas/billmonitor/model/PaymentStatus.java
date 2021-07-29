package com.kashyap.homeIdeas.billmonitor.model;

public enum PaymentStatus {
    PENDING,
    INPROGRESS,
    WAITING,
    DONE;

    public static PaymentStatus getPaymentStatus(String status) {
        final String statusInLowercase = status.toLowerCase();

        switch (statusInLowercase) {
            case "inprogress":
                return PaymentStatus.INPROGRESS;
            case "waiting":
                return PaymentStatus.WAITING;
            case "done":
                return PaymentStatus.DONE;
            default:
                return PaymentStatus.PENDING;
        }

    }
}
