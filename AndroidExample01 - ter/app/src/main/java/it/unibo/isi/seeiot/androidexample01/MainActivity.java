package it.unibo.isi.seeiot.androidexample01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import it.unibo.isi.seeiot.androidexample01.kb.C;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI(){
        final String username = getIntent().getStringExtra(C.extra.USERNAME_ID);
        final TextView helloLabel = findViewById(R.id.helloLabel);
        helloLabel.setText(String.format("Hello, %s!", username));
        findViewById(R.id.sendButton).setOnClickListener(v -> {
            // Create the text message with a string.
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Join "+username+" in using the app!");
            sendIntent.setType("text/plain");

            // Try to invoke the intent.
            try {
                startActivity(sendIntent);
            } catch (ActivityNotFoundException e) {
                showAlert("Attention!", "Couldn't send invite");
            }

        });
    }

    private void showAlert(final String title, final String message) {
        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .create();

        dialog.show();
    }
}
