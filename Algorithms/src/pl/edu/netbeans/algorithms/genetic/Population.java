/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.netbeans.algorithms.genetic;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
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
    Random generator = new Random();
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

        //fixme: nie powinno nigdy do tego dochodzić, usunąć to try/catch!!
        try {
            Collections.sort(pop);
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }

    }

    public void nextGeneration() {//e......
        LinkedList<Chromosom> newPop = new LinkedList<Chromosom>();
        int halfsize = pop.size() / 2;

        for (int i = 0; i < halfsize; ++i) {
            //krzyżuj pierwszą połowę osobników z losowym osobnikiem z grupiej połowy
            ChromosomPair childern = pop.get(i).crossover(pop.get(halfsize + generator.nextInt(this.pop.size() - halfsize)));
            Chromosom ch1 = childern.first();
            Chromosom ch2 = childern.second();
            if (getBoolean(iloscPokolenBezZmiany / maxPokolenBezZmiany * 500)) {
                ch1 = ch1.mutation(iloscPokolenBezZmiany / maxPokolenBezZmiany * dlugoscChromosomu);
                ch2 = ch2.mutation(iloscPokolenBezZmiany / maxPokolenBezZmiany * dlugoscChromosomu);
            }

            newPop.add(ch1);
            newPop.add(ch2);
        }

        Collections.sort(this.pop);
        Collections.sort(newPop);

//        double oldAvgFitness = 0;
//        double newAvgFitness = 0;
//
//        for( Chromosom ch : this.pop ) {
//            oldAvgFitness += 1/ch.fitness();
//        }
//        oldAvgFitness = this.pop.size()/oldAvgFitness;
//
//        for( Chromosom ch : newPop ) {
//            newAvgFitness += 1/ch.fitness();
//        }
//        newAvgFitness = newPop.size()/newAvgFitness;
//
//        if (newAvgFitness < oldAvgFitness) {
//            this.pop = newPop;
//        }


//        this.pop = newPop;

        if (this.pop.getFirst().fitness() > newPop.getFirst().fitness()) {
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
                iloscPokolenBezZmiany > maxPokolenBezZmiany ||
                numerGeneracji > maxNumerGeneracji;
        



//        System.out.println("Populacja: ");
//        for( Chromosom ch : this.pop ) {
//            System.out.println("\t" + ch.fitness() + ": " + ch);
//        }

//        System.out.println("Best: " + pop.getFirst() );
//        System.out.println(" Fitness: " + pop.getFirst().fitness() );
    }

    public Chromosom getBestChromosom() {
        return pop.getFirst();
    }

    public int getNumerGeneracji() {
        return numerGeneracji;
    }

    private boolean getBoolean(int percent) {
        return (generator.nextInt(100) < percent);
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
