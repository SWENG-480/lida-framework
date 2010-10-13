/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.actionselection.triggers;

import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionDriver;
import edu.memphis.ccrg.lida.framework.mockclasses.ActionSelectionImpl;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.SchemeImpl;

/**
 * @author Siminder
 *
 */
public class IndividualBehaviorActivationTriggerTest {
	Set<Scheme> setOfSchemes;
	Scheme schemeA;
	Scheme schemeB;
	IndividualBehaviorActivationTrigger trigger;
	ActionSelection as;
	ActionSelectionDriver asd;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		setOfSchemes = new HashSet<Scheme>();
		schemeA = new SchemeImpl("Scheme1",1,1);
		schemeB = new SchemeImpl("Scheme2",2,2);
			
		trigger = new IndividualBehaviorActivationTrigger();
		
		as = new ActionSelectionImpl();
		asd = new ActionSelectionDriver();
		schemeA.setActivation(0.8);
		schemeB.setActivation(0.2);		
		
		setOfSchemes.add(schemeA);
		setOfSchemes.add(schemeB);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.actionselection.triggers.IndividualBehaviorActivationTrigger#checkForTrigger(java.util.Set)}.
	 */
	@Test
	public void testCheckForTriggerSetOfScheme() {
		trigger.as=as;
		trigger.asd=asd;
		trigger.threshold=0.5;
		trigger.checkForTrigger(setOfSchemes);
	}

}