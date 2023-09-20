package org.geri.instructions.model;


public interface Instruction {

	void readInstruction(String input);
	InstructionType getType();
}
