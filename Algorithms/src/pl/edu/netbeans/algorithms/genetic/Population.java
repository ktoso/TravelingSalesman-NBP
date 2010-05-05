/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.netbeans.algorithms.genetic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import prefuse.data.Graph;

/**
 *
 * @author bartek
 */
public class Population {

    LinkedList<Chromosom> pop = new LinkedList<Chromosom>();
    int osobnikowPopulacji = 0;
    Random generator = new Random();
    private final Graph graph;

    /**
     * populacja powinna mieć też graf na podstawie którego bedzie oceniać chromosomy
     * @param osobnikowPopulacji
     * @param g
     */
    public Population(int osobnikowPopulacji, Graph g) {
        this.graph = g;
        int dlugoscChromosomu = g.getNodeCount();
        this.osobnikowPopulacji = osobnikowPopulacji;
        for (int i = 0; i < osobnikowPopulacji; ++i) {
            pop.add(new Chromosom(dlugoscChromosomu, this.graph));
            pop.get(i).create();
        }

        //fixme: nie powinno nigdy do tego dochodzić, usunąć to try/catch!!
        try{
            Collections.sort(this.pop);
        }catch(ArrayIndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
        
    }

    public void nextGeneration() throws Exception {
        LinkedList<Chromosom> newPop = new LinkedList<Chromosom>();
        int halfsize = (int) (this.pop.size() / 2);

        for (int i = 0; i < halfsize; ++i) {
            //krzyżuj pierwszą połowę osobników z losowym osobnikiem z grupiej połowy
            ChromosomPair childern = pop.get(i).crossover(pop.get(halfsize + generator.nextInt(this.pop.size() - halfsize)));
            Chromosom ch1 = childern.first();
            Chromosom ch2 = childern.second();
            if (getBoolean(70)) {
                ch1 = ch1.mutation();
                ch2 = ch2.mutation();
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

        if ( this.pop.getFirst().fitness() > newPop.getFirst().fitness() ) {
            this.pop = newPop;
        }

        
        
        System.out.println("Populacja: ");
        for( Chromosom ch : this.pop ) {
            System.out.println("\t" + ch.fitness() + ": " + ch);
        }

//        System.out.println("Best: " + pop.getFirst() );
//        System.out.println(" Fitness: " + pop.getFirst().fitness() );
    }

    public Chromosom getBestChromosom() {
        return pop.getFirst();
    }

    private boolean getBoolean(int percent) {
        return (generator.nextInt(100) < percent);
    }
}
