package com.bh.myownweathercaster;

public class ShortForecastListItem {

    private String FD;
    private String FT;
    private String POP;
    private String SKY;
    private String T3H;
    private String PTY;

    public String getFD() {
        return FD;
    }

    public String getFT() {
        return FT;
    }

    public String getPOP() {
        return POP;
    }

    public String getSKY() {
        return SKY;
    }

    public String getT3H() {
        return T3H;
    }

    public String getPTY() {
        return PTY;
    }

    public ShortForecastListItem(String FD, String FT, String POP, String SKY, String T3H, String PTY) {
        this.FD = FD;
        this.FT = FT;
        this.POP = POP;
        this.SKY = SKY;
        this.T3H = T3H;
        this.PTY = PTY;
    }
}
