public class Process {
	
	public PCB pcb;
	public String name;
	public int numCycles = 0;; //number of cycles to run/left to run
	public String currLine = ""; //the line in operation
	
	public Process() {
		pcb = new PCB();
	}
	
	public Process(String name, int pid, String state, int arrivalTime, int burstTime, int memRequirement) {		
		pcb = new PCB(name, pid, state, arrivalTime, burstTime, memRequirement);
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public void setNumCycles(int numCycles) {
		this.numCycles = numCycles;
	}
	
	public int getNumCycles() {
		return numCycles;
	}
	
//	public void setCurrLine(int lineNum) {
//		this.currLine = lineNum;
//	}
//	
//	public int getCurrLine() {
//		return currLine;
//	}
	public void setCurrLine(String line) {
		this.currLine = line;
	}
	
	public String getCurrLine() {
		return currLine;
	}
	
	@Override
	public String toString() {
		return "PROCESS: name = " + name
				+ ", pid = " + pcb.pid
				+ ", state = " + pcb.state
				+ ", memoryRequirement = " + pcb.memRequirement
				+ ", arrivalTime = " + pcb.arrivalTime
				+ ", burstTime = " + pcb.burstTime
				+ ", completionTime = " + pcb.completionTime
				+ ", turnAroundTime = " + pcb.turnAroundTime 
				+ ", waitTime = " + pcb.waitTime
				+ ", currLine = " + currLine
				+ ", numCycles = " + numCycles
				+ "\n";
	}
}

