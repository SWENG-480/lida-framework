/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.globalworkspace.triggers.Trigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TriggerListener;

/**
 * This class implements GlobalWorkspace and maintains the collection of
 * Coalitions. It supports triggers that are in charge to trigger the new
 * broadcast. Triggers should implement Trigger interface. This class maintains a
 * list of broadcastListeners. These are the modules (classes) that need to
 * receive broadcast content.
 * 
 * @author Javier Snaider
 * 
 */
public class GlobalWorkspaceImpl implements GlobalWorkspace, TriggerListener{
	private Set<Coalition> coalitions = new HashSet<Coalition>();
	private final int MAX_COALITIONS = 100;
	private List<Trigger> triggers = new ArrayList<Trigger>();
	private List<BroadcastListener> broadcastListeners = new ArrayList<BroadcastListener>();
	private Boolean broadcastStarted = false;
	private FrameworkGui testGui;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seelida.globalworkspace.GlobalWorkspace#addBroadcastListener(edu.memphis.ccrg.
	 * globalworkspace.BroadcastListener)
	 */
	public void addBroadcastListener(BroadcastListener bl) {
		broadcastListeners.add(bl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.globalworkspace.GlobalWorkspace#addTrigger(edu.memphis.ccrg.globalworkspace.
	 * Trigger)
	 */
	public void addTrigger(Trigger t) {
		triggers.add(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.globalworkspace.GlobalWorkspace#putCoalition(edu.memphis.ccrg.globalworkspace
	 * .Coalition)
	 */
	public synchronized boolean addCoalition(Coalition coalition) {
		if(coalitions.add(coalition)){
			if(coalitions.size() > MAX_COALITIONS)
				coalitions.clear();
			newCoalitionEvent();
			return true;
		}else{
			return false;
		}
	}

	private void newCoalitionEvent() {
		for (Trigger t : triggers) 
			t.command(coalitions);
	}//method

	private void sendBroadcast() {
		Coalition coal;
		synchronized (this) {
			coal = chooseCoalition();
			if (coal != null) {
				coalitions.remove(coal);
			}
		}
		if (coal != null) {
			BroadcastContent content = coal.getContent();
			for (BroadcastListener bl : broadcastListeners) {
				bl.receiveBroadcast(content);
			}
		}
		for(Coalition c:coalitions){
			c.decay();
		}
		resetTriggers();
		synchronized (broadcastStarted) {
			broadcastStarted = false;
		}
	}

	private void resetTriggers() {
		for (Trigger t : triggers) {
			t.reset();
		}
	}

	private Coalition chooseCoalition() {
		Coalition chosed = null;
		for (Coalition c : coalitions) {
			if (chosed == null || c.getActivation() > chosed.getActivation()) {
				chosed = c;
			}
		}
		return chosed;
	}

	public void start() {
		for (Trigger t : triggers) {
			t.start();
		}

	}

	/**
	 * This method realizes the broadcast. First it chooses the winner
	 * coalition. Then, all registered BroadcastListeners receive a reference
	 * SEEEEE to the coalition content.
	 * 
	 * this method is supposed to be called from Triggers. All triggers are
	 * reseted, reset() method is invoked on each of them. The coalition pool is
	 * cleared.
	 * 
	 */
	public void trigger() {
		synchronized (broadcastStarted) {
			if (!broadcastStarted) {
				broadcastStarted = true;
				sendBroadcast();
			}
		}
	}

	public void addTestGui(FrameworkGui testGui) {
		this.testGui = testGui;		
	}
}
