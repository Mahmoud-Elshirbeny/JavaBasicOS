package work;

import dataTypes.Process;

public class Interpeter {
	
	public Interpeter() {
		
	}
	
	public boolean decode(Object[] line, Process process, Kernel Kernel) {
		boolean done = true;
		switch((String)line[0]) {
		case "assign":
			switch ((String)line[2]) {
			case "input":
				line[2]=(String)Kernel.input();
				done = false;
				break;
			case "readFile":
				String path = (String)line[3];
				line[2] = Kernel.readFile(path);
				done = false;
				break;
			default:
				Kernel.assign(process, (String)line[1], line[2]);
			}
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
