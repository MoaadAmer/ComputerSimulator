package il.ac.telhai.os.hardware;


import java.util.ArrayList;
import java.util.List;

public class Clock {

    private double frequency;
    private List<Clockeable> devices;
    private boolean isALive;


    //    יצירה של שעון חדש עם תדירות פולסים לשניה
    public Clock(double frequency) {
        this.frequency = frequency;
        devices = new ArrayList<>();
        isALive = true;

    }

    //    רכיב המעוניין לקבל פולסים צריך להירשם אצל השעון בעזרת השיטה הבאה
    public void addDevice(Clockeable device) {
        devices.add(device);
    }

    //    התחלת הריצה של השעון
    public void run() {

        while (isALive) {
            for (Clockeable device : devices) {
                device.tick();

            }
            try {
                Thread.sleep((long) (1000 / frequency));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isALive() {
        return isALive;
    }

    //    הפסקה של השעון
    public void shutdown() {
        isALive = false;
    }
}
