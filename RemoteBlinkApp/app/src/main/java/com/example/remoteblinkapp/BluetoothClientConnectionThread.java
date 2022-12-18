package com.example.remoteblinkapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.function.Consumer;

@SuppressLint("MissingPermission")
public class BluetoothClientConnectionThread extends Thread {
    private final BluetoothSocket socket;
    private BluetoothAdapter btAdapter;
    Consumer<BluetoothSocket> handler;

    public BluetoothClientConnectionThread(BluetoothDevice device, BluetoothAdapter btAdapter, Consumer<BluetoothSocket> handler) {
        // Use a temporary object that is later assigned to socket
        // because socket is final.
        this.btAdapter = btAdapter;
        this.handler = handler;

        BluetoothSocket tmp = null;

        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            tmp = device.createRfcommSocketToServiceRecord(C.DEFAULT_DEVICE_UUID);
        } catch (IOException e) {
            Log.e(C.TAG, "Socket's create() method failed", e);
        }
        socket = tmp;
    }

    public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            btAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect();
            } catch (IOException connectException) {
                Log.e(C.TAG, "unable to connect");
                // Unable to connect; close the socket and return.
                try {
                    socket.close();
                } catch (IOException closeException) {
                    Log.e(C.TAG, "Could not close the client socket", closeException);
                }
                return;
            }
            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            handler.accept(socket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(C.TAG, "Could not close the client socket", e);
            }
        }
}

