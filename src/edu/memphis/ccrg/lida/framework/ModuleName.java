/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModuleName {
	
	public final String name;
	private static Map<String,ModuleName> moduleNames = new HashMap<String,ModuleName>();
	
	public static ModuleName getModuleName(String name){
		return moduleNames.get(name);
	}
	
	public static ModuleName addModuleName (String name){
		if (!moduleNames.containsKey(name)){
			new ModuleName(name);
		}
		return moduleNames.get(name);
	}
	private ModuleName(String name){
		this.name=name;
		moduleNames.put(name,this);
	}
	
	public static Collection<ModuleName> values(){
		return Collections.unmodifiableCollection(moduleNames.values());
	}
	
	public final static ModuleName Environment = new ModuleName("Environment"); 
public final static ModuleName	SensoryMemory = new ModuleName("SensoryMemory"); 
public final static ModuleName	PerceptualAssociativeMemory = new ModuleName("PerceptualAssociativeMemory"); 

public final static ModuleName	TransientEpisodicMemory = new ModuleName("TransientEpisodicMemory"); 
public final static ModuleName	DeclarativeMemory = new ModuleName("DeclarativeMemory"); 

public final static ModuleName	Workspace = new ModuleName("Workspace");  
public final static ModuleName	PerceptualBuffer = new ModuleName("PerceptualBuffer"); 
public final static ModuleName	EpisodicBuffer = new ModuleName("EpisodicBuffer"); 
public final static ModuleName	BroadcastQueue = new ModuleName("BroadcastQueue"); 

	public final static ModuleName CurrentSituationalModel = new ModuleName("CurrentSituationalModel"); 

public final static ModuleName	AttentionModule = new ModuleName("AttentionModule"); 
public final static ModuleName	StructureBuildingCodeletModule = new ModuleName("StructureBuildingCodeletModule");  
public final static ModuleName	GlobalWorkspace = new ModuleName("GlobalWorkspace"); 

public final static ModuleName	ProceduralMemory = new ModuleName("ProceduralMemory"); 
public final static ModuleName	ActionSelection = new ModuleName("ActionSelection"); 
public final static ModuleName	SensoryMotorMemory = new ModuleName("SensoryMotorMemory"); 

public final static ModuleName	AllModules = new ModuleName("AllModules"); 
public final static ModuleName	NoModule = new ModuleName("NoModule");  
public final static ModuleName	LIDA  = new ModuleName("LIDA"); 
}
