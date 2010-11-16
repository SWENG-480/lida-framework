/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NodeStructureTable.java
 *
 * Created on Sep 14, 2009, 7:10:07 PM
 */

package edu.memphis.ccrg.lida.framework.gui.panels;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

import javax.swing.table.AbstractTableModel;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.pam.PamNode;

/**
 *
 * @author ryanjmccall
 */
public class NodeStructureTable extends LidaPanelImpl {

	private static final long serialVersionUID = -2584446529055507492L;
	private NodeStructure nodeStructure;
    
	/** Creates new form NodeStructureTable */
    public NodeStructureTable() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jToolBar1 = new javax.swing.JToolBar();
        refreshButton = new javax.swing.JButton();        

        jToolBar1.setRollover(true);

        refreshButton.setText("refresh");
        refreshButton.setFocusable(false);
        refreshButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refreshButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(refreshButton);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
        );
        
        table = new javax.swing.JTable();

        table.setModel(new NodeStructureTableModel());
        jScrollPane1.setViewportView(table);

       /* org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
        );*/
    }// </editor-fold>


    // Variables declaration - do not modify
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton refreshButton;
    // End of variables declaration
	private LidaModule module;
    
    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
    	refresh();
    }//GEN-LAST:event_refreshButtonActionPerformed
    
    
	public void initPanel(String[]param){
		ModuleName moduleType=null;
		if (param==null || param.length==0){
		logger.log(Level.WARNING,"Error initializing NodeStructure Panel, not enough parameters.",0L);
		return;
		}
		String[] modules = param[0].split("\\.");
		try{
		 moduleType= ModuleName.valueOf(modules[0]);
		}catch (Exception e){
			logger.log(Level.WARNING,"Error initializing NodeStructure Panel, Parameter is not a ModuleType.",0L);
			return;
		}
		module = lida.getSubmodule(moduleType);
		if (module==null){
			logger.log(Level.WARNING,"Error initializing NodeStructure Panel, Module does not exist in LIDA.",0L);
			return;			
		}
		for (int i=1; i<modules.length;i++){
			try{
				 moduleType= ModuleName.valueOf(modules[i]);
				}catch (Exception e){
					logger.log(Level.WARNING,"Error initializing NodeStructure Panel, Parameter is not a ModuleType.",0L);
					return;
				}
			
			module=module.getSubmodule(moduleType);
			if (module==null){
				logger.log(Level.WARNING,"Error initializing NodeStructure Panel, SubModule "+moduleType+ " does not exist.",0L);
				return;			
			}
		}
		
		display(module.getModuleContent());
		//draw();
	}

    public void refresh(){
    	display(module.getModuleContent());
    }
    
    public void registerLida(Lida lida){
		super.registerLida(lida);
		
	}
	private class NodeStructureTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3902918248689475445L;
		private String[] columNames ={"Node","Activation","Base Activation","Threshold"};
		public int getColumnCount() {
			return columNames.length;
		}

		public int getRowCount() {
			return nodeStructure.getNodeCount();
		}

		public String getColumnName(int column){
			if(column<columNames.length){
				return columNames[column];
			}
			return "";
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			Node node=null;
			if (rowIndex>nodeStructure.getNodeCount() || columnIndex > columNames.length
					||rowIndex<0||columnIndex<0){
				return null;
			}
			Collection<Node> nodes = nodeStructure.getNodes();
			Iterator<Node> it = nodes.iterator();
			for (int i=0;i<=rowIndex;i++){
				node=it.next();
			}
			switch(columnIndex){
			case 0:
				return node.getLabel();
			case 1:
				return node.getActivation();
			case 2:
				if (node instanceof PamNode){
					return ((PamNode)node).getBaseLevelActivation();
				}else{
					return "";
				}
			case 3:
				if (node instanceof PamNode){
					return ((PamNode)node).getPerceptThreshold();
				}else{
					return "";
				}
			default:
				return "";
			}
		}
	}
	
	public void display(Object o) {
		if (o instanceof NodeStructure) {
			nodeStructure = (NodeStructure) o;
			((AbstractTableModel) table.getModel()).fireTableStructureChanged();
			//((AbstractTableModel) table.getModel()).fireTableDataChanged();
		}
	}// method
}
