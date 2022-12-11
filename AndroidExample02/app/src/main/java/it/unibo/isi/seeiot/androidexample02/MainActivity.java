package it.unibo.isi.seeiot.androidexample02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.unibo.isi.seeiot.androidexample02.counters.Counter;
import it.unibo.isi.seeiot.androidexample02.counters.CounterFactory;
import it.unibo.isi.seeiot.androidexample02.counters.CounterType;

public class MainActivity extends AppCompatActivity {
    private List<TextView> counterLabels;
    private List<Button> upButtons;
    private List<Button> downButtons;
    private List<Button> stopButtons;
    private List<Counter> counters = new ArrayList<>();

    private final static int COUNTER_NUMBER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        createAndStartCounters();
    }

    private void initUI() {
        counterLabels = Arrays.asList(
                findViewById(R.id.counterLabel1),
                findViewById(R.id.counterLabel2),
                findViewById(R.id.counterLabel3)
        );
        upButtons = Arrays.asList(
                findViewById(R.id.upButton1),
                findViewById(R.id.upButton2),
                findViewById(R.id.upButton3)
        );
        downButtons = Arrays.asList(
                findViewById(R.id.downButton1),
                findViewById(R.id.downButton2),
                findViewById(R.id.downButton3)
        );
        stopButtons = Arrays.asList(
                findViewById(R.id.stopButton1),
                findViewById(R.id.stopButton2),
                findViewById(R.id.stopButton3)
        );

        for (int i = 0; i < COUNTER_NUMBER; i++) {
            int index = i;
            stopButtons.get(i).setOnClickListener(v -> stopButtonOnClickEvent(index));
            upButtons.get(i).setOnClickListener(v -> upButtonOnClickEvent(index));
            downButtons.get(i).setOnClickListener(v -> downButtonOnClickEvent(index));

            stopButtons.get(i).setEnabled(true);
            upButtons.get(i).setEnabled(false);
            downButtons.get(i).setEnabled(true);
        }
    }

    private void upButtonOnClickEvent(int index) {
        counters.get(index).countUp();
        upButtons.get(index).setEnabled(false);
        downButtons.get(index).setEnabled(true);
    }

    private void downButtonOnClickEvent(int index) {
        counters.get(index).countDown();
        upButtons.get(index).setEnabled(true);
        downButtons.get(index).setEnabled(false);
    }

    private void stopButtonOnClickEvent(int index) {
        counters.get(index).stop();
        upButtons.get(index).setEnabled(false);
        downButtons.get(index).setEnabled(false);
        stopButtons.get(index).setEnabled(true);
    }

    private void createAndStartCounters() {
        for (int i = 0; i < COUNTER_NUMBER; i++) {
            Counter counter = CounterFactory.create(CounterType.RUN_ON_UI, counterLabels.get(i), this);
            counters.add(counter);
            new Thread(counter).start();
        }
    }
}