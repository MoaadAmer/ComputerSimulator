package il.ac.telhai.os.software;

import il.ac.telhai.os.hardware.InterruptSource;
import il.ac.telhai.os.hardware.MMU;
import il.ac.telhai.os.hardware.PageFault;
import il.ac.telhai.os.hardware.PageTableEntry;
import org.apache.log4j.Logger;

import java.util.LinkedList;

public class VMM implements InterruptHandler {
    private static final Logger logger = Logger.getLogger(VMM.class);

    private MMU mmu;
    private int numberOfRealSegments;
    private LinkedList<Integer> freeMemoryPages;

    public VMM(MMU mmu) {
        this.mmu = mmu;
        numberOfRealSegments = mmu.getNumberOfSegments();
        initMemoryFreeList();
    }

    private void initMemoryFreeList() {
        logger.info("Initializing Real Memory");
        freeMemoryPages = new LinkedList<Integer>();
        for (int i = 1; i < this.numberOfRealSegments; i++) {
            freeMemoryPages.add(i);
        }
        logger.info("Real Memory Initialized");
    }

    private int getFreePage() {
        int result = freeMemoryPages.remove();
        logger.info("Allocating segment " + result);
        return result;
    }

    @Override
    public void handle(InterruptSource source) {
        PageFault fault = (PageFault) source;
        PageTableEntry entry = fault.getEntry();
        logger.info("Handling page fault on segment " + entry.getSegmentNo());
        entry.setSegmentNo(getFreePage());
        entry.setMappedToMemory(true);

        logger.info("Allocating segment " + entry.getSegmentNo());
    }
}
