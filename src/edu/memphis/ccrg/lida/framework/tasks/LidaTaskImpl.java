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
package edu.memphis.ccrg.lida.framework.tasks;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.shared.ActivatibleImpl;

/**
 * This class implements the LidaTask Interface. This class should be used as the base class for all LidaTasks.
 * @author Javier Snaider
 */
public abstract class LidaTaskImpl extends ActivatibleImpl implements LidaTask {

	private static final long serialVersionUID = 9005638432126098336L;

	private static Logger logger= Logger.getLogger(LidaTaskImpl.class.getCanonicalName());

	private static int defaultTicksPerStep = 1;
	private long taskID;
	private int ticksPerStep = defaultTicksPerStep;
	private long nextExcecutionTickLap = defaultTicksPerStep;
	protected LidaTaskStatus status = LidaTaskStatus.WAITING;
	private Map<String, ? extends Object> parameters;
	private TaskSpawner ts;
	private long scheduledTick;
	private static long nextTaskID;
	
	public LidaTaskImpl() {
		this(defaultTicksPerStep,null);
	}
	public LidaTaskImpl(int ticksForCycle) {
		this(ticksForCycle,null);
	}
	public LidaTaskImpl(int ticksForCycle,TaskSpawner ts) {
		taskID = nextTaskID++;
		this.ts=ts;
		setNumberOfTicksPerRun(ticksForCycle);
	}
	
	/**
	 * @return the scheduledTick
	 */
	@Override
	public long getScheduledTick() {
		return scheduledTick;
	}
	/**
	 * @param scheduledTick the scheduledTick to set
	 */
	@Override
	public void setScheduledTick(long scheduledTick) {
		this.scheduledTick = scheduledTick;
	}
	
	/**
	 * @return the defaultTicksPerStep
	 */
	public static int getDefaultTicksPerStep() {
		return defaultTicksPerStep;
	}
	
	
	/**
	 * Sets the default number of ticks that is used in the constructor without this parameter.
	 * @param defaultTicksPerStep default number of ticks
	 */
	public static void setDefaultTicksPerStep(int defaultTicksPerStep) {
		if (defaultTicksPerStep > 0) {
			LidaTaskImpl.defaultTicksPerStep = defaultTicksPerStep;
		}
	}

	/** 
	 * This method is not supposed to be called directly nor overwritten.
	 * Overwrite the runThisLidaTask method.
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public LidaTask call() {
		nextExcecutionTickLap=ticksPerStep;
		
		try{
			runThisLidaTask();
		}catch(Exception e){
			e.printStackTrace();
			logger.log(Level.WARNING, "Exception " + e.toString() + " encountered in task " + this.toString(), LidaTaskManager.getActualTick());
		}
		
		if (ts != null) 
			ts.receiveFinishedTask(this);
		else 
			logger.log(Level.WARNING, "This task {1} doesn't have an assigned TaskSpawner",new Object[] { LidaTaskManager.getActualTick(), this });
		
		return this;
	}


	/**
	 * To be overridden by child classes. Overriding method should execute a
	 * handful of statements considered to constitute a single iteration of the
	 * task. For example, a codelet might look in a buffer for some
	 * representation and make a change to it in a single iteration.
	 * 
	 */
	protected abstract void runThisLidaTask();

	/**
	 *  If a task is canceled it cannot be restarted.
     *  So only set the status if the status is not CANCELED.
     *  
	 * @param status - the status to set
	 */
	@Override
	public void setTaskStatus(LidaTaskStatus status) {
		if (this.status != LidaTaskStatus.CANCELED)
			this.status = status;
	}

	/**
	 * @return the status
	 */
	@Override
	public LidaTaskStatus getStatus() {
		return status;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#getTaskId()
	 */
	@Override
	public long getTaskId() {
		return taskID;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#setTaskID(long)
	 */
	@Override
	public void setTaskID(long id) {
		taskID = id;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#getTicksPerStep()
	 */
	@Override
	public int getTicksPerStep() {
		return ticksPerStep;
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#setNumberOfTicksPerRun(int)
	 */
	@Override
	public void setNumberOfTicksPerRun(int ticks) {
		if (ticks > 0){
			ticksPerStep = ticks;
			setNextExcecutionTickLap(ticks);
		}
	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#reset()
	 */
	@Override
	public void reset() {

	}

	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#stopRunning()
	 */
	@Override
	public void stopRunning() {
		setTaskStatus(LidaTaskStatus.CANCELED);
	}
	
	/**
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTask#init(Map)
	 */
	@Override
	public void init(Map<String, ?> parameters) {
		this.parameters = parameters;
		init();
	}

	/**
	 * This is a convenience method to initialize Tasks. It is called from init(Map<String, Object> parameters). 
	 * Subclasses can overwrite this method in order to initialize the LidaTask
	 */
	@Override
	public void init() {
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#getParameter(java.lang.String)
	 */
	@Override
	public Object getParam(String name, Object defaultValue) {
		Object value = null;
		if (parameters != null) {
			value = parameters.get(name);
		}
		if (value == null) {
			logger.log(Level.WARNING, "Missing parameter, check factories data or first parameter: " + name, LidaTaskManager.getActualTick());
			value = defaultValue;
		}
		return value;
	}
	

	/* 
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#getTaskSpawner()
	 */
	@Override
	public TaskSpawner getControllingTaskSpawner() {		
		return ts;
	}

	/*
	 * @see edu.memphis.ccrg.lida.framework.LidaTask#setControllingTaskSpawner(edu.memphis.ccrg.lida.framework.TaskSpawner)
	 */
	@Override
	public void setControllingTaskSpawner(TaskSpawner ts) {
		this.ts=ts;		
	}
	
	@Override
	public long getNextExcecutionTickLap() {		
		return nextExcecutionTickLap;
	}
	
	/**
	 * For just the next execution of this task, sets the number of ticks in the future when this task will be run
	 */
	@Override
	public void setNextExcecutionTickLap(long lapTick) {
		this.nextExcecutionTickLap=lapTick;	
	}
	
	@Override
	public void setAssociatedModule(LidaModule module) {
	}	
}// class
