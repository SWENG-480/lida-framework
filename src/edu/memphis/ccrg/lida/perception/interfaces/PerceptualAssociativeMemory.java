/**
 * PAMinterface.java
 */
package edu.memphis.ccrg.lida.perception.interfaces;

import java.util.Set;
import java.util.Map;

import edu.memphis.ccrg.lida.perception.LinkImpl;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.perception.featureDetector.FeatureDetectorImpl;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;

/**
 * @author Ryan McCall
 */
public interface PerceptualAssociativeMemory{
	
	/**
	 * Updates PAM's parameters from the supplied map
	 */
	public void setParameters(Map<String, Object> parameters);
	
	/**
	 * Adds the specified nodes and/or links to the PAM
	 * Each nodes added must be register which requires
	 * the refresh and buildLayerMap operations (see PAM.java) 
	 */
	public void addToPAM(Set<PamNodeImpl> nodes, Set<FeatureDetectorImpl> featureDetectors, Set<LinkImpl> links);
	
	/**
	 * Sense the current SenseContent
	 */ 
	public void sense();
	
	/**
	 * Passes activation in a feed-forward direction. 
	 * After activation is passed nodes must synchronize() their total activation. 
	 */
	public void passActivation();
	
	/**
	 * Changes how the nodes in this PAM are excited.
	 * Affects the function of sense() and passActivation()
	 * 
	 * @param behavior
	 */
	public void setExciteBehavior(ExciteBehavior behavior);
	public void sendPercept();	
	public void decay();
	public void setDecayCurve(DecayBehavior c);

}//interface PAMinterface