package com.example.barcoapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SelectDeviceActivity extends AppCompatActivity {

    private static final int BLUETOOTH_PERMISSION_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted, request it
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.BLUETOOTH,
                                Manifest.permission.BLUETOOTH_ADMIN,
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.BLUETOOTH_CONNECT,
                        },
                        BLUETOOTH_PERMISSION_REQUEST
                );
            } else {
                // Proceed w/ bluetooth operation if permission is already granted
                // Bluetooth Setup
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                // Get List of Paired Bluetooth Device

                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                List<Object> deviceList = new ArrayList<>();
                if (pairedDevices.size() > 0) {
                    // There are paired devices. Get the name and address of each paired device.
                    for (BluetoothDevice device : pairedDevices) {
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress(); // MAC address
                        DeviceInfoModel deviceInfoModel = new DeviceInfoModel(deviceName, deviceHardwareAddress);
                        deviceList.add(deviceInfoModel);
                    }
                    // Display paired device using recyclerView
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewDevice);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    DeviceListAdapter deviceListAdapter = new DeviceListAdapter(this, deviceList);
                    recyclerView.setAdapter(deviceListAdapter);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                } else {
                    View view = findViewById(R.id.recyclerViewDevice);
                    Snackbar snackbar = Snackbar.make(view, "Activate Bluetooth or pair a Bluetooth device", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
                    snackbar.show();
                }
            }
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BLUETOOTH_PERMISSION_REQUEST) {
            // Check if the permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with Bluetooth operation
                // Bluetooth Setup
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                // Get List of Paired Bluetooth Device

                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                List<Object> deviceList = new ArrayList<>();
                if (pairedDevices.size() > 0) {
                    // There are paired devices. Get the name and address of each paired device.
                    for (BluetoothDevice device : pairedDevices) {
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress(); // MAC address
                        DeviceInfoModel deviceInfoModel = new DeviceInfoModel(deviceName, deviceHardwareAddress);
                        deviceList.add(deviceInfoModel);
                    }
                    // Display paired device using recyclerView
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewDevice);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    DeviceListAdapter deviceListAdapter = new DeviceListAdapter(this, deviceList);
                    recyclerView.setAdapter(deviceListAdapter);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                } else {
                    View view = findViewById(R.id.recyclerViewDevice);
                    Snackbar snackbar = Snackbar.make(view, "Activate Bluetooth or pair a Bluetooth device", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
                    snackbar.show();
                }
            } else {
                // Permission denied, handle accordingly
                // ...
            }
        }
    }

}