
public class PCB {
	public int pid;
	public String state;
	
	public int priority; //priority of process
	public int burstTime; //time required by a process for execution on the CPU
	public int arrivalTime; //time in which process arrives in ready queue (in READY state)
	public int waitTime; //Turn Around Time - Burst Time
	public int completionTime; //time in which process completes its execution	
	public int turnAroundTime; //Completion time - Arrival Time
	
	public int memRequirement;
	public int memAddress;
	
	public PCB() {
		pid = (int) ((Math.random() * (Integer.MAX_VALUE - 1)) + 1);
		state = "NEW";
		priority = 0;
		burstTime = 0;
		arrivalTime = 0;
		waitTime = 0;
		completionTime = 0;
		memRequirement = 0;
	}
	
	public PCB(String name, int pid, String state, int arrivalTime, int burstTime, int memRequirement) {
		this.pid = pid;
		this.state = state;
		this.arrivalTime = arrivalTime;
		this.burstTime = burstTime;
		this.memRequirement = memRequirement;
	}
	
	public void setPID(int pid) {
		this.pid = pid;
	}
	
	public int getPID() {
		return this.pid;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getState() {
		return this.state;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public int getArrivalTime() {
		return arrivalTime;
	}
	
	public void setBurstTime(int burstTime) {
		this.burstTime = burstTime;
	}
	
	public int getBurstTime() {
		return burstTime;
	}
	
	public void setTurnAroundTime(int tat) {
		this.turnAroundTime = tat;
	}
	
	public int getTurnAroundTime() {
		return turnAroundTime;
	}
	
	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}
	
	public int getWaitTime() {
		return waitTime;
	}
	
	public int getCompletionTime() {
		return completionTime;
	}
	
	public void setCompletiontime(int completionTime) {
		this.completionTime = completionTime;
	}
	
	public void setMemRequirement(int memRequirement) {
		this.memRequirement = memRequirement;
	}
	
	public int getMemRequirement() {
		return this.memRequirement;
	}
	
	public void setMemAddress(int memAddress) {
		this.memAddress = memAddress;	
	}
	
	public int getMemAddress() {
		return this.memAddress;
	}
}

