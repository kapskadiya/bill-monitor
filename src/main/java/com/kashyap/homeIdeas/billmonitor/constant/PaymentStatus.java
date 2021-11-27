package com.kashyap.homeIdeas.billmonitor.constant;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public enum PaymentStatus {
    PENDING,
    IN_PROGRESS,
    WAITING,
    DONE;

    public static PaymentStatus getPaymentStatus(String status) {
        final String statusInLowercase = status.toLowerCase();

        switch (statusInLowercase) {
            case "inprogress":
                return PaymentStatus.IN_PROGRESS;
            case "waiting":
                return PaymentStatus.WAITING;
            case "done":
                return PaymentStatus.DONE;
            default:
                return PaymentStatus.PENDING;
        }

    }
}
