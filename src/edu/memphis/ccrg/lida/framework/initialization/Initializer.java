package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Properties;

import edu.memphis.ccrg.lida.framework.Lida;

public interface Initializer {
	
	public abstract void initModule(Initializable module,Lida lida,Properties p); 

}
