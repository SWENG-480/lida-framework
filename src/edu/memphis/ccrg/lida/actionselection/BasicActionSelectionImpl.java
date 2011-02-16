/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.PreafferenceListener;
import edu.memphis.ccrg.lida.actionselection.triggers.ActionSelectionTrigger;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.TaskCountEvent;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;

/**
 * Rudimentary action selection that selects all behaviors sent to it which are
 * above the selectionThreshold. Only selects an action every
 * 'selectionFrequency' number of cycles it is run
 * 
 * @author Ryan J McCall
 * 
 */
public class BasicActionSelectionImpl extends LidaModuleImpl implements
		ActionSelection, ProceduralMemoryListener {

	private List<ActionSelectionTrigger> actionSelectionTriggers = new ArrayList<ActionSelectionTrigger>();

	private static final Logger logger = Logger
			.getLogger(BasicActionSelectionImpl.class.getCanonicalName());

	private double selectionThreshold = 0.95;

	private int selectionFrequency = 100, coolDownCounter = 0;

	private Queue<Behavior> behaviors = new ConcurrentLinkedQueue<Behavior>();
	private AtomicBoolean actionSelectionStarted = new AtomicBoolean(false);
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();

	/**
	 * default
	 */
	public BasicActionSelectionImpl() {
		super(ModuleName.ActionSelection);
	}

	private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();

	@Override
	public void addActionSelectionListener(ActionSelectionListener listener) {
		listeners.add(listener);
	}

	@Override
	public void receiveBehavior(Behavior b) {

		if (b.getActivation() > selectionThreshold) {
			if (coolDownCounter == selectionFrequency) {
				logger.log(Level.FINE, "selecting behavior " + b.getLabel()
						+ " " + b.getId() + " " + b.getAction() + " activ. "
						+ b.getActivation(), LidaTaskManager.getCurrentTick());
				sendAction(b.getAction());
				coolDownCounter = 0;
			} else
				coolDownCounter++;

			logger.log(Level.FINE, "Selected action: " + b.getAction(),
					LidaTaskManager.getCurrentTick());
		}

	}

	/**
	 * @param a
	 *            id of action
	 */
	public void sendAction(LidaAction a) {
		for (ActionSelectionListener l : listeners)
			l.receiveAction(a);
	}

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof ActionSelectionListener) {
			addActionSelectionListener((ActionSelectionListener) listener);
		}
	}

	@Override
	public void triggerActionSelection() {
		if (actionSelectionStarted.compareAndSet(false, true)) {
			selectAction();
		}
	}

	@Override
	public void selectAction() {
		Behavior behavior;
		behavior = chooseBehavior();
		if (behavior != null) {
			behaviors.remove(behavior);
		}

		if (behavior != null) {
			LidaAction action = behavior.getAction();
			for (ActionSelectionListener bl : listeners) {
				bl.receiveAction(action);

			}
			FrameworkGuiEvent ge = new TaskCountEvent(
					ModuleName.ActionSelection, behaviors.size() + "");
			sendEventToGui(ge);
		}
		logger.log(Level.FINE, "Action Selection Performed at tick: {0}",
				LidaTaskManager.getCurrentTick());

		resetTriggers();
		actionSelectionStarted.set(false);

	}

	/**
	 * @param evt
	 *            - gui event
	 */
	public void sendEventToGui(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener fg : guis)
			fg.receiveFrameworkGuiEvent(evt);
	}

	/**
	 * @return chosen behavior
	 */
	public Behavior chooseBehavior() {
		//TODO check if 
		Behavior chosenBehav = behaviors.peek();
		for (Behavior c : behaviors) {
			if (c.isAllContextConditionsSatisfied() && c.getActivation() > chosenBehav.getActivation()) {
				chosenBehav = c;
			}
		}// for
		return chosenBehav;
	}

	/**
	 * @param behavior
	 *            to add to selector
	 * @return true if behavior added
	 */
	public boolean addBehavior(Behavior behavior) {
		if (behaviors.add(behavior)) {
			logger.log(Level.FINE, "New Behavior added", LidaTaskManager
					.getCurrentTick());
			newBehaviorEvent(behaviors);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Object getState() {
		Object[] state = new Object[4];
		state[0] = this.behaviors;
		state[1] = null;
		state[2] = null;
		state[3] = null;
		return state;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean setState(Object content) {
		if (content instanceof Object[]) {
			Object[] state = (Object[]) content;
			if (state.length == 4) {
				try {
					this.behaviors = (Queue<Behavior>) state[0];
					return true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	public void addPreafferenceListener(PreafferenceListener listener) {
		// TODO Auto-generated method stub

	}

	/**
	 * To register Triggers
	 * 
	 * @param t
	 *            a new Trigger
	 */
	@Override
	public void addActionSelectionTrigger(ActionSelectionTrigger t) {
		actionSelectionTriggers.add(t);
	}

	/**
	 * Starts Triggers
	 */
	public void start() {
		for (ActionSelectionTrigger t : actionSelectionTriggers) {
			t.start();
		}
	}

	/**
	 * @param behaviors
	 *            behaviors to check
	 */
	public void newBehaviorEvent(Collection<Behavior> behaviors) {
		for (ActionSelectionTrigger trigger : actionSelectionTriggers)
			trigger.checkForTrigger(behaviors);
	}

	/**
	 * Resets all triggers
	 */
	public void resetTriggers() {
		for (ActionSelectionTrigger t : actionSelectionTriggers) {
			t.reset();
		}
	}

	@Override
	public void init() {
		getAssistingTaskSpawner().addTask(new BackgroundTask());
	}

	private class BackgroundTask extends LidaTaskImpl {

		public BackgroundTask() {
			super(1);
		}

		@Override
		protected void runThisLidaTask() {
			start();
			setTaskStatus(LidaTaskStatus.FINISHED); // Runs only once
		}

		@Override
		public String toString() {
			return BasicActionSelectionImpl.class.getSimpleName()
					+ " background task";
		}
	}

	@Override
	public void learn(BroadcastContent content) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void decayModule(long ticks) {
		super.decayModule(ticks);
		// TODO
	}

}