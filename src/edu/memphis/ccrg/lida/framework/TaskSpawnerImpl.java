package edu.memphis.ccrg.lida.framework;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class TaskSpawnerImpl extends LidaTaskImpl implements TaskSpawner{

	private static Logger logger = Logger.getLogger("lida.framework.TaskSpawnerImpl");
	/**
	 * Used to execute the tasks
	 */
	private ThreadPoolExecutor executorService;
	/**
	 * The running tasks
	 */
	private Set<LidaTask> runningTasks = new HashSet<LidaTask>();

	private boolean tasksPaused = false;

	/**
	 * @return the tasksArePaused
	 */
	public boolean isTasksPaused() {
		return tasksPaused;
	}

	public TaskSpawnerImpl(int ticksForCycle) {
		super(ticksForCycle);
		//
		int corePoolSize = 5;
		int maxPoolSize = 100;
		long keepAliveTime = 10;
		executorService = new LidaExecutorService(this, corePoolSize,
				maxPoolSize, keepAliveTime, TimeUnit.SECONDS);
	}// method

	public TaskSpawnerImpl() {
		this(1);
	}

	public void setInitialTasks(List<? extends LidaTask> initialTasks) {
		for (LidaTask r : initialTasks)
			addTask(r);
	}

	public void addTask(LidaTask task) {
		task.setTaskStatus(LidaTask.WAITING_TO_RUN);
		runningTasks.add(task);
		runTask(task);
	}

	/**
	 * Finished tasks from the FrameworkExecutorService are sent to this method.
	 * If it is overridden then is should still be called first using super.
	 */
	public void receiveFinishedTask(LidaTask task, Throwable t) {
		
		switch (task.getTaskStatus()) {
		case LidaTask.FINISHED: 
			// TODO: get and process result
			//System.out.println("FINISHED");
		case LidaTask.CANCELLED:
			logger.log(Level.FINEST, "cancelling task {0}", task);
			runningTasks.remove(task);
			//System.out.println("CANCELLED");
			break;
		case LidaTask.TO_RESET:
			logger.log(Level.FINEST, "reseting task {0}", task);
			task.reset();
			//System.out.println("TO_RESET");
		case LidaTask.WAITING_TO_RUN:
			//TODO:
		case LidaTask.RUNNING:
			task.setTaskStatus(LidaTask.WAITING_TO_RUN);
			logger.log(Level.FINEST, "Running task {0}", task);
			runTask(task);
			//System.out.println("RUN");
			break;
		}
	}// method
	protected void runTask(LidaTask task) {
		if (!tasksPaused) {
			if (shouldRun(task)) {
				task.setTaskStatus(LidaTask.RUNNING);
				executorService.execute(task);
			}
		}
	}
	protected boolean shouldRun(LidaTask r) {
		// if not in ticks mode then should run task
		if (!LidaTaskManager.isTicksModeEnabled())
			return true;
		// if task not already running and if task has enough ticks 
		// then should run task
		else if (r.getTaskStatus() != LidaTask.RUNNING && r.hasEnoughTicks())
			return true;
		return false;
		
//		if (!(LidaTaskManager.isTicksModeEnabled())
//				|| ((r.getTaskStatus() != LidaTask.RUNNING) && (r.hasEnoughTicks()))) {
//			return true;
//		}
	}

	public Collection<LidaTask> getAllTasks() {
		return Collections.unmodifiableCollection(runningTasks);
	}

	public int getSpawnedTaskCount() {
		return runningTasks.size();
	}// method

	public void stopRunning() {
		for (LidaTask s : runningTasks) {
			logger.log(Level.INFO, "Stopping task: {0}", s);
			s.stopRunning();
			//Don't remove this! we need it!!!!!!
			s.setTaskStatus(LidaTask.CANCELLED);
			//Don't remove this.
		}
		executorService.shutdownNow();
		logger.info("All spawned tasks have been told to stop");
	}// method

	/**
	 * This method is override in this class in order to spawn the ticks to the
	 * sub tasks
	 */
	@Override
	public void addTicks(int ticks) {
		super.addTicks(ticks);
		for (LidaTask s : runningTasks) {
			s.addTicks(ticks);
			runTask(s);
		}// for
	}// method

	public void pauseSpawnedTasks() {
		logger.log(Level.FINE, "All Tasks paused.");
		tasksPaused = true;
	}

	public void resumeSpawnedTasks() {
		tasksPaused = false;
		for (LidaTask task : runningTasks) {
			int status = task.getTaskStatus();
			if ((status & (LidaTask.RUNNING | LidaTask.WAITING_TO_RUN | LidaTask.TO_RESET)) != 0) {
				task.setTaskStatus(LidaTask.WAITING_TO_RUN);
				logger.log(Level.FINEST, "Resuming task {0}", task);
				runTask(task);
			}
		}
	}// method

}// class