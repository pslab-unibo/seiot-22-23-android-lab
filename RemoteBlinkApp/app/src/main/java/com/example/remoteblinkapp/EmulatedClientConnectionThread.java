package com.example.remoteblinkapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.function.Consumer;

public class EmulatedClientConnectionThread extends Thread {
    private Socket socket;
    Consumer<Socket> handler;

    public EmulatedClientConnectionThread(Consumer<Socket> handler) {
        this.handler = handler;
    }

    public void run() {
        // Use a temporary object that is later assigned to socket
        // because socket is final.
        Socket tmp = null;
        try {
            tmp = new Socket(InetAddress.getByName(C.emulator.HOST_IP), C.emulator.HOST_PORT);
        } catch (IOException e) {
            Log.e(C.TAG, e.getLocalizedMessage());
            try {
                tmp.close( );
            } catch (IOException e1) {
                Log.e(C.TAG, e1.getLocalizedMessage());
            }
        }
        socket = tmp;
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
