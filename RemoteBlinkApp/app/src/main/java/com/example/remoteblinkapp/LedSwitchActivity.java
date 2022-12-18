package com.example.remoteblinkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@SuppressLint("MissingPermission")
public class LedSwitchActivity extends AppCompatActivity {

    private OutputStream bluetoothOutputStream;
    private Button remoteButton;
    private boolean ledState;
    private BluetoothClientConnectionThread connectionThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_switch);
        ledState = false;
        initUI();
    }

    private void initUI() {
        remoteButton = findViewById(R.id.remotebutton);
        remoteButton.setBackgroundColor(Color.LTGRAY);
        remoteButton.setEnabled(false);
        remoteButton.setOnClickListener((v) -> sendMessage());
    }

    private void sendMessage() {
        new Thread(() -> {
            try {
                String message = ledState ? "off\n" : "on\n";
                bluetoothOutputStream.write(message.getBytes(StandardCharsets.UTF_8));
                ledState = !ledState;
                runOnUiThread(() -> remoteButton.setBackgroundColor(ledState? Color.GREEN : Color.RED));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(ScanActivity.X_BLUETOOTH_DEVICE_EXTRA);
        BluetoothAdapter btAdapter = getSystemService(BluetoothManager.class).getAdapter();
        Log.i(C.TAG, "Connecting to " + bluetoothDevice.getName());
        connectionThread = new BluetoothClientConnectionThread(bluetoothDevice, btAdapter, this::manageConnectedSocket);
        connectionThread.start();
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        try {
            bluetoothOutputStream = socket.getOutputStream();
            Log.i(C.TAG, "Connection successful!");
        } catch (IOException e) {
            Log.e(C.TAG, "Error occurred when creating output stream", e);
        }
        runOnUiThread(() -> {
            remoteButton.setEnabled(true);
            remoteButton.setBackgroundColor(Color.RED);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        connectionThread.cancel();
    }

}