/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.triggers;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

/**
 * If any behavior is above threshold, this trigger will fire.
 *
 */

public class IndividualBehaviorActivationTrigger extends AggregateBehaviorActivationTrigger {
private Logger logger = Logger.getLogger("lida.actionselection.triggers");
	
	public void checkForTrigger(Set<Scheme> behaviors) {
		for(Scheme c:behaviors){
			if(c.getActivation()>threshold){
				logger.log(Level.FINE,"Individual Activation trigger ",LidaTaskManager.getActualTick());
				as.triggerActionSelection();
				break;
			}
		}
	}//method


}