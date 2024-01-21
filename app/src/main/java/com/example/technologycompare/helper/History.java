package com.example.technologycompare.helper;

public class History {
    private String device1;
    private String device2;
    private String idHistory;

    public History(String device1, String device2, String idHistory) {
        this.device1 = device1;
        this.device2 = device2;
        this.idHistory = idHistory;
    }

    public String getDevice1() {
        return device1;
    }

    public void setDevice1(String device1) {
        this.device1 = device1;
    }

    public String getDevice2() {
        return device2;
    }

    public void setDevice2(String device2) {
        this.device2 = device2;
    }

    public String getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(String idHistory) {
        this.idHistory = idHistory;
    }
}
