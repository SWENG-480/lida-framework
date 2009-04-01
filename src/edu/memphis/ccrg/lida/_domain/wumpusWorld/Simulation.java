package edu.memphis.ccrg.lida._domain.wumpusWorld;


import java.util.ArrayList;



import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Misc;
import edu.memphis.ccrg.lida._domain.wumpusWorld.Action;
import edu.memphis.ccrg.lida._sensoryMemory.SimulationContentImpl;
import edu.memphis.ccrg.lida._sensoryMemory.SimulationListener;
import edu.memphis.ccrg.lida.actionSelection.BehaviorContent;

public class Simulation{
	//My Addition
	private final int VISION_SIZE = 3;
	private final int MAX_ENTITIES_PER_CELL = 4;//Pit, Wumpus, Gold, Agent	
	//Previous fields
	private static final int actionCost = -1;
	private static final int deathCost = -1000;
	private static final int shootCost = -10;
	private boolean nonDeterministic;
	private int currScore = 0;
	//Main fields
	private Agent agent;
	private Environment environment;
	private TransferPercept transferPercept;//This is pretty useless
	private char[][][] currentDirectionalSense;
		
	//To stimuli sent out 
	private SimulationContentImpl simContent = new SimulationContentImpl(VISION_SIZE);
	private ArrayList<SimulationListener> listeners = new ArrayList<SimulationListener>();
	//for actions received
	private boolean actionHasChanged = false;
	private BehaviorContent currentBehavior = null;
	private int lastAction = 0;	
	//for thread control
	private boolean keepRunning = true;	
	private FrameworkTimer timer;
	private long threadID;
	
	public Simulation(FrameworkTimer timer, Environment environ, boolean nonDet){ 		
		this.timer = timer;
		environment = environ;
		nonDeterministic = nonDet;
		//
		transferPercept = new TransferPercept(environment);
		agent = new Agent(environment, transferPercept, nonDeterministic);	
		currentDirectionalSense = new char[VISION_SIZE][VISION_SIZE][MAX_ENTITIES_PER_CELL];
		for(int i = 0; i < currentDirectionalSense.length; i++)
			for(int j = 0; j < currentDirectionalSense.length; j++)
				for(int k = 0; k < currentDirectionalSense.length; k++)
					currentDirectionalSense[i][j][k] = 0;
		
	}//Simulation
	
	public void addSimulationListener(SimulationListener listener){
		listeners.add(listener);
	}
	
	public synchronized void receiveBehaviorContent(BehaviorContent action){
		currentBehavior = action;		
		actionHasChanged = true;
	}	
	
	public void runSim(){				
		Integer currentAction = new Integer(-1);
		boolean runOneStep = false;
		int stepCounter = 0;		
		environment.placeAgent(agent);
	
		long startTime = System.currentTimeMillis();			
		while(keepRunning){
			try{Thread.sleep(50);
			}catch(Exception e){}			
			timer.checkForStartPause();//won't return if paused until started again			
			//runOneStep = timer.checkForNextStep(runOneStep, threadID);
			
			senseEnvironment();		
			simContent.setContent(currentDirectionalSense, environment.getEnvironmentString(), directionalSenseToString());			
			
			for(int i = 0; i < listeners.size(); i++)
				(listeners.get(i)).receiveSimContent(simContent);

			if(actionHasChanged){
				synchronized(this){
					currentAction = (Integer)currentBehavior.getContent();
					actionHasChanged = false;
				}
				if(!currentAction.equals(null))
					handleAction(currentAction);
			}//if actionHasChanged		

			stepCounter++;				
			if (keepRunning == false) {				
				lastAction = Action.END_TRIAL;
			}	
		}//while keepRunning and trials		
		long finishTime = System.currentTimeMillis();			
		System.out.println("\nSIM: Ave. cycle time: " + Misc.rnd((finishTime - startTime)/(double)stepCounter));		
		System.out.println("SIM: Num. cycles: " + stepCounter);			
		//printEndWorld();		
	}//method runSim
	
