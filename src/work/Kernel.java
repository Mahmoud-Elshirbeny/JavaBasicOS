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
import dataTypes.Process;
import dataTypes.Variable;

public class Kernel {
	
	public Mutex file; 
	public Mutex userInput; 
	public Mutex userOutput;
	public Memory memory;
	public Parser parser;
	public Scheduler scheduler;
	public Interpeter interpeter;
	
	public Kernel() {
		file = new Mutex();
		userInput = new Mutex();
		userOutput = new Mutex();
		memory = new Memory();
		parser = new Parser();
		scheduler= new Scheduler();
		interpeter = new Interpeter();
	}
	
	public void createProcess(String name, int time) {
		ArrayList<String[]> commands = parser.result(name);
		Process p = new Process(name,commands);
		scheduler.add(p, time);
	}
	
	public void startSystem() {
		scheduler.run(this);
	}
	
	public boolean executeNextCommand(Process p) {
		if(p.pointer<p.commands.size()) {
		p.print(scheduler.timeSlice);
		boolean done = interpeter.decode(p.nextCommand(), p, this);
		if(done) p.pointer++;	
		}
		boolean processDone = (p.pointer>=p.commands.size());
		return processDone;
	}
	
	public Object input() {
		Scanner sc= new Scanner(System.in);    //System.in is a standard input stream  
		System.out.print("Enter your input: ");  
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i<text.size();i++) {
			result +=text.get(i) + "\r\n";
		}

		return result;
	}
	
	public void assign(Process p, String desiredName, Object value) {
		for(int i = 0; i<memory.content.size();i++) {
			if (memory.content.get(i).name.equals(desiredName)&&memory.content.get(i).owner.equals(p)) {
				memory.content.get(i).value=value;
				return;
			}
		}
		Variable v = new Variable(desiredName, value, p);
		memory.add(v);
		
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
		for(int i = 0;i < memory.content.size();i++) {
			if(memory.content.get(i).name.equals(name)&&memory.content.get(i).owner.equals(p)) {
				return memory.content.get(i).value;
			}
		}
		return name;
	}

	public static void main(String[] args) {
		Kernel k = new Kernel();
		System.out.println(k.readFile("Program_1.txt"));
	}
}
