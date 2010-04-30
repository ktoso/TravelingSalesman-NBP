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
    int size = 0;
    Random generator = new Random();
    private final Graph graph;

//    public Population(int size, Graph g) {
//    populacja powinna mieć też graf na podstawie którego bedzie oceniać chromosomy
    public Population(int size, Graph g) {
        this.graph =g;
        int nodesnumber = 20; //Powinnno sie to wyliczyć z grafu
        this.size = size;
        for( int i = 0; i< size; ++i) {
            pop.add( new Chromosom(nodesnumber, this.graph));
            pop.get(i).create();
        }
    }

    public void nextGeneration() throws Exception {
        LinkedList<Chromosom> newPop = new LinkedList<Chromosom>();
        Collections.sort(this.pop);
        
        for( int i=0; i < this.pop.size() / 2 ; ++i ) {
            ChromosomPair childern = pop.get(i).crossover( pop.get(generator.nextInt(this.pop.size())) );
            Chromosom ch1 = childern.first();
            Chromosom ch2 = childern.second();
            if ( getBoolean(70) ) {
                ch1 = ch1.mutation() ;
                ch2 = ch2.mutation() ;
            }

            newPop.add(ch1);
            newPop.add(ch2);
            
        }

        this.pop = newPop;
        
        System.out.println("Best: " + pop.getFirst() );
        System.out.println(" Fitness: " + pop.getFirst().fitness() );

    }

    private boolean getBoolean(int percent) {
        return ( generator.nextInt(100) < percent );
    }



}
