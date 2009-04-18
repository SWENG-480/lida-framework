package edu.memphis.ccrg.lida.wumpusWorld.f_sbCodelets;

import java.util.Set;
import java.util.Collection;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.LinkImpl;
import edu.memphis.ccrg.lida.shared.LinkType;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WumpusIDs;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.RyanPamNode;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.SpatialLocation;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.RyanNodeStructure;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletAction;

public class SpatialLinkCodeletAction implements CodeletAction{
	
	private int linkCount = 100;
	
	private char getAgentDirection(RyanPamNode agent){
		char dir = ' ';
		if(agent != null){
			Set<SpatialLocation> locs = agent.getLocations();
			for(SpatialLocation sl: locs)
				dir = sl.getDirection();	
		}
		return dir;
	}//method
	
	public WorkspaceContent getResultOfAction(WorkspaceContent content) {		
		RyanNodeStructure graph = (RyanNodeStructure)content;	
		RyanPamNode agent = (RyanPamNode)graph.getNode(WumpusIDs.agent);
		char agentDirection = getAgentDirection(agent);
	
		Collection<Node> nodes = graph.getNodes();				
		for(Node n: nodes){
			RyanPamNode objectInWW = (RyanPamNode)n;
			Set<SpatialLocation> locations = objectInWW.getLocations();		

			int i = 0;
			int numLocations = locations.size();
			for(SpatialLocation sl: locations){
				//Spatial relations between agent and objects
				if(objectInWW.getId() != WumpusIDs.agent){
					LinkType spatialRelationType = calcRelationType(sl, agentDirection);	
					//Link between agent and each object sensed in WW.
					if(numLocations > 1){ //For objects that appear multiple times
						RyanPamNode copy = new RyanPamNode(objectInWW);
						copy.setId(copy.getId() * 10 * (i+1));						
						graph.addLink(new LinkImpl(agent, copy, spatialRelationType, linkCount++ + ""));
					}else
						graph.addLink(new LinkImpl(agent, objectInWW, spatialRelationType, linkCount++ + ""));
				}//if not the agent
				//Link between each object in WW and one of its exact spatial locations
				graph.addLink(new LinkImpl(objectInWW, sl, LinkType.spatial, linkCount++ + ""));
				i++;
			}//for all spatial locations of n	
		}//for nodes 		
		return content;
	}//method

	private LinkType calcRelationType(SpatialLocation sl, char agentDirection) {
		LinkType type = LinkType.none;
		int i = sl.getI();
		int j = sl.getJ();		
		
		if(agentDirection == 'V'){
			if(j == 1){
				type = LinkType.inLineWith;
				if(i == 1)
					type = LinkType.inFrontOf;
			}else if(j == 0 && i == 0){
				type = LinkType.rightOf;
			}else if(j == 2 && i == 0){
				type = LinkType.leftOf;
			}
		}else if(agentDirection == 'A'){
			if(j == 1){
				type = LinkType.inLineWith;
				if(i == 1)
					type = LinkType.inFrontOf;
			}else if(j == 0 && i == 2){
				type = LinkType.leftOf;
			}else if(j == 2 && i == 2){
				type = LinkType.rightOf;
			}
		}else if(agentDirection == '<'){
			if(i == 1){
				type = LinkType.inLineWith;
				if(j == 1)
					type = LinkType.inFrontOf;
			}else if(i == 0 && j == 2)
				type = LinkType.rightOf;
			else if(i == 2 && j == 2)
				type = LinkType.leftOf;
		}else if(agentDirection == '>'){
			if(i == 1){
				type = LinkType.inLineWith;
				if(j == 1)
					type = LinkType.inFrontOf;
			}else if(i == 2 && j == 0)
				type = LinkType.rightOf;
			else if(i == 0 && j == 0)
				type = LinkType.leftOf;
		}

		return type;		
	}//method

}//class
