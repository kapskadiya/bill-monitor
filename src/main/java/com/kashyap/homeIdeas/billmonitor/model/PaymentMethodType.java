package com.kashyap.homeIdeas.billmonitor.model;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public enum PaymentMethodType {

    UPI, CREDIT_CARD, DEBIT_CARD, ONLINE_WALLET;

    public static PaymentMethodType getPaymentMethodType(String value) {
        final String val = value.toLowerCase();
        switch (val) {
            case "upi" :
                return PaymentMethodType.UPI;
            case "credit_card" :
                return PaymentMethodType.CREDIT_CARD;
            case "debit_card" :
                return PaymentMethodType.DEBIT_CARD;
            case "online_wallet" :
                return PaymentMethodType.ONLINE_WALLET;
            default:
                throw new IllegalArgumentException("Payment method type should be valid.");
        }
    }
}
