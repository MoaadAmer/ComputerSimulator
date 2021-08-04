package main.java.il.ac.telhai.os.hardware;

import java.util.ArrayList;
import java.util.List;

public class Clock {

    private double frequency;
    private List<Clockeable> devices;

    private boolean isALive;


    public Clock(double frequency) {
        this.frequency = frequency;
        devices = new ArrayList<>();
        isALive = true;

    }


    public void addDevice(Clockeable device) {
        devices.add(device);
    }


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


    public void shutdown() {
        isALive = false;
    }
}
