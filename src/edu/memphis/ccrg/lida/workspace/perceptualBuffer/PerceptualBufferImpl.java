package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import edu.memphis.ccrg.lida.perception.GraphImpl;
import edu.memphis.ccrg.lida.perception.PAMContentImpl;
import edu.memphis.ccrg.lida.perception.Percept;
import edu.memphis.ccrg.lida.perception.interfaces.PAMListener;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.sbCodelets.CodeletAccessible;
import edu.memphis.ccrg.lida.workspace.sbCodelets.CodeletObjective;
import edu.memphis.ccrg.lida.workspace.sbCodelets.WorkspaceContent;

public class PerceptualBufferImpl implements PAMListener, PerceptualBuffer, CodeletAccessible{
	
	private PAMContentImpl pamContent;	
	private List<NodeStructure> perceptBuffer;
	private List<PerceptualBufferListener> pbListeners;	
	private final int PERCEPT_BUFFER_CAPACITY = 2;	
	
	public PerceptualBufferImpl(){
		pamContent = new PAMContentImpl();
		perceptBuffer = new ArrayList<NodeStructure>();
		pbListeners = new ArrayList<PerceptualBufferListener>();
	}//public Workspace()
	
	public synchronized void receivePAMContent(PAMContentImpl pc){
		pamContent = pc;
	}
	
	private synchronized void storePAMContent(){
		GraphImpl struct = (GraphImpl)pamContent.getContent();				
		if(struct != null){
			perceptBuffer.add(new GraphImpl(struct));			
		}
		if(perceptBuffer.size() > PERCEPT_BUFFER_CAPACITY){
			perceptBuffer.remove(0);
		}		
	}//public void storePAMContent()
	
	public void addPBufferListener(PerceptualBufferListener l){
		pbListeners.add(l);
	}
	
	public void sendContent(){
		storePAMContent();
		
		if(perceptBuffer.size() > 0){
			for(int i = 0; i < pbListeners.size(); i++){
				GraphImpl tempGraph = new GraphImpl((GraphImpl)perceptBuffer.get(0));
				//p.print();
				PerceptualBufferContentImpl content = new PerceptualBufferContentImpl();
				content.addContent(tempGraph);
				pbListeners.get(i).receivePBufferContent(content);
				
			}//for
		}
			
	}//sendContent

	////O(BufferSize * PerceptSize * log(objectives.size()))
	public WorkspaceContent getCodeletsObjective(CodeletObjective objective) {
		PerceptualBufferContentImpl content = new PerceptualBufferContentImpl();
		
		synchronized(this){
			for(NodeStructure struct: perceptBuffer){
				Set<Node> nodes = struct.getNodes();
				for(Node n: nodes){
					content.addNode(n);					
				}
			}
		}
		
		return content;

	}//getCodeletsObjective

}//PerceptualBuffer
