/*
 */
package pl.project13.tsalgorithms.api;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Poniższa klasa służy jedynie testowaniu czy i jak często wołana jest akcja
 * Przedstawia również logowanie do output window
 * @author ktoso
 */
public class MockTSSolverAction extends SolverAction implements TSSolverAction {//bo zdaje się tak sensownie jest do tego się dobrać przez lookup następnie

    @Override
    public void run(double frac) {
        log("OK @ " + System.currentTimeMillis());
        try {
            //getVisualization(). weź wszystko co nas interesuje, i działaj na tej kolekcji
            //udawanie że liczę coś...
            Thread.sleep(50);
            //end of udawanie że liczę coś...
        } catch (InterruptedException ex) {
            Logger.getLogger(MockTSSolverAction.class.getName()).log(Level.SEVERE, null, ex);
            err(ex.toString());
        }
        //end of udawanie że liczę coś...
        err("error @ " + System.currentTimeMillis());
    }

    public String getHumanReadibleName() {
        return "Mock algorytm";
    }

    public String getDescription() {
        return "Tylko udaje że działa, na cele testów";
    }
}
