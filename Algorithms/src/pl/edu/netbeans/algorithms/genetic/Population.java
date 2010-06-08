/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.netbeans.algorithms.genetic;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import pl.edu.netbeans.algorithms.exception.WrongGraphTypeException;
import pl.edu.netbeans.toolbox.RandomNG;
import prefuse.data.Graph;
import prefuse.data.Tuple;

/**
 *
 * @author bartek
 */
public class Population {

    LinkedList<Chromosom> pop = new LinkedList<Chromosom>();
    int numerGeneracji = 0;
    int osobnikowPopulacji = 0;
    RandomNG generator = new RandomNG();
    private final Graph graph;
    private double crossoverPropabilty = 0.25;
    private int iloscPokolenBezZmiany = 0;
    private int maxPokolenBezZmiany = 500;
    private boolean shouldStop = false;
    private int maxNumerGeneracji = 5000;
    private int dlugoscChromosomu;
    private boolean greedy = false;
    private String crossoverType = "Heurystyczne";
    private String selectionType = "Ruletka";

    public int getMaxNumerGeneracji() {
        return maxNumerGeneracji;
    }

    public void setMaxNumerGeneracji(int maxNumerGeneracji) {
        this.maxNumerGeneracji = maxNumerGeneracji;
    }

    public int getMaxPokolenBezZmiany() {
        return maxPokolenBezZmiany;
    }

    public void setMaxPokolenBezZmiany(int maxPokolenBezZmiany) {
        this.maxPokolenBezZmiany = maxPokolenBezZmiany;
    }

    public int getOsobnikowPopulacji() {
        return osobnikowPopulacji;
    }

    public void setOsobnikowPopulacji(int osobnikowPopulacji) {
        this.osobnikowPopulacji = osobnikowPopulacji;
    }

    /**
     * Konstruktor
     * @param op Rozmiar populacji
     * @param greedy Czy pierwsza populacja powinna być generowana z algorytmen zachłąnnym
     * @param g Graf w którym poszukujemy najkrótszego pełnego cyklu hamiltona (problem komiwojażera)
     * @throws WrongGraphTypeException
     */
    public Population(int op, boolean greedy, Graph g) throws WrongGraphTypeException {
        graph = g;
        dlugoscChromosomu = g.getNodeCount();
        osobnikowPopulacji = op;
        for (int i = 0; i < osobnikowPopulacji; ++i) {
            pop.add(new Chromosom(dlugoscChromosomu, graph));
            pop.get(i).create(greedy);

        }

        // na samym początku ustaw każdej krawedzi parametr marked=0
        clearGraph();
        //i najlepszej losowej trasie marked=2
        pop.getFirst().mark(2);

        Collections.sort(pop);

    }

    /**
     * Funkcja tworzy nowe pokolenie wg. ustalonych wcześniej zasad,
     * sprawdza również warunki stopu i zaznacza na grafie najlepszą scieżkę
     */
    public void nextGeneration() throws WrongGraphTypeException {

//        System.out.println("pop-fisrt: " + pop.getFirst().fitness());
//        System.out.println("pop-last: " + pop.getLast().fitness());

        LinkedList<Chromosom> offspring;

        // Wygeneruj potomstwo wg wybranego typu selekcji
        if (selectionType.equals("Ruletka")) {
            offspring = Roullete(pop);
        } else {
            offspring = Tournament(pop);
        }


        //Zliczaj jeśli nie wystąpiły żadne zmiany
        if (offspring.getFirst().fitness() < pop.getFirst().fitness()) {
            iloscPokolenBezZmiany = 0;
        } else {
            iloscPokolenBezZmiany++;
        }
        // Jeśli się coś zmieniło to ustawiamy wszystkim krawedziom
        // marked=0, najlepszej sciezce ze starego pokolenia marked=1,
        // a najlepszej sciezce z aktualnego pokolenia marked=2
        clearGraph();
        this.pop = offspring;
        this.pop.getFirst().mark(1);

        numerGeneracji++;

        shouldStop =
                iloscPokolenBezZmiany > maxPokolenBezZmiany
                || numerGeneracji > maxNumerGeneracji;

    }

