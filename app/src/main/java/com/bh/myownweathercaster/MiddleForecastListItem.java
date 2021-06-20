package com.bh.myownweathercaster;

public class MiddleForecastListItem {

    private String FD;
    private String SKY_AM;
    private String SKY_PM;
    private String RAIN_AM;
    private String RAIN_PM;
    private String TEMP_MAX;
    private String TEMP_MIN;
    private int ViewType;

    public String getFD() {
        return FD;
    }

    public String getSKY_AM() {
        return SKY_AM;
    }

    public String getSKY_PM() {
        return SKY_PM;
    }

    public String getRAIN_AM() {
        return RAIN_AM;
    }

    public String getRAIN_PM() {
        return RAIN_PM;
    }

    public String getTEMP_MIN() {
        return TEMP_MIN;
    }

    public String getTEMP_MAX() {
        return TEMP_MAX;
    }

    public int getViewType() {
        return ViewType;
    }

    public MiddleForecastListItem(String FD, String SKY_AM, String SKY_PM, String RAIN_AM, String RAIN_PM, String TEMP_MIN, String TEMP_MAX, int ViewType) {
        this.FD = FD;
        this.SKY_AM = SKY_AM;
        this.SKY_PM = SKY_PM;
        this.RAIN_AM = RAIN_AM;
        this.RAIN_PM = RAIN_PM;
        this.TEMP_MIN = TEMP_MIN;
        this.TEMP_MAX = TEMP_MAX;
        this.ViewType = ViewType;
    }
}
