package edu.memphis.ccrg.lida.actionselection.mockclasses;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

public class ActionSelectionImpl implements ActionSelection {

	@Override
	public void addActionSelectionListener(ActionSelectionListener listener) {
		

	}

	@Override
	public void selectAction() {
		System.out.println("action selected");

	}

	@Override
	public void setTaskSpawner(TaskSpawner taskSpawner) {
		

	}

	@Override
	public void triggerActionSelection() {
		
		selectAction();
	}

}
