package il.ac.telhai.os.hardware;

import il.ac.telhai.os.software.InterruptHandler;
import il.ac.telhai.os.software.OperatingSystem;
import il.ac.telhai.os.software.Software;
import il.ac.telhai.os.software.language.Instruction;
import il.ac.telhai.os.software.language.Program;
import il.ac.telhai.os.software.language.Registers;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cmshalom
 */
public class CPU implements Clockeable {

    private Clock clock;
    private Registers registers = new Registers();
    private Memory realMemory;
    private Software running;

    private Map<Class<? extends InterruptSource>, InterruptHandler> interruptVector = new HashMap<>();
    private InterruptSource pendingInterrupt;

    public CPU(Clock clock, RealMemory realMemory) {
        this.clock = clock;
        clock.addDevice(this);
        this.realMemory = realMemory;
    }

    @Override
    public void tick() {
        if (registers.getFlag(Registers.FLAG_HALTED)) {
            clock.shutdown();
            return;
        }


        if (pendingInterrupt != null) {
            InterruptSource source = pendingInterrupt;
            pendingInterrupt = null;
            InterruptHandler handler = getHandler(source);
            if (handler != null) {
                registers.setFlag(Registers.FLAG_USER_MODE, false);
                handler.handle(source);
            }
        }


        if (running != null) {
            // This allows us to run either an Operating system written in Java,
            // or a program written in an Assembly Language
            if (running instanceof OperatingSystem) {
                ((OperatingSystem) running).step();
            } else {
                Instruction instruction = ((Program) running).fetchLine(registers);
                instruction.execute(registers, realMemory);
            }
        }

    }

    public void execute(Instruction instruction) {
        instruction.execute(registers, realMemory);
    }

    public void setInterruptHandler(Class<? extends InterruptSource> cls, InterruptHandler handler) {
        interruptVector.put(cls, handler);

    }

    public void interrupt(InterruptSource source) {
        pendingInterrupt = source;
    }


    public void contextSwitch(Software running) {
        this.running = running;
    }

    public String getRegisters() {
        return registers.toString();
    }


    private InterruptHandler getHandler(InterruptSource source) {
        InterruptHandler result = null;

        Class cls = source.getClass();
        while (result == null && cls != null) {
            result = interruptVector.get(cls);
            cls = cls.getSuperclass();
        }
        return result;
    }

}