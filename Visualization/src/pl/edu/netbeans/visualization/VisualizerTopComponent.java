/*
 */
package pl.edu.netbeans.visualization;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.logging.Logger;
import javax.swing.JButton;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.api.settings.ConvertAsProperties;
import pl.edu.netbeans.algorithms.FirstTSSolverAction;
import pl.edu.netbeans.algorithms.genetic.Population;
import pl.edu.netbeans.visualization.actions.RouteDataColorAction;
import pl.edu.netbeans.visualization.renderers.PointRenderer;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.layout.SpecifiedLayout;
import prefuse.activity.Activity;
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

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//pl.edu.netbeans.visualization//Visualizer//EN",
autostore = false)
public final class VisualizerTopComponent extends TopComponent {


    private static VisualizerTopComponent instance;
    private static final String PREFERRED_ID = "VisualizerTopComponent";
    private Graph graph = null;
    private String nodes = "graph.nodes";
    private Visualization vis = null;
    private FirstTSSolverAction solver;
    int panelHeight, panelWidth;

    public VisualizerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(VisualizerTopComponent.class, "CTL_VisualizerTopComponent"));
        setToolTipText(NbBundle.getMessage(VisualizerTopComponent.class, "HINT_VisualizerTopComponent"));

        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.FALSE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.FALSE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
    }

    public void open(String filename) {
        super.open();
        super.setName(filename);

        initGraph(filename);
    }

    public Population getPopulation() {
        return solver.getPopulation();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(java.awt.Color.white);
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
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
        return TopComponent.PERSISTENCE_ALWAYS;//nie zapamiętuj że był otwarty
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

    /**
     * Głupawe czyszczenie zasobów, nullowanie potencjalnie zbędne
     * @return
     */
    @Override
    public boolean canClose() {
        if (vis != null) {//nie możesz tego nie sprawdzać...
            vis.removeAction("algorythm");
            vis.removeAction("layout");
            vis = null;
        }

        if (graph != null) {
            graph.clear();
            graph = null;
        }

        return super.canClose();
    }

    private void initGraph(String filename) {
        loadGraph(filename);

        // add the graph to the visualization as the data group "graph"
        // nodes and edges are accessible as "graph.nodes" and "graph.edges"
        vis = new Visualization();
        vis.add("graph", graph);

        ActionList algorithm = new ActionList(Activity.INFINITY);
        solver = new FirstTSSolverAction(graph);
        algorithm.add(solver);

        //poniższa seria akcji będzie wykonywana w nieskończoność
        ActionList layout = new ActionList(Activity.INFINITY);
        //layout.add(new ForceDirectedLayout("graph", true, false));
        layout.add(new SpecifiedLayout(nodes, "x", "y"));
        //TODO: zdobywać to przez opcje oraz lookup najlepiej
//        solver = new FirstTSSolverAction(graph);
//        layout.add(solver);
        //layout.add(new MockTSSolverAction(graph));
        layout.add(new RepaintAction());

        PointRenderer r = new PointRenderer(5);
//        LabelRenderer r = new LabelRenderer("name");
//        r.setRoundedCorner(8, 8);

        // create a new default renderer factory
        // return our name label renderer as the default for all non-EdgeItems
        // includes straight line edges for EdgeItems by default
        vis.setRendererFactory(new DefaultRendererFactory(r));

        ColorAction text = new ColorAction(nodes, VisualItem.TEXTCOLOR, ColorLib.gray(0));
        ColorAction fill = new ColorAction(nodes, VisualItem.FILLCOLOR, ColorLib.rgb(0, 0, 0));//kolor Node'ów
        ColorAction dataMarked = new RouteDataColorAction();

        ActionList color = new ActionList(Activity.INFINITY);
        color.add(fill);
        color.add(text);
        color.add(dataMarked);

        // add the actions to the visualization
        vis.putAction("color", color);
        vis.putAction("algorithm", algorithm);
        vis.putAction("layout", layout);

        // create a new Display that pull from our Visualization
        Display display = new Display(vis);
//        display.addControlListener(new FocusControl(1));
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
        display.addControlListener(new WheelZoomControl());
        display.addControlListener(new ZoomToFitControl());
//        display.addControlListener(new NeighborHighlightControl());

        //drobna poprawka aby było wszystko lepiej widoczne
        display.pan(-100, -100);
        display.zoom(new Point(100, 100), 0.7);

        add(display, BorderLayout.CENTER);

        panelWidth = getWidth();
        panelHeight = getHeight();


        vis.run("color");  // assign the colors
        vis.run("algorithm"); // start algorithm
        vis.run("layout"); // start up the animated layout

        //Odkomentować jak uda sie uruchomić akcję start/pause/step/stop
        vis.getAction("algorithm").setEnabled(false); // default, an action is paused


        revalidate();
        repaint();
    }

    /**
     * Load the graph into the current graph field.
     * @param path the path to the GraphML to read ("data/12nodes.xml")
     */
    private void loadGraph(String path) {
        try {
            String filename = path;
            graph = new GraphMLReader().readGraph(filename);
        } catch (DataIOException e) {
            e.printStackTrace();
            System.err.println("Error loading graph. Exiting...");
        }

        if (graph == null) {
            System.err.println("NULL graph! Exiting...");
            throw new RuntimeException("Null graph! Check why!");
        }
    }


    public boolean isPlayable() {
        if ( solver != null) {
            return solver.isPaused() && !solver.isStopped();
        } else {
            return false;
        }
    }

    public boolean isPauseable() {
        if ( solver != null) {
            return ! (solver.isPaused() || solver.isStopped() );
        } else {
            return false;
        }
    }

    public boolean isStepable() {
        return isPlayable();
    }

    public boolean isStopable() {
        if ( solver != null) {
            return ! solver.isStopped();
        } else {
            return false;
        }
    }


    public void play() {
        System.out.println("Play!");
        if ( solver != null) {
            solver.play();
            this.firePropertyChange("play", 0, 1);
        } else {
            System.err.println("No solver!");
        }
    }

    public void pause() {
        System.out.println("Pause!");
        if ( solver != null) {
            solver.pause();
            this.firePropertyChange("pause", 0, 1);
        } else {
            System.err.println("No solver!");
        }
    }

    public void step() {
        System.out.println("Step!");
        if ( solver != null) {
            solver.step();
            this.firePropertyChange("step", 0, 1);
        } else {
            System.err.println("No solver!");
        }
    }

    public void stop() {
        System.out.println("Stop!");
        if ( solver != null) {
            solver.stop();
            this.firePropertyChange("stop", 0, 1);
        } else {
            System.err.println("No solver!");
        }
    }
}
