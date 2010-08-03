/*
 * ProtectedGoal.java
 *
 * Created on December 15, 2003, 4:48 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.*;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public class ProtectedGoal extends Goal
{
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.ProtectedGoal");
    private Hashtable inhibitoryPropositions;    
    
    public ProtectedGoal(String name)
    {
        super(name);
                
        inhibitoryPropositions = new Hashtable();
    }
    
    public ProtectedGoal(String name, boolean persistent) 
    {
        super(name, persistent);
                
        inhibitoryPropositions = new Hashtable();
    }
    
    public void grantActivation(double delta, double gamma, NodeStructure state)
    {        
        super.grantActivation(gamma);
        
        if(super.isActive())
        {         
            logger.info("GOAL : INHIBITION " + name);
            
            Iterator iterator = inhibitoryPropositions.keySet().iterator();
            while(iterator.hasNext())
            {
                Object deleteProposition = iterator.next();
                
                if(state.hasNode((Node) deleteProposition))
                {
                    LinkedList behaviors = (LinkedList)inhibitoryPropositions.get(deleteProposition);

                    if(behaviors.size() > 0)
                    {
                        double inhibited = delta / behaviors.size();

                        Iterator li = behaviors.iterator();            
                        while(li.hasNext())
                        {
                            try 
                            {
                                Behavior behavior = (Behavior)li.next();
                                behavior.inhibit(inhibited / behavior.getDeleteList().size());
                                logger.info("\t<--" + behavior.getName() + " " + inhibited / behavior.getAddList().size() + " for " + deleteProposition);
                            }
                            catch(ArithmeticException ae)
                            {
                                ae.printStackTrace();
                            }
                        }
                    }
                }
            }
        }        
    }
    
    public Hashtable getInhibitoryPropositions()
    {
        return inhibitoryPropositions;
    }
    
    public void setInhibitoryPropositions(Hashtable inhibitoryPropositions) throws NullPointerException                                  
    {
        if(inhibitoryPropositions != null)
            this.inhibitoryPropositions = inhibitoryPropositions;
        else
            throw new NullPointerException();
    }     
    
    public static double getInhibitoryStrength()
    {
    	//TODO
    	return 0.8;
     //   return BehaviorNetworkImpl.getDelta();
    }    
}
