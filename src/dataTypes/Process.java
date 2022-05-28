package dataTypes;

import java.util.ArrayList;

public class Process {
	public static int counter = 0;
	public String name;
	public int pointer;
	public ArrayList<String[]> commands;
	public int id;
	
	public Process(String name ,ArrayList<String[]> commands) {
		counter ++;
		this.name=name;
		this.pointer = 0;
		this.commands=commands;
		id = counter;
	}
	
	public Object[] nextCommand() {
		return commands.get(pointer);
	}
	
	public void print(int time) {
		String command = "";
		for(int i = 0; i<commands.get(pointer).length;i++) {
			command += commands.get(pointer)[i] + " ";
		}
		
		System.out.println("Process Execution:: " + "Name: " + name + ". Current Instruction: " + command + ". Cycles left: " + time);
		System.out.println("------------");
	}
	
	public String toString() {
		return (name);
	}
	
}
