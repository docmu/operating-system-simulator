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
//	public static Process[] processes;
	public static ArrayList<Process> processes = new ArrayList<Process>();
	public static Scheduler scheduler = new Scheduler();
	public static final int MAX_MEMORY = 2048;
	public static int memUsage;
	public static int memLeft;
	public static int run = 0;
	public static int quantum = 3;
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("Enter the number of processes: ");	
		Scanner in = new Scanner(System.in);
		int numProcesses = in.nextInt();
//		processes = new Process[numProcesses];
		
		for(int i = 0; i < numProcesses; i++) {
			System.out.println(i + ": Enter the program name: ");
			in = new Scanner(System.in);
			String program = in.next();
			parseFile(program);	
			processes.add(process);
//			processes[i] = process;
		}
		
		System.out.println("Choose a scheduling algorithm:" + "\n1:Shortest Job First" + "\n2:Priority Scheduling");
		in = new Scanner(System.in);
		int schedulerType = in.nextInt();
		
		schedule(schedulerType);
	}
	
	//invoke the scheduling algorithm
	public static void schedule(int n) {	
		switch(n) {
			case 1:
				scheduler.SJF(processes);
				break;
			case 2:
				scheduler.PriorityScheduling(processes);
				break;
		}
	}
	
	//parse the file
	public static void parseFile(String fileName) throws FileNotFoundException, IOException {
		if(!fileName.contains(".txt")) {
			fileName += ".txt";
		}
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			process = new Process();
		    process.pcb.setName(fileName);
		    String memReq = br.readLine();
		    process.pcb.setMemRequirement(Integer.parseInt(memReq));
//		    System.out.println(memReq);
//		    String line;
		    
		    //parse the file while there are lines to read
//		    while ((line = br.readLine()) != null) {
//		    	System.out.println(line);
//		    }
		}
	}

	//generate random number of cycles (25-50)
	public static int operationCycle() {
		return (int)(Math.random() * 25) + 25;
	}

	//the state of the process
	public static String state(String process, String memory, String state) {	
		switch (state.toUpperCase()) {
			case "NEW":
				memUsage = Integer.parseInt(memory);
				memLeft = MAX_MEMORY - memUsage;
				if(memLeft <= MAX_MEMORY) {
					state = "READY";
				} else {
					state = "WAIT";
				}
			case "READY":
				return process;	
			case "RUN":
				return process;
			case "WAIT":
				return process;
			case "EXIT":
				memUsage = Integer.parseInt(memory);
				memLeft +=  memUsage;
				return process;
			default:
				System.out.println("Cannot do job");
		}
		
		return "Error";
	}

}


