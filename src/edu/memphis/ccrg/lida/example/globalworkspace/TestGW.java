/**
 * 
 */
package edu.memphis.ccrg.lida.example.globalworkspace;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TriggerListener;
import edu.memphis.ccrg.lida.globalworkspace.triggers.AggregateActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.IndividualActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TimeOutLapTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TimeOutTrigger;


/**
 * @author Javier Snaider
 * 
 */
public class TestGW {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GlobalWorkspace gw = new GlobalWorkspaceImpl();
		BroadcastTrigger tr;
		Map<String, Object> parameters;
		
		tr = new TimeOutTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("name", "TimeOut");
		parameters.put("delay", 100L);
		tr.setUp(parameters, (TriggerListener) gw);
		gw.addBroadcastTrigger(tr);

		tr = new AggregateActivationTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("threshold", 0.8);
		tr.setUp(parameters, (TriggerListener) gw);
		gw.addBroadcastTrigger(tr);

		tr = new TimeOutLapTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("name", "TimeOutLap");
		parameters.put("delay", 50L);
		tr.setUp(parameters, (TriggerListener) gw);
		gw.addBroadcastTrigger(tr);

		tr = new IndividualActivationTrigger();
		parameters = new HashMap<String, Object>();
		parameters.put("threshold", 0.5);
		tr.setUp(parameters, (TriggerListener) gw);
		gw.addBroadcastTrigger(tr);

		gw.addBroadcastListener(new BroadcastListener(){

			public void receiveBroadcast(BroadcastContent bc) {
				System.out.println(bc);
			}});
		
		Thread threadW=new Thread(new MockWorkspace(gw,55));
		gw.start();
		threadW.start();
		
	}

}
