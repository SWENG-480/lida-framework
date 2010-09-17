/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriver;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TriggerListener;

/** 
 *
 * Interface for GlobalWorkspace.
 * 
 * It receives Coalitions generated by Attention Codelets from Workspace
 * It stores them until a new broadcast is triggered
 * Different Triggers can be registered dynamically
 * Modules that receive broadcast must register implementing BroadcastListener interface
 * 
 * @author Javier Snaider
 */
public interface GlobalWorkspace extends LidaModule, TriggerListener,ModuleDriver{
	/**
	 * This method is invoked by the 
	 * @param coalition
	 * @return true is coalition was added
	 */
	public boolean addCoalition(long attentionCodeletId, Coalition coalition);
	/**
	 * To register Triggers
	 * @param t a new Trigger
	 */
	public void addBroadcastTrigger(BroadcastTrigger t);
	/**
	 * To register a BroadcastListener. Each registered BroadcastListener receives Broadcast
	 * Implements Observer Pattern
	 * 
	 * @param bl the BroadcastListener to register
	 */
	public void addBroadcastListener(BroadcastListener bl);
	
	/**
	 * This method starts GlobalWorkspace mechanism. It should be called after registered 
	 * Triggers and BroadcastListeners.
	 */
	public void start();
	
	public boolean containsCoalition(long attentionCodeletId);
	
	public void updateCoalition(long attentionCodeletId);
	
}
