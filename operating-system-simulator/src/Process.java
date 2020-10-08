public class Process {
	
	public PCB pcb;
	public int numCycles; //number of cycles to run/left to run
	public String currLine; //the line in operation
	
	public Process() {
		pcb = new PCB();
	}
	
	public Process(String name, int pid, String state, int arrivalTime, int burstTime, int memRequirement, int memAddress) {		
		pcb = new PCB(name, pid, state, arrivalTime, burstTime, memRequirement, memAddress);
	}
	
	public void setNumCycles(int numCycles) {
		this.numCycles = numCycles;
	}
	
	public int getNumCycles() {
		return numCycles;
	}
	
	public void setCurrLine(String line) {
		this.currLine = line;
	}
	
	public String getCurrLine() {
		return currLine;
	}
	
	@Override
	public String toString() {
		return "PROCESS: name = " + pcb.name
				+ ", pid = " + pcb.pid
				+ ", memoryRequirement = " + pcb.memRequirement
				+ ", burstTime = " + pcb.burstTime
				+ ", arrivalTime = " + pcb.arrivalTime
				+ ", waitTime = " + pcb.waitTime
				+ ", turnAroundTime = " + pcb.turnAroundTime 
				+ ", numCycles = " + numCycles
				+ "\n";
	}
}

