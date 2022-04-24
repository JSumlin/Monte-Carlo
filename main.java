//Joseph Sumlin
//4/23/2022
//Monte Carlo simulation for 5.10 in the Prob & Stats textbook,
import java.util.Random;
import java.util.Scanner;
import java.util.LinkedList;
public class main {

	public static void main(String[] args) {
		//All variables for main
		//2D array with 2nd dimension acting as a flag for if the computer has been infected.
		int[][] CPUs=new int[setTotalCPUs()][2];
		double probability=setProbability(); //user sets probability of virus spreading
		int numCleaned=setNumCleaned(); //user sets how many the technician cleans each day
		int numOfIterations=setIterations(); //user sets how many times the experiment will be run
		LinkedList<Integer> infIndexes=new LinkedList<Integer>(); //holds indexes of currently infected computers
		int days=0;
		int tempTotal=0; //temporarily holds how many got infected at least once within the current iteration
		int totalInfected=0; //tracks total number of computer infected across all experiments
		int allInfectedCounter=0; //tracks in how many experiments all computer were infected
		
		//Runs experiment the given amount of iterations
		for(int i=0; i<numOfIterations;i++) {
			reset(CPUs, infIndexes); // Sets/resets data for each experiment
			while(!infIndexes.isEmpty()) {
				days++;
				infect(CPUs, infIndexes, probability); //Simulates the virus spreading each morning
				//Simulates the technician cleaning computers in the afternoon
				if(infIndexes.size()<=numCleaned) break;
				clean(CPUs, infIndexes, numCleaned);
			}
			tempTotal=findTotalInfected(CPUs); //saves how many got infected at least once in current experiment
			totalInfected+=tempTotal; //adds up how many got infected throughout all experiments
			if(tempTotal==CPUs.length) allInfectedCounter++; //increments if all computer got infected in the experiment
		}
		
		//Prints results
		System.out.println("Average number of days: "+(double)days/numOfIterations);
		System.out.println("Expected number of computers infected: "+(double)totalInfected/numOfIterations);
		System.out.println("Probability of all computer being infected at least once: "+(double)allInfectedCounter/numOfIterations);
	}
	
	//Simulates the virus spreading in the morning.
	private static void infect(int[][] CPUs, LinkedList<Integer> infIndexes, double probability) {
		Random rand=new Random();
		//saves how many were initially infected for the day, so newly infected don't spread as well.
		int numOfInfected=infIndexes.size(); 
		for(int i=0; i<numOfInfected;i++) {
			for(int j=0;j<CPUs.length;j++) {
				if(CPUs[j][0]==0) { //checks if computer is uninfected
					if(rand.nextDouble()<=probability) { //simulates random probability of computer getting infected
						CPUs[j][0]=CPUs[j][1]=1;
						infIndexes.add(j); //saves index of infected computer
					}
				}
			}
		}
	}
	
	//Simulates the technician cleaning computers in the afternoon.
	private static void clean(int[][] CPUs, LinkedList<Integer> infIndexes, int numCleaned) {
		Random rand = new Random();
		//picks the selected number of infected computers at random and cleans them
		for(int i=0;i<numCleaned;i++) {
			CPUs[infIndexes.remove(rand.nextInt(infIndexes.size()))][0]=0;
		}
	}
	
	//Takes user input for int values.
	private static int intScanner(String instructions) {
		int input=-1;
		Scanner userInput = new Scanner(System.in);
		//Ensures the user gives valid inputs.
		try {
			System.out.print(instructions);
			input=userInput.nextInt();
			if(input<=0) throw new Exception();
		} catch(Exception e) {
			System.out.println("Invalid input. Please try again.");
		}
		return input;
	}
	
	//Functions for the user to set the values of the simulation.
	private static int setTotalCPUs() {
		int totalCPU=-1;
		while(totalCPU<=0) {
			totalCPU=intScanner("What's the total number of computers in the system? ");
		}
		return totalCPU;
	}
	private static int setNumCleaned() {
		int numCleaned=-1;
		while(numCleaned<=0) {
			numCleaned=intScanner("How many computers does the technician clean per day? ");
		}
		return numCleaned;
	}
	private static int setIterations() {
		int numOfIterations=-1;
		while(numOfIterations<=0) {
			numOfIterations=intScanner("How many iterations of the simulation would you like? ");
		}
		return numOfIterations;
	}
	//uses its own input code since it uses double values, rather than int.
	private static double setProbability() {
		double probability=-1;
		while(probability<0||probability>1.0) {
			Scanner userInput = new Scanner(System.in);
			System.out.print("What's the probability of the virus spreading? (Enter decimal between 0 and 1): ");
			try {
				probability=userInput.nextDouble();
				if(probability<0 || probability>1.0) throw new Exception();
			} catch(Exception e) {
				System.out.println("Invalid input. Please try again.");
			}
		}
		return probability;
	}
	
	//Resets the data set after each iteration.
	private static void reset(int[][] CPUs, LinkedList<Integer> infIndexes) {
		infIndexes.clear();
		infIndexes.add(1);
		CPUs[0][0]=CPUs[0][1]=1;
		for(int i=1;i<CPUs.length;i++) {
			CPUs[i][0]=CPUs[i][1]=0;
		}
	}
	
	//adds up how many computers got infected at least once for eacb experiment
	private static int findTotalInfected(int[][] CPUs) {
		int total=0;
		for(int i=0;i<CPUs.length;i++) {
			if(CPUs[i][1]==1) total++;
		}
		return total;
	}
}
