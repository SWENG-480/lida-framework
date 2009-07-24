/* 
 * RunLida.java - Initializes, starts, and ends the 
 *  main LIDA components. Sets up the intermodule communication 
 *  as well the GUIs.   
 */
package edu.memphis.ccrg.lida.example.genericlida.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.memphis.ccrg.lida.actionselection.ActionSelectionImpl;
import edu.memphis.ccrg.lida.attention.AttentionDriver;
import edu.memphis.ccrg.lida.declarativememory.DeclarativeMemoryImpl;
import edu.memphis.ccrg.lida.example.genericlida.environsensorymem.VisionEnvironment;
import edu.memphis.ccrg.lida.example.genericlida.environsensorymem.VisionSensoryMemory;
import edu.memphis.ccrg.lida.example.genericlida.gui.ControlPanelGui;
import edu.memphis.ccrg.lida.example.genericlida.gui.NodeLinkFlowGui;
import edu.memphis.ccrg.lida.example.genericlida.gui.VisualFieldGui;
import edu.memphis.ccrg.lida.example.genericlida.io.GlobalWorkspace_Input;
import edu.memphis.ccrg.lida.example.genericlida.io.PamInput;
import edu.memphis.ccrg.lida.framework.FrameworkTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriver;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.perception.PAMDriver;
import edu.memphis.ccrg.lida.perception.PerceptualAssociativeMemoryImpl;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryDriver;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryImpl;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryDriver;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemory;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryImpl;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.transientepisodicmemory.TEMImpl;
import edu.memphis.ccrg.lida.workspace.broadcastbuffer.BroadcastQueueDriver;
import edu.memphis.ccrg.lida.workspace.broadcastbuffer.BroadcastQueueImpl;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CSMDriver;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CurrentSituationalModelImpl;
import edu.memphis.ccrg.lida.workspace.episodicbuffer.EpisodicBufferDriver;
import edu.memphis.ccrg.lida.workspace.episodicbuffer.EpisodicBufferImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.perceptualbuffer.PerceptualBufferDriver;
import edu.memphis.ccrg.lida.workspace.perceptualbuffer.PerceptualBufferImpl;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.SBCodeletDriver;

public class OldGenericLida{
	
	//Perception 
	private VisionEnvironment environment;
	private SensoryMotorMemory sma;
	private VisionSensoryMemory sensoryMemory;
	private PerceptualAssociativeMemoryImpl pam;
	//Episodic memory
	private TEMImpl tem;
	private DeclarativeMemoryImpl declarativeMemory;
	//Workspace
	private WorkspaceImpl workspace;
	private PerceptualBufferImpl perceptBuffer;
	private EpisodicBufferImpl episodicBuffer;
	private BroadcastQueueImpl broadcastQueue;
	private CurrentSituationalModelImpl csm;
	//Attention
	private GlobalWorkspaceImpl globalWksp;
	//Action Selection
	private ProceduralMemoryImpl procMem;
	private ActionSelectionImpl actionSelection;
	
	//Drivers
	private SensoryMemoryDriver sensoryMemoryDriver;
	private PAMDriver pamDriver;
	private PerceptualBufferDriver perceptBufferDriver;
	private EpisodicBufferDriver episodicBufferDriver;
	private BroadcastQueueDriver broadcastBufferDriver;
	private SBCodeletDriver sbCodeletDriver;
	private CSMDriver csmDriver;
	private AttentionDriver attnDriver;
	private ProceduralMemoryDriver proceduralMemDriver;		

	/**
	 * GUI to show the environment
	 */
	private VisualFieldGui visualFieldGui = new VisualFieldGui();
	
	/**
	 * GUI to show counts of active nodes and links in the modules
	 */
	private NodeLinkFlowGui nodeLinkFlowGui = new NodeLinkFlowGui();
	
	/**
	 * GUI for basic control of the system
	 */
	private ControlPanelGui controlPanelGui;

	/**
	 * List of drivers which run the major components of LIDA
	 */
	private List<ModuleDriver> drivers = new ArrayList<ModuleDriver>();
	
	/**
	 * A class that helps pause and control the drivers. 
	 */
	private FrameworkTaskManager timer;

	public static void main(String[] args){
		new OldGenericLida().run();
	}//method

	/**
	 * Start up LIDA
	 */
	public void run(){
		initThreadControl();
		
		initEnvironmentThread();		
		//perception
		initSensoryMemoryThread();
		initPAMThread();		
		//episodic memories
		initTransientEpisodicMemory();
		initDeclarativeMemory();		
		//workspace
		initWorkspace();			
		//attention
		initGlobalWorkspace();
		initAttentionThread();		
		//action selection and execution
		initProceduralMemoryThread();
		initActionSelectionThread();
		initSensoryMotorAutomatism();
		//gui
		initGUI();
		defineListeners();//Define the listeners
		
		//Start all drivers up. The user can stop everything from the Control GUI.
		startLidaSystem();
	}//method

	private void initThreadControl(){
		boolean frameworkStartsRunning = false;
		int threadSleepTime = 150;
		timer = new FrameworkTaskManager(frameworkStartsRunning, threadSleepTime);	
	}

