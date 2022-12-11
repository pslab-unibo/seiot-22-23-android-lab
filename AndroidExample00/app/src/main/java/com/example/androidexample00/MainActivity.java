package com.example.androidexample00;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button confirmBtn;
    private EditText usernameEditText;
    private TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameEditText = findViewById(R.id.usernameEditText);
        confirmBtn = findViewById(R.id.confirmBtn);
        usernameTextView = findViewById(R.id.usernameTextView);
        if(savedInstanceState != null) {
            usernameTextView.setText(savedInstanceState.getString("username"));
        }
        logInfo("0) on create");
    }

    @Override
    protected void onStart() {
        super.onStart();
        confirmBtn.setOnClickListener(v -> {
            final String username = usernameEditText.getText().toString();
            usernameTextView.setText("Hello ".concat(username));
        });
        logInfo("1) on start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logInfo("2) on resume (the activity comes to foreground)");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logInfo("3) on pause (the user is leaving the activity)");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logInfo("3-1) on restart");
    }


    @Override
    protected void onStop() {
        super.onStop();
        logInfo("4) on stop (the activity is no longer visible to the user)");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        logInfo("4-5) on save instance");
//        savedInstanceState.putString("username", usernameTextView.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logInfo("5) on destroy");
    }

    private static void logInfo(String message) {
        Log.i(MainActivity.class.getSimpleName(), message);
    }
}