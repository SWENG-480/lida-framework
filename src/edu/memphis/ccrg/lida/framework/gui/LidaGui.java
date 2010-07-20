/* To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LidaGui.java
 *
 * Created on 12/07/2009, 08:47:40
 */

package edu.memphis.ccrg.lida.framework.gui;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.converters.collections.MapConverter;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.panels.LidaPanel;
import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;

/**
 * 
 * @author Javier Snaider
 */
public class LidaGui extends javax.swing.JFrame {

	private static final long serialVersionUID = 100L;
	private static int PANEL_NAME = 0;
	private static int CLASS_NAME = 1;
	private static int PANEL_POSITION = 2;
	private static int TAB_ORDER = 3;
	private static int MUST_REFRESH = 4;
	private static int FIRST_PARAM = 5;

	private List<LidaPanel> panels = new ArrayList<LidaPanel>();
	private Lida lida;
	private LidaGuiController controller;
	private static Logger logger = Logger
			.getLogger("lida.framework.gui.LidaGui");

	public LidaGui(Lida lida, LidaGuiController controller,
			Properties panelProperties) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e){}
		
		initComponents();
		this.lida = lida;
		this.controller = controller;

		loadPanels(panelProperties);
		pack();
		logger.log(Level.INFO, "LidaGUI started", 0L);
	}

	/**
	 * @param lida
	 * @param controller
	 * @param panelsFile
	 * @throws Exception 
	 */
	private void loadPanels(Properties panelProp){

		String[][] panelsArray = new String[panelProp.size()][];
		int i = 0;
		for (Object key : panelProp.keySet()) {
			String line = panelProp.getProperty((String) key);
			String[] vals = line.split(","); // name,class,pos,tab
												// Order,refresh[Y/N],[optional
												// parameters],...
			if ((vals.length < FIRST_PARAM)) {
				logger.log(Level.WARNING,
						"Error reading line for Panel " + key, 0L);
			} else {
				panelsArray[i++] = vals;
			}
		}
		Arrays.sort(panelsArray, new Comparator<String[]>() { // sort panel by
																// position and
																// tab order
					public int compare(String[] arg0, String[] arg1) {
						String s1 = arg0[PANEL_POSITION];
						String s2 = arg1[PANEL_POSITION];
						int pos = s1.compareToIgnoreCase(s2);
						if (pos == 0) {
							s1 = arg0[TAB_ORDER];
							s2 = arg1[TAB_ORDER];
							pos = s1.compareToIgnoreCase(s2);
						}
						return pos;
					}
				});

		for (String[] vals : panelsArray) {
			LidaPanel panel;

			try {
				panel = (LidaPanel) (Class.forName(vals[CLASS_NAME]))
						.newInstance();
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), 0L);
				continue;
			}
			panel.setName(vals[PANEL_NAME]);
			panel.registerLida(lida);
			panel.registrerLidaGuiController(controller);
			String[] param = new String[vals.length - FIRST_PARAM];
			System.arraycopy(vals, FIRST_PARAM, param, 0, vals.length
					- FIRST_PARAM);
			panel.initPanel(param);
			addLidaPanel(panel, vals[PANEL_POSITION]);
			panels.add(panel);
			if (vals[MUST_REFRESH].equalsIgnoreCase("Y")) {
				panel.refresh();
			}
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jSplitPane1 = new javax.swing.JSplitPane();
		jSplitPane2 = new javax.swing.JSplitPane();
		jTabbedPanelR = new javax.swing.JTabbedPane();
		principalTabbedPanel = new javax.swing.JTabbedPane();
		menuBar = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		openMenuItem = new javax.swing.JMenuItem();
		saveMenuItem = new javax.swing.JMenuItem();
		saveAsMenuItem = new javax.swing.JMenuItem();
		exitMenuItem = new javax.swing.JMenuItem();
		editMenu = new javax.swing.JMenu();
		cutMenuItem = new javax.swing.JMenuItem();
		copyMenuItem = new javax.swing.JMenuItem();
		pasteMenuItem = new javax.swing.JMenuItem();
		deleteMenuItem = new javax.swing.JMenuItem();
		jMenu1 = new javax.swing.JMenu();
		helpMenu = new javax.swing.JMenu();
		contentsMenuItem = new javax.swing.JMenuItem();
		aboutMenuItem = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

		jSplitPane2.setRightComponent(jTabbedPanelR);

		jSplitPane1.setLeftComponent(jSplitPane2);
		jSplitPane1.setRightComponent(principalTabbedPanel);
		principalTabbedPanel.getAccessibleContext().setAccessibleName("Visual");

		getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

		fileMenu.setText("File");

		openMenuItem.setText("Open");
		openMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(openMenuItem);

		saveMenuItem.setText("Save");
		saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(saveMenuItem);

		saveAsMenuItem.setText("Save As ...");
		saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveAsMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(saveAsMenuItem);

		exitMenuItem.setText("Exit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

		editMenu.setText("Edit");

		cutMenuItem.setText("Cut");
		editMenu.add(cutMenuItem);

		copyMenuItem.setText("Copy");
		editMenu.add(copyMenuItem);

		pasteMenuItem.setText("Paste");
		editMenu.add(pasteMenuItem);

		deleteMenuItem.setText("Delete");
		editMenu.add(deleteMenuItem);

		menuBar.add(editMenu);

		jMenu1.setText("Panels");
		menuBar.add(jMenu1);

		helpMenu.setText("Help");

		contentsMenuItem.setText("Contents");
		helpMenu.add(contentsMenuItem);

		aboutMenuItem.setText("About");
		helpMenu.add(aboutMenuItem);

		menuBar.add(helpMenu);

		setJMenuBar(menuBar);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN
																				// -
																				// FIRST
																				// :
																				// event_saveMenuItemActionPerformed
		XStream xstream = new XStream();
		xstream.alias("node", NodeImpl.class);
		xstream.alias("pamNode", PamNodeImpl.class);
		xstream.alias("map", ConcurrentHashMap.class);
		xstream.alias("link", LinkImpl.class);

		xstream.registerLocalConverter(NodeImpl.class, "refNode",
				new AbstractSingleValueConverter() {
					@SuppressWarnings("unchecked")
					@Override
					public boolean canConvert(Class arg0) {
						return (PamNode.class).isAssignableFrom(arg0);
					}

					@Override
					public Object fromString(String arg0) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public String toString(Object o) {
						return ((PamNode) o).getId() + "";
					}
				});
		xstream.registerConverter(new MapConverter(xstream.getMapper()) {
			@SuppressWarnings("unchecked")
			public boolean canConvert(Class type) {
				return type.isAssignableFrom(ConcurrentHashMap.class);
			}
		});
		logger.log(Level.INFO, "saving in XML");
		  try {
			xstream.toXML(lida.getSubmodule(ModuleName.PerceptualAssociativeMemory).getModuleContent(),new FileWriter("configs/lidaPAM.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String xml = xstream.toXML(lida.getTaskManager().getSpawnedTasks());
		logger.log(Level.INFO, "XML Generated");
//		System.out.println(xml);
//		try {
//			PrintWriter out = new FileWriter("configs/lida.xml");
//			logger.log(Level.INFO, "File Opened");
//			out.print(xml);
//			out.flush();
//			out.close();
//			logger.log(Level.INFO, "File closed");
//		} catch (FileNotFoundException e) {
//			logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()), 0L);
//		}

	}// GEN-LAST:event_saveMenuItemActionPerformed

	private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN
																				// -
																				// FIRST
																				// :
																				// event_openMenuItemActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_openMenuItemActionPerformed

	private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN
																				// -
																				// FIRST
																				// :
																				// event_saveAsMenuItemActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_saveAsMenuItemActionPerformed

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		// System.exit(0);
	}// GEN-LAST:event_exitMenuItemActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JMenuItem aboutMenuItem;
	private javax.swing.JMenuItem contentsMenuItem;
	private javax.swing.JMenuItem copyMenuItem;
	private javax.swing.JMenuItem cutMenuItem;
	private javax.swing.JMenuItem deleteMenuItem;
	private javax.swing.JMenu editMenu;
	private javax.swing.JMenuItem exitMenuItem;
	private javax.swing.JMenu fileMenu;
	private javax.swing.JMenu helpMenu;
	private javax.swing.JMenu jMenu1;
	private javax.swing.JSplitPane jSplitPane1;
	private javax.swing.JSplitPane jSplitPane2;
	private javax.swing.JTabbedPane jTabbedPanelR;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenuItem openMenuItem;
	private javax.swing.JMenuItem pasteMenuItem;
	private javax.swing.JTabbedPane principalTabbedPanel;
	private javax.swing.JMenuItem saveAsMenuItem;
	private javax.swing.JMenuItem saveMenuItem;

	// End of variables declaration//GEN-END:variables

	/**
	 * Adds a Panel to the main GUI.
	 * 
	 * @param panel
	 *            the panel to add.
	 * @param panelPosition
	 *            Determines where the panel is going to be placed. </br> A:
	 *            Upper Left position. </br> B: Upper Right position. In a new
	 *            TAB</br> C: Lower position. In a new TAB</br> TOOL: In the
	 *            ToolBox </br> FLOAT: In a new frame. </br>
	 */
	private void addLidaPanel(LidaPanel panel, String panelPosition) {
		JPanel jPanel = panel.getPanel();

		if ("A".equalsIgnoreCase(panelPosition)) {
			jSplitPane2.setTopComponent(jPanel);
		} else if ("B".equalsIgnoreCase(panelPosition)) {
			jTabbedPanelR.addTab(panel.getName(), jPanel);
		} else if ("C".equalsIgnoreCase(panelPosition)) {
			principalTabbedPanel.addTab(panel.getName(), jPanel);
		} else if ("FLOAT".equalsIgnoreCase(panelPosition)) {
			JDialog dialog = new JDialog(this, panel.getName());
			dialog.add(jPanel);
			dialog.pack();
			dialog.setVisible(true);
		} else if ("TOOL".equalsIgnoreCase(panelPosition)) {
			getContentPane().add(jPanel, java.awt.BorderLayout.PAGE_START);
		} else {
			logger.log(Level.WARNING, "Position error for panel "
					+ panel.getName() + " pos:" + panelPosition, 0L);
		}
	}

	public Collection<LidaPanel> getPanels() {
		return Collections.unmodifiableCollection(panels);
	}

}