	private void initEnvironmentThread(){
		int height = 10;
		int width = 10;
		environment = new VisionEnvironment(timer, height, width);	
		drivers.add(environment);			
	}
	private void initSensoryMotorAutomatism(){
		sma = new SensoryMotorMemoryImpl();
	}	
	private void initSensoryMemoryThread(){
		sensoryMemory = new VisionSensoryMemory(environment);		
		sensoryMemoryDriver = new SensoryMemoryDriver(sensoryMemory, timer);
		drivers.add(sensoryMemoryDriver);				
	}
	private void initPAMThread(){
		pam = new PerceptualAssociativeMemoryImpl();
		String pamInputPath = "";
		PamInput reader = new PamInput();
		reader.read(pam, sensoryMemory, pamInputPath);
		//PAM THREAD		
		pamDriver = new PAMDriver(pam, timer);  
		drivers.add(pamDriver);
	}//method
	
	private void initWorkspace(){
		initPerceptualBufferThread();
		initEpisodicBufferThread();
		initBroadcastBufferThread();
		initCSMThread();
		initWorkspaceFacade();
		initSBCodeletsThread();
	}
	private void initPerceptualBufferThread(){
		int capacity = 2;
		perceptBuffer = new PerceptualBufferImpl(capacity);	
	    perceptBufferDriver = new PerceptualBufferDriver(perceptBuffer, timer); 
		drivers.add(perceptBufferDriver);
	}
	private void initEpisodicBufferThread(){
		int capacity = 10;
		episodicBuffer = new EpisodicBufferImpl(capacity);
		episodicBufferDriver = new EpisodicBufferDriver(episodicBuffer, timer); 
		drivers.add(episodicBufferDriver);
	}
	private void initBroadcastBufferThread(){
		int capacity = 10;
		broadcastQueue = new BroadcastQueueImpl(capacity);
		broadcastBufferDriver = new BroadcastQueueDriver(broadcastQueue, timer);
		drivers.add(broadcastBufferDriver);		
	}
	private void initCSMThread(){
		csm = new CurrentSituationalModelImpl();
		csmDriver = new CSMDriver(csm, timer);
		drivers.add(csmDriver);
	}
	private void initDeclarativeMemory() {
		declarativeMemory = new DeclarativeMemoryImpl();
		//TODO will we use a driver?
	}
	private void initTransientEpisodicMemory() {
		tem = new TEMImpl(new NodeStructureImpl());
		//TODO will we use a driver?
	}
	private void initWorkspaceFacade() {
		workspace = new WorkspaceImpl(perceptBuffer, episodicBuffer, 
									  broadcastQueue, csm);		
	}//method
	private void initSBCodeletsThread() {
		sbCodeletDriver = new SBCodeletDriver(workspace, timer);		 
		drivers.add(sbCodeletDriver);			
	}
	private void initGlobalWorkspace() {
		globalWksp = new GlobalWorkspaceImpl();
		String globalWorkspaceInputPath = "";
		GlobalWorkspace_Input reader = new GlobalWorkspace_Input();
		reader.read(globalWksp, globalWorkspaceInputPath);
	}//method
	private void initAttentionThread(){
		attnDriver = new AttentionDriver(timer, csm, globalWksp);
		drivers.add(attnDriver);
	}	
	private void initProceduralMemoryThread(){
		procMem = new ProceduralMemoryImpl();
		proceduralMemDriver = new ProceduralMemoryDriver(procMem, timer);
		drivers.add(proceduralMemDriver);
	}
	private void initActionSelectionThread() {
		actionSelection = new ActionSelectionImpl();
	}
	private void initGUI() {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new LidaGui(GenericLida.this,sbCodeletDriver, environment).setVisible(true);
//            }
//        });

//		controlPanelGui = new ControlPanelGui(timer,this, sbCodeletDriver, environment);
		controlPanelGui.setVisible(true);
		visualFieldGui.setVisible(true);
		nodeLinkFlowGui.setVisible(true);
	}//method
	
	private void defineListeners(){
		//sensoryMemory.addSensoryListener(sma);
		sma.addSensoryMotorListener(sensoryMemory);
		//sensoryMemory.addSensoryListener(pam);
		//
		pam.addPAMListener(workspace);
		
		perceptBuffer.addBufferListener(workspace);
		//
		episodicBuffer.addBufferListener(workspace);
		//
		csm.addBufferListener(workspace);
		
		workspace.addCueListener(declarativeMemory);
		//workspace.addCueListener(tem);
		workspace.add_PAM_WorkspaceListener(pam);
		//
		globalWksp.addBroadcastListener(pam);
		globalWksp.addBroadcastListener(workspace);
		globalWksp.addBroadcastListener(tem);
		globalWksp.addBroadcastListener(attnDriver);
		globalWksp.addBroadcastListener(procMem);
		globalWksp.start();
		//
		procMem.addProceduralMemoryListener(actionSelection);
		//
		actionSelection.addActionSelectionListener(environment);
		actionSelection.addActionSelectionListener(workspace);		
	}//method
	
	private void startLidaSystem(){
		ExecutorService executorService = Executors.newCachedThreadPool();
		int size = drivers.size();
		for(int i = 0; i < size; i++)
			executorService.execute(drivers.get(i));
		
		executorService.shutdown();
	}//method
	
	/**
	 * Stop in reverse order of starting
	 */	
	public void stopSpawnedThreads(){		
		int size = drivers.size();
		for(int i = 0; i < size; i++)			
			(drivers.get(size - 1 - i)).stopRunning();
		
		//TODO: Run serialization!
		try{
			Thread.sleep(500);
		}catch(Exception e){
			//
		}
		//Kill GUI windows & anything else still running
		System.exit(0);
	}//method	

	/**
	 * For ControlGUI to display thread count
	 * 
	 * @return number of threads started by this class
	 */
	public int getSpawnedThreadCount() {
		return drivers.size();
	}//method

}//class