package work;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import dataTypes.Memory;
import dataTypes.Process;

public class Kernel {
	
	public Mutex file; 
	public Mutex userInput; 
	public Mutex userOutput;
	public Memory memory;
	public Parser parser;
	public Scheduler scheduler;
	public Interpeter interpeter;
	public static int processCounter = 0;
	
	
	public Kernel() {
		file = new Mutex();
		userInput = new Mutex();
		userOutput = new Mutex();
		memory = new Memory();
		parser = new Parser();
		scheduler= new Scheduler();
		interpeter = new Interpeter();
	}
	
	public void addNewProcess(String name, int time) {
		processCounter++;
		Process p = new Process(name,processCounter);
		scheduler.add(p, time);
	}
	
	public void createProcess(Process p) {
		
		Object[] commands =parser.result(p.name);
		memory.addProcess(p.id, "READY", 9, commands);
	}
	
	public void addVariable(int id, String name, String value) {
		if(name.equals("@temp")) {
			memory.storeTemp(id, value);
		}
		else {
			memory.storeVariable(id, name, value);
		}
		
	}
	
	public void startSystem() {
		scheduler.run(this);
	}
	
	public boolean executeNextCommand(Process p) {
		String line = memory.returnNextLine(p.id);
		if(line=="done") return true;
		
		p.print(scheduler.timeSlice);
		System.out.println("EXECUTING: "+line);
		interpeter.decode(line, p, this);
		
		
		return false;
	}
	
	public Object input() {
		Scanner sc= new Scanner(System.in);    //System.in is a standard input stream  
		System.out.print("Enter your input: ");  
		Object a= sc.nextLine();
		
		return a;
		
	}
	
	public Object contin() {
		Scanner sc= new Scanner(System.in);    //System.in is a standard input stream  
		System.out.print("Press enter to continue.");  
		Object a= sc.nextLine();
		
		return a;
		
	}
	
	public String readFile(String path) {
		path =(String)variableToValue(path, scheduler.current);
		ArrayList<String> text = new ArrayList<String>();
		String result = "";
		String currentLine = "";
		FileReader fileReader;
		try {
			fileReader = new FileReader(path);
			BufferedReader br = new BufferedReader(fileReader);
			while ((currentLine = br.readLine()) != null) {
				text.add(currentLine);
				}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i<text.size();i++) {
			result +=text.get(i) + "\r\n";
		}

		return result;
	}
	
	public void assign(Process p, String desiredName, Object value) {
		memory.storeVariable(p.id, desiredName, (String)value);
	}
	
	public void writeFile(String n, Object d) {
		String name= (String)variableToValue((String)n,scheduler.current) + ".txt";
		String data = (String) variableToValue((String)d, scheduler.current);
		File theFile = new File(name);
		try {
			theFile.createNewFile();
			FileWriter writer = new FileWriter(name);
		    writer.write((String)data);
		    writer.close();   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print("error");
		}
		
	}
	
	public void printFromTo(String v1, String v2) {
		
		int a = Integer.parseInt((String)variableToValue(v1, scheduler.current));
		int b = Integer.parseInt((String)variableToValue(v2, scheduler.current));
		
		boolean smaller = true;
		if(a>b) {
			smaller = false;
		}
		
		if(smaller) {
			for(int i = a; i<=b;i+=1) {
				System.out.println(i);
			}
		}
		else {
			for(int i = a; i>=b;i-=1) {
				System.out.println(i);
			}
		}
	}
	
	public void semWait(Process p, String name) {
		boolean getBlocked=false;
		switch(name) {
		case "file": 
			getBlocked = file.semWait(p);
			break;
		case "userInput":
			getBlocked=userInput.semWait(p);
			break;
		case "userOutput":
			getBlocked=userOutput.semWait(p);
			break;
		}
		if(getBlocked) {
			scheduler.addBlocked(p);
		}
	}
	
	public void semSignal(Process p, String name) {
		Process unblocked = null;
		switch(name) {
		case "file": 
			unblocked = file.semSignal(p);
			break;
		case "userInput":
			unblocked=userInput.semSignal(p);
			break;
		case "userOutput":
			unblocked=userOutput.semSignal(p);
			break;
		}
		if(unblocked!=null) {
			scheduler.removeBlocked(unblocked, name);
		}
	}
	
	public void print(Object s) {
		s = variableToValue((String)s, scheduler.current);
		System.out.println(s);
	}
	
	public Object variableToValue(String name, Process p) {
		if(p==null) return name;
		name=memory.getValue(p.id, name);
		return name;
	}
	
	public void printMem() {
		memory.print();
	}
	
	public void changeProcessState(int id, String state) {
		memory.changeProcessState(id, state);
	}
	
	public void printDisk() {
		memory.printDisk();
	}

	public static void main(String[] args) {
		Kernel k = new Kernel();
		System.out.println(k.readFile("Program_1.txt"));
	}
}
