package work;

import java.util.LinkedList;
import java.util.Queue;
import dataTypes.Process;

public class Mutex {

	Queue<Process> blockedQ;
	Process owner;
	boolean taken;

	public Mutex() {
		blockedQ = new LinkedList<Process>();
		owner = null;
		taken = false;
	}

	// if return value == true then add to general blocked queue
	public boolean semWait(Process owner) {
		if (taken == false) {
			this.owner = owner;
			taken = true;
			return false;
		} else {
			blockedQ.add(owner);
			return true;
		}

	}

	// if return value == true then remove from general blocked queue and add to
	// ready

	public Process semSignal(Process owner) {
		if (owner.equals(this.owner)) {
			taken = false;
			if (!blockedQ.isEmpty()) {
				Process dequeuedProcess = blockedQ.remove();
				this.owner = dequeuedProcess;
				taken = true;
				return dequeuedProcess;
			}
		}
		return null;
	}
}