/*
 */
package pl.project13.tsalgorithms.api;

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
    private static int lol = 0;//TODO usuń mnie, jestem tylko na pokaz

    @Override
    public void run(double frac) {
        try {
            //getVisualization(). weź wszystko co nas interesuje, i działaj na tej kolekcji
            //udawanie że liczę coś...
            graph.getEdges().removeTuple(graph.getEdge(lol++));//TODO: usuń mnie, jestem tylko na pokaz
            Thread.sleep(100);
            log("[OK] Usunięto krawędź o ID: " + (lol - 1));
            //end of udawanie że liczę coś...
        } catch (InterruptedException ex) {
            Logger.getLogger(MockTSSolverAction.class.getName()).log(Level.SEVERE, null, ex);
            err(ex.toString());
        }
        //end of udawanie że liczę coś...
//        err("error @ " + System.currentTimeMillis());
    }

    public String getHumanReadibleName() {
        return "Mock algorytm";
    }

    public String getDescription() {
        return "Tylko udaje że działa, na cele testów";
    }
}
