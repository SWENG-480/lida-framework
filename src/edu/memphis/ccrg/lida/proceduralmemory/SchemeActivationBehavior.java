/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public interface SchemeActivationBehavior {
	
	/**
	 * 
	 * @param broadcast source NodeStructure
	 * @param schemeMap indexed schemes
	 */
	public void activateSchemesWithBroadcast(NodeStructure broadcast, Map<? extends Object, Set<Scheme>> schemeMap);
	
	/**
	 * set amount of activation a scheme must have for instantiation
	 * @param d threshold
	 */
	public void setSchemeSelectionThreshold(double d);

}
