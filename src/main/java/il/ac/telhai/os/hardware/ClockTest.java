package main.java.il.ac.telhai.os.hardware;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClockTest implements Clockeable {
	private static final int FREQUENCY = 10;
	Clock clock = new Clock(FREQUENCY);
	private int ticks = 0;

	@Test
	public void test() throws InterruptedException {
		Thread.sleep(1000);
		assertEquals(0, ticks);
		clock.addDevice(this);
		Thread.sleep(1000);
		assertEquals(0, ticks);
		clock.run();
		assertEquals(FREQUENCY, ticks);
		Thread.sleep(1000);
		assertEquals(FREQUENCY, ticks);
	}

	@Override
	public void tick() {
		ticks++;
		if (ticks == FREQUENCY) {
			clock.shutdown();
		}
	}

}
