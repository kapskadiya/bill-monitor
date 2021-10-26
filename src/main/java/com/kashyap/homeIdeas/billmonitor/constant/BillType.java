package com.kashyap.homeIdeas.billmonitor.constant;

import com.kashyap.homeIdeas.billmonitor.exception.BillMonitorValidationException;
import org.apache.commons.lang3.StringUtils;

public enum BillType {
    ELECTRICITY,
    DTH,
    GAS,
    MOBILE,
    BROADBAND;

    public static BillType getBillType(String type) {
        if (StringUtils.isBlank(type)) {
            throw new BillMonitorValidationException("Bill type should be valid.");
        }

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
               throw new BillMonitorValidationException("Bill type should be valid.");
        }
    }
}
