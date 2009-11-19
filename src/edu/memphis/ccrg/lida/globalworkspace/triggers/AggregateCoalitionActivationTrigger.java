package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.globalworkspace.Coalition;

/**
 * Implements a trigger that is activated when the sum of all coalitions in GW 
 * is greater than the threshold.
 * 
 * @author Javier Snaider
 *
 */
public class AggregateCoalitionActivationTrigger implements BroadcastTrigger {

	protected TriggerListener gw;
	protected double threshold;
/**
 * This method is executed each time a new coalition enters the GW.
 * 
 * @param coalitions a Set with all the coallitions in the GW.
 */
	public void checkForTrigger(Set<Coalition> coalitions) {
		double acc=0;
		for(Coalition c:coalitions){
			acc=acc+c.getActivation();
		}
		if(acc>threshold){
			//System.out.println("aggregate ");
			gw.triggerBroadcast();
		}
	}//method

	public void reset() {
		// not applicable
	}

	public void setUp(Map<String, Object> parameters, TriggerListener gw) {
		this.gw=gw;
		Object o = parameters.get("threshold");
		if ((o != null)&& (o instanceof Double)) {
			threshold= (Double)o;
		}
	}//method

	public void start() {
		// not applicable
	}

}//class