	private void printDirectionalSense() {
		for(int i = 0; i < currentDirectionalSense.length; i++){
			for(int j = 0; j < currentDirectionalSense[0].length; j++){
				for(int k = 0; k < currentDirectionalSense[0][0].length; k++){
					System.out.print(currentDirectionalSense[i][j][k] + " ");
				}
				System.out.print(" ");
			}
			System.out.println("\n");
		}
		
	}
	
	private String directionalSenseToString(){
		String s = "\n\n";
		
		for(int i = 0; i < currentDirectionalSense.length; i++){
			for(int j = 0; j < currentDirectionalSense[0].length; j++){
				s += "(";
				for(int k = 0; k < currentDirectionalSense[0][0].length; k++){
					s = s + currentDirectionalSense[i][j][k] + " ";
				}
				s += ")  ";
			}
			s += "\n\n";
		}
		
		
		return s;
	}

	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//stopRunning
	
	public void senseEnvironment(){
		currentDirectionalSense = environment.getCurrentSense(currentDirectionalSense);
		
//		for(int i = 0; i < currentSense.length; i++)
//			currentSense[i] = 0;
		
//		if (transferPercept.getBump() == true) {
//			currentSense[0] = 1;
//			//System.out.print(" bump,");
//			//outputWriter.write("bump,");
//		}
//		//else if (transferPercept.getBump() == false) {
//			//System.out.print(" none,");
//			//outputWriter.write("none,");
//		//}
//		if (transferPercept.getGlitter() == true) {
//			currentSense[1] = 1;
//			//System.out.print(" glitter,");
//			//outputWriter.write("glitter,");
//		}
//		//else if (transferPercept.getGlitter() == false) {
//			//System.out.print(" none,");
//			//outputWriter.write("none,");
//		//}
//		if (transferPercept.getBreeze() == true) {
//			currentSense[2] = 1;
//			//System.out.print(" breeze,");
//			//outputWriter.write("breeze,");
//		}
//		//else if (transferPercept.getBreeze() == false) {
//			//System.out.print(" none,");
//			//outputWriter.write("none,");
//		//}
//		if (transferPercept.getStench() == true) {
//			currentSense[3] = 1;
//			//System.out.print(" stench,");
//			//outputWriter.write("stench,");
//		}
//		//else if (transferPercept.getStench() == false) {
//			//System.out.print(" none,");
//			//outputWriter.write("none,");
//		//}
//		if (transferPercept.getScream() == true) {
//			currentSense[4] = 1;
//			//System.out.print(" scream>\n");
//			//outputWriter.write("scream>\n");
//		}
		//else if (transferPercept.getScream() == false) {
		//	System.out.print(" none>\n");
			//outputWriter.write("none>\n");
		//}
		
		//System.out.println("sense list has size " + currentSense.length + " end of sense method");
	}//senseEnvironment	
	
	public void printEndWorld() {
		
		try {			
			environment.printEnvironment();
			
			System.out.println("Final score: " + currScore);
			//outputWriter.write("Final score: " + currScore + "\n");
			
			System.out.println("Last action: " + Action.printAction(lastAction));
			//outputWriter.write("Last action: " + Action.printAction(lastAction) + "\n");

		}
		catch (Exception e) {
			System.out.println("An exception was thrown: " + e);
		}
		
	}
	
