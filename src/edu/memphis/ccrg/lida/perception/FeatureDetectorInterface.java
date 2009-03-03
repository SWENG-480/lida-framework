package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;

public interface FeatureDetectorInterface {
	public void detect(SensoryContent sm); 
	public void setDetectBehavior(DetectBehavior b);
}
