package oyun.net.anagram.helper;

import android.os.Handler;


// http://stackoverflow.com/questions/4597690/android-timer-how
public class TimerHelper implements Runnable {
    
    private final Handler handler = new Handler();

    private TimerListener mListener;

    private volatile long startTime;
    private volatile long elapsedTime;

    public TimerHelper() {}


    @Override
    public void run() {
        boolean pendingStop = false;
        if (mListener != null) {
            pendingStop = mListener.onTimer(getMillis());

            if (pendingStop) {
                mListener.onTimeout();
                return;
            }
        }

        if (elapsedTime == -1) {
            handler.postDelayed(this, 100);
        }
    }

    public void start() {
        this.startTime = now();
        this.elapsedTime = -1;
        handler.post(this);
    }

    public void stop() {
        this.elapsedTime = now() - startTime;
        handler.removeCallbacks(this);
    }

    private long getMillis() {
        return now() - startTime;
    }

    private long now() {
        return System.currentTimeMillis();
    }

    public interface TimerListener {
        public boolean onTimer(long elapsed);
        public void onTimeout();
    }

    public void setListener(TimerListener listener) {
        mListener = listener;
    }
}
