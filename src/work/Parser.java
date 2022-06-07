package work;
import java.io.BufferedReader;  // Import the File class  // Import this class to handle errors
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; // Import the Scanner class to read text files


public class Parser {
	
	public Parser() {	
		
	}
	
	public Object[] result(String name){
		ArrayList<String> commands = new ArrayList<String>();
		String currentLine = "";
		FileReader fileReader;
		try {
			fileReader = new FileReader(name);
			BufferedReader br = new BufferedReader(fileReader);
			while ((currentLine = br.readLine()) != null) {
				
				String [] splitLine= currentLine.split(" ");
				if(splitLine.length>2){
					if (splitLine[2].equals("input")) {
					commands.add(splitLine[2]);
					commands.add(splitLine[0]+" "+ splitLine[1]+" "+"@temp");
					continue;
					}
					else {if(splitLine[2].equals("readFile")) {
						commands.add(splitLine[2]+" "+splitLine[3]);
						commands.add(splitLine[0]+" "+ splitLine[1]+" "+"@temp");
						continue;
					}
					}
					
				}
				
				commands.add(currentLine);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return commands.toArray();
	}
}