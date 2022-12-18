package com.example.remoteblinkapp;

import java.util.UUID;

public class C {
    public static final String TAG = "RemoteLedApp";
    public static final UUID DEFAULT_DEVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public class emulator {
        public static final String HOST_IP = "10.0.2.2";
        public static final int HOST_PORT = 8080;
    }
}
