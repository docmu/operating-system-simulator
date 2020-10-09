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
	public static Scheduler scheduler = new Scheduler();
	public static Dispatcher dispatcher = new Dispatcher();
	private static Clock clock = new Clock();
	private static int counter = 0;
	
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
		
		System.out.println("Choose a scheduling algorithm:" + "\n1:Shortest Job First" + "\n2:Priority Scheduling");
		in = new Scanner(System.in);
		int schedulerType = in.nextInt();
		
		schedule(schedulerType);
		readFile();
//		print(readyQueue);
	}
	
	public static void initProcess(String fileName) throws FileNotFoundException, IOException {
		if(!fileName.contains(".txt")) {
			fileName += ".txt";
		}
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			process = new Process();
		    process.setName(fileName);
		    String memReq = br.readLine();
		    process.pcb.setMemRequirement(Integer.parseInt(memReq));
		}
	}
	
	//invoke the scheduling algorithm
	public static void schedule(int n) {	
		switch(n) {
			case 1:
				scheduler.SJF(readyQueue);
				break;
			case 2:
				scheduler.PriorityScheduling(readyQueue);
				break;
		}
		dispatcher.init(readyQueue);
	}
	
	//execute commands
	public static void readFile() throws FileNotFoundException, IOException {
		System.out.println("inside readFile");
		//loop through all processes
		for(int i = 0; i < readyQueue.size(); i++) {
			System.out.println(i + " " + readyQueue.get(0));
			BufferedReader br = new BufferedReader(new FileReader(readyQueue.get(0).name));
//				System.out.println(readyQueue.get(i).name);
			    String line; 
			    //read each line of process
			    while ((line = br.readLine()) != null) {
			    	String[] instruction = line.split(" ");
//			    	for(int j = 0; j < instruction.length; j++) {
			    	if(counter > 0) {
			    		for(int j = 0; j < instruction.length; j++) {
			    			System.out.print(instruction[j]);
			    		}
			    		dispatcher.execute(readyQueue.get(i), instruction);
			    	}
//			    	}
			    	//move onto next line of program when done executing instruction
			    	counter++;
		    		process.setCurrLine(counter);
			    }
			    counter = 0;
			
			System.out.println();
		}	
	}
	
	public static void print(ArrayList<Process> queue) {
		for(int i=0; i< queue.size(); i++) {
			System.out.println(queue.get(i));
		}
	}

}


