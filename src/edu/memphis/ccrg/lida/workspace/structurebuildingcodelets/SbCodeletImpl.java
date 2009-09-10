package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.memphis.ccrg.lida.framework.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.LidaTaskNames;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

public class SbCodeletImpl extends LidaTaskImpl implements StructureBuildingCodelet{

	//Initialized by constructor
	private List<Collection<NodeStructure>> accessibleBuffers = new ArrayList<Collection<NodeStructure>>();
	private List<CodeletWritable> writables = new ArrayList<CodeletWritable>();
	
	private NodeStructure soughtContent;
	private CodeletAction action;
	//
	private int type;
	private CodeletResult results = new BasicCodeletResult();
	
	public SbCodeletImpl(LidaTaskManager tm){
		super(tm, LidaTaskNames.STRUCTURE_BUILDING_CODELET);
	}

	public Object getResult(){
		return results;
	}

	protected void runThisLidaTask(){	
		for(Collection<NodeStructure> buffer: accessibleBuffers)
			for(CodeletWritable writable: writables)
				action.performAction(buffer, writable);	
		results.reportFinished();
	}
	
	//**** StructureBuildingCodelet methods ****
	public void setSoughtContent(NodeStructure content){
		soughtContent = content;
	}
	public NodeStructure getSoughtContent(){
		return soughtContent;
	}
	//
	public void setCodeletAction(CodeletAction a){
		action = a;
	}	
	public CodeletAction getCodeletAction(){
		return action;
	}

	public void addReadableBuffer(Collection<NodeStructure> buffer) {
		accessibleBuffers.add(buffer);		
	}
	public void addWritableModule(CodeletWritable module) {
		writables.add(module);
	}
	public List<Collection<NodeStructure>> getReadableBuffers() {
		return accessibleBuffers;
	}
	public List<CodeletWritable> getWriteableBuffers() {
		return writables;
	}
	//
	public void reset() {
		accessibleBuffers.clear();
		writables.clear();
		setNumberOfTicksPerStep(50);
		setActivation(0.0);
		soughtContent = new NodeStructureImpl();
		action = new BasicCodeletAction();
		type = 0;		
	}
	public int getType() {
		return type;
	}

	public void addReadableBuffer(NodeStructure buffer) {
		List<NodeStructure> list = new ArrayList<NodeStructure>();
		list.add(buffer);
		accessibleBuffers.add(list);
	}
	
}//class SBCodelet
