/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.attention.AttentionCodelet;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TriggerListener;

/** 
 *
 * Interface for GlobalWorkspace.
 * 
 * It receives {@link Coalition} objects generated by {@link AttentionCodelet}s from Workspace.
 * Different {@link BroadcastTrigger}s can be registered dynamically.
 * When a Trigger fires, all registered {@link BroadcastListener}s receive the winner Coalition content.
 * Modules that receive broadcast must register implementing {@link BroadcastListener} interface
 * 
 * @author Javier Snaider
 */
public interface GlobalWorkspace extends LidaModule, TriggerListener{
	/**
	 * This method is invoked by the {@link AttentionCodelet} to add a new {@link Coalition}
	 * @param coalition to be added 
	 * @return true if coalition was added
	 */
	public boolean addCoalition(Coalition coalition);
	/**
	 * To register Triggers
	 * @param t a new Trigger
	 */
	public void addBroadcastTrigger(BroadcastTrigger t);
	/**
	 * To register a {@link BroadcastListener}. Each registered {@link BroadcastListener} receives Broadcast
	 * Implements Observer Pattern
	 * 
	 * @param bl the BroadcastListener to register
	 */
	public void addBroadcastListener(BroadcastListener bl);	
}
