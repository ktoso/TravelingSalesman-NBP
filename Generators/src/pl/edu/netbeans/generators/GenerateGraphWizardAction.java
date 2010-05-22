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
import org.openide.WizardDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;
import pl.edu.netbeans.toolbox.Pair;
import pl.edu.netbeans.visualization.VisualizerTopComponent;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLWriter;

// An example action demonstrating how the wizard could be called from within
// your code. You can copy-paste the code below wherever you need.
public final class GenerateGraphWizardAction extends CallableSystemAction {

    //FIXME: prototypowy setup max pozycji miast
    public static final int MAX_NODE_X = 300;
    public static final int MAX_NODE_Y = 200;
    private static final String DATA_FOLDER = "../data";
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
            Boolean loadingExistingGraph = (Boolean) wizardDescriptor.getProperty("loadingExistingGraph");

            String nodesFilename;
            if (loadingExistingGraph) {
                nodesFilename = DATA_FOLDER + (String) wizardDescriptor.getProperty("graphFilename");
            } else {
                nodesFilename = doGenerateGraph(Integer.parseInt(nodeCount));//pewne iż jest integerem, przeszło walidację
            }

            String maxG = (String) wizardDescriptor.getProperty("maxGenerations");
            String maxGWGB = (String) wizardDescriptor.getProperty("maxGenerationsWGB");
            String popSize = (String) wizardDescriptor.getProperty("populationSize");



            //TODO: rewrite na lookup
            //Lookup  global = Lookup.getDefault();
            VisualizerTopComponent top = new VisualizerTopComponent();
            top.open(nodesFilename);

            top.getPopulation().setMaxNumerGeneracji(Integer.parseInt(maxG));
            top.getPopulation().setMaxPokolenBezZmiany(Integer.parseInt(maxGWGB));
            top.getPopulation().setOsobnikowPopulacji(Integer.parseInt(popSize));


            top.requestActive();
        }
    }

    /**
     * Generuje oraz otwiera nowy graf o podanej licznie wierzchołków.
     * Nazwy miast może pobrać z data/cities.txt
     * @return nazwa pliku z grafem
     */
    private String doGenerateGraph(int nodeCount) {
        Graph graph = new Graph(false);

        //dodaję obsługę potrzebnych nam dodatkowych informacji
        graph.addColumn("name", String.class);
        graph.addColumn("weight", int.class, 1);
        graph.addColumn("marked", int.class);
        //w kierunku ustalonych koordynatów
        graph.addColumn("x", int.class, 0);
        graph.addColumn("y", int.class, 0);

        CityNameProvider cities = new CityNameProvider();

        //TODO: progress bar?
        //dla każdego node, ustaw mu imię
        for (int i = 0; i < nodeCount; i++) {
            graph.addNode().setString("name", cities.getRandomName());

            Node self = graph.getNode(i);
            Pair<Integer, Integer> pos = cities.getRandomPosition(MAX_NODE_X);
            int x = pos.first();
            int y = pos.second();

            self.setInt("x", x);
            self.setInt("y", y);

            //oraz dla każdej krawędzi, ustal pewne dane
            for (int j = 0; j < i; j++) {
                graph.addEdge(i, j);

                Node target = graph.getNode(j);

                Edge edge = graph.getEdge(self, target);

                //TODO: do sprawdzenia czy ok
                edge.setInt("weight", cities.calculateDistance(self, target));
            }
        }

        GraphMLWriter writer = new GraphMLWriter();
        String nodesFilename = DATA_FOLDER + File.separator + nodeCount + "nodes.xml";
        try {
            File nodesFile = new File(nodesFilename);
            if (!nodesFile.exists()) {
                nodesFile.createNewFile();
            }

            writer.writeGraph(graph, nodesFile);
        } catch (DataIOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        return nodesFilename;
    }
}
