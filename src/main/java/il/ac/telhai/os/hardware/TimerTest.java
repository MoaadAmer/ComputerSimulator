package main.java.il.ac.telhai.os.hardware;

import org.junit.Test;


public class TimerTest extends CPU {
	private static final int FREQUENCY = 10;
	Clock clock = new Clock(FREQUENCY);

	@Test
	public void test() {
		Timer t = new Timer(this, clock);
		t.set(10);
		clock.run();
	}

	public void interrupt(InterruptSource source) {
		clock.shutdown();
	}

}
