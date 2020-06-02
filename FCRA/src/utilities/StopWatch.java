package utilities;

import java.text.DecimalFormat;

public class StopWatch {
    
    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;

    
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    
    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }

    
    //elaspsed time in milliseconds
    public long getElapsedTime() {
        long elapsed;
        if (running) {
             elapsed = (System.currentTimeMillis() - startTime);
        }
        else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }
    
    //elaspsed time in seconds
    public float getElapsedTimeSecs() {
        double i=startTime;
        double i2=stopTime;
        if (running)
        	i2=System.currentTimeMillis();
        i2 = (i2-i)/1000;
        float elapsed = (float) Math.round(i2 * 1000) / 1000;
        return elapsed;
    }
}
