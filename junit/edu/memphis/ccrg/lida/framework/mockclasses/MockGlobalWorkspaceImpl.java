package edu.memphis.ccrg.lida.framework.mockclasses;

import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;

public class MockGlobalWorkspaceImpl extends FrameworkModuleImpl implements GlobalWorkspace {
	
	@Override
	public void addBroadcastListener(BroadcastListener bl) {
		
		
	}

	@Override
	public void addBroadcastTrigger(BroadcastTrigger t) {
		
		
	}

	@Override
	public boolean addCoalition(Coalition coalition) {
		System.out.println("New coalition added: "+coalition.getContent());
		System.out.println();
		return true;
	}

	@Override
	public void addListener(ModuleListener listener) {
		
		
	}


	@Override
	public void decayModule(long ticks) {
		
		
	}

	@Override
	public Object getModuleContent(Object... params) {
		
		return null;
	}


	@Override
	public void triggerBroadcast(BroadcastTrigger trigger) {
		System.out.println("Broadcast started at tick: "+TaskManager.getCurrentTick());
		System.out.println();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
