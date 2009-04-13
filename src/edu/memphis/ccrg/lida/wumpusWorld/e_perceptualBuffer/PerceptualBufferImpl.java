package edu.memphis.ccrg.lida.wumpusWorld.e_perceptualBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBuffer;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBufferListener;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletsDesiredContent;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.NodeStructureRyan;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.PAMContentImpl;

public class PerceptualBufferImpl implements PerceptualBuffer, CodeletReadable{
	
	private WorkspaceContent pamContent;	
	private List<NodeStructure> perceptBuffer;
	private List<PerceptualBufferListener> pbListeners;	
	private final int PERCEPT_BUFFER_CAPACITY = 2;
	private FrameworkGui testGui;	
	
	public PerceptualBufferImpl(){
		pamContent = new NodeStructureRyan();
		perceptBuffer = new ArrayList<NodeStructure>();
		pbListeners = new ArrayList<PerceptualBufferListener>();
	}//public Workspace()

	public void addTestGui(FrameworkGui testGui) {
		this.testGui = testGui;		
	}
	
	public void addPBufferListener(PerceptualBufferListener l){
		pbListeners.add(l);
	}
	
	public synchronized void receivePAMContent(WorkspaceContent pc){
		pamContent = pc;
	}
	
	private synchronized void storePAMContent(){
		NodeStructureRyan struct = (NodeStructureRyan)pamContent.getContent();	
		
		if(struct != null)		
			perceptBuffer.add(new NodeStructureRyan(struct));			
		
		if(perceptBuffer.size() > PERCEPT_BUFFER_CAPACITY)
			perceptBuffer.remove(0);	
	}//public void storePAMContent()
	
	/**
	 * Main method of the perceptual buffer.  Stores shared content 
	 * and then sends it to the codelet driver.
	 */
	public void sendContentToCodelets(){
		storePAMContent();
		
		if(perceptBuffer.size() > 0){
			NodeStructureRyan tempGraph = new NodeStructureRyan((NodeStructureRyan)perceptBuffer.get(0));
			for(int i = 0; i < pbListeners.size(); i++){				
				pbListeners.get(i).receivePBufferContent(tempGraph);				
			}//for

			List<Object> guiContent = new ArrayList<Object>();			
			guiContent.add(tempGraph.getNodes().size());
			guiContent.add(tempGraph.getLinks().size());			
			testGui.receiveGuiContent(FrameworkGui.FROM_PERCEPTUAL_BUFFER, guiContent);
		}
			
	}//sendContent

	/**
	 * for codelets to get Content from the buffer.  Eventually based on an objective.
	 * Currently objective not used.
	 */
	public WorkspaceContent getCodeletsObjective(CodeletsDesiredContent objective) {
		NodeStructureRyan content = new NodeStructureRyan();
		
		synchronized(this){
			for(NodeStructure struct: perceptBuffer){
				Set<Node> nodes = struct.getNodes();					
				for(Node n: nodes)
					content.addNode(n);				
			}//for each struct in the buffer
		}//synchronized
		
		return content;
	}//getCodeletsObjective

}//PerceptualBuffer
