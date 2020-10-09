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
		} else {
			process.pcb.setState("WAIT");
		}
	}
	
	//move all processes from NEW to either READY or WAIT
	public void initState(ArrayList<Process> readyQueue) {
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
//						waitingQueue.add(readyQueue.get(i));
//						readyQueue.remove(readyQueue.get(i));
					}
				break;
				default:
					System.out.println("Error");
					break;
			}
		
		}
	}
	
	public void terminateProcess(Process process) {
		process.pcb.setState("TERMINATED");
		memLeft += process.pcb.getMemRequirement();
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
			readyQueue.remove(process);
			//stay in waiting queue for n cycles
			for(int i = 1; i <= numCycles; i++) {
				clock.count();
				process.setNumCycles(process.getNumCycles() - 1);
			}
			//move process back to readyQueue once finished
			readyQueue.add(process);
			waitingQueue.remove(process);
			
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
	
	//generate random number of cycles within the range
	public int operationCycle(int min, int max) {
		return (int)(Math.random() * (max-min)) + min;
	}
}
