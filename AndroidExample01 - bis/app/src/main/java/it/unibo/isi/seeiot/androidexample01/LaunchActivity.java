package it.unibo.isi.seeiot.androidexample01;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import it.unibo.isi.seeiot.androidexample01.kb.C;

public class LaunchActivity extends AppCompatActivity {

    private static final int LOGIN_REQ_ID = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        initUI();
    }

    private void initUI() {
        final Button launchButton = findViewById(R.id.launch_button);
        launchButton.setOnClickListener( (b) -> {
            //calling an activity for result
            logInfo("starting request for login");
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(i, LOGIN_REQ_ID);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQ_ID && resultCode == Activity.RESULT_OK){
            logInfo("login request successful");
            String username = data.getStringExtra("username");
            //Calling another activity here with an explicit Intent
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra(C.extra.USERNAME_ID, username);
            logInfo("launching main activity");
            startActivity(i);
        }
    }

    private void logInfo(String log){
        Log.println(Log.INFO, "[Launch-Activity]", log);
    }
}