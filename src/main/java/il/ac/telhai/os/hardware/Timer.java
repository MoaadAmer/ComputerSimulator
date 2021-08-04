package main.java.il.ac.telhai.os.hardware;

public class Timer implements Clockeable {

    private CPU cpu;
    private Clock clock;
    private int clockTicks;
    int counter = 0;

       public Timer(CPU cpu, Clock clock) {
        this.cpu = cpu;
        this.clock = clock;
        this.clock.addDevice(this);

    }

    public void set(int clockTicks) {
        this.clockTicks = clockTicks;
    }


    @Override
    public void tick() {
        counter++;
        if (counter == clockTicks) {
            cpu.interrupt(new InterruptSource() {
            });
            counter = 0;
        }
    }
}

