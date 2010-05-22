/*
 */
package pl.edu.netbeans.algorithms;

import java.util.Random;
import javax.swing.JOptionPane;
import org.openide.util.Lookup;
import org.openide.util.LookupListener;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import pl.edu.netbeans.algorithms.genetic.Chromosom;
import pl.edu.netbeans.algorithms.genetic.Population;
import prefuse.data.Graph;

/**
 * Poniższa klasa służy jedynie testowaniu czy i jak często wołana jest akcja
 * Przedstawia również logowanie do output window
 * @author ktoso
 */
public class FirstTSSolverAction extends SolverAction implements TSSolverAction, Lookup.Provider {//bo zdaje się tak sensownie jest do tego się dobrać przez lookup następnie

    private final Population population;
    private int iloscOsobnikow = 50; // Pwoinno być ustawiane w programie

    /*Kosmiczna komunikacja między-wątkowo-modułowa poprzez dynamiczne lookupy*/
    InstanceContent dynamicContent = new InstanceContent();
    Lookup myLookup = new AbstractLookup(dynamicContent);

    public FirstTSSolverAction(Graph graph) {
        super(graph);
        population = new Population(iloscOsobnikow, graph);

        setupLineGraphDrawerListener();
    }

    @Override
    public void run(double frac) {
        if (population.shouldStop()) {
            getVisualization().getAction("layout").setEnabled(false);
            Chromosom ch = population.getBestChromosom();
            log("--- ALGORYTM ZAKONCZONY ---");
            log("Best (" + ch.fitness() + "):");
            log(ch.toString());
            return;
        }

        try {
            population.nextGeneration();
            Chromosom ch = population.getBestChromosom();
            log("Generation " + population.getNumerGeneracji() + ": best chromosom: " + ch + " (" + ch.fitness() + ")");

            /* Słuchający tego lookup zostaną powiadomieni o zmianie, przerysują wykres */
            dynamicContent.add(ch.fitness());

        } catch (Exception ex) {
            log("ERROR in FirstTSSolver: " + ex);
            throw new RuntimeException(ex);
        }


    }

    public Population getPopulation() {
        return population;
    }

    @Override
    public String getHumanReadibleName() {
        return "Algorytm genetyczny";
    }

    @Override
    public String getDescription() {
        return "Pierwsza wersja algorytmu genetycznego";
    }

    @Override
    public Lookup getLookup() {
        return myLookup;
    }

    /**
     * Poszukuje oraz ustawia listenera implementującego LineChartDrawer
     * aby ten reagował na każdorazową zmianę w naszym lookup - aktualizował wykres
     * //Zdaje się że pomijamy Visualizera dzięki temu, super!//
     */
    private void setupLineGraphDrawerListener() {
        //znajdź implementację LineChartDrawer
        TopComponent drawer = WindowManager.getDefault().findTopComponent("FitnessGraphTopComponent");
        if (drawer == null) {
            JOptionPane.showMessageDialog(null, "NIE ZNALEZIONO FitnessGraphTopComponent!!!", "Wystąpił dość krytyczny błąd!", JOptionPane.ERROR_MESSAGE);
        }

        //reaguj na dodania Doubli - czyli kolejnych wartości fitness
        Lookup.Result res = myLookup.lookup(new Lookup.Template(Double.class));
        res.addLookupListener((LookupListener) drawer);
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

