package edu.memphis.ccrg.lida.wumpusWorld.a_environment;


import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.wumpusWorld.a_environment.Action;
import edu.memphis.ccrg.lida.actionSelection.ActionContent;
import edu.memphis.ccrg.lida.environment.EnvironmentListener;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.ThreadSpawner;

public class Simulation{
	//My Addition
	private final int VISION_SIZE = 3;
	private final int MAX_ENTITIES_PER_CELL = 4;//Pit, Wumpus, Gold, Agent	
	//Previous fields
	private static final int basicActionPoints = -1;
	private static final int deathPoints = -1000;
	private static final int shootArrowPoints = -10;
	private static final int getGoldPoints = 1000;
	private static final int detectImpossibilityPoints = 1000;
	private static final int killWumpusPoints = 0;
	private static final List<Integer> finalScores = new ArrayList<Integer>();
	private static final int MAX_ACTIONS = 100;
	
	private boolean nonDeterministic;
	private int currScore = 0;
	private int actionCount = 0;
	//Main fields
	private Agent agent;
	private TransferPercept transferPercept;//This is pretty useless
	private char[][][] currentDirectionalSense;
	
	private Environment environment;
	private List<Environment> worlds = new ArrayList<Environment>();
	private int worldCounter = 0;
		
	//To stimuli sent out 
	private EnvironmentContentImpl simContent = new EnvironmentContentImpl(VISION_SIZE);
	private ArrayList<EnvironmentListener> listeners = new ArrayList<EnvironmentListener>();
	//for actions received
	private boolean actionHasChanged = false;
	private ActionContent currentBehavior = null;
	private int lastAction = 0;	
	//for thread control
	private boolean keepRunning = true;	
	private FrameworkTimer timer;
	private long threadID;
	private String message = "";
	private boolean trialIsOver = false;
	private BufferedWriter out = null;
	private ThreadSpawner motherThread = null;
	
	public Simulation(FrameworkTimer timer, Environment environ, boolean nonDet, ThreadSpawner s){ 		
		this.timer = timer;
		environment = environ;
		nonDeterministic = nonDet;
		motherThread = s;
		//
		transferPercept = new TransferPercept(environment);
		agent = new Agent(environment, transferPercept, nonDeterministic);	
		environment.placeAgent(agent);
		
		currentDirectionalSense = new char[VISION_SIZE][VISION_SIZE][MAX_ENTITIES_PER_CELL];
		for(int i = 0; i < currentDirectionalSense.length; i++)
			for(int j = 0; j < currentDirectionalSense.length; j++)
				for(int k = 0; k < currentDirectionalSense.length; k++)
					currentDirectionalSense[i][j][k] = '0';		
	}//Simulation

	public void setNewEnvironment(Environment wumpusEnvironment) {
		if(timer.threadsArePaused()){//extra precaution to make sure this thread is not active during update
			environment = wumpusEnvironment;
			transferPercept = new TransferPercept(environment);
			agent = new Agent(environment, transferPercept, nonDeterministic);	
			environment.placeAgent(agent);
			currScore = 0;
			trialIsOver = false;
			
			currentDirectionalSense = new char[VISION_SIZE][VISION_SIZE][MAX_ENTITIES_PER_CELL];
			for(int i = 0; i < currentDirectionalSense.length; i++)
				for(int j = 0; j < currentDirectionalSense.length; j++)
					for(int k = 0; k < currentDirectionalSense.length; k++)
						currentDirectionalSense[i][j][k] = '0';		
			message = "";
			//System.out.println("\nEnvironment was reset.\n");
		}		
	}//method

	public Simulation(FrameworkTimer timer, Environment wumpusEnvironment,
			boolean nonDeterministicMode) {
		this.timer = timer;
		environment = wumpusEnvironment;
		nonDeterministic = nonDeterministicMode;
		//
		transferPercept = new TransferPercept(environment);
		agent = new Agent(environment, transferPercept, nonDeterministic);	
		environment.placeAgent(agent);
		
		currentDirectionalSense = new char[VISION_SIZE][VISION_SIZE][MAX_ENTITIES_PER_CELL];
		for(int i = 0; i < currentDirectionalSense.length; i++)
			for(int j = 0; j < currentDirectionalSense.length; j++)
				for(int k = 0; k < currentDirectionalSense.length; k++)
					currentDirectionalSense[i][j][k] = '0';	
	}

	public void addEnvironmentListener(EnvironmentListener listener){
		listeners.add(listener);
	}
	
	public synchronized void receiveBehaviorContent(ActionContent action){
		currentBehavior = action;		
		actionHasChanged = true;
	}	
	
