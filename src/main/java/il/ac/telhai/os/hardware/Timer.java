package il.ac.telhai.os.hardware;


public class Timer implements Clockeable {

    int counter = 0;
    private CPU cpu;
    private Clock clock;
    private int clockTicks;

    //        שעון העצר מקבל את המעבד שלו יתן פסיקה, ואת השעון שממנו רוצה לקבל פולסים
    public Timer(CPU cpu, Clock clock) {
        this.cpu = cpu;
        this.clock = clock;
        this.clock.addDevice(this);

    }
//        מתודה שתכוון את שעון העצר למספר פולסים

    public void set(int clockTicks) {
        this.clockTicks = clockTicks;
    }

    //        קוד שירוץ בכל פולס
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