	public void printCurrentPerceptSequence() {
		
		System.out.println("Percept: <bump, glitter, breeze, stench, scream>");
		
		try {
		
			System.out.print("Percept: <");	
			//outputWriter.write("Percept: <");
			
			if (transferPercept.getBump() == true) {
				System.out.print(" bump,");
				//outputWriter.write("bump,");
			}
			else if (transferPercept.getBump() == false) {
				System.out.print(" none,");
				//outputWriter.write("none,");
			}
			if (transferPercept.getGlitter() == true) {
				System.out.print(" glitter,");
				//outputWriter.write("glitter,");
			}
			else if (transferPercept.getGlitter() == false) {
				System.out.print(" none,");
				//outputWriter.write("none,");
			}
			if (transferPercept.getBreeze() == true) {
				System.out.print(" breeze,");
				//outputWriter.write("breeze,");
			}
			else if (transferPercept.getBreeze() == false) {
				System.out.print(" none,");
				//outputWriter.write("none,");
			}
			if (transferPercept.getStench() == true) {
				System.out.print(" stench,");
				//outputWriter.write("stench,");
			}
			else if (transferPercept.getStench() == false) {
				System.out.print(" none,");
				//outputWriter.write("none,");
			}
			if (transferPercept.getScream() == true) {
				System.out.print(" scream>\n");
				//outputWriter.write("scream>\n");
			}
			else if (transferPercept.getScream() == false) {
				System.out.print(" none>\n");
				//outputWriter.write("none>\n");
			}
		
		}
		catch (Exception e) {
			System.out.println("An exception was thrown: " + e);
		}
		
	}
	
	public void handleAction(int action) {
		
			if (action == Action.GO_FORWARD) {				
				if (environment.getBump() == true) environment.setBump(false);
				
				agent.goForward();
				environment.placeAgent(agent);
				
				if (environment.checkDeath() == true) {
					System.out.println("You die! ...but gnomes resurrect you.");
					currScore += deathCost;
					//keepRunning = false;
					
					agent.setIsDead(true);
				}
				else {
					currScore += actionCost;
				}
				
				if (environment.getScream() == true) environment.setScream(false);
				
				lastAction = Action.GO_FORWARD;
			}
			else if (action == Action.TURN_RIGHT) {
				
				currScore += actionCost;
				agent.turnRight();		
				environment.placeAgent(agent);
				
				if (environment.getBump() == true) environment.setBump(false);
				if (environment.getScream() == true) environment.setScream(false);
				
				lastAction = Action.TURN_RIGHT;
			}
			else if (action == Action.TURN_LEFT) {
				
				currScore += actionCost;
				agent.turnLeft();		
				environment.placeAgent(agent);
				
				if (environment.getBump() == true) environment.setBump(false);
				if (environment.getScream() == true) environment.setScream(false);
				
				lastAction = Action.TURN_LEFT;
			}
			else if (action == Action.GRAB) {
				
				if (environment.grabGold() == true) {
					
					currScore += 1000;
					//keepRunning = false;
					System.out.println("Got the Gold!");
					agent.setHasGold(true);
				}
				else{
					currScore += actionCost;
					System.out.println("Grab gold fail");
				}
				
				environment.placeAgent(agent);
				
				if (environment.getBump() == true) environment.setBump(false);
				if (environment.getScream() == true) environment.setScream(false);
				
				lastAction = Action.GRAB;
			}
			else if (action == Action.SHOOT) {
				
				if (agent.shootArrow() == true) {
					
					if (environment.shootArrow() == true){
						environment.setScream(true);
						System.out.println("RAAAAAAWWWWWRRRRRrrrr");
					}else{
						System.out.println("Shot arrow fail");
					}
				
					currScore += shootCost;					
				}
				else {
					
					if (environment.getScream() == true) environment.setScream(false);
					
					currScore += actionCost;
				}
				
				environment.placeAgent(agent);
				
				if (environment.getBump() == true) environment.setBump(false);
				
				lastAction = Action.SHOOT;
			}
			else if (action == Action.NO_OP) {
				
				environment.placeAgent(agent);
				
				if (environment.getBump() == true) environment.setBump(false);
				if (environment.getScream() == true) environment.setScream(false);
				
				lastAction = Action.NO_OP;
			}
			

	}
	
	public int getScore() {		
		return currScore;		
	}

	public void setThreadID(long id) {
		threadID = id;		
	}

	public long getThreadID() {
		return threadID;
	}	
}//class Simulation