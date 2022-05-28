package work;
import java.io.BufferedReader;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner; // Import the Scanner class to read text files
import dataTypes.Process;

public class Parser {
	
	public Parser() {	
		
	}
	
	public ArrayList<String[]> result(String name){
		ArrayList<String> commands = new ArrayList<String>();
		ArrayList<String[]> finalCommands = new ArrayList<String[]>();
		String currentLine = "";
		FileReader fileReader;
		try {
			fileReader = new FileReader(name);
			BufferedReader br = new BufferedReader(fileReader);
			while ((currentLine = br.readLine()) != null) {
				commands.add(currentLine);
				// Parsing the currentLine String
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0; i<commands.size();i++) {
			String [] result= ((String)commands.get(i)).split(" ");
			finalCommands.add(result);
		}
		return finalCommands;
	}

public static void main(String[] args) {
	

}

}