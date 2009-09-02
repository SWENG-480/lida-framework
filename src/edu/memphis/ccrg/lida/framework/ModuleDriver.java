package edu.memphis.ccrg.lida.framework;

/**
 * A ModuleDriver is a class that runs a major module of the 
 * framework such as PAM or Procedural Memory.
 * 
 * The main difference b/w it an a LidaTask is that a LidaTask
 * may not be constantly running while a ModuleDriver is always running.
 * 
 * @author Ryan J. McCall
 *
 */
public interface ModuleDriver extends TaskSpawner{
	
	public abstract void runThisDriver();
	
}
