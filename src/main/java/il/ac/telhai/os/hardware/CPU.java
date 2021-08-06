package il.ac.telhai.os.hardware;

import il.ac.telhai.os.software.language.Instruction;
import il.ac.telhai.os.software.language.Program;
import il.ac.telhai.os.software.language.Registers;

public class CPU implements Clockeable {

    private Clock clock;
    private Registers registers = new Registers();
    private Memory realMemory;
    private Program program;

    // constructs a CPU and attaches it to the clock and memory
    public CPU(Clock clock, RealMemory realMemory) {
        this.clock = clock;
        this.realMemory = realMemory;

    }

    // CPU logic that should run every clock tick
    @Override
    public void tick() {
        if (!registers.getFlag(Registers.FLAG_HALTED)) {
            Instruction ins = program.fetchLine(registers);
            execute(ins);

        } else
            clock.shutdown();


    }

    public void execute(Instruction instruction) {
        instruction.execute(registers, realMemory);
    }

    public void interrupt(InterruptSource source) {
    }

    public void contextSwitch(Program program) {
        this.program = program;
    }

    public String getRegisters() {
        return registers.toString();
    }

}