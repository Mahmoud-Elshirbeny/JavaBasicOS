package dataTypes;


public class Process {
	public String name;
	public String[] pcb;
	public int id;
	public int start;
	
	public Process(String name,int id) {
		this.name=name;
		this.id=id;
	}
	
	public void print(int time) {
		
		System.out.println("Process Execution:: " + "Name: " + name + ". Cycles left: " + time);
		System.out.println("------------");
	}
	
	public String toString() {
		return (name);
	}
	
}
