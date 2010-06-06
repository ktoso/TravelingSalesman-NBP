/*
 */
package pl.edu.netbeans.generators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JPanel;
import pl.edu.netbeans.toolbox.Constants;

public final class GenerateGraphVisualPanel1 extends JPanel {

    /** Creates new form GenerateGraphVisualPanel1 */
    public GenerateGraphVisualPanel1() {
        initComponents();

        initMyComponents();
    }

    @Override
    public String getName() {
        return "Wybierz parametry symulacji";
    }

    public String getNodeCount() {
        return nodeCount.getText();
    }

    public boolean isLoadingExisting() {
        return loadExisting.isSelected();
    }

    public String getGraphFilename() {
        return graphFilename.getSelectedItem().toString();
    }

    public String getMaxGenerations() {
        return maxGenerations.getText();
    }

    public String getMaxGenerationsWithoutGettingBetter() {
        return maxGenerationsWGB.getText();
    }

    public String getPopulationSize() {
        return populationSize.getText();
    }

    public boolean isGreedy() {
        return greedyAlgorithm.isSelected();
    }

    public String getCrossoverType() {
        return crossoverType.getSelectedItem().toString();
    }

    public String getSelectionType() {
        return selectionType.getSelectedItem().toString();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        nodeCount = new javax.swing.JTextField();
        loadExisting = new javax.swing.JCheckBox();
        graphFilename = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        maxGenerationsWGB = new javax.swing.JTextField();
        maxGenerations = new javax.swing.JTextField();
        populationSize = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        crossoverType = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        greedyAlgorithm = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        selectionType = new javax.swing.JComboBox();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.jLabel1.text")); // NOI18N

        nodeCount.setText(org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.nodeCount.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(loadExisting, org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.loadExisting.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.jLabel3.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.jLabel4.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.jLabel5.text")); // NOI18N

        maxGenerationsWGB.setText(org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.maxGenerationsWGB.text")); // NOI18N
        maxGenerationsWGB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxGenerationsWGBActionPerformed(evt);
            }
        });

        maxGenerations.setText(org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.maxGenerations.text")); // NOI18N
        maxGenerations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxGenerationsActionPerformed(evt);
            }
        });

        populationSize.setText(org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.populationSize.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.jLabel6.text_1")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.jLabel7.text_1")); // NOI18N

        crossoverType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Heurystyczne", "OX" }));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.jLabel8.text_1")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(greedyAlgorithm, org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.greedyAlgorithm.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.jLabel9.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(GenerateGraphVisualPanel1.class, "GenerateGraphVisualPanel1.jLabel10.text")); // NOI18N

        selectionType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ruletka", "Turniej" }));
        selectionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectionTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(302, 302, 302))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                            .addComponent(jLabel6)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(greedyAlgorithm, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(populationSize, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(crossoverType, javax.swing.GroupLayout.Alignment.TRAILING, 0, 166, Short.MAX_VALUE)
                                    .addComponent(selectionType, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel3))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(maxGenerations)
                                    .addComponent(maxGenerationsWGB, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(loadExisting))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(graphFilename, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nodeCount, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nodeCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(graphFilename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadExisting))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(maxGenerationsWGB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(maxGenerations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(populationSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(crossoverType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(selectionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(greedyAlgorithm))
                .addContainerGap(38, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void maxGenerationsWGBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxGenerationsWGBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maxGenerationsWGBActionPerformed

    private void maxGenerationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxGenerationsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maxGenerationsActionPerformed

    private void selectionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectionTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_selectionTypeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox crossoverType;
    private javax.swing.JComboBox graphFilename;
    private javax.swing.JCheckBox greedyAlgorithm;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JCheckBox loadExisting;
    private javax.swing.JTextField maxGenerations;
    private javax.swing.JTextField maxGenerationsWGB;
    private javax.swing.JTextField nodeCount;
    private javax.swing.JTextField populationSize;
    private javax.swing.JComboBox selectionType;
    // End of variables declaration//GEN-END:variables

    private void initMyComponents() {

        //generators/../data
        File datapath = new File(Constants.DATA_FOLDER);
        System.out.println("DATA_FOLDER: "+ datapath.getAbsolutePath());

        File files[] = datapath.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.getName().matches(".*.xml");
            }
        });

        if ( files.length > 0 ) {
            //sensowne sortowanie zebranych plików, "\dnodes" są sortowane po wartościach tych liczb 100 > 5 etc.
            Arrays.sort(files, new Comparator<File>() {

                private final static String PATTERN = "^(\\d)*nodes.xml$";

                public int compare(File o1, File o2) {
                    String s1 = o1.getName();
                    String s2 = o2.getName();

                    if (s1.matches(PATTERN) && s2.matches(PATTERN)) {
                        return extractInteger(s1).compareTo(extractInteger(s2));
                    }

                    return o1.compareTo(o2);
                }

                private Integer extractInteger(String filename) {
                    return Integer.parseInt(filename.substring(0, filename.indexOf('n')));
                }
            });
        }

        for (File f : files) {
            graphFilename.addItem(f.getName());
        }

        graphFilename.setEnabled(false);

        loadExisting.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graphFilename.setEnabled(!graphFilename.isEnabled());
                nodeCount.setEnabled(!nodeCount.isEnabled());
            }
        });
    }
}

