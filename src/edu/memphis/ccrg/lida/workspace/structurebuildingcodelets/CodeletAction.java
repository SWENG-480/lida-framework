package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.Collection;
import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface CodeletAction {

	public void performAction(Collection<NodeStructure> buffer, CodeletWritable destination);
	
}
  