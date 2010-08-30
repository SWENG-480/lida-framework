/**
 * BehaviorNet.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 6:25 PM
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.BasicReinforcer;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.Reinforcer;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.BasicSelector;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.Selector;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;

/**
 * 
 * @author Ryan J McCall, Sidney D'Mello
 *
 */
public class BehaviorNetworkImpl extends LidaModuleImpl implements ActionSelection, ProceduralMemoryListener, BroadcastListener{    
	
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Net");
    
//    /**
//     * Starting value for candidateBehaviorThreshold
//     */
//    private final double startingCandidateBehaviorThreshold = 0.9;
//
//    //TODO get rid of this?
//    /**
//     * Current threshold for becoming active (THETA)
//     */
//    private double candidateBehaviorThreshold = startingCandidateBehaviorThreshold;    
    
    /**
     * Amount of excitation by conscious broadcast (PHI)
     */
    private double broadcastExcitationAmount = 0.5;        
    
    /**
	 * If behaviors' activation falls below this threshold after they are decayed then the behavior
	 * will be removed from the behavior network.
	 */
	private double lowerBoundForBehaviorActivation = 0.0;
	
    private double successorExcitationFactor = 1.0;

    private double predecessorExcitationFactor = 1.0;
    
    private double conflictorExcitationFactor = 1.0;   
    
//    /**
//     * function by which the behavior activation threshold is reduced
//     */
//	private CandidateThresholdReducer candidateThresholdReducer = new BasicCandidationThresholdReducer();
    
	/**
	 * Way that a winning behavior is chosen among those over threshold 
	 */
    private Selector selectorStrategy = new BasicSelector();
    
    /**
     * How behavior's base-level activation are reinforced
     * TODO Use excite strategy 
     */
    private Reinforcer reinforcementStrategy = new BasicReinforcer();
    
    /**
     * Currently selected behavior
     */
    private Behavior winner = null;        
    
    /**
     * Current conscious broadcast
     */
    private NodeStructure currentState = null;
    
    /**
     * Listeners of this action selection
     */
    private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();
       
    /**
     * All the streams currently in this behavior network
     */
    private ConcurrentMap<Long, Stream> streams = new ConcurrentHashMap<Long, Stream>();  
    
    /**
     * Map of behaviors indexed by the propositions appearing in their pre conditions
     * Stores environmental links.
     * 
     * this is similar to our procedural memory
     */
    private ConcurrentMap<Node, WeakHashSet<Behavior>> behaviorsByPrecondition = new ConcurrentHashMap<Node, WeakHashSet<Behavior>>();
    
    private ConcurrentMap<Node, WeakHashSet<Behavior>> behaviorsByDeleteItem = new ConcurrentHashMap<Node, WeakHashSet<Behavior>>();

	private ConcurrentMap<Node, WeakHashSet<Behavior>> behaviorsByAddItem = new ConcurrentHashMap<Node, WeakHashSet<Behavior>>();
	
	/**
	 * I expect this to be the action selection driver
	 */
	private TaskSpawner taskSpawner;
    
    public BehaviorNetworkImpl() {    
    	super();
    }
    
    //TODO make sure this is set up by initializer
    public void setTaskSpawner(TaskSpawner ts){
    	this.taskSpawner = ts;
    }
    
    @Override
    public void init(Map<String, ?> params) {
    	
    }
    
    //*** Module communication methods
    //TODO check this everywhere. 
    public void receiveBroadcast(BroadcastContent bc){
    	currentState = (NodeStructure) bc;
 
    	LidaTask activatingTask = new ActivateBehaviorsFromBroadcastTask(this);
    	taskSpawner.addTask(activatingTask);
    }
	/**
	 * Theory says receivers of the broadcast should learn from it.
	 */
	public void learn(){}
	
    public void addActionSelectionListener(ActionSelectionListener listener){
        listeners.add(listener);
   }
    
    @Override
	public void addListener(ModuleListener listener) {
    	if (listener instanceof ActionSelectionListener)
			addActionSelectionListener((ActionSelectionListener)listener);
	}
    
	@Override
	public void receiveBehavior(Behavior newBehavior){
		indexBehaviorByElements(newBehavior, newBehavior.getContextConditions(), behaviorsByPrecondition);      
		indexBehaviorByElements(newBehavior, newBehavior.getAddList(), behaviorsByAddItem);
		indexBehaviorByElements(newBehavior, newBehavior.getDeleteList(), behaviorsByDeleteItem);
        createInterBehaviorLinks(newBehavior);
        //
        Stream newStream = new Stream(streamIdGenerator++);
        newStream.addBehavior(newBehavior);
        streams.put(newStream.getId(), newStream);
	}
	
