package com.example.remoteblinkapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ScanActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1234;
    private static final int REQUEST_PERMISSION_CONNECT = 758;
    private static final int REQUEST_PERMISSION_SCAN = 759;
    private static final int LEGACY_REQUEST_PERMISSION_BLUETOOTH = 555;
    private static final int REQUEST_PERMISSION_ADMIN = 556;

    public static final String X_BLUETOOTH_DEVICE_EXTRA = "X_BLUETOOTH_DEVICE_EXTRA";


    private List<BluetoothDevice> scannedDevices = new ArrayList<>();
    private List<String> scannedNameList = new ArrayList<>();

    private List<BluetoothDevice> pairedDevices = new ArrayList<>();
    private List<String> pairedNameList = new ArrayList<>();
    private BluetoothAdapter btAdapter;

    private ListView scannedListView;
    private ListView pairedListView;
    private Button scanButton;
    private ArrayAdapter<String> scannedListAdapter;
    private ArrayAdapter<String> pairedListAdapter;
    private boolean bluetoothEnabled = false;

    //When new bluetooth devices are discovered Bluetooth the system sends out an event.
    //A broadcast receiver is needed to capture system events and react accordingly
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                onBluetoothDeviceFound(device);
            }
        }
    };

    @SuppressLint("MissingPermission")
    private void onBluetoothDeviceFound(BluetoothDevice device) {
        this.scannedDevices.add(device);
        if(device.getName()!= null){
            this.scannedNameList.add(device.getName());
        } else {
            this.scannedNameList.add(device.getAddress());
        }
        runOnUiThread(() -> scannedListAdapter.notifyDataSetChanged());
    }

    /* ================== LIFECYCLE ========================== */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //You need to register your receiver to the proper Intent filter
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        initUI();
    }

    private void initUI() {
        //shortcut to avoid bluetooth scanning if using emulator
        findViewById(R.id.emulator).setOnClickListener(v -> {
            startActivity(new Intent(this, LedSwitchEmulatedActivity.class));
        });

        scannedListView = findViewById(R.id.scannedView);

        //the adapter will let you update the view notifying changes to the data source
        scannedListAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, scannedNameList);
        scannedListView.setAdapter(scannedListAdapter);

        //registering listeners
        scannedListView.setOnItemClickListener((adapterView, view, i, l) -> {
            onDeviceClicked(scannedDevices.get(i));
        });

        //repeating for paired listview
        pairedListView = findViewById(R.id.pairedView);
        pairedListAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, pairedNameList);
        pairedListView.setAdapter(pairedListAdapter);

        pairedListView.setOnItemClickListener((adapterView, view, i, l) -> {
            onDeviceClicked(pairedDevices.get(i));
        });

        scanButton = findViewById(R.id.scan_button);
        scanButton.setOnClickListener((v) -> startScanning());
    }

    @Override
    protected void onStart() {
        btAdapter = getSystemService(BluetoothManager.class).getAdapter();
        if (btAdapter == null) {
            displayError("Bluetooth not supported!");
            finish();
        }
        super.onStart();
        this.scanButton.setEnabled(false);
        checkPermissionAndEnableBluetooth();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                //do not ask for permissions on resume otherwise you will get in a loop!
                displayError("Please grant legacy permissions first");
            } else {
                checkPairedDevices();
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                //do not ask for permissions on resume otherwise you will get in a loop!
                displayError("Please grant permissions first");
            } else {
                checkPairedDevices();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void checkPairedDevices(){
        pairedDevices.clear();
        pairedNameList.clear();
        runOnUiThread(() -> pairedListAdapter.notifyDataSetChanged());
        pairedDevices.addAll(btAdapter.getBondedDevices());
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                if(device.getName()!= null){
                    this.pairedNameList.add(device.getName());
                } else {
                    this.pairedNameList.add(device.getAddress());
                }
                runOnUiThread(() -> pairedListAdapter.notifyDataSetChanged());
            }
        }
    }

    /* ================== UI EVENT HANDLERS ========================== */

    @SuppressLint("MissingPermission")
    private void onDeviceClicked(BluetoothDevice device) {
        logMessage(device.getName());
        Intent intent = new Intent(this, LedSwitchActivity.class);
        intent.putExtra(X_BLUETOOTH_DEVICE_EXTRA, device);
        startActivity(intent);
    }


    private void startScanning() {
        //ask the scanning permission when needed
        String permission = Build.VERSION.SDK_INT < Build.VERSION_CODES.S ? Manifest.permission.BLUETOOTH_ADMIN : Manifest.permission.BLUETOOTH_SCAN;
        int reqID = Build.VERSION.SDK_INT < Build.VERSION_CODES.S ? REQUEST_PERMISSION_ADMIN : REQUEST_PERMISSION_SCAN;
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission}, reqID);
            return;
        } else {
            //empty lists
            if(!btAdapter.isDiscovering()) {
                checkPermissionAndEnableBluetooth();
                scannedDevices.clear();
                scannedNameList.clear();
                runOnUiThread(() -> scannedListAdapter.notifyDataSetChanged());
                //debounce to avoid multiple discoveries
                btAdapter.startDiscovery();
            } else {
                displayError("Already scanning! Wait please");
            }
        }
    }

    /* ================== PERMISSION MANAGEMENT ========================== */

    private void checkPermissionAndEnableBluetooth(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
             || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_FINE_LOCATION}, LEGACY_REQUEST_PERMISSION_BLUETOOTH);
            } else {
                if (!btAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    this.scanButton.setEnabled(true);
                }
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_PERMISSION_CONNECT);
            } else {
                if (!btAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    this.scanButton.setEnabled(true);
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CONNECT:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //go back to enabling bluetooth
                    checkPermissionAndEnableBluetooth();
                } else {
                    //note, since android 11 permissions are asked only twice to the user.
                    // You should avoid terminating your app and instead maybe redirect the user to the settings to enable the permission manually
                    displayError("You need to grant bluetooth connection permissions to use this app");
                }
                break;
            case REQUEST_PERMISSION_SCAN:
            case REQUEST_PERMISSION_ADMIN:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //go back at scanning after receiving the permission
                    startScanning();
                } else {
                    displayError( "You need to grant bluetooth scan permission to use this feature");
                }
                break;
            case LEGACY_REQUEST_PERMISSION_BLUETOOTH:
                if(grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    logMessage("legacy ok");
                    checkPermissionAndEnableBluetooth();
                break;
            default:
                logMessage("result of unknown activity request");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK) {
                    this.scanButton.setEnabled(true);
                } else {
                    displayError("You need to enable bluetooth to use the app");
                }
                break;
            default:
                logMessage("result of unknown activity request");
        }
    }

    /* ================== LOGGING AND ERROR HANDLING ========================== */

    private void displayError(String s) {
        Log.e(C.TAG, s);
    }

    private void logMessage(String message){
        Log.i(C.TAG, message);
    }
}