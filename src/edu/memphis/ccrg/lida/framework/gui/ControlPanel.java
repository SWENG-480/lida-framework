/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ControlPanel.java
 *
 * Created on 12/07/2009, 09:26:00
 */
package edu.memphis.ccrg.lida.framework.gui;

import javax.swing.JPanel;
import javax.swing.JSlider;

import edu.memphis.ccrg.lida.framework.Lida;

/**
 *
 * @author Javier Snaider
 */
public class ControlPanel extends javax.swing.JPanel implements LidaPanel,FrameworkGuiEventListener {

    private LidaGuiController controller;
    private Lida lida;
    
	boolean isPaused = true;
	private int sliderMin = 15;
	private int sliderMax = 350;
	private int sliderStartValue = 100;

    /** Creates new form ControlPanel */
    public ControlPanel() {

    	initComponents();
        jLabel3.setText("ms");

        minSleepTimeLabel.setText(sliderMin+" ms");
        maxSleepTimeLabel.setText(sliderMax+" ms");
        sleepTimeTextField.setText(this.sliderStartValue + "");

    	//actionSelection = as;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startPauseButton = new javax.swing.JButton();
        resetEnvironmentButton = new javax.swing.JButton();
        quitButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        threadCountTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        sleepTimeTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        speedSlider = new javax.swing.JSlider();
        minSleepTimeLabel = new javax.swing.JLabel();
        maxSleepTimeLabel = new javax.swing.JLabel();

        startPauseButton.setText("Start/Pause");
        startPauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startPauseButtonActionPerformed(evt);
            }
        });

        resetEnvironmentButton.setText("Reset Environment");
        resetEnvironmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetEnvironmentButtonClicked(evt);
            }
        });

        quitButton.setText("Quit");
        quitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel1.setText("LIDA Control Panel");

        jLabel4.setText("Thread Count");

        threadCountTextField.setText("--");

        jLabel7.setText("System Status");

        statusLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        statusLabel.setText("Paused");

        jLabel2.setText("Module Sleep Time");

        sleepTimeTextField.setText("--");

        jLabel3.setText("ms");

        speedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                speedSliderStateChanged(evt);
            }
        });

        minSleepTimeLabel.setText("Min");

        maxSleepTimeLabel.setText("Max");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(startPauseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetEnvironmentButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(quitButton))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
			                        .addComponent(jLabel4)
			                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(threadCountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sleepTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(speedSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(minSleepTimeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 175, Short.MAX_VALUE)
                        .addComponent(maxSleepTimeLabel)
                        .addGap(8, 8, 8)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 156, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(speedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(threadCountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(sleepTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(maxSleepTimeLabel)
                        .addComponent(minSleepTimeLabel))
                    .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startPauseButton)
                    .addComponent(resetEnvironmentButton)
                    .addComponent(quitButton)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void startPauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startPauseButtonActionPerformed
    	isPaused = !isPaused;
		if(isPaused){
			statusLabel.setText("PAUSED");
		controller.pauseRunningThreads();
		}else{
			statusLabel.setText("RUNNING");
			controller.resumeRunningThreads();
		}
		refresh();
    }//GEN-LAST:event_startPauseButtonActionPerformed

    private void resetEnvironmentButtonClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetEnvironmentButtonClicked
     	//environment.resetEnvironment();
    	controller.resetEnvironment();
    }//GEN-LAST:event_resetEnvironmentButtonClicked

    private void quitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitButtonActionPerformed
    	statusLabel.setText("QUITTING");
    	controller.quitAll();
		//frameworkTimer.resumeRunningThreads(); 
		//mainThread.stopSpawnedThreads();
    }//GEN-LAST:event_quitButtonActionPerformed

    private void speedSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_speedSliderStateChanged
   	 	JSlider source = (JSlider)evt.getSource();
   	 	if(!source.getValueIsAdjusting()){
   	 		int sleepTime = (int)source.getValue();
   	 		sleepTimeTextField.setText(sleepTime + "");
   	 		//frameworkTimer.setSleepTime(sleepTime);
   	 		controller.setSleepTime(sleepTime);
   	 		refresh();
   	 	}    
    }//GEN-LAST:event_speedSliderStateChanged
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel maxSleepTimeLabel;
    private javax.swing.JLabel minSleepTimeLabel;
    private javax.swing.JButton quitButton;
    private javax.swing.JButton resetEnvironmentButton;
    private javax.swing.JTextField sleepTimeTextField;
    private javax.swing.JSlider speedSlider;
    private javax.swing.JButton startPauseButton;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField threadCountTextField;
    // End of variables declaration//GEN-END:variables

    public void registrerLidaGuiController(LidaGuiController lgc) {
        controller = lgc;
    }

    public void display(Object o) {
    }

    public void refresh() {
    	isPaused = lida.getTimer().threadsArePaused();  	
    	String threadCount = "";
        	threadCount = (lida.getTimer().getSpawnedThreadCount() + lida.getSbCodeletDriver().getSpawnedThreadCount())+"";
          threadCountTextField.setText(threadCount);
    }

    public JPanel getPanel() {
        return this;
    }

    public int moduleSuported() {
        return FrameworkGuiEvent.FRAMEWORK;
    }

    public void receiveGuiEvent(FrameworkGuiEvent event) {
    }
    
	public void registrerLida(Lida lida) {
		this.lida=lida;		
	}
    
}
