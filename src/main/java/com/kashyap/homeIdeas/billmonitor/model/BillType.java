package com.kashyap.homeIdeas.billmonitor.model;

public enum BillType {
    ELECTRICITY,
    DTH,
    GAS,
    MOBILE,
    BROADBAND;

    public static BillType getBillType(String type) {

        final String typeInLowercase = type.toLowerCase();

        switch (typeInLowercase) {
            case "electricity":
                return BillType.ELECTRICITY;
            case "gas":
                return BillType.GAS;
            case "dth":
                return BillType.DTH;
            case "mobile":
                return BillType.MOBILE;
            case "broadband":
                return BillType.BROADBAND;
            default:
               throw new IllegalArgumentException("Bill type should be valid.");
        }
    }
}
