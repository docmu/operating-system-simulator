import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
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
	
	// set state to TERMINATED and remove it from the readyQueue
	public void terminateProcess(Process process) {
		process.pcb.setState("TERMINATED");
		System.out.println(process);
		readyQueue.remove(process);
		memLeft += process.pcb.getMemRequirement();
		
		int i = readyQueue.size() - 1;
		//cascading termination of child processes
		while(readyQueue.size() > 0) {
			if(i < 1) break;
			//if the parent process has child processes, terminate it and remove from the readyQueue
			if(readyQueue.get(i).getName().contains(
					process.getName().substring(0, process.getName().length()-4))) {
				readyQueue.get(i).pcb.setState("TERMINATED");
				System.out.println(readyQueue.get(i));
				readyQueue.remove(readyQueue.get(i));
			} 
			i--;
		}
	}
	
	
	public void executeCriticalSection(Process process, String[] line) {
		//when unlocked the critical section is available for execution
		boolean unlock = true;
		while(unlock) {
			//entry section
			execute(process, line); //critical section
			//exit section
			unlock = false;
		}
	}
	
	//creation of child process
	public void createChildProcess(Process process, int num) {
		//create the child process as a copy of the parent process
		Process childProcess = new Process();
		childProcess.setName(process.getName().substring(0, process.getName().length()-4) + "child" + Integer.toString(num) + ".txt");	
		childProcess.pcb.setMemRequirement(process.pcb.getMemRequirement());
		childProcess.pcb.setArrivalTime(process.pcb.getArrivalTime());
		childProcess.pcb.setBurstTime(process.pcb.getBurstTime());
		childProcess.pcb.setCompletiontime(process.pcb.getCompletionTime());
		childProcess.pcb.setTurnAroundTime(process.pcb.getTurnAroundTime());
		childProcess.pcb.setWaitTime(process.pcb.getWaitTime());
		
		//create the file
		createFile(childProcess.getName());
		
		//write to file
		writeToFile(childProcess.getName(), process.getName());
		
		//add the childProcess to the readyQueue
		readyQueue.add(childProcess);
	}
	
	//make a copy of the parent process without FORK instruction
	public void writeToFile(String childProcess, String parentProcess) {
		try {
			  PrintStream fileStream = new PrintStream(new File(childProcess));
		      BufferedReader br = new BufferedReader(new FileReader(parentProcess));
		      String line; 
			  while ((line = br.readLine()) != null) {
				  if(!line.contains("FORK")) {
					  fileStream.println(line);
				  }
			  }
		      fileStream.close();
		      
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
	//create new child program
	public void createFile(String childProcess) {
		try {
		      File file = new File("C:\\Users\\Ctint\\git\\operating-system-simulator\\operating-system-simulator\\" + childProcess);
		      if (file.createNewFile()) {
		        System.out.println("File created: " + file.getName());
		      } else {
		        System.out.println("File already exists.");
		      }
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
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
		} else if(line[0].equals("FORK")) {
			//update state to RUN
			process.pcb.setState("RUN");
			//generate n child processes
			for(int i = 1; i <= numCycles; i++) {
				createChildProcess(process, i);
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