	private long streamIdGenerator = 0L;
	
	//TODO review making this method thread-safe
	private void indexBehaviorByElements(Behavior behavior, Set<Node> elements, Map<Node, WeakHashSet<Behavior>> map){
		for(Node element: elements){
			synchronized(element){
				WeakHashSet<Behavior> values = map.get(element);
				if(values == null){
					values = new WeakHashSet<Behavior>();
					map.put(element, values);
				}
				values.add(behavior);
			}
			
		}
	}
	
	public void createInterBehaviorLinks(Behavior newBehavior){
		//Go through the add items and create all predecessor/successor links 
		//as required by behaviors whose preconditions overlap with these items
		for(Node addItem: newBehavior.getAddList()){
			WeakHashSet<Behavior> behaviors = behaviorsByPrecondition.get(addItem);
			for(Behavior successorBehavior: behaviors){
				//Create predecessor link for other behavior
				successorBehavior.addPredecessor(addItem, newBehavior);
				//Create successor link for this behavior
				newBehavior.addSuccessor(addItem, successorBehavior);
			}
		}
		
		//Add this new behavior as a conflictor of whatever behaviors stopped by new behavior
		for(Node deleteItem: newBehavior.getDeleteList()){
			Set<Behavior> behaviorsAffectedByDelete = behaviorsByPrecondition.get(deleteItem);
			for(Behavior affectedBehavior: behaviorsAffectedByDelete){
				affectedBehavior.addConflictor(deleteItem, newBehavior);
			}
		}
		
		for(Node condition: newBehavior.getContextConditions()){
			//Find all of the new behavior's conflictors - behaviors that 
			//hurt its chances of activating
			Set<Behavior> deletors = behaviorsByDeleteItem.get(condition);
			if(deletors != null)
				newBehavior.addConflictors(condition, deletors);
			
			//Create successor links for other behaviors
			//Create predecessor links for this behavior
			Set<Behavior> addingBehaviors = behaviorsByAddItem.get(condition);
			if(addingBehaviors != null){
				for(Behavior adder: addingBehaviors){
					newBehavior.addPredecessor(condition, adder);
					adder.addSuccessor(condition, newBehavior);
				}
			}
		}//for
	}//method

	@Override
	public void triggerActionSelection() {
		selectAction();	
	}
          
	/**
	 * 
	 */
    public void selectAction(){   
        //spread activation and inhibition among behaviors        
        passActivationAmongBehaviors();
        
        //Select winner
        winner = selectorStrategy.selectBehavior(getStreams());
    	processWinner();
        
    	//Deactivate preconditions
    	deactivateAllPreconditions();
    }//method 
    

    /**
     * 
     */
    public void passActivationAmongBehaviors(){
    	for(Stream stream: getStreams()){
        	for(Behavior behavior: stream.getBehaviors()){
        		 if(behavior.isAllContextConditionsSatisfied())
        	         spreadSuccessorActivation(behavior);        
        	     else
        	    	 spreadPredecessorActivation(behavior);
        		 spreadConflictorActivation(behavior);
        	}
        }
    }
    
    /**
     * Only excite successor if precondition is not yet satisfied
     * @param behavior
     */
    public void spreadSuccessorActivation(Behavior behavior){           
        for(Node addProposition: behavior.getAddList()){
            Set<Behavior> behaviors = behavior.getSuccessors(addProposition);
            for(Behavior successor: behaviors){
            	//Should only grant activation to a successor if its precondition
            	//has not yet been satisfied
                if(successor.isContextConditionSatisfied(addProposition) == false){
                	double granted = (behavior.getActivation() * successorExcitationFactor) / successor.getContextSize();
                   //oldway: double granted = ((behavior.getActivation() * broadcastExcitationAmount) / (goalExcitationAmount * behaviors.size() * successor.getPreconditionCount());
                    successor.excite(granted);
                    logger.log(Level.FINEST, 
                    		behavior.getLabel() + "-->" + granted + " to " +
                                    successor + " for " + addProposition,
                            LidaTaskManager.getActualTick());
                }                
            }
        }        
    }//method
    
