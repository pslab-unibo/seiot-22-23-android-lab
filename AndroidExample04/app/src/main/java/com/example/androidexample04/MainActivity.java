package com.example.androidexample04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener   {

    private SensorManager sensorManager;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];

    private TextView azimuthLabel;
    private TextView pitchLabel;
    private TextView rollLabel;

    private ProgressBar azimuthBar;
    private ProgressBar pitchBar;
    private ProgressBar rollBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        initUI();
    }

    private void initUI() {
        azimuthLabel = findViewById(R.id.azimuth);
        pitchLabel = findViewById(R.id.pitch);
        rollLabel = findViewById(R.id.roll);

        azimuthBar = findViewById(R.id.azimuthbar);
        pitchBar = findViewById(R.id.pitchbar);
        rollBar = findViewById(R.id.rollbar);


        azimuthBar.setClickable(false);
        rollBar.setClickable(false);
        pitchBar.setClickable(false);
    }

    private void updateUI() {
        azimuthLabel.setText(String.format(Locale.ITALIAN, "%f", Math.toDegrees(orientationAngles[0])));
        pitchLabel.setText(String.format(Locale.ITALIAN, "%f", Math.toDegrees(orientationAngles[1])));
        rollLabel.setText(String.format(Locale.ITALIAN, "%f", Math.toDegrees(orientationAngles[2])));

        //TODO fix visualization
        float azimuthAngle = (float) (90 - Math.toDegrees(orientationAngles[0]));
        azimuthBar.setRotation(azimuthAngle);

        pitchBar.setMin(0);
        pitchBar.setMax(180);
        pitchBar.setProgress((int)Math.toDegrees(orientationAngles[1])+90);

        rollBar.setMin(0);
        rollBar.setMax(180);
        rollBar.setProgress((int)Math.toDegrees(orientationAngles[2])+90);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            sensorManager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    // Get readings from accelerometer and magnetometer. To simplify calculations,
    // consider storing these readings as unit vectors.
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading,
                    0, accelerometerReading.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading,
                    0, magnetometerReading.length);
        }
        updateOrientationAngles();
        updateUI();
    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);
        // "rotationMatrix" now has up-to-date information.
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
        // "orientationAngles" now has up-to-date information
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }
}