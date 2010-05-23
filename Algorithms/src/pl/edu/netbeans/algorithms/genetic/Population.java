/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.netbeans.algorithms.genetic;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
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
    private int iloscPokolenBezZmiany = 0;
    private int maxPokolenBezZmiany = 500;
    private boolean shouldStop = false;
    private int maxNumerGeneracji = 5000;
    private int dlugoscChromosomu;

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
     * populacja powinna mieć też graf na podstawie którego bedzie oceniać chromosomy
     * @param osobnikowPopulacji
     * @param g
     */
    public Population(int op, Graph g) {
        graph = g;
        dlugoscChromosomu = g.getNodeCount();
        osobnikowPopulacji = op;
        for (int i = 0; i < osobnikowPopulacji; ++i) {
            pop.add(new Chromosom(dlugoscChromosomu, graph));
            pop.get(i).create();
        }

        // na samym początku ustaw każdej krawedzi parametr marked=0
        clearGraph();
        //i najlepszej losowej trasie marked=2
        pop.getFirst().mark(2);

        Collections.sort(pop);

    }

    /**
     * Funkcja tworzy kolejne pokolenie które jest odrzucane lub przyjmowane na
     * podstawie wartości dopasowania najlepszego chromosomu
     */
    public void nextGeneration() {

        System.out.println("pop-fisrt: " + pop.getFirst().fitness());
        System.out.println("pop-last: " + pop.getLast().fitness());

        int size = pop.size();
        double fitnessSum = 0;
        for (Chromosom ch : pop) {
            fitnessSum += 1 / ch.fitness();
        }

        // Dystrybuanta każdego chromosomu
        double[] q = new double[size];
        for (int i = 0; i < size; ++i) {
            q[i] = 0;
            for (int j = 0; j <= i; ++j) {
                q[i] += 1 / pop.get(j).fitness();
            }
            q[i] /= fitnessSum;
        }

        // Losowanie osobników do krzyżowania
        int forCrossoverCount = generator.nextInt(size / 2) * 2;
        LinkedList<Chromosom> forCrossover = new LinkedList<Chromosom>();
        for (int i = 0; i < forCrossoverCount; ++i) {
            double r = generator.nextDouble();
            int j = 0;
            while (r > q[j]) {
                j++;
            }
            forCrossover.add(pop.get(j));
        }

        // Losowanie osobników do nowej populacji
        LinkedList<Chromosom> newPop = new LinkedList<Chromosom>();
        for (int i = 0; i < size - forCrossoverCount; ++i) {
            double r = generator.nextDouble();
            int j = 0;
            while (r > q[j]) {
                j++;
            }
            newPop.add(pop.get(j));
        }

        /* Krzyżowanie:
         * Krzyzują się tylko wybrane wczesniej osobniki
         */
        while (forCrossover.size() > 0) { //długość tej listy jest parzysta wiec moge pobierać w każdej pętli dwa obiekty

            Chromosom fistParent = forCrossover.removeFirst();
            Chromosom secondParent = forCrossover.removeFirst();

            ChromosomPair childern = fistParent.crossover(secondParent);

            newPop.add(childern.first());
            newPop.add(childern.second());
        }

        // Mutacje: każdy osobnik ma (iloscPokolenBezZmiany/maxpokolenBezZmiany)*0.5
        // szans na mutacje rosną wraz z iloscią pokolen bez zmiany
        for (int i = 0; i < size; ++i) {
            if (generator.nextBoolean((iloscPokolenBezZmiany / maxPokolenBezZmiany) * 0.5)) {
                newPop.set(i, newPop.get(i).mutation(iloscPokolenBezZmiany / maxPokolenBezZmiany));
            }
        }

        Collections.sort(newPop);

        if ( pop.getFirst().compareTo(newPop.getFirst()) > 0) {
            // Jeśli się coś zmieniło to ustawiamy wszystkim krawedziom
            // marked=0, najlepszej sciezce ze starego pokolenia marked=1,
            // a najlepszej sciezce z aktualnego pokolenia marked=2
            clearGraph();
            this.pop.getFirst().mark(1);
            newPop.getFirst().mark(2);
            this.pop = newPop;
            iloscPokolenBezZmiany = 0;
        } else {
            iloscPokolenBezZmiany++;
        }



        numerGeneracji++;

        shouldStop =
                iloscPokolenBezZmiany > maxPokolenBezZmiany
                || numerGeneracji > maxNumerGeneracji;

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
        double averangeFittnes = 0;
        for (Chromosom ch : pop) {
            averangeFittnes += ch.fitness();
        }
        return averangeFittnes / pop.size();
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
}
