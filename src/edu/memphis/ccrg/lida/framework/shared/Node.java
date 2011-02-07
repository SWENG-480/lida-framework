/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * Node.java
 */
package edu.memphis.ccrg.lida.framework.shared;

import java.io.Serializable;

import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.pam.PamNode;

/**
 * Node represents a Concept in LIDA. It could be implemented in different ways 
 * for different parts of the system. For example could be pamNodes in the PAM and WorkspaceNodes
 * in the workspace.
 * Nodes with the same id represents the same concept so equals have to return true even if the objects are
 * of different classes.
 * 
 * @author Javier Snaider, Ryan J. McCall
 * 
 */
public interface Node extends Linkable, Activatible, Serializable {
	
	/**
	 * Returns the grounding PamNode.
	 * @return PamNode, in PAM, underlying this node.
	 */
    public PamNode getGroundingPamNode();
    
    /**
     * Used by factory to set the underlying PamNode for this node
     * @param n PamNode
     */
    public void setGroundingPamNode (PamNode n);
    
    /**
     * Returns Node's id
     * @return unique id
     */
    public int getId();
    
    
    /**
     * Sets Node's id
     * @param id unique id
     */
    public void setId(int id);
    
    @Override
	public String getLabel();
    
    /**
     * Sets label
     * @param label readable label
     */
    public void setLabel(String label);
    
    /**
     * Returns the name of this node in the NodeFactory
     * @return factory name of this node
     * @see LidaElementFactory 
     */
	public String getFactoryNodeType();
	
	/**
	 * Returns desirability of this node.
	 * @return a double signifying the degree to which this node is a goal of the agent
	 */
	public double getDesirability();
	
	/**
	 * Set node desirability.
	 * @param degree  degree to which this node is a goal of the agent
	 */
	public void setDesirability(double degree);

}

