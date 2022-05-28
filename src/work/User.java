package work;

public class User {
	public static void main(String[] args) {
		Kernel k=new Kernel();
		k.createProcess("Program_1.txt", 5);
		k.createProcess("Program_2.txt", 0);
		k.createProcess("Program_3.txt", 2);
		k.startSystem();
	}
}
