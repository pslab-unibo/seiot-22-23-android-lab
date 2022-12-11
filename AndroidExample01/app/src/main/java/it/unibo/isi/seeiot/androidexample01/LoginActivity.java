package it.unibo.isi.seeiot.androidexample01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import it.unibo.isi.seeiot.androidexample01.kb.C;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
    }

    private void initUI(){

        final Button loginBtn = findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText usernameField = findViewById(R.id.username_edit);
                final EditText passwordField = findViewById(R.id.password_edit);

                final String username = usernameField.getText().toString();
                final String password = passwordField.getText().toString();

                if(checkLogin(username, password)){
                    //Calling another activity here with an explicit Intent
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra(C.extra.USERNAME_ID, username);
                    startActivity(i);
                } else {
                    showAlert("Attention!", "Invalid credentials. Retry!");
                }
            }
        });

    }

    private boolean checkLogin(final String username, final String password){
        return username.equals(C.login.USERNAME) && password.equals(C.login.PASSWORD);
    }

    private void showAlert(final String title, final String message) {
        final AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
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
