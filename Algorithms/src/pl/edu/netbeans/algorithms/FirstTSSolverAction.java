/*
 */
package pl.edu.netbeans.algorithms;

import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupListener;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import pl.edu.netbeans.algorithms.genetic.Chromosom;
import pl.edu.netbeans.algorithms.genetic.Population;
import pl.edu.netbeans.toolbox.ChartDataDTO;
import prefuse.data.Graph;

/**
 * Poniższa klasa służy jedynie testowaniu czy i jak często wołana jest akcja
 * Przedstawia również logowanie do output window
 * @author ktoso
 */
public class FirstTSSolverAction extends SolverAction implements TSSolverAction, Lookup.Provider {//bo zdaje się tak sensownie jest do tego się dobrać przez lookup następnie

    /**Bardzo istotna zmienna, zachowuje ostatnio wysłany DTO, aby w kolejnej iteracji być w stanie go usunąć, umożliwiając banalne rysowanie wykresu przez inny moduł*/
    private ChartDataDTO lastSentByMe;
    private final Population population;
    private int iloscOsobnikow = 50; // Pwoinno być ustawiane w programie
    /**Służy indentyfikacji różnych serii danych podczas rysowania wykresów*/
    private static int simcount = 1;
    private final String SIMULATION_ID = "symulacja " + FirstTSSolverAction.simcount++;

    /*Kosmiczna komunikacja między-wątkowo-modułowa poprzez dynamiczne lookupy*/
    private InstanceContent dynamicContent = new InstanceContent();
    private Lookup myLookup = new AbstractLookup(dynamicContent);
    private Lookup.Result res;

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

        population.nextGeneration();
        Chromosom ch = population.getBestChromosom();
        int numerGeneracji = population.getNumerGeneracji();
        double avgFitness = population.getAvgFitness();
        double maxFitness = population.getWorstFittness();
        double minFitness = population.getBestFitness();

        log("Generacja " + numerGeneracji + ": naj. chromosom: " + ch + " (" + avgFitness + ")");

//        removeLastSent();
        /* Słuchający tego lookup zostaną powiadomieni o zmianie, przerysują wykres */
        lastSentByMe = new ChartDataDTO(SIMULATION_ID, numerGeneracji, avgFitness, maxFitness, minFitness);
        dynamicContent.add(lastSentByMe);
        res.allItems();

        waitAMomentPlease(300);
    }

    private void removeLastSent() {
        if (lastSentByMe == null) {
            return;
        }

        dynamicContent.remove(lastSentByMe);
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
    @SuppressWarnings("unchecked")
    private void setupLineGraphDrawerListener() {
        //znajdź implementację LineChartDrawer
        TopComponent drawer = WindowManager.getDefault().findTopComponent("FitnessGraphTopComponent");
        if (drawer == null) {
            throw new RuntimeException("Nie znaleziono implementacji FitnessGraphTopComponent. Nikt mnie nie słucha!");
        }

        //reaguj na dodania DTO; w nich przekazujemy wszystkie istotne innym informacje
        res = myLookup.lookup(new Lookup.Template(ChartDataDTO.class));
        res.allItems();//zdaje się odświeżać wnętrzności takiego lookupa (ładować spis klas)
        res.addLookupListener((LookupListener) drawer);
    }

    private void waitAMomentPlease(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
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

