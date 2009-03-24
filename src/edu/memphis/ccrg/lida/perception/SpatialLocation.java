package edu.memphis.ccrg.lida.perception;

import java.util.Map;

import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;

public class SpatialLocation implements Node{
	private int iLocation = 0;
	private int jLocation = 0;

	public SpatialLocation(int i, int j) {
		iLocation = i;
		jLocation = j;
	}
	
	public boolean equals(Object obj){
		if(!(obj instanceof SpatialLocation))
			return false;
		SpatialLocation other = (SpatialLocation)obj;
		return iLocation == other.iLocation && jLocation == other.jLocation;
	}	
	
	/**
	 * 
	 */
	public int hashCode(){ 
        int hash = 1;
        Integer i = new Integer(iLocation);
        Integer j = new Integer(jLocation);
        
        hash = hash * 31 + i.hashCode();
        hash = hash * 31 + (j == null ? 0 : i.hashCode());
        return hash;
    }   
	
	public int getI(){return iLocation;}
	public void setI(int i){iLocation = i;}
	public int getJ(){return jLocation;}
	public void setJ(int j){jLocation = j;}

	public void print() {
		System.out.println("x: " + jLocation + " y: " + iLocation);		
	}

	public Node copy() {
		// TODO Auto-generated method stub
		return null;
	}

	public void decay() {
		// TODO Auto-generated method stub
		
	}

	public void excite(double amount) {
		// TODO Auto-generated method stub
		
	}

	public double getCurrentActivation() {
		// TODO Auto-generated method stub
		return 0;
	}

	public DecayBehavior getDecayBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public ExciteBehavior getExciteBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getImportance() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	public Node getReferencedNode() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isRelevant() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setActivation(double d) {
		// TODO Auto-generated method stub
		
	}

	public void setDecayBehavior(DecayBehavior c) {
		// TODO Auto-generated method stub
		
	}

	public void setExciteBehavior(ExciteBehavior behavior) {
		// TODO Auto-generated method stub
		
	}

	public void setID(long id) {
		// TODO Auto-generated method stub
		
	}

	public void setLabel(String label) {
		// TODO Auto-generated method stub
		
	}

	public void setReferencedNode(Node n) {
		// TODO Auto-generated method stub
		
	}

	public void setValue(Map<String, Object> values) {
		// TODO Auto-generated method stub
		
	}

	public void synchronize() {
		// TODO Auto-generated method stub
		
	}

	public int getLayerDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void printActivationString() {
		// TODO Auto-generated method stub
		
	}

	public void decay(DecayBehavior decayBehavior) {
		// TODO Auto-generated method stub
		
	}	
	
}