    /**
     * Don't bother exciting a predecessor for a precondition that 
     * is already satisfied.
     * @param behavior
     */
    public void spreadPredecessorActivation(Behavior behavior){             
        for(Node precondition: behavior.getContextConditions()){
            if(behavior.isContextConditionSatisfied(precondition) == false){
            	Set<Behavior> predecessors = behavior.getPredecessors(precondition);    
            	for(Behavior predecessor: predecessors){
            		double granted = (behavior.getActivation() * predecessorExcitationFactor) / (predecessor.getAddListCount() * predecessors.size());                        
                    predecessor.excite(granted);
                    logger.log(Level.FINEST, behavior.getActivation() + " " + behavior.getLabel() + "<--" + granted + " to " +
                                        predecessor + " for " + precondition,
                               LidaTaskManager.getActualTick());
                    
                }
            }
        }        
    } 
    
    //TODO Double check I converted this monster correctly
    public void spreadConflictorActivation(Behavior behavior){
        for(Node stateNode: currentState.getNodes()){
        	Set<Behavior> behaviors = behavior.getConflictors(stateNode); 
            for(Behavior conflictor: behaviors){
            	//between conflictor and behaivor
              	boolean mutualConflict = false;
               	double inhibitionAmount = -1.0 * (behavior.getActivation() * conflictorExcitationFactor) / (conflictor.getDeleteListCount());
                //oldway:double inhibited = (getTotalActivation(b) * fraction) / (behaviors.size() * conflictor.getDeleteList().size());

                for(Node conflictorPreCondition: conflictor.getContextConditions()){
                   	if(conflictor.isContextConditionSatisfied(conflictorPreCondition) == false){
                   		for(Node behaviorDeleteItem: behavior.getDeleteList()){
                   			if(conflictorPreCondition.equals(behaviorDeleteItem)){
                   				mutualConflict = true;
                   				if(conflictor.getActivation() < behavior.getActivation()){
                                    conflictor.excite(inhibitionAmount);
                                    logger.log(Level.FINEST, 
                                    		behavior.getLabel() + " inhibited " + conflictor + 
                                    		" amount " + inhibitionAmount + " for " + stateNode,
                                    		LidaTaskManager.getActualTick());
                                    		
                                }
                   				break;
                   			}
                   		}
                   	}
                    if(mutualConflict)
                       	break;
                }//for   
                
                if(!mutualConflict){
                    conflictor.excite(inhibitionAmount);
                    logger.log(Level.FINEST, 
                    		behavior.getLabel() + " inhibited " + conflictor + " amount " + inhibitionAmount + " for " + stateNode,
                    		LidaTaskManager.getActualTick());
                }
                    
            }//for each conflictor
        }//for nodes in current state    
    }//method    
    
    /**
     * For each proposition get the behaviors indexed by that proposition
     * For each behavior, excite it an amount equal to 
     * (phi)/(num behaviors indexed at current proposition * # of preconditions in behavior)
	 */
    public void grantActivationFromBroadcast(){
        for(Node proposition: currentState.getNodes()){
        	if(behaviorsByPrecondition.containsKey(proposition)){
        		Set<Behavior> behaviors = behaviorsByPrecondition.get(proposition);
                double excitationAmount = broadcastExcitationAmount / behaviors.size();
                for(Behavior b: behaviors){
                	b.satisfyContextCondition(proposition);
                	//TODO use excite strategy
                    b.excite(excitationAmount / b.getContextSize());       
                    logger.log(Level.FINEST, b.toString() + " " + excitationAmount / b.getContextSize() + " for " + proposition,
                    		LidaTaskManager.getActualTick());
                }
        	}
            
        }//for
    }//method
    
    public void processWinner(){
    	if(winner != null){                                                    
            prepareToFire(winner);
            sendAction();
//            resetCandidateBehaviorThreshold();
            reinforcementStrategy.reinforce(winner, currentState, taskSpawner);
            winner.setActivation(0.0);
          }
//        }else       
//            reduceCandidateBehaviorThreshold();
    }
    private void prepareToFire(Behavior b){
        logger.log(Level.FINEST, "BEHAVIOR : PREPARE TO FIRE " + b.getLabel(),
        		LidaTaskManager.getActualTick());
        //TODO spawn expectation codelets looking for results
    }
    
//    public void reduceCandidateBehaviorThreshold(){
//    	candidateBehaviorThreshold = candidateThresholdReducer.reduce(candidateBehaviorThreshold);
//        logger.log(Level.FINEST, "Candidate behavior threshold REDUCED to " + candidateBehaviorThreshold,
//        		LidaTaskManager.getActualTick());
//    }
//    public void resetCandidateBehaviorThreshold(){
//        candidateBehaviorThreshold = startingCandidateBehaviorThreshold;
//        logger.log(Level.FINEST, "Candidate behavior threshold RESET to  " + candidateBehaviorThreshold, 
//        		LidaTaskManager.getActualTick());
//    }
       
