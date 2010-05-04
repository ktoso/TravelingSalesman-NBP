/*
 */
package pl.edu.netbeans.algorithms;

import java.util.logging.Level;
import java.util.logging.Logger;
import prefuse.data.Graph;

/**
 * Poniższa klasa służy jedynie testowaniu czy i jak często wołana jest akcja
 * Przedstawia również logowanie do output window
 * @author ktoso
 */
public class MockTSSolverAction extends SolverAction implements TSSolverAction {//bo zdaje się tak sensownie jest do tego się dobrać przez lookup następnie

    public MockTSSolverAction(Graph graph) {
        super(graph);
    }

    @Override
    public void run(double frac) {
        try {
            Thread.sleep(100);
            log("[OK] Udaję że coś robię...");
        } catch (InterruptedException ex) {
            Logger.getLogger(MockTSSolverAction.class.getName()).log(Level.SEVERE, null, ex);
            err(ex.toString());
        }
    }

    public String getHumanReadibleName() {
        return "Mock algorytm";
    }

    public String getDescription() {
        return "Tylko udaje że działa, na cele testów";
    }
}
