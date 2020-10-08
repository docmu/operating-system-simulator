import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Scheduler {
	private static Clock clock = new Clock();
	private static Process process = new Process();
	private PriorityQueue<Process> waitingQueue = new PriorityQueue<Process>();
	
	private static ArrayList<Process> sortByArrivalTime(ArrayList<Process> processes) {
		Process temp;
		int n = processes.size();
		//sort by arrival time
		for(int i = 0; i < n; i++){
			for(int j = i+1; j < n; j++) { 
				if(processes.get(i).pcb.getArrivalTime() > processes.get(j).pcb.getArrivalTime()) {
					temp = processes.get(i); 
					processes.set(i, processes.get(j));
					processes.set(j, temp); 
				}		
			}
		}
		//set completion time of first process
		processes.get(0).pcb.setCompletiontime(processes.get(0).pcb.getBurstTime() + processes.get(0).pcb.getArrivalTime());
		return processes;
	}
	
	private static ArrayList<Process> sortByBurstTime(ArrayList<Process> processes) {
		Process temp;
		int n = processes.size();
		for(int i = 1; i < n; i++){
			for(int j = i+1; j < n; j++) { 
				if(processes.get(i).pcb.getBurstTime() > processes.get(j).pcb.getBurstTime()
						&& processes.get(j).pcb.getArrivalTime() <= processes.get(i-1).pcb.getCompletionTime()) {
					temp = processes.get(i); 
					processes.set(i, processes.get(j));
					processes.set(j, temp); 
				}	
			}
			//set completion time
			processes.get(i).pcb.setCompletiontime(processes.get(i-1).pcb.getCompletionTime() + processes.get(i).pcb.getBurstTime());
		}
		return processes;
	}
	
	//set turn around time for processes
	private static ArrayList<Process> setTAT(ArrayList<Process> processes) {
		int n = processes.size();
		
		for(int i = 0; i < n; i++){
			processes.get(i).pcb.setTurnAroundTime(processes.get(i).pcb.getCompletionTime() - processes.get(i).pcb.getArrivalTime());
		}
		
		return processes;
	}
	
	//set wait time for processes
	private static ArrayList<Process> setWT(ArrayList<Process> processes) {
		int n = processes.size();
			
		for(int i = 0; i < n; i++){
			processes.get(i).pcb.setWaitTime(processes.get(i).pcb.getTurnAroundTime() - processes.get(i).pcb.getBurstTime());
		}
			
		return processes;
	}
	
	public static void SJF(ArrayList<Process> processes) {
		ArrayList<Process> readyQueue = new ArrayList<Process>();
		int n = processes.size();
		Scanner in = new Scanner(System.in);
		
		for(int i = 0; i < n; i++) {
			System.out.println("Enter the arrival time of process " + i);
			processes.get(i).pcb.setArrivalTime(in.nextInt());
			System.out.println("Enter the burst time of process " + i);	
			processes.get(i).pcb.setBurstTime(in.nextInt());
		}
		
		processes = sortByArrivalTime(processes);
		processes = sortByBurstTime(processes);
		processes = setTAT(processes);
		processes = setWT(processes);
		
		//set readyQueue to the sorted processes
		readyQueue = processes;
		
		for(int i = 0; i < readyQueue.size(); i++) {
			System.out.println(readyQueue.get(i));
		}
		
	}
	
	public static void RoundRobin(ArrayList<Process> processes, int quantum) {
		int n = processes.size();
		Scanner in = new Scanner(System.in);
		
		for(int i = 0; i < n; i++) {
			System.out.println("Enter the arrival time of process " + i);
			processes.get(i).pcb.setArrivalTime(in.nextInt());
			System.out.println("Enter the burst time of process " + i);
			processes.get(i).pcb.setBurstTime(in.nextInt());	
		}
		
		for(int i = 0; i < n; i++){
			
		}
		
	}
}
