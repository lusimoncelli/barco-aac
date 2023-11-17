package com.example.barcoapp;

import android.app.Application;

public class SensorDataApplication extends Application {
    private static String sensorData = "0";

    public static String getSensorData() {
        return sensorData;
    }

    public void setSensorData(String sensorData) {
        this.sensorData = sensorData;
    }
}
