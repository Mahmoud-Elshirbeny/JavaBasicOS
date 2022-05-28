package dataTypes;

public class UpcomingProcess implements Comparable<UpcomingProcess> {
	public Process proc;
	public int time;
	
	public UpcomingProcess(Process proc,int time) {
		this.proc=proc;
		this.time=time;
	}

	@Override
	public int compareTo(UpcomingProcess o) {
		// TODO Auto-generated method stub
		return this.time-o.time;
	}
}
