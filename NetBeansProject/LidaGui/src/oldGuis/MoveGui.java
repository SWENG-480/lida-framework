/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MoveGui.java
 *
 * Created on Mar 31, 2009, 12:56:39 PM
 */

package oldGuis;

/**
 *
 * @author ryanjmccall
 */
public class MoveGui extends javax.swing.JFrame {

    /** Creates new form MoveGui */
    public MoveGui() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grabButton = new javax.swing.JButton();
        goForwardButton = new javax.swing.JButton();
        turnLeftButton = new javax.swing.JButton();
        turnRightButton = new javax.swing.JButton();
        shootButton = new javax.swing.JButton();
        manualModeButton = new javax.swing.JButton();
        actionStatusLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        grabButton.setText("Grab");
        grabButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grabButtonActionPerformed(evt);
            }
        });

        goForwardButton.setText("Go Forward");
        goForwardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goForwardButtonActionPerformed(evt);
            }
        });

        turnLeftButton.setText("Turn Left");
        turnLeftButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                turnLeftButtonActionPerformed(evt);
            }
        });

        turnRightButton.setText("Turn Right");
        turnRightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                turnRightButtonActionPerformed(evt);
            }
        });

        shootButton.setText("Shoot");
        shootButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shootButtonActionPerformed(evt);
            }
        });

        manualModeButton.setText("Toggle Action Selection ");
        manualModeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualModeButtonActionPerformed(evt);
            }
        });

        actionStatusLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        actionStatusLabel.setText("ACTIONS MANUAL");

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 16));
        jLabel1.setText("Action Control Gui");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(jLabel1))
                    .add(layout.createSequentialGroup()
                        .add(7, 7, 7)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(layout.createSequentialGroup()
                                    .add(grabButton)
                                    .add(18, 18, 18)
                                    .add(goForwardButton)
                                    .add(12, 12, 12)
                                    .add(shootButton))
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                    .add(turnLeftButton)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(turnRightButton)))
                            .add(layout.createSequentialGroup()
                                .add(manualModeButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(actionStatusLabel)))))
                .add(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(actionStatusLabel)
                    .add(manualModeButton))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(shootButton)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(goForwardButton)
                        .add(grabButton)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(turnRightButton)
                    .add(turnLeftButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goForwardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goForwardButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_goForwardButtonActionPerformed

    private void turnLeftButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_turnLeftButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_turnLeftButtonActionPerformed

    private void turnRightButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_turnRightButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_turnRightButtonActionPerformed

    private void grabButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grabButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_grabButtonActionPerformed

    private void shootButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shootButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_shootButtonActionPerformed

    private void manualModeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualModeButtonActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_manualModeButtonActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MoveGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel actionStatusLabel;
    private javax.swing.JButton goForwardButton;
    private javax.swing.JButton grabButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton manualModeButton;
    private javax.swing.JButton shootButton;
    private javax.swing.JButton turnLeftButton;
    private javax.swing.JButton turnRightButton;
    // End of variables declaration//GEN-END:variables

}
