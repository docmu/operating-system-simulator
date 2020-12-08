import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Simulator{
	public static Process process;
	public static ArrayList<Process> readyQueue = new ArrayList<Process>();
	public static Dispatcher dispatcher = new Dispatcher();
	public static ArrayList<Thread> threads = new ArrayList<Thread>();
	
	public static void main(String[] args) throws IOException {	
	
		System.out.println("Enter the number of processes: ");	
		Scanner in = new Scanner(System.in);
		int numProcesses = in.nextInt();
		
		for(int i = 0; i < numProcesses; i++) {
			System.out.println(i + ": Enter the program name: ");
			in = new Scanner(System.in);
			String program = in.next();
			initProcess(program);	
			readyQueue.add(process);
		}
		
		System.out.println("Choose a scheduler:" + "\n1:Short Term Scheduler" + "\n2:Medium Term Scheduler");
		in = new Scanner(System.in);
		int schedulerType = in.nextInt();
		
		switch(schedulerType) {
			case 1: 
				shortTermScheduling();
				break;
			case 2:
				mediumTermScheduling();
				break;
		}
		
		readFile();
	}
	
	// initialize a new process 
	public static void initProcess(String fileName) throws FileNotFoundException, IOException {
		if(!fileName.contains(".txt")) {
			fileName += ".txt";
		}
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			process = new Process();
		    process.setName(fileName);
		    String memReq = br.readLine();
		    process.pcb.setMemRequirement(Integer.parseInt(memReq));
		    // assign a thread for each process
		 	threads.add(new Thread());
		}
	}
	
	//invoke the short term scheduling algorithm
	public static void shortTermScheduling() {	
		ShortTermScheduler shortTermScheduler = new ShortTermScheduler();
		System.out.println("Choose a scheduling algorithm:" + "\n1:Shortest Job First" + "\n2:Priority Scheduling");
		Scanner scan = new Scanner(System.in);
		int schedulingAlgorithm = scan.nextInt();
		switch(schedulingAlgorithm) {
			case 1:
				shortTermScheduler.SJF(readyQueue);
				break;
			case 2:
				shortTermScheduler.PriorityScheduling(readyQueue);
				break;
		}
		readyQueue = dispatcher.initState(readyQueue);
	}
	
	// invoke the medium term scheduler
	public static void mediumTermScheduling() {
		MediumTermScheduler mediumTermScheduler = new MediumTermScheduler(readyQueue);
		readyQueue = dispatcher.initState(readyQueue);
	}
	
	//execute commands
	@SuppressWarnings("deprecation")
	public static void readFile() throws FileNotFoundException, IOException {
		BufferedReader br = null; 
		//loop through all processes & execute the process at the head of the queue
		while(readyQueue.size() > 0) {
			Process process = readyQueue.get(0);
			Thread thread = threads.get(0);
			// start the thread
			thread.start();
			br = new BufferedReader(new FileReader(process.getName()));
			String line; 
			//read each line of process
			while ((line = br.readLine()) != null) {
				String[] instruction = line.split(" ");
				//if the line is an instruction then execute
				if(instruction.length > 1) {
				    //let the first instruction be the critical section
				    if(process.getLineNum() == 1) {
				    	dispatcher.executeCriticalSection(process, instruction);
				    } else {
				    	dispatcher.execute(process, instruction);
				    }
				    process.setCurrLine(line);
				 }
				 process.incrementLineNum();
				}
			dispatcher.terminateProcess(process);
			// stop the thread
			thread.stop();
			// remove the thread when done
			threads.remove(thread);
		}	
			
		br.close();
	}

}


