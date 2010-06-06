/*

 * Klasa reprezentująca chromosom w algorytmie ewolucyjnym
 */
package pl.edu.netbeans.algorithms.genetic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import pl.edu.netbeans.algorithms.exception.EdgeDoesNotExistException;
import pl.edu.netbeans.algorithms.exception.WrongGraphTypeException;
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

    public void create() throws WrongGraphTypeException {
        create(false);
    }

    public void create(boolean greedy) throws WrongGraphTypeException {
        int length = size();
        if (greedy) {
            LinkedList<Integer> unused = new LinkedList<Integer>();
            for (int i = 0; i < length; ++i) {
                unused.add(i);
            }
            int start = generator.nextInt(length);
            int it = 0;
            set(it++, start);
            unused.remove(start);
            for (; it < length; it++) {
                int closest = getClosestNeighbor(start, unused);
                if (closest == -1) {
                    throw new WrongGraphTypeException("Graf został źle zbudowany");
                }
                set(it, closest);
                unused.remove(Integer.valueOf(closest));
                start = closest;
            }

        } else {
            for (int i = 0; i < length; ++i) {
                int num = generator.nextInt(length);
                while (indexOf(num) != -1) {
                    num = generator.nextInt(length);
                }
                set(i, num);
            }
        }

    }

    public int find(int elem) {
        for (int i = 0; i < size(); ++i) {
            if (get(i) == elem) {
                return i;
            }
        }
        return -1;
    }

    public ChromosomPair crossover(Chromosom ch) {

        if (size() != ch.size()) {
            throw new RuntimeException("Niezgodnośc rozmiarów chromosomów");
        }

        int length = size();

        Chromosom child1 = new Chromosom(length, this.graph);
        Chromosom child2 = new Chromosom(length, this.graph);


        // Znajdz najdłuższą scieżkę do przecięcia
//        int from = 0;
//        for (int i = 0; i < length; ++i) {
//            if (getEdgeWeight(i) > getEdgeWeight(from)) {
//                from = i;
//            }
//        }
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

    public ChromosomPair heuristicCrossover(Chromosom ch) throws WrongGraphTypeException {


        if (size() != ch.size()) {
            throw new WrongGraphTypeException("Niezgodnośc rozmiarów chromosomów");
        }

        int length = size();

        Chromosom child1 = new Chromosom(length, this.graph);
        Chromosom child2 = new Chromosom(length, this.graph);

        LinkedList<Integer> unused1 = new LinkedList<Integer>();
        LinkedList<Integer> unused2 = new LinkedList<Integer>();

        for (int i = 0; i < length; ++i) {
            unused1.add(i);
            unused2.add(i);
        }

        int start = generator.nextInt(length);

        child1.set(0, get(start));
        unused1.remove(get(start));

        child2.set(0, ch.get(start));
        unused2.remove(ch.get(start));

        ++start;

        for (int i = 1; i < length; ++i) {

            if (getEdgeWeight(start) < ch.getEdgeWeight(start)) {
                if (!unused1.contains(get(start + 1))) {
                    child1.set(i, get(start + 1));
                    unused1.remove(get(start + 1));
                } else if (!unused1.contains(ch.get(start + 1))) {
                    child1.set(i, ch.get(start + 1));
                    unused1.remove(ch.get(start + 1));
                } else {

                    int closest = getClosestNeighbor(get(start), unused1);
                    if (closest == -1) {
                        throw new WrongGraphTypeException("Graf jest źle zbudowany");
                    }
                    child1.set(i, closest);
                    unused1.remove(closest);
                }
            }

            ++start;
        }


        return new ChromosomPair(child1, child2);
    }

    /**
     * Funkcja mutująca dany chromosom
     * @param mutationSize liczba z przedziału (0, 1) określająca rozmiar mutacji
     * @return Zmutowany chromosom
     */
    public Chromosom mutation(double mutationSize) {
        int length = size();



        Chromosom child = new Chromosom(length, this.graph);

        for (int i = 0; i < length; ++i) {
            child.set(i, get(i));
        }

        mutationSize = (mutationSize > 1 ? 1 : mutationSize) * length;

        //ilość zamian genów w mutacji
        for (int i = 0; i < mutationSize; ++i) {
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

        b.append(" (" + fitness() + " )");
        return b.toString();
    }

    /**
     * Pobiera wagę sciezki i-tego miasta do masta i+1-ego
     * @param i numer miasta w tablicy porządku
     * @return waga scieżki do kolejnego miasta
     */
    private double getEdgeWeight(int i) {
        if (i < 0) {
            return Double.MAX_VALUE;
        }

        int source = get(i);
        int target = get(i + 1);

        return getEdgeWeight(source, target);


    }

    private double getEdgeWeight(int s, int t) {
        if (s < 0 || s > size() || t < 0 || t > size()) {
            return Double.MAX_VALUE;
        }

        Edge e = graph.getEdge(graph.getEdge(s, t));

        if (e == null) {
            e = graph.getEdge(graph.getEdge(t, s));
        }

        if (e != null) {
            return e.getDouble("weight");
        }

        return 0;
    }

    public double fitness() {
        double f = 0;
        for (int i = 0; i < size(); ++i) {
            f += getEdgeWeight(i);
        }
        return f;
    }

    /**
     * Ustawia scieżce reprezentowanej przez siebie parametr marked=mark
     * @param mark wartosc parametru marked dla reprezentowanej scieżki
     */
    public void mark(int mark) {
        for (int i = 0; i < size(); ++i) {
            int source = get(i);
            int target = get(i + 1);

            Edge e = this.graph.getEdge(this.graph.getEdge(source, target));

            if (e == null) {
                e = this.graph.getEdge(this.graph.getEdge(target, source));
            }

            if (e != null) {
                e.setInt("marked", mark);
            }
        }
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

    private int getClosestNeighbor(int from, LinkedList<Integer> unused) {

        if (unused.isEmpty()) {
            return -1;
        }

        int closest = unused.getFirst();
        double min = getEdgeWeight(from, closest);
        Iterator<Integer> it = unused.iterator();
        while (it.hasNext()) {
            int act = it.next();
            if (getEdgeWeight(from, act) < getEdgeWeight(from, closest)) {
                min = getEdgeWeight(from, act);
                closest = act;
            }
        }
        return closest;
    }
}
