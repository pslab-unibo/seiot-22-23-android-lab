package it.unibo.isi.seeiot.androidexample02.handlers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import java.util.Locale;

/**
 * When instantiated, the Handler is automatically bound to the control
 * flow of the thread that creates it.
 */
public class MainUiHandler extends Handler {
    public static final int NEW_CNT_VALUE_MSG = 1;
    public static final String NEW_CNT_VALUE = "new-cnt-value";
    private final TextView counterLabel;

    public MainUiHandler(final Looper looper, final TextView label){
        super(looper);
        this.counterLabel = label;
    }

    /**
     * Allows you to intercept messages received by the handler
     * and sent by the other threads
     * @param msg: message containing some information (ex. the current count)
     */
    @Override
    public void handleMessage(Message msg) {
        switch(msg.what){
            case NEW_CNT_VALUE_MSG:
                int newValue = msg.getData().getInt(NEW_CNT_VALUE);
                counterLabel.setText(String.format(Locale.ITALY, "%d", newValue));
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
