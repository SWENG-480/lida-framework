package edu.memphis.ccrg.lida.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionImpl;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.attention.AttentionDriver;
import edu.memphis.ccrg.lida.declarativememory.DeclarativeMemory;
import edu.memphis.ccrg.lida.declarativememory.DeclarativeMemoryImpl;
import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.perception.PAMDriver;
import edu.memphis.ccrg.lida.perception.PAMListener;
import edu.memphis.ccrg.lida.perception.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.perception.PerceptualAssociativeMemoryImpl;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemory;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryDriver;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryImpl;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryDriver;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemory;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryImpl;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorListener;
import edu.memphis.ccrg.lida.transientepisodicmemory.CueListener;
import edu.memphis.ccrg.lida.transientepisodicmemory.TEMImpl;
import edu.memphis.ccrg.lida.transientepisodicmemory.TransientEpisodicMemory;
import edu.memphis.ccrg.lida.workspace.broadcastbuffer.BroadcastQueueImpl;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CurrentSituationalModelImpl;
import edu.memphis.ccrg.lida.workspace.episodicbuffer.EpisodicBufferImpl;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;
import edu.memphis.ccrg.lida.workspace.perceptualbuffer.PerceptualBufferImpl;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.SBCodeletDriver;

/**
 * @author Javier Snaider
 * 
 */
public class Lida {
	
	private Logger logger = Logger.getLogger("lida.framework.Lida");
	// Perception
	private EnvironmentImpl environment;
	private SensoryMotorMemory sensoryMotorMemory;
	private SensoryMemory sensoryMemory;
	private PerceptualAssociativeMemory pam;
	// Episodic memory
	private TransientEpisodicMemory tem;
	private DeclarativeMemory declarativeMemory;
	// Workspace
	private Workspace workspace;
	private SBCodeletDriver sbCodeletDriver;
	// Attention
	private AttentionDriver attnDriver;
	private GlobalWorkspace globalWksp;
	// Action Selection
	private ProceduralMemory procMem;
	private ActionSelection actionSelection;
	// A class that helps pause and control the drivers.
	private LidaTaskManager taskManager;

	/**
	 * List of drivers which run the major components of LIDA
	 */
	private List<ModuleDriver> drivers = new ArrayList<ModuleDriver>();

	public Lida(LidaTaskManager ft, EnvironmentImpl e, SensoryMemory sm) {
		logger.info("Starting Lida");
		initComponents(ft, e, sm);
		initDrivers();
		initListeners();
		start();
	}

	private void initComponents(LidaTaskManager tm, EnvironmentImpl e, SensoryMemory sm) {
		taskManager = tm;
		environment = e;
		sensoryMemory = sm;
		pam = new PerceptualAssociativeMemoryImpl();
		tem = new TEMImpl(); 
		declarativeMemory = new DeclarativeMemoryImpl();
		//
		int bufferCapacity = 2;
		int queueCapacity = 10;
		workspace = new WorkspaceImpl(new PerceptualBufferImpl(bufferCapacity),
									  new EpisodicBufferImpl(bufferCapacity), 
									  new BroadcastQueueImpl(queueCapacity),
									  new CurrentSituationalModelImpl());
		//
		globalWksp = new GlobalWorkspaceImpl();
		procMem = new ProceduralMemoryImpl();
		actionSelection = new ActionSelectionImpl();
		sensoryMotorMemory = new SensoryMotorMemoryImpl();
		logger.info("Lida submodules Created");		
	}

	private void initDrivers() {
		//finish initializing drivers 
		attnDriver = new AttentionDriver(taskManager, workspace.getCSM(), globalWksp);
		sbCodeletDriver = new SBCodeletDriver(workspace, taskManager);
		//Add drivers to a list for execution
		drivers.add(environment);
		//drivers.add(new SensoryMemoryDriver(sensoryMemory, taskManager));
		//drivers.add(new PAMDriver(pam, taskManager));
		//drivers.add(attnDriver);
		//drivers.add(sbCodeletDriver);
		//drivers.add(new ProceduralMemoryDriver(procMem, taskManager));
		//done creating drivers
		logger.info("Lida drivers Created");		
	}

	private void initListeners() {
		if (sensoryMemory instanceof SensoryMotorListener)
			sensoryMotorMemory.addSensoryMotorListener((SensoryMotorListener) sensoryMemory);
		else
			logger.warning("Cannot add SM as a listener");
		//sensoryMemory.addSensoryListener(sensoryMotorMemory);
		//sensoryMemory.addSensoryListener(pam);
		if (workspace instanceof PAMListener)
			pam.addPAMListener((PAMListener) workspace);
		else
			logger.warning("Cannot add WORKSPACE as a listener");

		if (declarativeMemory instanceof CueListener)
			workspace.addCueListener((CueListener)declarativeMemory);
		else
			logger.warning("Cannot add DM as a listener");
		
		if (tem instanceof CueListener)		
			workspace.addCueListener((CueListener)tem);
		else
			logger.warning("Cannot add TEM as a listener");
		
		if(pam instanceof WorkspaceListener)
			workspace.addPamWorkspaceListener(pam);
		else 
			logger.warning("Cannot add PAM as a listener");
		//check
		globalWksp.addBroadcastListener(pam);
		globalWksp.addBroadcastListener((BroadcastListener)workspace);
		globalWksp.addBroadcastListener(tem);
		globalWksp.addBroadcastListener((BroadcastListener)attnDriver);
		globalWksp.addBroadcastListener(procMem);

		procMem.addProceduralMemoryListener((ProceduralMemoryListener)actionSelection);
		actionSelection.addActionSelectionListener(environment);
		actionSelection.addActionSelectionListener((ActionSelectionListener)workspace);
		logger.info("Lida listeners added");	
	}
	public void start(){
		globalWksp.start(); //TODO: change to the ThreadSpawner
		taskManager.setInitialTasks(drivers);		
		logger.info("Lida submodules Started\n");		
	}

	/**
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * @return the sensoryMotorMemory
	 */
	public SensoryMotorMemory getSensoryMotorMemory() {
		return sensoryMotorMemory;
	}

	/**
	 * @return the sensoryMemory
	 */
	public SensoryMemory getSensoryMemory() {
		return sensoryMemory;
	}

	/**
	 * @return the pam
	 */
	public PerceptualAssociativeMemory getPam() {
		return pam;
	}

	/**
	 * @return the tem
	 */
	public TransientEpisodicMemory getTem() {
		return tem;
	}

	/**
	 * @return the declarativeMemory
	 */
	public DeclarativeMemory getDeclarativeMemory() {
		return declarativeMemory;
	}

	/**
	 * @return the workspace
	 */
	public Workspace getWorkspace() {
		return workspace;
	}

	/**
	 * @return the globalWksp
	 */
	public GlobalWorkspace getGlobalWksp() {
		return globalWksp;
	}

	/**
	 * @return the procMem
	 */
	public ProceduralMemory getProcMem() {
		return procMem;
	}

	/**
	 * @return the actionSelection
	 */
	public ActionSelection getActionSelection() {
		return actionSelection;
	}

	/**
	 * @return the timer
	 */
	public LidaTaskManager getTaskManager() {
		return taskManager;
	}

	/**
	 * @return the sbCodeletDriver
	 */
	public SBCodeletDriver getSbCodeletDriver() {
		return sbCodeletDriver;
	}

	public AttentionDriver getAttentionDriver() {
		return attnDriver;
	}
}
