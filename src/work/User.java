package work;

public class User {
	public static void main(String[] args) {
		Kernel k=new Kernel();
		k.addNewProcess("Program_1.txt", 0);
		k.addNewProcess("Program_2.txt", 1);
		k.addNewProcess("Program_3.txt", 4);
		k.startSystem();
	}
}