	private void sendAction(long actionId) {
        for(ActionSelectionListener l: listeners)
        	l.receiveActionId(actionId);
    }

	private void sendAction() {
		sendAction(winner.getActionId());
	}
	
	public void deactivateAllPreconditions(){
		 for(Stream s: getStreams())				
	        for(Behavior b: s.getBehaviors())
	        	b.deactivateAllContextConditions();   
	}
	
	/**
	 * Decay all the behaviors in all the schemes.  
	 * Remove the behavior after decay if its activation is below the lower bound.
	 */
	@Override
	public void decayModule(long ticks){
		for(Stream stream: getStreams()){
			for(Behavior behavior: stream.getBehaviors()){
				behavior.decay(ticks);
				if(behavior.getActivation() <= lowerBoundForBehaviorActivation){
					logger.log(Level.FINER, "Removing behavior: " + behavior.getLabel(), LidaTaskManager.getActualTick());
					removeBehavior(stream, behavior);
				}
			}
		}
	}
	
	/**
	 * Removes specified behavior from the behavior net, severing all links to other
	 * behaviors and removing it from the specified stream which contained it.
	 * @param containingStream
	 * @param behavior
	 */
	private void removeBehavior(Stream containingStream, Behavior behavior){	
		//remove behavior as a successor of other behaviors
		for(Node precondition: behavior.getContextConditions()){
			Set<Behavior> addersOfPrecondition = behaviorsByAddItem.get(precondition);
			for(Behavior adder: addersOfPrecondition)
				adder.removeSuccessor(precondition, behavior);
			
			Set<Behavior> behaviors = behaviorsByPrecondition.get(precondition);
			behaviors.remove(behavior);
		}
		
		//remove behavior as a predecessor of other behaviors
		for(Node addItem: behavior.getAddList()){
			Set<Behavior> benefactors = behaviorsByPrecondition.get(addItem);
			for(Behavior benefactor: benefactors){
				benefactor.removePredecessor(addItem, behavior);
			}
				
			Set<Behavior> behaviors = behaviorsByAddItem.get(addItem);
			behaviors.remove(behavior);
		}
		
		
		//remove behavior as a conflictor of other behaviors
		for(Node deleteItem: behavior.getDeleteList()){
			Set<Behavior> conflicteds = behaviorsByPrecondition.get(deleteItem);
			for(Behavior conflicted: conflicteds)
				conflicted.removeConflictor(deleteItem, behavior);
			
			//Remove behavior from delete item map
			Set<Behavior> behaviors = behaviorsByDeleteItem.get(deleteItem);
			behaviors.remove(behavior);
		}
		
		//remove behavior from stream
		containingStream.removeBehavior(behavior);
	}
	
    public void setReinforcementStrategy(Reinforcer reinforcer){
        this.reinforcementStrategy = reinforcer;
    }
    
//	public void setBehaviorActivationThreshold(double threshold){
//		this.candidateBehaviorThreshold = threshold;
//	}
	public void setBroadcastExcitationAmount(double broadcastExciation){
		this.broadcastExcitationAmount = broadcastExciation;
	}
	
	//*** Gets ***
	public Behavior getBehavior(long id, long actionId) {
		for(Stream s: getStreams()){
			for(Behavior b: s.getBehaviors()){
				if(b.getId() == id && b.getActionId() == actionId)
					return b;
			}
		}
		return null;		
	}
    
//    public double getBehaviorActivationThreshold(){
//        return candidateBehaviorThreshold;
//    }
    public double getBroadcastExcitationAmount(){
        return broadcastExcitationAmount;                
    }

    public Collection<Stream> getStreams(){
        return Collections.unmodifiableCollection(streams.values());        
    }        

//	public CandidateThresholdReducer getCandidateThresholdReducer() {
//		return candidateThresholdReducer;
//	}
//
//	public void setCandidateThresholdReducer(CandidateThresholdReducer reducer) {
//		this.candidateThresholdReducer = reducer;
//	}

	public Selector getSelectorStrategy() {
		return selectorStrategy;
	}

	public void setSelectorStrategy(Selector selector) {
		this.selectorStrategy = selector;
	}

	public Reinforcer getReinforcementStrategy() {
		return reinforcementStrategy;
	}
	
	@Override
	public Object getModuleContent() {
		return null;
	}

}//class