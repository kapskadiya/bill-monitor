package com.kashyap.homeIdeas.billmonitor.model;

public enum PaymentType {

    ONLINE, OFFLINE;

    public static PaymentType getPaymentType(String value) {
        final String val = value.toLowerCase();

        switch (val) {
            case "online":
                return PaymentType.ONLINE;
            case "offline":
                return PaymentType.OFFLINE;
            default:
                throw new IllegalArgumentException("Payment type should be valid.");
        }
    }
}
