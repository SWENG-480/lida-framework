package edu.memphis.ccrg.main;

import edu.memphis.ccrg.workspace.broadcasts.PreviousBroadcasts;

public class PBroadsDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private PreviousBroadcasts pbroads;
	
	public PBroadsDriver(PreviousBroadcasts p){
		pbroads = p;
	}

	public void run(){
		while(keepRunning){
			
		}//while keepRunning		
	}//public void run()

	public void stopRunning(){
		keepRunning = false;		
	}//public void stopRunning()
}
