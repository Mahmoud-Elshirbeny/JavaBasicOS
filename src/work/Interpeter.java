package work;

import dataTypes.Process;

public class Interpeter {
	
	public Interpeter() {
		
	}
	
	public boolean decode(String command, Process process, Kernel Kernel) {
		boolean done = true;
		String[] line = command.split(" ");
		switch((String)line[0]) {
			case "input":
				String input =(String)Kernel.input();
				Kernel.addVariable(process.id, "@temp", input);
				break;
			case "readFile":
				String path = (String)line[1];
				String file = Kernel.readFile(path);
				Kernel.addVariable(process.id, "@temp", file);
				break;
			case "assign":
				if(((String)line[2]).equals("@temp")) {
					line[2]=(String) Kernel.variableToValue("@temp", process);
				}	
				Kernel.assign(process, (String)line[1], line[2]);
				break;
			case "writeFile": 
				Kernel.writeFile((String)line[1], line[2]);
				break;
			case "printFromTo":
				Kernel.printFromTo(((String)line[1]),((String)line[2]));
				break;
			case "print": 
				Kernel.print(line[1]);
				break;
			case "semWait":
				Kernel.semWait(process, (String)line[1]);
				break;
			case "semSignal":
				Kernel.semSignal(process, (String)line[1]);
				break;
			}
		
		return done;
	}

}
