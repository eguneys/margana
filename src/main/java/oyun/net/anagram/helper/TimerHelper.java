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
        int deciMillis = (int) (getMillis() / 100) % 10;
        int seconds = (int) (getMillis() / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        if (mListener != null) {
            mListener.onTimer(minutes, seconds, deciMillis);
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
        public void onTimer(int minutes, int seconds, int deciMillis);
    }

    public void setListener(TimerListener listener) {
        mListener = listener;
    }
}