    /**
     * Metoda generująca potomstwo wg. selekcji typu ruletka
     * @param p Populacja rodziców
     * @return Liste zawierającą potomstwo
     * @throws WrongGraphTypeException
     */
    private LinkedList<Chromosom> Roullete(LinkedList<Chromosom> p) throws WrongGraphTypeException {
        int size = p.size();

        //Suma odwrotności dopasowania każdego z chromosomów
        double fitnessSum = 0;
        for (Chromosom ch : p) {
            fitnessSum += 1 / ch.fitness();
        }

        // Dystrybuanta każdego chromosomu
        double[] q = new double[size];
        for (int i = 0; i < size; ++i) {
            q[i] = 0;
            for (int j = 0; j <= i; ++j) {
                q[i] += 1 / p.get(j).fitness();
            }
            q[i] /= fitnessSum;
        }

        //Wybieram do potomstwa osobniki wg. zaady ruletki
        LinkedList<Chromosom> offspring = new LinkedList<Chromosom>();
        for (int i = 0; i < osobnikowPopulacji; ++i) {
            double r = generator.nextDouble();
            int j = 0;
            while (r > q[j]) {
                j++;
            }
            offspring.add(p.get(j));
        }

        //Każdy osobnik ma stałe parwdopodobieństwo na krzyżowanie
        //Tworzę liste wskaźników które osobniki bedą się krzyżowały
        LinkedList<Integer> forCrossover = new LinkedList<Integer>();
        for (int i = 0; i < size; ++i) {
            if (generator.nextDouble() < crossoverPropabilty) {
                forCrossover.add(i);
            }
        }

        //Krzyżownie, potomkowie zastępują rodziców
        for (Integer it : forCrossover) {
            Chromosom p1 = offspring.get(it);
            int p2it = generator.nextInt(size);
            Chromosom p2 = offspring.get(p2it);

            ChromosomPair children = p1.crossover(p2, crossoverType);
            offspring.set(it, children.first());
            offspring.set(p2it, children.second());
        }

        //Mutowanie, szanse na mutacje rosną wraz ze stabilizacją w kolejnych pokoleniach
        for (int i = 0; i < size; ++i) {
            if (generator.nextBoolean((iloscPokolenBezZmiany / maxPokolenBezZmiany) * 0.5)) {
                offspring.set(i, offspring.get(i).mutation());
            }
        }

        //Sortuje potomstwo przed jego zwróceniem
        Collections.sort(offspring);

        return offspring;
    }

    /**
     * Metoda tworząca nowe pokolenie wg. zasady turnieju
     * Zasada turnioeju została tutaj zdegenerowana do jednej "walki" wszystkich
     * osobników. Wygrywa lepsza połowa, która ma możliwosć krzyżowania się z gorszymi osobnikami,
     * abu zapobiec utracie dobrych genów w słabszych chromosomach
     * @param p Populacja rodziców
     * @return Posortowaną populację dzieci
     * @throws WrongGraphTypeException
     */
    private LinkedList<Chromosom> Tournament(LinkedList<Chromosom> p) throws WrongGraphTypeException {

        LinkedList<Chromosom> offspring = new LinkedList<Chromosom>();

        //Wybieram środek populacji w którym dzielę osobniki na lepsze i gorsze
        int halfsize = p.size() / 2;
        for (int i = 0; i < halfsize; ++i) {
            //krzyżuj pierwszą połowę osobników z losowym osobnikiem od niego gorszym
            int toCrossover = i + 1 + generator.nextInt(p.size() - i - 1);
            ChromosomPair childern = p.get(i).crossover(p.get(toCrossover), crossoverType);
            Chromosom ch1 = childern.first();
            Chromosom ch2 = childern.second();
            if (generator.nextBoolean((iloscPokolenBezZmiany / maxPokolenBezZmiany) * 0.5)) {
                ch1 = ch1.mutation();
                ch2 = ch2.mutation();
            }

            offspring.add(ch1);
            offspring.add(ch2);

        }

        //Sortowanie przed zwraceniem
        Collections.sort(offspring);

        return offspring;
    }

    public Chromosom getBestChromosom() {
        return pop.getFirst();
    }

    public double getBestFitness() {
        return pop.getFirst().fitness();
    }

    public double getWorstFittness() {
        return pop.getLast().fitness();
    }

    public double getAvgFitness() {
        return getAvgFitness(pop);
    }

    private double getAvgFitness(LinkedList<Chromosom> p) {
        double averangeFittnes = 0;
        for (Chromosom ch : p) {
            averangeFittnes += ch.fitness();
        }
        return averangeFittnes / p.size();
    }

    public int getNumerGeneracji() {
        return numerGeneracji;
    }

    public boolean shouldStop() {
        return shouldStop;
    }

    @SuppressWarnings("unchecked")
    private void clearGraph() {
        //nadanie parametrom marked wszystkich krawędzi wartości 0
        Iterator<Tuple> it = this.graph.getEdges().tuples();
        while (it.hasNext()) {
            it.next().setInt("marked", 0);
        }
    }

    public void setCrossoverType(String cType) {
        this.crossoverType = cType;
    }

    public void setSelectionType(String sType) {
        this.selectionType = sType;
    }
}
