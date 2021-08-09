package il.ac.telhai.os.hardware;

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

    @Override
    public byte readByte(int pageNo, int offset) {
        pageNo = translate(pageNo);
        return pageNo != -1 ? memory.readByte(pageNo, offset) : -1;

    }

    private int translate(int pageNo) {

        if (pageTable != null) {
            if (pageNo < pageTable.length && pageTable[pageNo].isMappedtoMemory()) {
                return pageTable[pageNo].getSegmentNo();

            }
            throw new PageFault(pageTable[pageNo]);
        }
        return -1;
    }

    @Override
    public void writeByte(int pageNo, int offset, byte value) {
        pageNo = translate(pageNo);
        if (pageNo != -1)
            memory.writeByte(pageNo, offset, value);
    }

    @Override
    public int readWord(int pageNo, int offset) {
        pageNo = translate(pageNo);
        return pageNo != -1 ? memory.readWord(pageNo, offset) : -1;
    }

    @Override
    public void writeWord(int pageNo, int offset, int value) {
        pageNo = translate(pageNo);
        if (pageNo != -1)
            memory.writeWord(pageNo, offset, value);
    }

}
