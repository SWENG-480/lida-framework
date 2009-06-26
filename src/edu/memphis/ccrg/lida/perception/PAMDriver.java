package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;

public class PAMDriver implements Runnable, Stoppable{

	private PerceptualAssociativeMemory pam;
	private FrameworkTimer timer;
	private boolean keepRunning = true;		
	private FrameworkGui flowGui;
	
	public PAMDriver(PerceptualAssociativeMemory pam, FrameworkTimer timer, FrameworkGui gui){
		this.pam = pam;
		this.timer = timer;
		flowGui = gui;
	}//constructor
		
	public void run(){
		while(keepRunning){
			try{
				Thread.sleep(timer.getSleepTime());
			}catch(InterruptedException e){
				stopRunning();
			}				
			timer.checkForStartPause();//won't return if paused until started again					
						
			pam.detectSensoryMemoryContent();				
			pam.propogateActivation();//Pass activation	
			pam.sendOutPercept(); //Send the percept to p-Workspace
			pam.decayPAM();  //Decay the activations	
			flowGui.receiveGuiContent(FrameworkGui.FROM_PAM, pam.getGuiContent());
		}//while	
	}//method run
	
	public void stopRunning(){
		keepRunning = false;		
	}//method
	
}//class PAMDriver