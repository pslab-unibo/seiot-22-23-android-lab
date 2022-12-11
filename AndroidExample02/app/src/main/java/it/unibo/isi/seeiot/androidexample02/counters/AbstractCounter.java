package it.unibo.isi.seeiot.androidexample02.counters;

import java.util.Random;

public abstract class AbstractCounter implements Runnable, Counter {
    /**
     * volatile: the value of a volatile field becomes visible to all readers
     * (other threads) after a write operation completes on it.
     * Without volatile, readers could see some non-updated value.
     */
    private volatile boolean up = true;
    private volatile boolean stop = false;

    @Override
    public void run() {
        int cnt = 0;

        while(!stop) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(up) {
                cnt = cnt + 1;
            } else {
                cnt = cnt - 1;
            }

            updateCounter(cnt);
        }
    }

    @Override
    public void countUp(){
        up = true;
    }

    @Override
    public void countDown(){
        up = false;
    }

    @Override
    public void stop(){
        stop = true;
    }

    /**
     * The logic requires to interact with the UI
     * @param value: current count
     */
    protected abstract void updateCounter(final int value);
}