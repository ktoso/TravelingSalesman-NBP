/*
 */
package pl.edu.netbeans.algorithms;

import org.openide.util.Lookup;
import org.openide.util.LookupListener;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import pl.edu.netbeans.algorithms.exception.WrongGraphTypeException;
import pl.edu.netbeans.algorithms.genetic.Chromosom;
import pl.edu.netbeans.algorithms.genetic.Population;
import pl.edu.netbeans.toolbox.ChartDataDTO;
import prefuse.Visualization;
import prefuse.data.Graph;

/**
 * Poniższa klasa rozwiązuje problem komiwojażera wybraną metodą algorytmu genetycznego (mamy kilka) -
 * TODO: może wydzielić je na inne Solvery - jak pierwotnie zakłądano? - pomyślę
 * Przedstawia również logowanie do output window
 * @author ktoso
 */
public class FirstTSSolverAction extends SolverAction implements TSSolverAction, Lookup.Provider {//bo zdaje się tak sensownie jest do tego się dobrać przez lookup następnie

    /**Bardzo istotna zmienna, zachowuje ostatnio wysłany DTO, aby w kolejnej iteracji być w stanie go usunąć, umożliwiając banalne rysowanie wykresu przez inny moduł*/
    private ChartDataDTO lastSentByMe;
    private final Population population;
    /**Służy indentyfikacji różnych serii danych podczas rysowania wykresów*/
    private static int simcount = 1;
    private final String SIMULATION_ID = "Symulacja " + FirstTSSolverAction.simcount++;

    /*Kosmiczna komunikacja między-wątkowo-modułowa poprzez dynamiczne lookupy*/
    private InstanceContent dynamicContent = new InstanceContent();
    private Lookup myLookup = new AbstractLookup(dynamicContent);
    private Lookup.Result res;

    public FirstTSSolverAction(int popSize, boolean greedy, Graph graph) throws WrongGraphTypeException {
        super(graph);
        population = new Population(popSize, greedy, graph);
        setupLineGraphDrawerListener();
    }

    @Override
    public void run(double frac) {
        try {
            population.nextGeneration();
            Chromosom ch = population.getBestChromosom();
            int numerGeneracji = population.getNumerGeneracji();
            double avgFitness = population.getAvgFitness();
            double maxFitness = population.getWorstFittness();
            double minFitness = population.getBestFitness();

            log(SIMULATION_ID + " - pokolenie " + numerGeneracji + ": (" + minFitness + " - " + avgFitness + " - " + maxFitness + ")");

            /* Słuchający tego lookup zostaną powiadomieni o zmianie, przerysują wykres */
            lastSentByMe = new ChartDataDTO(SIMULATION_ID, numerGeneracji, avgFitness, maxFitness, minFitness);
            dynamicContent.add(lastSentByMe);
            res.allItems();
        } catch (WrongGraphTypeException ex) {
            log("Nieoczekiwany błąd - koniec symulacji");
        }


//        removeLastSent();


        if (population.shouldStop()) {
            stop();
        }
    }

    public void setPopulationParameters(int maxCount, int maxCountWGB, String cType, String sType) {
        population.setMaxNumerGeneracji(maxCount);
        population.setMaxPokolenBezZmiany(maxCountWGB);
        population.setCrossoverType(cType);//TODO: Generalnie wypadałoby te klasy rozdzielić a nie stringami ustalać typ: AntiPattern http://blogs.msdn.com/blogfiles/cdndevs/WindowsLiveWriter/NewProgrammingJargon_A38D/i%20can%20has%20string_0e1d6ac7-2adf-4b93-8e1a-8caf9e3db81d.jpg
        population.setSelectionType(sType);
    }

    public void play() {
        if (!isStopped()) {
            getVisualization().getAction("algorithm").setEnabled(true);
        }
    }

    public void pause() {
        if (!isStopped()) {
            getVisualization().getAction("algorithm").setEnabled(false);
            log("--- PAUZA ---");
        }
    }

    public void step() {
        if (!isStopped() && isPaused()) {
            run(1);
        }
    }

    public void stop() {
        if (!isStopped()) {
            getVisualization().removeAction("algorithm");
            showSummary();
        }
    }

    public boolean isStopped() {
        Visualization vis = getVisualization();
        return vis == null || vis.getAction("algorithm") == null;
    }

    public boolean isPaused() {
        return !isStopped() && !getVisualization().getAction("algorithm").isEnabled();
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

    private void showSummary() {
        Chromosom ch = population.getBestChromosom();
        log("--- ALGORYTM ZAKONCZONY ---");
        log("Best (" + ch.fitness() + "):");
        log(ch.toString());
    }
}

