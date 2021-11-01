package com.kashyap.homeIdeas.billmonitor.model;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public class ChartValue {

    private String xValue;
    private String yValue;

    public ChartValue() {
    }

    public ChartValue(String xValue, String yValue) {
        this.xValue = xValue;
        this.yValue = yValue;
    }

    public String getxValue() {
        return xValue;
    }

    public void setxValue(String xValue) {
        this.xValue = xValue;
    }

    public String getyValue() {
        return yValue;
    }

    public void setyValue(String yValue) {
        this.yValue = yValue;
    }
}
