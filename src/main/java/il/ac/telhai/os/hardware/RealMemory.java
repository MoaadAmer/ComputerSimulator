package il.ac.telhai.os.hardware;

import org.apache.log4j.Logger;

/**
 * This is an implementation of segmented memory, where all the segments are disjoint.
 * As usual, the basic addressable unit is a byte
 * Bytes can be accesses via the readByte and writeByte methods
 * Double Words also can be accessed as one unit
 * via the readInt and writeInt methods
 *
 * @author Mordo Shalom
 */
public class RealMemory implements Memory {
    public static final int BYTES_PER_INT = 4;
    public static final int BITS_PER_BYTE = 8;
    private static Logger logger = Logger.getLogger(RealMemory.class);
    private final int segmentSize;
    private final int numberOfSegments;

    private byte[][] memory;


    // construct a new memory with multiple segments of a given size
    RealMemory(int segmentSize, int numberOfSegments) {
        memory = new byte[numberOfSegments][segmentSize];
        this.segmentSize = segmentSize;
        this.numberOfSegments = numberOfSegments;
    }

    @Override
    // writes a byte to a specific segment at a specific offset
    public void writeByte(int segment, int offset, byte value) {
        getSegment(segment)[offset] = value;
    }


    public byte readByte(int segment, int offset) {
        assert (offset >= 0 && offset < segmentSize);
        return getSegment(segment)[offset];
    }


    // reads a word (4 bytes) from a specific segment at a specific offset

    public int readWord(int segment, int offset) {

        byte b1 = memory[segment][offset];
        byte b2 = memory[segment][offset + 1];
        byte b3 = memory[segment][offset + 2];
        byte b4 = memory[segment][offset + 3];
        int result = ((b1 << 24 & 0xFF000000) | (b2 << 16 & 0x00FF0000) | (b3 << 8 & 0x0000FF00) | b4 & 0x000000FF);
        return result;
    }

    // writes a word (4 bytes) to a specific segment at a specific offset


    public void writeWord(int segment, int offset, int value) {

        memory[segment][offset] = (byte) ((value & 0xFF000000) >> 24);
        memory[segment][offset+1] = (byte) ((value & 0x00FF0000) >> 16);
        memory[segment][offset+2] = (byte) ((value & 0x0000FF00) >> 8);
        memory[segment][offset+3] = (byte) (value & 0x000000FF);

    }

    // copy a block bytes between two segments at a specific offset
    void dma(int destinationSegment, int sourceSegment, int offset, int length) {
        assert (offset + length < segmentSize);
        byte[] src = getSegment(sourceSegment);
        for (int i = 0; i < length; i++) {
            writeByte(destinationSegment, offset + i, src[offset + i]);
        }

    }

    // copy all bytes between two segments
    void dma(int destinationSegment, int sourceSegment) {
        byte[] src = getSegment(sourceSegment);

        for (int i = 0; i < segmentSize; i++) {
            writeByte(destinationSegment, i, src[i]);
        }
    }


    @Override
    public int getSegmentSize() {
        return segmentSize;
    }

    @Override
    public int getNumberOfSegments() {
        return numberOfSegments;
    }

    private byte[] getSegment(int segment) {
        assert (segment >= 0 && segment < numberOfSegments);
        return memory[segment];
    }


    public String dump(int segment) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < segmentSize; i += BYTES_PER_INT) {
            int value = this.readWord(segment, i);
            if (value != 0) {
                if (sb.length() == 0) {
                    sb.append("Dump of Segment:" + segment + "\n");
                }
                sb.append(i + ":" + value + "\n");
            }
        }
        return sb.toString();
    }

}
