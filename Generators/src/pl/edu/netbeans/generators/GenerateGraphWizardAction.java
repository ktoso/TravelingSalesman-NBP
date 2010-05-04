/*
 */
package pl.edu.netbeans.generators;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import javax.swing.JComponent;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.WizardDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;
import prefuse.data.Graph;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLWriter;

// An example action demonstrating how the wizard could be called from within
// your code. You can copy-paste the code below wherever you need.
public final class GenerateGraphWizardAction extends CallableSystemAction {

    private WizardDescriptor.Panel[] panels;

    public void performAction() {
        WizardDescriptor wizardDescriptor = new WizardDescriptor(getPanels());
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle("Your wizard dialog title here");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            // do something
        }
    }

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            panels = new WizardDescriptor.Panel[]{
                        new GenerateGraphWizardPanel1()
                    };
            String[] steps = new String[panels.length];
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                // Default step name to component name of panel. Mainly useful
                // for getting the name of the target chooser to appear in the
                // list of steps.
                steps[i] = c.getName();
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    // Sets step number of a component
                    // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_*:
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                    // Sets steps names for a panel
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    // Turn on subtitle creation on each step
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                    // Show steps on the left side with the image on the background
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                    // Turn on numbering of all steps
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
                }
            }
        }
        return panels;
    }

    public String getName() {
        return "Start Sample Wizard";
    }

    @Override
    public String iconResource() {
        return null;
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        WizardDescriptor wizardDescriptor = new WizardDescriptor(getPanels());
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle("Generator Grafów");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;

        if (!cancelled) {
            String nodeCount = (String) wizardDescriptor.getProperty("nodeCount");
            DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("Rozpoczynam generowanie " + nodeCount + " węzłów..."));

            doGenerateGraph(Integer.parseInt(nodeCount));//pewne iż jest integerem, przeszło walidację
        }
    }

    /**
     * Generuje oraz otwiera nowy graf o podanej licznie wierzchołków.
     * Nazwy miast może pobrać z data/cities.txt
     */
    private void doGenerateGraph(int nodeCount) {
        Graph graph = new Graph(false);

        //dodaję obsługę potrzebnych nam dodatkowych informacji
        graph.addColumn("name", String.class);
        graph.addColumn("weight", int.class, 1);

        CityNameProvider cities = new CityNameProvider();

        //TODO: progress bar?
        for (int i = 0; i < nodeCount; i++) {
            graph.addNode().setString("name", cities.getRandomName());
            for (int j = 0; j < i; j++) {
                graph.addEdge(i, j);
                graph.getEdge(graph.getNode(i), graph.getNode(j)).setInt("weight", cities.getRandomDistance());
            }
        }

        GraphMLWriter writer = new GraphMLWriter();
        try {
            File file = new File("data/" + nodeCount + "nodes.xml");
            if (!file.exists()) {
                file.createNewFile();
            }

            writer.writeGraph(graph, file);
        } catch (DataIOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }


        DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("Zakończono generowanie węzłów..."));

        //TODO: napisać otwieranie edytowa z nową symulacją
    }
}
