package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * This interface defines how codelets can access data from Workspace sub modules.
 * Modules that need to be accessible to codelets should implement this interface.
 * 
 * @author ryanjmccall
 *
 */
public interface WorkspaceBuffer extends LidaModule{

//	/**
//	 * @return NodeStructure representation of the buffer contents.
//	 */
//	public abstract Object getModuleContent();
//	
//	/**
//	 * decays all the nodes in the buffer.
//	 * If a node's activation results lower than lowerActivationBound, it is removed from the buffer.
//	 * 
//	 * @param lowerActivationBound
//	 */
//	public void decayNodes(double lowerActivationBound);
	
 public void setLowerActivationBound (double lowerActivationBound);
}
