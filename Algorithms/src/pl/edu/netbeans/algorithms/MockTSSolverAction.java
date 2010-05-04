/*
 */
package pl.edu.netbeans.algorithms;

import pl.edu.netbeans.algorithms.genetic.Chromosom;
import pl.edu.netbeans.algorithms.genetic.Population;
import prefuse.data.Graph;

/**
 * Poniższa klasa służy jedynie testowaniu czy i jak często wołana jest akcja
 * Przedstawia również logowanie do output window
 * @author ktoso
 */
public class MockTSSolverAction extends SolverAction implements TSSolverAction {//bo zdaje się tak sensownie jest do tego się dobrać przez lookup następnie

    private final Population population;
    private int lol = 0;
    
    public MockTSSolverAction(Graph graph) {
        super(graph);
        this.population = new Population(20, graph);
    }

    @Override
    public void run(double frac) {
        try {
            this.population.nextGeneration();
            Chromosom ch = this.population.getBestChromosom();
            log("Generation " + lol + ": best chromosom: " + ch + " (" + ch.fitness() + ")");
            
        } catch (Exception ex) {
            log("ERROR: " + ex.getMessage());
        }
    }

    public String getHumanReadibleName() {
        return "Mock algorytm";
    }

    public String getDescription() {
        return "Tylko udaje że działa, na cele testów";
    }
}

//        Może sie kiedyś przyda!
//        try {
////            getVisualization(). weź wszystko co nas interesuje, i działaj na tej kolekcji
////            udawanie że liczę coś...
//            graph.getEdge(lol++).set(null, lol);//TODO: usuń mnie, jestem tylko na pokaz
//            Thread.sleep(100);
//            log("[OK] Usunięto krawędź o ID: " + (lol - 1));
////            end of udawanie że liczę coś...
//        } catch (InterruptedException ex) {
//            Logger.getLogger(MockTSSolverAction.class.getName()).log(Level.SEVERE, null, ex);
//            err(ex.toString());
//        }
////        end of udawanie że liczę coś...
//        err("error @ " + System.currentTimeMillis());
