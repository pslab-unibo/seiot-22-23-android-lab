package it.unibo.isi.seeiot.androidexample02.counters;

public interface Counter extends Runnable {
    void countUp();
    void countDown();
    void stop();
}
