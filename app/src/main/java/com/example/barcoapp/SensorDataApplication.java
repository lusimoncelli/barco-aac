package com.example.barcoapp;

import android.app.Application;
import android.util.Log;

public class SensorDataApplication extends Application {
    private static String sensorData = "1";
    private static String tmp;

    public static String getSensorData() {
        tmp = sensorData;
        if (!tmp.equals("1")){
            Log.d("Sensor data", tmp);}
        sensorData = "1"; // refractory period

        return tmp;
    }

    public static void setSensorData(String sensorData) {
        if(SensorDataApplication.sensorData.equals("1")){
            SensorDataApplication.sensorData = sensorData;
        }
    }
}