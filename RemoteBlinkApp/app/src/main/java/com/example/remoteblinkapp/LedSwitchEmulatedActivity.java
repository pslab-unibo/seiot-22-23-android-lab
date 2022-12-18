package com.example.remoteblinkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class LedSwitchEmulatedActivity extends AppCompatActivity {

    private Button remoteButton;
    private boolean ledState;
    private OutputStream emulatedBluetoothOutputStream;
    private EmulatedClientConnectionThread connectionThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_switch);
        initUI();
    }

    private void initUI() {
        remoteButton = findViewById(R.id.remotebutton);
        remoteButton.setOnClickListener((v) -> sendMessage());
    }

    private void sendMessage() {
        new Thread(() -> {
            try {
                String message = ledState ? "off\n" : "on\n";
                emulatedBluetoothOutputStream.write(message.getBytes(StandardCharsets.UTF_8));
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
        connectionThread = new EmulatedClientConnectionThread(this::manageConnectedSocket);
        connectionThread.start();
    }

    private void manageConnectedSocket(Socket socket) {
        try {
            emulatedBluetoothOutputStream = socket.getOutputStream();
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