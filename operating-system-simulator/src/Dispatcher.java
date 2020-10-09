import java.util.ArrayList;

// responsible for moving processes between queues, context switch with PCBs, & changing the state of processes
public class Dispatcher {
	
	private final int MAX_MEMORY = 2048;
	private int memUsage;
	private int memLeft;
	private ArrayList<Process> waitingQueue = new ArrayList<Process>();
	private ArrayList<Process> readyQueue;
	private Clock clock = new Clock();
	
	public void init(ArrayList<Process> readyQueue) {
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
					}
				case "READY":
//					System.out.println("in ready state");
					break;
//				case "RUN":
//					break;
				case "WAIT":
//					System.out.println("in wait state");
					break;
//				case "EXIT":
//					memUsage = readyQueue.get(i).pcb.getMemRequirement();
//					memLeft +=  memUsage;
//					break;
				default:
					System.out.println("Cannot do job");
					break;
		}
		
		}
	}
	
	//execute command by line
	public void execute(Process process, String[] line) {
		int min = Integer.parseInt(line[1]);
		int max = Integer.parseInt(line[2]);
		int numCycles = operationCycle(min, max);
		process.setNumCycles(numCycles);
		System.out.print(" set num cycles " + process.getNumCycles() + ":");
		if(line[0].equals("I/O")) {
			//move process to waiting queue
			waitingQueue.add(process);
			readyQueue.remove(process);
			//stay in waiting queue for n cycles
			for(int i = 1; i <= numCycles; i++) {
				clock.count();
				process.setNumCycles(process.getNumCycles() - 1);
				System.out.print(" " + process.getNumCycles());
			}
			System.out.println();
			//move process back to readyQueue once finished
			readyQueue.add(process);
			waitingQueue.remove(process);
			
		} else if(line[0].equals("CALCULATE")) {
			//stays on cpu for n cycles
			for(int i = 1; i <= numCycles; i++) {
				clock.count();
				process.setNumCycles(process.getNumCycles() - 1);
				System.out.print(" " + process.getNumCycles());
			}
			System.out.println();
		}
		//reset the clock
		clock.reset();
	}
	
	//generate random number of cycles within the range
	public int operationCycle(int min, int max) {
		return (int)(Math.random() * (max-min)) + min;
	}
}
