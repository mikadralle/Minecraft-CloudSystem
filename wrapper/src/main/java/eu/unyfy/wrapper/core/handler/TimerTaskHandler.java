package eu.unyfy.wrapper.core.handler;

import eu.unyfy.wrapper.Wrapper;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskHandler implements Runnable {

    @Override
    public void run() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Wrapper.getInstance().getWrapperCore().startServer();
            }
        }, 2000, 6000);

    }
}
