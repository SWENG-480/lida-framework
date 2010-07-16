package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.shared.LearnableActivatible;
import edu.memphis.ccrg.lida.framework.shared.Node;

/**
 * A PamNode extends nodes.  The added functionalities are mainly due to the fact that
 * PamNodes are involved in activation passing where Nodes are not. 
 * @author Ryan J McCall, Javier Snaider
 *
 */
public interface PamNode extends Node, LearnableActivatible{

	/**
	 * Determines if this node is relevant. A node is relevant if its total
	 * activation is greater or equal to the selection threshold.
	 * 
	 * @return <code>true</code> if this node is relevant
	 * @see #selectionThreshold
	 */
	public abstract boolean isOverThreshold();

	/**
	 * returns selection threshold
	 * 
	 * @return Selection threshold
	 */
	public abstract double getSelectionThreshold();
	
	/**
	 * Set the threshold
	 * @param threshold
	 */
	public abstract void setSelectionThreshold(double threshold);

	/**
	 * Returns final min activation variable
	 * @return
	 */
	public abstract double getMinActivation();

	/**
	 * Returns final max activation variable
	 * @return
	 */
	public abstract double getMaxActivation();
	


}// interface