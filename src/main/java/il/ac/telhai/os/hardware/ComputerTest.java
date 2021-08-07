package il.ac.telhai.os.hardware;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.junit.Test;

public class ComputerTest extends Computer {

	@Test
	public void test() throws InterruptedException,IOException {
		Path src = Paths.get("src/main/resources/init1.prg");
		Path dst = Paths.get("src/main/resources/init.prg");
		Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
		Computer c = new Computer(FREQUENCY, SEGMENT_SIZE, NUMBER_OF_SEGMENTS, NUMBER_OF_PAGES);
		c.run();
		assertEquals(		
				"\nCS=0	DS=1	SS=0	ES=2" +
				"\nAX=120	BX=0	CX=0	DX=0" +
				"\nSP=92	IP=11	SI=0	DI=0" +
				"\nBP=0	FL=2\t", c.cpu.getRegisters());
	}

}
