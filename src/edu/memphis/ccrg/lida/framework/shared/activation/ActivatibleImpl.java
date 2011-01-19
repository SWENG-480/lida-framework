/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared.activation;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * Generic Activatible Implementation. Useful to inherit from it 
 * Activatible classes like nodes or codelets.
 *  
 * @author Javier Snaider
 * 
 */
public class ActivatibleImpl implements Activatible {
	
	//TODO set decayRate
	private double decayRate = 0.1;
	private double currentExcitation;
	private double activation;
	private ExciteStrategy exciteStrategy;
	private DecayStrategy decayStrategy;
	private static final Logger logger = Logger.getLogger(ActivatibleImpl.class.getCanonicalName());

	public ActivatibleImpl() {
		activation = 0.0;
		exciteStrategy = new DefaultExciteStrategy();
		decayStrategy = new LinearDecayStrategy();
	}
	
	public ActivatibleImpl(double activation, ExciteStrategy eb, DecayStrategy db) {
		this.activation = activation;
		this.exciteStrategy = eb;
		this.decayStrategy = db;
	}

	@Override
	public void decay(long ticks) {	
		if (decayStrategy != null) {
			logger.log(Level.FINEST,this.toString() + " before decay has " + activation,LidaTaskManager.getCurrentTick());
			synchronized(this){
				currentExcitation -= decayRate*ticks;
				//activation = curveStrategy.getY(currentExcitation);
				activation = decayStrategy.decay(activation,ticks);
			}
			logger.log(Level.FINEST,this.toString() + " after decay has " + activation,LidaTaskManager.getCurrentTick());
		}
	}

	@Override
	public void excite(double excitation) {	
		if (exciteStrategy != null) {
			logger.log(Level.FINEST,this.toString() + " before excite has " + activation,LidaTaskManager.getCurrentTick());
			synchronized(this){
				currentExcitation += excitation;
				//activation = curveStrategy.getY(currentExcitation);
				activation = exciteStrategy.excite(activation, excitation);
			}
			logger.log(Level.FINEST,this.toString() + " after excite has " + activation,LidaTaskManager.getCurrentTick());
		}
	}

	@Override
	public double getActivation() {
		return activation;
	}

	@Override
	public DecayStrategy getDecayStrategy() {
		return decayStrategy;
	}

	@Override
	public ExciteStrategy getExciteStrategy() {
		return exciteStrategy;
	}

	@Override
	public synchronized void setActivation(double d) {
		this.activation = d;
	}

	@Override
	public synchronized void setDecayStrategy(DecayStrategy db) {
		this.decayStrategy = db;
	}

	@Override
	public synchronized void setExciteStrategy(ExciteStrategy eb) {
		this.exciteStrategy = eb;
	}

	@Override
	public double getTotalActivation() {
		return activation;
	}

	@Override
	public double getExcitation() {
		return currentExcitation;
	}

	@Override
	public void setExcitation(double excitation) {
		currentExcitation = excitation;
	}

}//
