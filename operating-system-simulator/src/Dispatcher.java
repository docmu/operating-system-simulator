import java.util.ArrayList;

// responsible for moving processes between queues, context switch with PCBs, & changing the state of processes
public class Dispatcher {
	
	private final int MAX_MEMORY = 2048;
	private int memUsage;
	private int memLeft;
	private ArrayList<Process> waitingQueue = new ArrayList<Process>();
	private ArrayList<Process> readyQueue;
	private Clock clock = new Clock();
	
	public void updateState(Process process) {
		if(memLeft <= MAX_MEMORY) {
			process.pcb.setState("READY");
			//move back to readyQueue if process was previously in waitingQueue
//			if(!readyQueue.contains(process) && waitingQueue.contains(process)) {
//				readyQueue.add(process);
//				waitingQueue.remove(process);
//			}
		} else {
			process.pcb.setState("WAIT");
			//move back to waitingQueue if process was previously in readyQueue
//			if (readyQueue.contains(process) && !waitingQueue.contains(process)) {
//				waitingQueue.add(process);
//				readyQueue.remove(process);
//			}
		}
	}
	
	//move all processes from NEW to either READY or WAIT
	public ArrayList<Process> initState(ArrayList<Process> readyQueue) {
		this.readyQueue = readyQueue;
		for(int i = 0; i < readyQueue.size(); i++) {
			switch (readyQueue.get(i).pcb.getState()) {
				case "NEW":
					memUsage = readyQueue.get(i).pcb.getMemRequirement();
					memLeft = MAX_MEMORY - memUsage;
					if(memLeft <= MAX_MEMORY) {
						readyQueue.get(i).pcb.setState("READY");
					} else {
						readyQueue.get(i).pcb.setState("WAIT");
						//not enough memory, move to waiting queue
						waitingQueue.add(readyQueue.get(i));
						readyQueue.remove(readyQueue.get(i));
					}
				break;
				default:
					System.out.println("Error");
					break;
			}
		
		}
		return readyQueue;
	}
	
	public void terminateProcess(Process process) {
		process.pcb.setState("TERMINATED");
		readyQueue.remove(process);
		memLeft += process.pcb.getMemRequirement();
	}
	
	
	public void executeCriticalSection(Process process, String[] line) {
		//entry section
		execute(process, line); //critical section
		//exit section
	}
	
	//execute command by line
		public void execute(Process process, String[] line) {
			int min = Integer.parseInt(line[1]);
			int max = Integer.parseInt(line[2]);
			int numCycles = operationCycle(min, max);
			process.setNumCycles(numCycles);

			if(line[0].equals("I/O")) {
				//update state to WAIT
				process.pcb.setState("WAIT");
				//move process to waiting queue
				waitingQueue.add(process);
//				readyQueue.remove(process);
				//update state to RUN
				process.pcb.setState("RUN");
				//stay in waiting queue for n cycles until it finishes running
				for(int i = 1; i <= numCycles; i++) {
					clock.count();
					process.setNumCycles(process.getNumCycles() - 1);
				}
			} else if(line[0].equals("CALCULATE")) {
				//update state to RUN
				process.pcb.setState("RUN");
				//stays on cpu for n cycles
				for(int i = 1; i <= numCycles; i++) {
					clock.count();
					process.setNumCycles(process.getNumCycles() - 1);
				}
			}
			
			//update state
			updateState(process);
			//reset the clock
			clock.reset();
		}
	
	//execute command by line
//	public void execute(Process process, String[] line) {
//		int min = Integer.parseInt(line[1]);
//		int max = Integer.parseInt(line[2]);
//		int numCycles = operationCycle(min, max);
//		process.setNumCycles(numCycles);
//		
//		if(line[0].equals("I/O")) {
//			//update state to WAIT
//			process.pcb.setState("WAIT");
//			//move process to waiting queue
////			waitingQueue.add(process);
////			readyQueue.remove(process);
//			//update state to RUN
//			process.pcb.setState("RUN");
//			//stay in waiting queue for n cycles until it finishes running
//			for(int i = 1; i <= numCycles; i++) {
//				clock.count();
//				process.setNumCycles(process.getNumCycles() - 1);
//			}
//			//done executing, put back on readyQueue
//			readyQueue.add(process);
//		} else if(line[0].equals("CALCULATE")) {
//			//update state to RUN
//			process.pcb.setState("RUN");
//			//stays on cpu for n cycles
//			for(int i = 1; i <= numCycles; i++) {
//				clock.count();
//				process.setNumCycles(process.getNumCycles() - 1);
//			}
//		}
//		
//		//update state
////		updateState(process);
//		//increment line number
//		process.incrementLineNum();
//		//reset the clock
//		clock.reset();
//	}
	
	//generate random number of cycles within the range
	public int operationCycle(int min, int max) {
		return (int)(Math.random() * (max-min)) + min;
	}
}
