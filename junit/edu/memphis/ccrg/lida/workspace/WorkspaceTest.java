/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved.
 * This program and the accompanying materials are made available
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.workspace;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.episodicmemory.CueListener;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * This class is the JUnit test for <code>Workspace</code> class.
 * @author Rodrigo Silva-Lugo
 */
public class WorkspaceTest {

    /**
     *
     */
    public WorkspaceTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     * 
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of addCueListener method, of class Workspace.
     */
    @Test
    public void testAddCueListener() {
        // TODO review test
        System.out.println("addCueListener");
        CueListener l = null;
        Workspace instance = new MockWorkspaceImpl();
        instance.addCueListener(l);
    }

    /**
     * Test of addWorkspaceListener method, of class Workspace.
     */
    @Test
    public void testAddWorkspaceListener() {
        // TODO review test
        System.out.println("addWorkspaceListener");
        WorkspaceListener l = null;
        Workspace instance = new MockWorkspaceImpl();
        instance.addWorkspaceListener(l);
    }

    /**
     * Test of cueEpisodicMemories method, of class Workspace.
     */
    @Test
    public void testCueEpisodicMemories() {
        // TODO review test
        System.out.println("cueEpisodicMemories");
        NodeStructure ns = null;
        Workspace instance = new MockWorkspaceImpl();
        instance.cueEpisodicMemories(ns);
    }

    /**
     *
     */
    public class MockWorkspaceImpl extends FrameworkModuleImpl implements Workspace {

        @Override
        public void addCueListener(CueListener l) {
        }

        @Override
        public void addWorkspaceListener(WorkspaceListener l) {
        }

        private NodeStructure receivedNs;
        
        @Override
        public void cueEpisodicMemories(NodeStructure ns) {
        	receivedNs = ns;
        }
        
        public NodeStructure getCueEpisodicNS(){
        	return receivedNs;
        }

        @Override
        public Object getModuleContent(Object... params) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void decayModule(long ticks) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void addListener(ModuleListener listener) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        @Override
        public void init() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}