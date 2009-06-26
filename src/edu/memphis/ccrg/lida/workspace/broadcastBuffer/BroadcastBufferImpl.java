package edu.memphis.ccrg.lida.workspace.broadcastBuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;

public class BroadcastBufferImpl implements BroadcastBuffer, CodeletReadable{
	
	private NodeStructure broadcastContent = new NodeStructureImpl();	
	private List<NodeStructure> broadcastBuffer = new ArrayList<NodeStructure>();
	private List<BroadcastBufferListener> bBufferListeners = new ArrayList<BroadcastBufferListener>();	
	private final int BROADCAST_BUFFER_CAPACITY;
	private List<Object> guiContent = new ArrayList<Object>();	

	public BroadcastBufferImpl(int capacity){
		BROADCAST_BUFFER_CAPACITY = capacity;
		broadcastBuffer.add(broadcastContent);
	}

	public void addBroadcastBufferListener(BroadcastBufferListener l) {
		bBufferListeners.add(l);		
	}

	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastBuffer.add(new NodeStructureImpl((NodeStructure) bc));		
		//Keep the buffer at a fixed size
		if(broadcastBuffer.size() > BROADCAST_BUFFER_CAPACITY)
			broadcastBuffer.remove(0);//remove oldest	
	}

	/**
	 * Main method of the perceptual buffer.  Stores shared content 
	 * and then sends it to the codelet driver.
	 */
	public void activateCodelets(){
		NodeStructureImpl copiedStruct = new NodeStructureImpl((NodeStructure) broadcastBuffer.get(0));
		for(int i = 0; i < bBufferListeners.size(); i++)
			bBufferListeners.get(i).receiveBroadcastBufferContent(copiedStruct);				
		
		guiContent.clear();
		guiContent.add(copiedStruct.getNodeCount());
		guiContent.add(copiedStruct.getLinkCount());					
	}//sendContent

	/**
	 * for codelets to get Content from the buffer.  Eventually based on an objective.
	 * Currently objective not used.
	 */
	public NodeStructure lookForContent(NodeStructure objective) {
		NodeStructureImpl result = new NodeStructureImpl();
		synchronized(this){
			for(NodeStructure content: broadcastBuffer){
				Collection<Node> nodes = content.getNodes();					
				for(Node n: nodes)
					result.addNode(n);				
			}//for each struct in the buffer
		}//synchronized
		return result;
	}//method

	public List<Object> getGuiContent() {
		return guiContent;
	}//method

}//class