package com.kashyap.homeIdeas.billmonitor.dto;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public class AmountVsTimeDto {

    private String billType;
    private String timeIn;

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }
}