	public void runSim(){				
		Integer currentAction = new Integer(-1);
		boolean runOneStep = false;
		while(keepRunning){
			try{Thread.sleep(50);}catch(Exception e){}			
			timer.checkForStartPause();//Doesn't return if 'pause' clicked in the gui until another gui click			
			//runOneStep = timer.checkForNextStep(runOneStep, threadID);
			
			senseEnvironment();		
			simContent.setContent(currentDirectionalSense, environment.getEnvironmentString(), 
								directionalSenseToString(), message , Action.getActionString(lastAction), currScore + "");			
			
			for(int i = 0; i < listeners.size(); i++)
				(listeners.get(i)).receiveSimContent(simContent);

			if(actionHasChanged){
				currentAction = (Integer)currentBehavior.getContent();
				synchronized(this){
					actionHasChanged = false;
				}
				if(!currentAction.equals(null) && !trialIsOver)
					handleAction(currentAction);
			}//if actionHasChanged		
							
			if(keepRunning == false)			
				lastAction = Action.END_TRIAL;	
		}//while 	

	}//method runSim
	
	private void printScores() {
		int j = 1;
		int sum = 0;
		for(Integer i: finalScores){
			sum += i;
			System.out.println("Trial " + j + ": " + i);
			j++;
		}
		double average = (sum) / (finalScores.size() * 1.0);
		System.out.println("Average score across trials " + average);
	}

	public void handleAction(int action) {	
		actionCount++;
		if (action == Action.GO_FORWARD) {				
			if (environment.getBump() == true) 
				environment.setBump(false);
			
			agent.goForward();
			environment.placeAgent(agent);
			if (environment.checkDeath() == true) {
				message = "Died but resurrected";
				currScore += deathPoints;
//				endTrial();
//				agent.setIsDead(true);
			}
			else
				currScore += basicActionPoints;
			
			if (environment.getScream() == true) 
				environment.setScream(false);
		
			lastAction = Action.GO_FORWARD;
		}
		else if (action == Action.TURN_RIGHT) {
			currScore += basicActionPoints;
			agent.turnRight();		
			environment.placeAgent(agent);
			
			if (environment.getBump() == true) environment.setBump(false);
			if (environment.getScream() == true) environment.setScream(false);
			lastAction = Action.TURN_RIGHT;
		}
		else if (action == Action.TURN_LEFT) {
			currScore += basicActionPoints;
			agent.turnLeft();		
			environment.placeAgent(agent);
			
			if (environment.getBump() == true) environment.setBump(false);
			if (environment.getScream() == true) environment.setScream(false);
			lastAction = Action.TURN_LEFT;
		}
		else if (action == Action.GRAB) {
			
			if (environment.grabGold() == true) {
				currScore += getGoldPoints;
				//
				//endTrial();				
				//keepRunning = false;
				message = "Got the Gold";
				agent.setHasGold(true);
			}
			else{
				currScore += basicActionPoints;
				message = "Gold grab failed";
			}
			
			environment.placeAgent(agent);
			if (environment.getBump() == true) environment.setBump(false);
			if (environment.getScream() == true) environment.setScream(false);
			lastAction = Action.GRAB;
		}
		else if (action == Action.SHOOT) {
			if (agent.hasArrows()){
				if (environment.shootArrow()){
					environment.setScream(true);
					message = "Wumpus killed";
					currScore += killWumpusPoints;
				}else{
					message = "Arrow shot missed";
					currScore += shootArrowPoints;
				}			
			}
			else { //Tried to shoot w/ no arrows
				if (environment.getScream() == true) 
					environment.setScream(false);
				currScore += basicActionPoints;
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
		}else if(action == Action.END_TRIAL){
			message = "I can't win, giving up.";
			//
			if(lastAction != Action.NO_OP)
				currScore += detectImpossibilityPoints;
			//endTrial();
			//
			environment.placeAgent(agent);
			if (environment.getBump() == true) environment.setBump(false);
			if (environment.getScream() == true) environment.setScream(false);
			lastAction = Action.NO_OP;			
		}
		
//		if(actionCount > MAX_ACTIONS)
//			endTrial();
	}//method
	
//	private void endTrial(){
//		System.out.println(currScore);
//		try{
//			out.write(currScore + "\n");
//		}catch(Exception e){}
//		finalScores.add(currScore);
//		currScore = 0;
//		trialIsOver = true;		
//		timer.resumeRunningThreads(); 
//		motherThread.stopThreads();		
//	}
	
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
	}//method

	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//method
	
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
	}//senseEnvironment	
	
	public void printEndWorld() {
		environment.printEnvironment();
		System.out.println("Final score: " + currScore);
		System.out.println("Last action: " + Action.getActionString(lastAction));
	}
	
	public void printCurrentPerceptSequence() {
		System.out.println("Percept: <bump, glitter, breeze, stench, scream>");
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
	}//method
	
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