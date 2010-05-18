/*
 */
package pl.edu.netbeans.algorithms;

import java.util.Random;
import pl.edu.netbeans.algorithms.genetic.Chromosom;
import pl.edu.netbeans.algorithms.genetic.Population;
import prefuse.data.Graph;

/**
 * Poniższa klasa służy jedynie testowaniu czy i jak często wołana jest akcja
 * Przedstawia również logowanie do output window
 * @author ktoso
 */
public class FirstTSSolverAction extends SolverAction implements TSSolverAction {//bo zdaje się tak sensownie jest do tego się dobrać przez lookup następnie

    private final Population population;
    private int iloscOsobnikow = 50; // Pwoinno być ustawiane w programie

    public FirstTSSolverAction(Graph graph) {
        super(graph);
        this.population = new Population(iloscOsobnikow, graph);
    }

    @Override
    public void run(double frac) {
        try {
            this.population.nextGeneration();
            Chromosom ch = this.population.getBestChromosom();
            log("Generation " + this.population.getNumerGeneracji() + ": best chromosom: " + ch + " (" + ch.fitness() + ")");

//            Czy w ten sposób możemy konczyć symulację (warunek stopu może być inny)?
//            if ( this.population.getNumerGeneracji() > 100 ) {
//                getVisualization().removeAction("layout");
//            }
            
            //testing testing testing testing testing testing testing
//            graph.getEdge(1).setInt("marked", ( this.population.getNumerGeneracji() / 100) % 4 );
            //System.out.println(this.population.getNumerGeneracji() + " : " + graph.getEdge(1).getInt("marked") );
            //testing testing testing testing testing testing testing

            
        } catch (Exception ex) {
            log("ERROR: " + ex.getMessage() );
        }

        
    }

    public String getHumanReadibleName() {
        return "Pierwszy algorytm";
    }

    public String getDescription() {
        return "Pierwsza wersja algorytmu genetycznego";
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
