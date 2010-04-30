/*
 */
package pl.edu.netbeans.visualization;

import java.awt.BorderLayout;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JList;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.Lookup;
import pl.edu.netbeans.algorithms.MockTSSolverAction;
import pl.edu.netbeans.algorithms.TSSolverAction;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import prefuse.visual.tuple.TableNodeItem;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//pl.edu.netbeans.visualization//Visualizer//EN",
autostore = false)
public final class VisualizerTopComponent extends TopComponent {

    private static VisualizerTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "VisualizerTopComponent";

    public VisualizerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(VisualizerTopComponent.class, "CTL_VisualizerTopComponent"));
        setToolTipText(NbBundle.getMessage(VisualizerTopComponent.class, "HINT_VisualizerTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

        initGraph();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel = new javax.swing.JPanel();

        setBackground(java.awt.Color.white);

        jPanel.setLayout(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized VisualizerTopComponent getDefault() {
        if (instance == null) {
            instance = new VisualizerTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the VisualizerTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized VisualizerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(VisualizerTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof VisualizerTopComponent) {
            return (VisualizerTopComponent) win;
        }
        Logger.getLogger(VisualizerTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component closing
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    private void initGraph() {
        Graph graph = null;
        try {
            String filename = "data/sampleCityGraph.xml";
//            String filename = "data/socialnet.xml";
            graph = new GraphMLReader().readGraph(filename);
        } catch (DataIOException e) {
            e.printStackTrace();
            System.err.println("Error loading graph. Exiting...");
            System.exit(1);
        }

        if (graph == null) {
            System.err.println("NULL graph! Exiting...");
            System.exit(1);
        }

        ////////////////////////////////////////////////////////////////////////
        //////////////aktualizowanie listy itemów
        ////////////////////////////////////////////////////////////////////////


        // add the graph to the visualization as the data group "graph"
        // nodes and edges are accessible as "graph.nodes" and "graph.edges"
        Visualization vis = new Visualization();
        vis.add("graph", graph);

        //poniższa seria akcji będzie wykonywana w nieskończoność
        ActionList layout = new ActionList(Activity.INFINITY);
        layout.add(new ForceDirectedLayout("graph", true, false));
        //TODO: zdobywać to przez opcje oraz lookup najlepiej
        //layout.add(new TSSolverAction("graph", ???Activity.INFINITY???))//wykonaj kolejny krok alg. genetycznego
        //TODO: usunąć tą akcję testową
        layout.add(new MockTSSolverAction(graph));
        layout.add(new RepaintAction());


        // draw the "name" label for NodeItems
        LabelRenderer r = new LabelRenderer("name");
        r.setRoundedCorner(8, 8); // round the corners

        // create a new default renderer factory
        // return our name label renderer as the default for all non-EdgeItems
        // includes straight line edges for EdgeItems by default
        vis.setRendererFactory(new DefaultRendererFactory(r));


        // create our nominal color palette
        // pink for females, baby blue for males
        int[] palette = new int[]{
            ColorLib.rgb(255, 180, 180), ColorLib.rgb(190, 190, 255)
        };
        // map nominal data values to colors using our provided palette
//        DataColorAction fill = new DataColorAction("graph.nodes", "gender",
//                Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
        // use black for node text
        ColorAction text = new ColorAction("graph.nodes",
                VisualItem.TEXTCOLOR, ColorLib.gray(0));
        // use light grey for edges
        ColorAction edges = new ColorAction("graph.edges",
                VisualItem.STROKECOLOR, ColorLib.gray(200));

        // create an action list containing all color assignments
        ActionList color = new ActionList();
        //color.add(fill);//////////////////////////
        color.add(text);
        color.add(edges);

        // add the actions to the visualization
        vis.putAction("color", color);
        vis.putAction("layout", layout);

        // create a new Display that pull from our Visualization
        Display display = new Display(vis);
        display.addControlListener(new FocusControl(1));
        display.addControlListener(new DragControl());
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
        display.addControlListener(new WheelZoomControl());
        display.addControlListener(new ZoomToFitControl());
        display.addControlListener(new NeighborHighlightControl());

        display.pan(300, 300);

        add(display);

        jPanel.setLayout(new BorderLayout());
        jPanel.add(display, BorderLayout.CENTER);

        vis.run("color");  // assign the colors
        vis.run("layout"); // start up the animated layout

        revalidate();
        repaint();
    }
}
