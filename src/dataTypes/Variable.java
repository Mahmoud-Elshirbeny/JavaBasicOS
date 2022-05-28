package dataTypes;

public class Variable {
	public Object value;
	public String name;
	public Process owner;
	
	public Variable(String name,Object value, Process owner) {
		this.value=value;
		this.name=name;
		this.owner=owner;
	}
}
