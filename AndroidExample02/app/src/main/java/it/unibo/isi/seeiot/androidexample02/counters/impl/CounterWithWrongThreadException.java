package it.unibo.isi.seeiot.androidexample02.counters.impl;

import android.widget.TextView;

import java.util.Locale;

import it.unibo.isi.seeiot.androidexample02.counters.AbstractCounter;

public class CounterWithWrongThreadException extends AbstractCounter {
    private final TextView label;

    public CounterWithWrongThreadException(final TextView label){
        this.label = label;
    }

    /**
     * It violates the principle that only tha Main thread can performs
     * operations on the UI.
     * @param value: current count
     */
    @Override
    public void updateCounter(int value) {
        String text = String.format(Locale.ITALY, "%d", value);
        //text = text + " " + Thread.currentThread().getName();
        label.setText(text);
    }
}
