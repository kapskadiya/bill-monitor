package com.kashyap.homeIdeas.billmonitor.model;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
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
