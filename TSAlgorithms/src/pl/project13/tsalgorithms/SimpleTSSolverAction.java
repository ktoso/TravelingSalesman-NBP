/*
 */
package pl.project13.tsalgorithms;

import pl.project13.tsalgorithms.api.TSSolverAction;
import prefuse.action.Action;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import prefuse.Visualization;

import prefuse.action.layout.Layout;
import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.tuple.TupleSet;
import prefuse.util.PrefuseLib;
import prefuse.util.force.DragForce;
import prefuse.util.force.ForceItem;
import prefuse.util.force.ForceSimulator;
import prefuse.util.force.NBodyForce;
import prefuse.util.force.SpringForce;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

/**
 *
 * @author ktoso
 */
public class SimpleTSSolverAction extends Action implements TSSolverAction {

    public SimpleTSSolverAction(Visualization vis) {
        //TODO przemyśleć to tak, aby zawsze był poprawnie zawołany konstruktor!
//        super(vis, )
    }



    @Override
    public void run(double frac) {
        //TODO: przykładowa implementacja

        throw new UnsupportedOperationException("Not supported yet.");
    }


    public String getHumanReadibleName() {
        return "Banalny zgadywacz";
    }


    public String getDescription() {
        return "działa zgadując ścieżkę, zupełnie nie ma pojęcia co robi innymi słowy.";
    }
}
