package edu.memphis.ccrg.lida.framework;

import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * ThreadSpawners are classes that create, manage, and end new threads.  
 * 
 * @author ryanjmccall
 *
 */
public interface ThreadSpawner {

	public abstract int getSpawnedThreadCount();
	
	public abstract void stopSpawnedThreads();
	
	/**
	 * The supplied runnables will be start by the spawner right away.
	 */
	public abstract void setInitialRunnables(List<Runnable> initialRunnables);

	/**
	 * TODO:
	 * 
	 * @param r
	 * @param t
	 */
	public abstract void receiveFinishedTask(FutureTask<Object> finishedTask, Throwable t);

}
