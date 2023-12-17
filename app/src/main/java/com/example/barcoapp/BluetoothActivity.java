package com.example.barcoapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
public class BluetoothActivity extends AppCompatActivity {

    private SensorDataApplication sensorDataApplication  = new SensorDataApplication();
    private String deviceName = null;
    public static Handler handler;
    public static BluetoothSocket mmSocket;
    public static ConnectedThread connectedThread;
    public static CreateConnectThread createConnectThread;
    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update

    private Button buttonConnect;
    private Button buttonNavigate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        // UI Initialization
        buttonConnect = findViewById(R.id.buttonConnect);
        buttonConnect.setOnClickListener(view -> {
            Intent intent = new Intent(BluetoothActivity.this, SelectDeviceActivity.class);
            handler.removeCallbacksAndMessages(null);
            startActivity(intent);
        });


        buttonNavigate = findViewById(R.id.button_inicio);
        buttonNavigate.setOnClickListener(view -> {
            Intent intent = new Intent(BluetoothActivity.this, LogInActivity.class);
            handler.removeCallbacksAndMessages(null);
            startActivity(intent);
        });

        final Toolbar toolbar = findViewById(R.id.toolbar);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        // If a bluetooth device has been selected from SelectDeviceActivity
        deviceName = getIntent().getStringExtra("deviceName");
        if (deviceName != null) {
            // Get the device address to make BT Connection
            String deviceAddress = getIntent().getStringExtra("deviceAddress");
            // Show progress and connection status
            toolbar.setSubtitle("Connecting to " + deviceName + "...");
            progressBar.setVisibility(View.VISIBLE);
            buttonConnect.setEnabled(false);

            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            createConnectThread = new CreateConnectThread(bluetoothAdapter, deviceAddress);
            createConnectThread.start();
        }

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == CONNECTING_STATUS){
                    if(msg.arg1 == 1)
                        toolbar.setSubtitle("Connected to " + deviceName);
                    else
                        toolbar.setSubtitle("Device fails to connect");
                    progressBar.setVisibility(View.GONE);
                    buttonConnect.setEnabled(true);
                } else if (msg.what == MESSAGE_READ)
                    sensorDataApplication.setSensorData(msg.obj.toString());
            }
        };

    }
    /* ============================ Thread to Create Bluetooth Connection =================================== */
    public static class CreateConnectThread extends Thread {
        @SuppressLint("MissingPermission")
        public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address) {
            /*
            Use a temporary object that is later assigned to mmSocket
            because mmSocket is final.
             */
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            UUID uuid = bluetoothDevice.getUuids()[0].getUuid();
            try {
                /*
                Get a BluetoothSocket to connect with the given BluetoothDevice.
                Due to Android device varieties,the method below may not work fo different devices.
                You should try using other methods i.e. :
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                 */
                tmp = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }
        @SuppressLint("MissingPermission")
        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.cancelDiscovery();
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Log.e("Status", "Device connected");
                handler.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                    Log.e("Status", "Cannot connect to device");
                    handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }
            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.start();
        }
        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }
    /* =============================== Thread for Data Transfer =========================================== */
    public static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;

            // Get the input stream, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();

            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            mmInStream = tmpIn;

        }
        public void run() {
            int sensorSignal;
            int count = 0;
            int byteRead;
            int bufferSize = 1024;

            while (true) {
                byte[] mmBuffer = new byte[bufferSize];
                try {
                    int a = mmInStream.read(mmBuffer);
                    Log.d("BYTEs_READ", String.valueOf(a));

                    for (int i = 0; i <  a ; i++) {
                        byteRead = (mmBuffer[i] & 0xFF); // Transform it to int
                        Log.d("BYTE_READ", String.valueOf(byteRead));

                        if (byteRead == 0) {
                            count++;
                        } else {
                            if (count > 400) {
                                sensorSignal = 2;
                                Log.d("PRESS", "Long press");
                            } else if (count > 10) {
                                Log.d("PRESS", "short press");
                                sensorSignal = 0;
                            } else {
                                sensorSignal = 1;
                            }
                            Log.d("SENSOR_SIGNAL", String.valueOf(sensorSignal));
                            handler.obtainMessage(MESSAGE_READ, sensorSignal).sendToTarget();
                            count = 0;
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

    }

}