import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

public class Scheduler {
	private static Clock clock = new Clock();
	private static Process process;
	private PriorityQueue<Process> waitingQueue = new PriorityQueue<Process>();
	ArrayList<Process> readyQueue = new ArrayList<Process>();
	ArrayList<Process> jobQueue = new ArrayList<Process>();
	
	//set completion time of processes
	//used in PriorityScheduling method
	private ArrayList<Process> setCT(ArrayList<Process> processes){
		int n = processes.size();
		
		for(int i = 0; i < n; i++){
			if(i == 0) {
				processes.get(i).pcb.setCompletiontime(processes.get(i).pcb.getArrivalTime() 
						+ processes.get(i).pcb.getBurstTime());
			} else {
				processes.get(i).pcb.setCompletiontime(processes.get(i-1).pcb.getCompletionTime() 
						+ processes.get(i).pcb.getBurstTime());
			}
		}
		
		return processes;
	}
	
	private ArrayList<Process> sortByPriority(ArrayList<Process> processes) {
		Process temp;
		int n = processes.size();
		//sort by priority
		for(int i = 0; i < n; i++){
			for(int j = i+1; j < n; j++) { 
				if(processes.get(i).pcb.getPriority() > processes.get(j).pcb.getPriority()) {
					temp = processes.get(i); 
					processes.set(i, processes.get(j));
					processes.set(j, temp); 
				} else if(processes.get(i).pcb.getPriority() == processes.get(j).pcb.getPriority()) {
					//if 2 processes have the same priority, sort by arrival time
					if(processes.get(i).pcb.getArrivalTime() > processes.get(j).pcb.getArrivalTime()) {
						temp = processes.get(i); 
						processes.set(i, processes.get(j));
						processes.set(j, temp); 
					}
				}
			}
		}
		
		
		return processes;
	}
	
	private ArrayList<Process> sortByArrivalTime(ArrayList<Process> processes) {
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
		processes.get(0).pcb.setCompletiontime(processes.get(0).pcb.getBurstTime() 
				+ processes.get(0).pcb.getArrivalTime());
		
		return processes;
	}
	
