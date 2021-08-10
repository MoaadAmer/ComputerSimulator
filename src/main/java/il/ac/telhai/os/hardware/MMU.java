package il.ac.telhai.os.hardware;

import il.ac.telhai.os.software.SegmentationViolation;
import org.apache.log4j.Logger;

/**
 * @author cmshalom
 * This is a partial implementation of an MMU
 * It has a manual write-through cache of size of one segment it
 */
public class MMU implements Memory {
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(MMU.class);

    private RealMemory memory;
    private PageTableEntry[] pageTable = null;

    public MMU(RealMemory memory, int numberOfPages) {
        if (memory.getSegmentSize() < 2 * numberOfPages) {
            throw new IllegalArgumentException("Page table does not fit to one page");
        }
        this.memory = memory;
    }

    public void setRealMode() {
        pageTable = null;
    }

    public void setPageTable(PageTableEntry[] pageTable) {
        this.pageTable = pageTable;
    }

    public PageTableEntry[] getPageTable() {
        return pageTable;
    }

    @Override
    public int getSegmentSize() {
        return memory.getSegmentSize();
    }

    @Override
    public int getNumberOfSegments() {
        return pageTable == null ? memory.getNumberOfSegments() : pageTable.length;
    }

    public void copySegment(int destinationSegment, int sourceSegment) {
        memory.dma(destinationSegment, sourceSegment);
    }

    int translateSegment(int pageNo, boolean isWrite) {

        if (pageTable == null) return pageNo;
        PageTableEntry entry = (pageNo >= 0 && pageNo < pageTable.length) ? pageTable[pageNo] : null;
        if (entry == null || !entry.isMappedtoMemory()) throw new PageFault(entry);
        if (entry.isCopyOnWrite() && isWrite) throw new PageFault(entry);
        return entry.getSegmentNo();

    }

    @Override
    public byte readByte(int pageNo, int offset) {
        int segmentNo = translateSegment(pageNo, false);
        return memory.readByte(segmentNo, offset);
    }

    @Override
    public void writeByte(int pageNo, int offset, byte value) {
        int segmentNo = translateSegment(pageNo, true);
        memory.writeByte(segmentNo, offset, value);
    }

    @Override
    public int readWord(int pageNo, int offset) {
        int segmentNo = translateSegment(pageNo, false);
        return memory.readWord(segmentNo, offset);
    }

    @Override
    public void writeWord(int pageNo, int offset, int value) {
        int segmentNo = translateSegment(pageNo, true);
        memory.writeWord(segmentNo, offset, value);
    }

}
