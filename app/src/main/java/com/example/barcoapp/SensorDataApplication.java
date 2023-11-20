package com.example.barcoapp;

import android.app.Application;

public class SensorDataApplication extends Application {
    private static String sensorData = "1";
    private static String tmp;

    public static String getSensorData() {
        tmp = sensorData;
        sensorData = "1";
        return tmp;
    }

    public void setSensorData(String sensorData) {
        this.sensorData = sensorData;
    }
}
