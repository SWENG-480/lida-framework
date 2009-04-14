package edu.memphis.ccrg.lida.wumpusWorld.c_perception.featureDetection;

import edu.memphis.ccrg.lida.perception.FeatureDetector;
import edu.memphis.ccrg.lida.shared.strategies.DetectBehavior;
import edu.memphis.ccrg.lida.util.Misc;
import edu.memphis.ccrg.lida.wumpusWorld.b_sensoryMemory.SensoryContentImpl;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.PamNodeImpl;

public class FeatureDetectorImpl implements FeatureDetector{

	public DetectBehavior detectBehav;
	private PamNodeImpl pamNode;
	
	public FeatureDetectorImpl(PamNodeImpl n, DetectBehavior b){
		detectBehav = b;
		pamNode = n;
	}
	
	public FeatureDetectorImpl(FeatureDetectorImpl n){
		this.detectBehav = n.detectBehav;
		pamNode = n.pamNode;
	}

    public void detect(SensoryContentImpl sc){    	
    	if(sc.equals(null)){
    		Misc.p("Tried to detect null SensoryContent");
    		return;
    	}
    	
    	detectBehav.detectAndExcite(pamNode, sc);
    }    

	public void setDetectBehavior(DetectBehavior b){
    	detectBehav = b;
    }
	
	public PamNodeImpl getNode(){return pamNode;}
	
}