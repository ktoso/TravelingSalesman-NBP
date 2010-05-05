/*

 * Klasa reprezentująca chromosom w algorytmie ewolucyjnym
 */
package pl.edu.netbeans.algorithms.genetic;

import java.util.LinkedList;
import java.util.Random;
import prefuse.data.Edge;
import prefuse.data.Graph;

/**
 *
 * @author bartek
 */
public class Chromosom extends LinkedList<Integer> implements Comparable<Chromosom> {

    Random generator = new Random();
    Graph graph;

    /**
     * Tworzymy chromosom wypiełniony numerami wezłów w losowej kolejnosci.
     *
     * @param length Ilość wezłów w grafie - długośc najdłużeszego cyklu hamiltona
     */
    public Chromosom(int length, Graph g) {
        this.graph = g;
        for (int i = 0; i < length; ++i) {

            add(-1);
        }
    }

    public void create() {
        int length = size();
        for (int i = 0; i < length; ++i) {
            int num = generator.nextInt(length);
            while (indexOf(num) != -1) {
                num = generator.nextInt(length);
            }
            set(i, num);
        }
    }

    public ChromosomPair crossover(Chromosom ch) throws Exception {

        if (size() != ch.size()) {
            throw new Exception("Niezgodnośc rozmiarów chromosomów");
        }

        int length = size();

        Chromosom child1 = new Chromosom(length, this.graph);
        Chromosom child2 = new Chromosom(length, this.graph);

        /*
         * TODO: jakieś randomy
         */
        int from = generator.nextInt(length);
        int to = from + generator.nextInt(length - 1);

        for (int i = from; i < to; ++i) {
            child1.set(i, get(i));
            child2.set(i, ch.get(i));
        }

        int ch1pos = to;
        int ch2pos = to;
        for (int i = 0; i < length; ++i) {
            if (child1.indexOf(ch.get(to + i)) == -1) {
                child1.set(ch1pos, ch.get(to + i));
                ++ch1pos;
            }
            if (child2.indexOf(get(to + i)) == -1) {
                child2.set(ch2pos, get(to + i));
                ++ch2pos;
            }
        }

        return new ChromosomPair(child1, child2);
    }

    public Chromosom mutation() {
        int length = size();

        Chromosom child = new Chromosom(length, this.graph);

        for (int i = 0; i < length; ++i) {
            child.set(i, get(i));
        }

        //ilość zamian genów w mutacji
        for (int i = 0; i < ((int) length / 2 + 1); ++i) {
            int from = generator.nextInt(length);
            int with = from + generator.nextInt(length - 1);

            int tmp = child.get(from);
            child.set(from, child.get(with));
            child.set(with, tmp);
        }

        return child;
    }

    @Override
    public Integer get(int i) {
        return super.get(i % size());
    }

    @Override
    public Integer set(int index, Integer element) {
        return super.set(index % size(), element);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Integer i : this) {
            try {
                b.append(this.graph.getNode(i).get("name")).append(" ");
            } catch (ArrayIndexOutOfBoundsException ex) {
                b.append(" null ");
            }
//            b.append(i).append(" ");
        }
        return b.toString();
    }

//    public double fitness(Graph g) {  dopasowanie ze względu na graf
//    powinno liczyć długość ścieżki
//    teraz funkcja uważa odległość miedzy grafami za bezwzględną róznice miedzy ich numerami, czyli najlepszy graf to 1 2 3 4 5 6 7 8 9 itd.
    public double fitness() {
        double f = 0;
//        System.out.println("Chromosom fitness:");
        for (int i = 0; i < size(); ++i) {
            int source = get(i);
            int target = get(i + 1);

            Edge e = this.graph.getEdge(this.graph.getEdge(source, target));

            if (e == null) {
                e = this.graph.getEdge(this.graph.getEdge(target, source));
            }

            if (e != null) {

//                System.out.println("\t" + e.getSourceNode().getString("name") + " " + e.getTargetNode().getString("name") + " " + e.getDouble("weight"));
                f += e.getDouble("weight");

            }

        }
        return f;
    }

    public int compareTo(Chromosom ch) {
        return (int) (fitness() - ch.fitness());
    }

    public boolean equals(Chromosom ch) {
        for (int i = 0; i < size(); ++i) {
            if (get(i) != ch.get(i)) {
                return false;
            }
        }
        return true;
    }
}
