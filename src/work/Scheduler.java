package work;
import java.util.*;

import dataTypes.*;
import dataTypes.Process;


public class Scheduler {
	public Process current;
	public Queue<Process> ready;
	public Queue<Process> blocked;
	public PriorityQueue<UpcomingProcess> newProcess;
	public final int slicePerProcess;
	public int timeSlice;
	public int clockCycles;
	public boolean printChosen = false;
	public Kernel kernel;
	
	public Scheduler() {
		current = null;
		ready =  new LinkedList<Process>();
		blocked  = new LinkedList<Process>();
		newProcess = new PriorityQueue<UpcomingProcess>();
		slicePerProcess = 2;
		timeSlice = slicePerProcess;
		clockCycles = 0;
	}
	
	public void add(Process P, int time) {
		UpcomingProcess uP = new UpcomingProcess(P,time);
		newProcess.add(uP);
	}
	
	public void run(Kernel kernel) {
		this.kernel=kernel;
		while((!ready.isEmpty())||(!blocked.isEmpty())||(!newProcess.isEmpty())||current!=null) {
			boolean done = false;
			System.out.println("Clock Cycle: " + clockCycles);
			System.out.println("------------");
			kernel.printMem();
			System.out.println("-------------------------------------------------------------------");
			
			if(newProcess.peek()!=null&&newProcess.peek().time==clockCycles) {
				Process npp=newProcess.remove().proc;
				ready.add(npp);
				System.out.println("Added process " + npp.name + " to the ready queue.");
				System.out.println("------------");
				kernel.createProcess(npp);
			}
			
			clockCycles++;
			if(current==null && !ready.isEmpty()) { 
				pickNewProcess();
			}
			timeSlice--;
			if(current!=null) {
				kernel.changeProcessState(current.id, "EXECUTING");
				boolean processDone = kernel.executeNextCommand(current);
				if(processDone) {
					System.out.println("Process: " + current.name + " Finished.");
					System.out.println("------------");
					kernel.changeProcessState(current.id, "FINISHED");
					printQueues();
					printChosen = false;
					pickNewProcess();
				}
				else{if(timeSlice == 0) {
					ready.add(current);
					kernel.changeProcessState(current.id, "READY");
					done = pickNewProcess();
				}}
				System.out.println("-------------------------------------------------------------------");
			}
			if(!done) {
			kernel.printMem();
			System.out.println("-------------------------------------------------------------------");
			
			kernel.printDisk();
			System.out.println("-------------------------------------------------------------------");
			
			kernel.contin();
			System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
			
			
			}
		}
	}
	
	public void addBlocked(Process p) {
		blocked.add(p);
		System.out.println(p.name + " got blocked.");
		System.out.println("------------");
		kernel.changeProcessState(p.id, "BLOCKED");
		printQueues();
		printChosen = false;
		pickNewProcess();
		
	}
	
	public void removeBlocked(Process p, String mutexName) {
		blocked.remove(p);
		System.out.println("Unblocked process:  " + p.name + ", And gave it mutex: " + mutexName +".");
		System.out.println("------------");
		kernel.changeProcessState(p.id, "READY");
		ready.add(p);
	}
	
	public boolean pickNewProcess() {
		if(ready.isEmpty()&&newProcess.isEmpty()) {
			System.out.println("Done.");
			current=null;
			return true;
		}
		if(ready.isEmpty()) {
			current=null;
			return false;
		}
		System.out.println("Before choosing new process:");
		System.out.println();
		printQueues();
		
		current=ready.remove();
		timeSlice = slicePerProcess;
		System.out.println("After choosing new process:");
		System.out.println();
		printQueues();
		return false;
	}
	public void printQueues() {
		if(ready.isEmpty()&&blocked.isEmpty()&&current==null)
			return;
		if(current!=null&&printChosen) {
			System.out.println("Chosen: " + current.name);
			kernel.changeProcessState(current.id, "EXECUTING");
			System.out.println("------------");
		}
		System.out.println("Ready Queue: ");
		if(ready.isEmpty()) System.out.println("Empty.");
		for(Process s : ready) { 
			  System.out.println(s); 
			}
		System.out.println("------");
		System.out.println("Blocked Queue: ");
		if(blocked.isEmpty()) System.out.println("Empty.");
		for(Process s : blocked) { 
			  System.out.println(s); 
			}
		System.out.println("------------");
		
		printChosen=!printChosen;
	}
	
}
