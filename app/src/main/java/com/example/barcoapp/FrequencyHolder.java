package com.example.barcoapp;

public class FrequencyHolder {
    private static int frequency = 3000; // Default frequency

    public static int getFrequency() {
        return frequency;
    }

    public static void setFrequency(int newFrequency) {
        frequency = newFrequency;
    }
}