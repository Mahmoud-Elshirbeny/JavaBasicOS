package dataTypes;


import java.util.*;

public class Memory {
	public ArrayList<Variable> content;
	
	public Memory() {
		content = new ArrayList<Variable>();
	}
	
	public void add(String name, Object value, Process op) {
		Variable v = new Variable(name,value,op);
		content.add(v);
	}
	public void add(Variable v) {
		content.add(v);
	}
	
	public Object retrieve(String code, Process op) {
		Variable[] temp =(Variable[]) content.toArray();
		for(int i = 0; i<temp.length;i++) {
			if (temp[i].name.equals(code) &&temp[i].owner.equals(op)) {
				return temp[i].value;
			}
		}
		return code;
	}
}
