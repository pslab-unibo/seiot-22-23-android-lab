package it.unibo.isi.seeiot.androidexample02.counters;

import android.app.Activity;
import android.os.Looper;
import android.widget.TextView;

import it.unibo.isi.seeiot.androidexample02.counters.impl.CounterWithHandler;
import it.unibo.isi.seeiot.androidexample02.counters.impl.CounterWithRunOnUiThread;
import it.unibo.isi.seeiot.androidexample02.counters.impl.CounterWithWrongThreadException;
import it.unibo.isi.seeiot.androidexample02.handlers.MainUiHandler;

public class CounterFactory {
    public static Counter createCounterWithHandler(TextView view) {
        return new CounterWithHandler(new MainUiHandler(Looper.getMainLooper(), view));
    }

    public static Counter createCounterWithRunOnUiThread(TextView view, Activity activity) {
        return new CounterWithRunOnUiThread(activity, view);
    }

    public static Counter createCounterWithWrongThreadException(TextView view) {
        return new CounterWithWrongThreadException(view);
    }

    public static Counter create(CounterType type, TextView view) {
        return create(type, view, null);
    }

    public static Counter create(CounterType type, TextView view, Activity activity) {
        switch (type) {
            case HANDLER: return createCounterWithHandler(view);
            case RUN_ON_UI: return createCounterWithRunOnUiThread(view, activity);
            case WRONG: return createCounterWithWrongThreadException(view);
            default:
                throw new IllegalStateException("Invalid counter type");
        }
    }
}
