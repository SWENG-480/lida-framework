package edu.memphis.ccrg.lida.attention;

import java.util.Collection;
//import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CurrentSituationalModel;

public class AttentionDriver extends ModuleDriverImpl implements BroadcastListener {

	//private Logger logger = Logger.getLogger("lida.attention.AttentionDriver");
	//
	private CurrentSituationalModel csm;
	private GlobalWorkspace global;
	//
	private double defaultActiv = 1.0;// TODO: move these to factory?
	private NodeStructure broadcastContent;
	private int defaultTicksPerStep = 3;

	public AttentionDriver(CurrentSituationalModel csm,
						   GlobalWorkspace gwksp, int ticksPerCycle) {
		super(ticksPerCycle);
		this.csm = csm;
		global = gwksp;
	}

	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = (NodeStructure) bc;
	}

	public void runThisLidaTask() {
		activateCodelets();
	}

	public void activateCodelets() {
//		//For testing only!!!!!!
//		if (getSpawnedTaskCount() < 10) {
//			addTask(new AttentionCodeletImpl(csm,global,defaultTicksPerStep,defaultActiv,getTaskManager() ,new NodeStructureImpl()));
//		}
	}

	public void learn() {
		Collection<Node> nodes = broadcastContent.getNodes();
		for (Node n : nodes) {
			// TODO:
			n.getId();
		}
	}// method

	@Override
	protected void processResults(LidaTask task) {
		// TODO Auto-generated method stub
		
	}

}// class