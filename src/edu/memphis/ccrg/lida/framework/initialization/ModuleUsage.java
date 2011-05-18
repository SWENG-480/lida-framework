package edu.memphis.ccrg.lida.framework.initialization;

import edu.memphis.ccrg.lida.framework.FrameworkModule;

/**
 * Specifies different ways a {@link FullyInitializable} will use
 * an associated module.
 * 
 * @see FullyInitializable#setAssociatedModule(FrameworkModule, String)
 * @author Javier Snaider
 *
 */
public class ModuleUsage {
	//TODO dynamic enum like ModuleName

	public static final String NOT_SPECIFIED = "NOT_SPECIFIED";
	public static final String TO_READ_FROM = "TO_READ_FROM";
	public static final String TO_WRITE_TO = "TO_WRITE_TO";
	public static final String TO_DELETE_FROM = "TO_DELETE_FROM";
	public static final String TO_CHECK_FROM = "TO_CHECK_FROM";
	public static final String TO_LISTEN_FROM = "TO_LISTEN_FROM";	
	
}