	private ArrayList<Process> sortByBurstTime(ArrayList<Process> processes) {
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
			//set completion time of processes
			processes.get(i).pcb.setCompletiontime(processes.get(i-1).pcb.getCompletionTime() 
					+ processes.get(i).pcb.getBurstTime());
		}
		return processes;
	}
	
	//set turn around time for processes
	private ArrayList<Process> setTAT(ArrayList<Process> processes) {
		int n = processes.size();
		
		for(int i = 0; i < n; i++){
			processes.get(i).pcb.setTurnAroundTime(processes.get(i).pcb.getCompletionTime() 
					- processes.get(i).pcb.getArrivalTime());
		}
		
		return processes;
	}
	
	//set wait time for processes
	private ArrayList<Process> setWT(ArrayList<Process> processes) {
		int n = processes.size();
			
		for(int i = 0; i < n; i++){
			processes.get(i).pcb.setWaitTime(processes.get(i).pcb.getTurnAroundTime() 
					- processes.get(i).pcb.getBurstTime());
		}
			
		return processes;
	}
	
	//Shortest Job First Scheduling Algorithm
	public void SJF(ArrayList<Process> processes) {
		int n = processes.size();
		Scanner in = new Scanner(System.in);
		
		//prompt user for arrival and burst times
		for(int i = 0; i < n; i++) {
			System.out.println("Enter the arrival time of process " + i);
			processes.get(i).pcb.setArrivalTime(in.nextInt());
			System.out.println("Enter the burst time of process " + i);	
			processes.get(i).pcb.setBurstTime(in.nextInt());
		}
		
		//sort the queue SJF
		processes = sortByArrivalTime(processes);
		processes = sortByBurstTime(processes);
		
		//set turn around time and wait time
		processes = setTAT(processes);
		processes = setWT(processes);
		
		//set readyQueue to the sorted processes
		readyQueue = processes;		
	}
	
	//Priority Scheduling Algorithm (smallest integer = greatest priority)
	public void PriorityScheduling(ArrayList<Process> processes) {
		int n = processes.size();
		Scanner in = new Scanner(System.in);
		
		for(int i = 0; i < n; i++) {
			System.out.println("Enter the priority of process " + i);
			processes.get(i).pcb.setPriority(in.nextInt());
			System.out.println("Enter the arrival time of process " + i);
			processes.get(i).pcb.setArrivalTime(in.nextInt());
			System.out.println("Enter the burst time of process " + i);
			processes.get(i).pcb.setBurstTime(in.nextInt());	
		}
		
		//sort the queue by priority
		processes = sortByPriority(processes);
		
		//set completion time, turn around time, wait time
		processes = setCT(processes);
		processes = setTAT(processes);
		processes = setWT(processes);
		
		for(int i = 0; i< n; i++) {
			System.out.println(processes.get(i));
		}

	}
	
	//Round Robin Scheduling Algorithm
	public void RoundRobin(ArrayList<Process> processes, int quantum) {
		int n = processes.size();
		Scanner in = new Scanner(System.in);
		
		for(int i = 0; i < n; i++) {
			System.out.println("Enter the arrival time of process " + i);
			processes.get(i).pcb.setArrivalTime(in.nextInt());
			System.out.println("Enter the burst time of process " + i);
			processes.get(i).pcb.setBurstTime(in.nextInt());	
		}
		
		processes = sortByArrivalTime(processes);
		
		boolean done= false;
		int count= 0;
		int remainingBurstTime[]= new int [n];
	
		//copy process burst times into array
		for(int i = 0; i<processes.size(); i++) {
			remainingBurstTime[i] = processes.get(i).pcb.getBurstTime();
		}
	       
		do {	
			for(int t = 0 ; t<processes.size(); t++ ){
				if(t == 0) {
					readyQueue.add(processes.get(t));
					if(remainingBurstTime[t] > quantum) {
						readyQueue.get(t).pcb.setCompletiontime(readyQueue.get(t).pcb.getArrivalTime() + quantum);
						remainingBurstTime[t] -= quantum;
					} else {
						readyQueue.get(t).pcb.setCompletiontime(readyQueue.get(t).pcb.getArrivalTime() + readyQueue.get(t).pcb.getBurstTime());
						remainingBurstTime[t] = 0;
					}
				} else {
					readyQueue.add(jobQueue.get(0));
					if(remainingBurstTime[t] > quantum) {
						readyQueue.get(t).pcb.setCompletiontime(readyQueue.get(t-1).pcb.getCompletionTime() + quantum);
						remainingBurstTime[t] -= quantum;
					} else {
						readyQueue.get(t).pcb.setCompletiontime(readyQueue.get(t-1).pcb.getCompletionTime() + readyQueue.get(t).pcb.getBurstTime());
						remainingBurstTime[t] = 0;
					}
				}
				
				
				for(int j = t+1 ; j<processes.size(); j++){
					if(processes.get(j).pcb.getArrivalTime() <= processes.get(t).pcb.getCompletionTime()) {
						jobQueue.add(processes.get(j));
						System.out.println("added " + processes.get(j));
					} else {
						break;
					}
				}
				
				if(remainingBurstTime[t] > 0) {
					jobQueue.add(processes.get(t));
				}
				
				jobQueue.remove(0);
			}
			
			if(jobQueue.size() == 0) done = true;
			
		} while(!done);
		
		//copy process burst times into remainingTime array
		System.out.println(jobQueue.size());
		for(int k = 0; k < n; k++) {
//			remainingTime[k] = processes.get(k).pcb.getBurstTime();
			System.out.println("ready queue");
			System.out.println(readyQueue.get(k));
		}
		
	}
}
