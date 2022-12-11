package it.unibo.isi.seeiot.androidexample01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    }
}
