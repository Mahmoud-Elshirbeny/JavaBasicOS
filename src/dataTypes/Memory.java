package dataTypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Memory {
	public boolean left = true;
	public Word[] content;

	public Memory() {
		content = new Word[40];
	}

	public String returnNextLine(int id) {
		int lowerBound = -1;
		if (content[0] != null && String.valueOf(content[0].value).equals(String.valueOf(id))) {
			lowerBound = 0;
		} else {
			if (content[20] != null && Integer.valueOf(String.valueOf(content[20].value)) == id) {
				lowerBound = 20;
			}
		}
		if (lowerBound == -1) {

			Word[] old = swapToMemory();
			if (left) {
				swapToDisk(20);
				left = false;
				lowerBound = 20;
			} else {
				swapToDisk(0);
				left = true;
				lowerBound = 0;
			}
			System.out.println("Process "+ id+ " was swapped to Memory.");
			System.out.println("------------");
			for (int i = 0; i < 20; i++) {
				content[i + lowerBound] = old[i];
			}
			content[lowerBound+3].value=lowerBound;
			content[lowerBound+4].value=lowerBound+19;
			

		}
		int pc = (int) Integer.parseInt((String.valueOf(content[lowerBound + 2].value)));
		if (pc == 20 || content[pc + lowerBound] == null)
			return "done";
		String line = (String) content[pc + lowerBound].value;
		pc++;
		content[lowerBound + 2].value = pc;
		content[lowerBound + 1].value = "EXECUTING";
		return line;
	}

	public void storeVariable(int id, String name, String value) {
		Word w = new Word(name, value, id);
		int lowerBound = 0;
		if (Integer.valueOf(String.valueOf(content[20].value)) == (id)) {
			lowerBound = 20;
		}
		for (int i = lowerBound + 5; i < lowerBound + 8; i++) {
			if (content[i] == null || content[i].name == name) {
				content[i] = w;
				return;
			}
		}
	}

	public void storeTemp(int id, String value) {
		Word w = new Word("@temp", value, id);
		if (Integer.valueOf(String.valueOf(content[0].value)) == id) {
			content[8] = w;
		} else {
			content[28] = w;
		}
	}

	public String getValue(int id, String name) {
		int lowerBound = -1;
		if (content[0] != null && String.valueOf(content[0].value).equals(String.valueOf(id))) {
			lowerBound = 0;
		} else {
			if (content[20] != null && Integer.valueOf(String.valueOf(content[20].value)) == id) {
				lowerBound = 20;
			}
		}
		if (lowerBound == -1) {

			Word[] old = swapToMemory();
			if (left) {
				swapToDisk(20);
				left = false;
				lowerBound = 20;
				old[3].value = 20;
				old[4].value = 39;
				
			} else {
				swapToDisk(0);
				left = true;
				lowerBound = 0;
				old[3].value = 0;
				old[4].value = 19;
			}
			System.out.println("Process "+ id+ " was swapped to Memory.");
			System.out.println("------------");
			for (int i = 0; i < 20; i++) {
				if (old[i] == null) {
					content[i + lowerBound] = null;
				}
				content[i + lowerBound] = old[i];
			}

		}
		for (int i = lowerBound + 5; i <= lowerBound + 9; i++) {
			if (content[i] != null && content[i].name.equals(name) && content[i].id == id) {
				return (String) content[i].value;
			}
		}
		return name;
	}

	public void addProcess(int id, String state, int pc, Object[] commands) {

		int lowerBoundV = 0;
		if (content[0] == null) {
			left = true;
		} else {
			if (content[20] == null) {
				lowerBoundV = 20;
				left = false;
			} else {
				if (left) {
					swapToDisk(20);
					lowerBoundV = 20;
					left=false;
				} else {
					swapToDisk(0);
					lowerBoundV = 0;
					left=true;
				}
			}
		}

		content[lowerBoundV + 0] = new Word("id", id, id);
		content[lowerBoundV + 1] = new Word("state", state, id);
		content[lowerBoundV + 2] = new Word("pc", pc, id);
		content[lowerBoundV + 3] = new Word("lowerBound", lowerBoundV, id);
		content[lowerBoundV + 4] = new Word("upperBound", lowerBoundV + 19, id);
		content[lowerBoundV + 5] = null;
		content[lowerBoundV + 6] = null;
		content[lowerBoundV + 7] = null;
		content[lowerBoundV + 8] = null;
		for (int i = 0; i < commands.length; i++) {
			content[lowerBoundV + 9 + i] = new Word("line " + i, commands[i], id);
		}
		
		System.out.println("Process "+ id+ " was added to memory.");
		System.out.println("------------");
	}

	public void swapToDisk(int index) {
		String result = "";
		String id =String.valueOf(content[index].value) ;
		for (int i = index; i < index + 20; i++) {
			if (content[i] == null) {
				result += " " + "\r\n";
				continue;
			}
			result += content[i].name + ":" + content[i].value + "\r\n";
		}
		String name = "disk.txt";
		String data = result;
		File theFile = new File(name);
		try {
			theFile.createNewFile();
			FileWriter writer = new FileWriter(name);
			writer.write((String) data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print("error");
		}
		System.out.println("Process "+ id+ " was swapped to Disk.");
		System.out.println("------------");
		
		
		
	}

	public Word[] swapToMemory() {
		ArrayList<String> file = new ArrayList<String>();
		String currentLine = "";
		FileReader fileReader;
		int id = 0;
		try {
			fileReader = new FileReader("disk.txt");
			BufferedReader br = new BufferedReader(fileReader);
			while ((currentLine = br.readLine()) != null) {
				file.add(currentLine);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Object[] fileArray = (Object[]) file.toArray();
		Word[] mem = new Word[20];
		String[] idWord = ((String) fileArray[0]).split(":");
		id = Integer.parseInt(idWord[1]);
		for (int i = 0; i < 20; i++) {
			if (fileArray[i].equals(" ")) {
				mem[i] = null;
			} else {
				String[] valued = ((String) fileArray[i]).split(":");
				mem[i] = new Word(valued[0], valued[1], id);
			}
		}
		
		return mem;

//		if(index==0) {
//			left =true;
//		}
//		else {
//			left = false;
//		}

	}
	
	public Word[] readDisk() {
		ArrayList<String> file = new ArrayList<String>();
		String currentLine = "";
		FileReader fileReader;
		int id = 0;
		try {
			fileReader = new FileReader("disk.txt");
			BufferedReader br = new BufferedReader(fileReader);
			while ((currentLine = br.readLine()) != null) {
				file.add(currentLine);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}

		Object[] fileArray = (Object[]) file.toArray();
		Word[] mem = new Word[20];
		if(fileArray.length==0)
			return null;
		String[] idWord = ((String) fileArray[0]).split(":");
		id = Integer.parseInt(idWord[1]);
		for (int i = 0; i < 20; i++) {
			if (fileArray[i].equals(" ")) {
				mem[i] = null;
			} else {
				String[] valued = ((String) fileArray[i]).split(":");
				mem[i] = new Word(valued[0], valued[1], id);
			}
		}
		return mem;
	}

	public void print() {
		System.out.println("Memory: [");
		for (int i = 0; i < 40; i++) {
			if (content[i] == null) {
				System.out.println("Word " + i + "::  empty");
				continue;
			}
			System.out.println(
					"Word " + i + "::  " + content[i].name + ": " + content[i].value + " || PID: " + content[i].id);
		}
		System.out.println("]");
	}
	
	public void changeProcessState(int id, String state) {
		int lowerBound = -1;
		if (content[0] != null && String.valueOf(content[0].value).equals(String.valueOf(id))) {
			lowerBound = 0;
		} else {
			if (content[20] != null && Integer.valueOf(String.valueOf(content[20].value)) == id) {
				lowerBound = 20;
			}
		}
		if (lowerBound == -1) {
			Word[] disk = readDisk();
			if (disk==null) return;
			disk[1].value=state;
			writeToDisk(disk);
		}
		else {
			content[lowerBound+1].value=state;
		}
		
	}
	
	public void writeToDisk(Word[] w) {
		String result = "";
		for (int i = 0; i < 20; i++) {
			if (w[i] == null) {
				result += " " + "\r\n";
				continue;
			}
			result += w[i].name + ":" + w[i].value + "\r\n";
		}
		String name = "disk.txt";
		String data = result;
		File theFile = new File(name);
		try {
			theFile.createNewFile();
			FileWriter writer = new FileWriter(name);
			writer.write((String) data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print("error");
		}
	}
	public void printDisk() {
		Word[] disk = readDisk();
		if(disk==null) {
			System.out.println("Disk is Empty.");
			return;
		}
		System.out.println("Disk: [");
		for (int i = 0; i < 20; i++) {
			if (disk[i] == null) {
				System.out.println("Word " + i + "::  empty");
				continue;
			}
			System.out.println(
					"Word " + i + "::  " + disk[i].name + ": " + disk[i].value + "|| PID: " + disk[i].id);
		}
		System.out.println("]");
	}
	}
