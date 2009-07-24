package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

/**
 * 
 * @author ryanjmccall
 */
public class ProceduralMemoryDriver extends GenericModuleDriver {

	private ProceduralMemory procMem;

	public ProceduralMemoryDriver(ProceduralMemory pm, LidaTaskManager timer) {
		super(timer);
		procMem = pm;
	}// constructor

	/**
	 * This loop drives the procedural memory
	 */
	public void cycleStep() {
		//procMem.sendEvent();   // Change for Logger 
	}// method

}// class