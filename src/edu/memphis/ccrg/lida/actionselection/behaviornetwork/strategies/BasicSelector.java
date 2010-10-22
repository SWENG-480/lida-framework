/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * Selector.java
 *
 * Created on December 12, 2003, 2:52 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;

/**
 * Selector iterates and chooses competitor with max alpha
 *
 */
public class BasicSelector implements Selector{
	
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Selector");
    
//    stochastic in behavior net + drives to explore novel things
    // have a parameter which at 1.0 gives deterministic action selection.  
    // If 0.0 then completely random
    
    public BasicSelector() {       
    }
    
    public static void p(String s){
    	System.out.println(s);
    }
    
    public Behavior selectSingleBehavior(Collection<Behavior> candidateBehaviors, double candidateThreshold){
        double maxActivation = 0.0;
        List<Behavior>winners=new ArrayList<Behavior>();
        Behavior winner = null;
        p("\nStarting selection, num candidates: " + candidateBehaviors.size());
      
        for(Behavior current: candidateBehaviors){
        	
    		double currentActivation = current.getTotalActivation();
    		p("action " + current.getLabel() + " has activ: " + currentActivation);
    		if(currentActivation > maxActivation) {                    
                winners.clear();
    			winners.add(current);
                maxActivation = currentActivation;
            }else if(currentActivation == maxActivation){   
        		winners.add(current);
            }
    	}
        
        switch(winners.size()){
        	case 1:
        		winner = winners.get(0);
        		logger.log(Level.FINE, "Winner: " + winner.getLabel() + ", activ: " + maxActivation);
        		break;
        	case 0:
        		winner = null;
        		break;
        	default:
        		winner = winners.get((int)(Math.random()* winners.size()));
        		logger.log(Level.FINE, "Winner: " + winner.getLabel() + ", activ: " + maxActivation);
        }
        
        return winner;
    }
 
}
