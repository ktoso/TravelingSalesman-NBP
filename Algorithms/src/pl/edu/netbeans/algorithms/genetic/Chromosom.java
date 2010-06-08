/*

 * Klasa reprezentująca chromosom w algorytmie ewolucyjnym
 */
package pl.edu.netbeans.algorithms.genetic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
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

    /**
     * Utwórz nowy chromosom
     * @param greedy Czy korzystać z algorytmu zachłannego przy tworzeniu chromosomu
     * @throws WrongGraphTypeException
     */
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

    /**
     * Znajdź indeks danego miasta
     * @param elem Szukane miasto
     * @return Indeks danego miasta
     */
    public int find(int elem) {
        for (int i = 0; i < size(); ++i) {
            if (get(i) == elem) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Krzyżowanie wg. wybranego wcześniej typu
     * @param ch Drugi rodzic
     * @param type Typ krzyżowania
     * @return Para potomków
     * @throws WrongGraphTypeException
     */
    public ChromosomPair crossover(Chromosom ch, String type) throws WrongGraphTypeException {
        if (type.equals("Heurystyczne")) {
            return new ChromosomPair(this.heuristicCrossover(ch), ch.heuristicCrossover(this));
        } else {
            return OXcrossover(ch);
        }
    }

    /**
     * Krzyżowanie typu OX
     * @param ch Drugi rodzic
     * @return Para potomków
     */
    public ChromosomPair OXcrossover(Chromosom ch) {

        //Próba krzyżowania chromosomów o róznych długościach
        if (size() != ch.size()) {
            throw new RuntimeException("Niezgodnośc rozmiarów chromosomów");
        }

        int length = size();
        //Tworzenie pustych dzieci
        Chromosom child1 = new Chromosom(length, this.graph);
        Chromosom child2 = new Chromosom(length, this.graph);


        // Wybierz dwa losowe punkty przecięcia
        int from = generator.nextInt(length);
        int to = from + generator.nextInt(length - 1);

        //Przenieś do dzieci miasta z pomiedzy punktów cięć
        for (int i = from; i < to; ++i) {
            child1.set(i, get(i));
            child2.set(i, ch.get(i));
        }

        //Uzupełnij pozostałe pozycje
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

    /**
     * Krzyżowanie heurystyczne
     * @param ch Drugi rodzic
     * @return Jeden potomek
     * @throws WrongGraphTypeException
     */
    public Chromosom heuristicCrossover(Chromosom ch) throws WrongGraphTypeException {
        //Gdyby przypadkiem chcały się mutować osobniki z różnych symulacji
        if (size() != ch.size()) {
            throw new WrongGraphTypeException("Niezgodnośc rozmiarów chromosomów");
        }

        int length = size();

        Chromosom child = new Chromosom(length, this.graph);

        //Tworzę liste nie użytych jeszcze miast
        LinkedList<Integer> unused = new LinkedList<Integer>();

        for (int i = 0; i < length; ++i) {
            unused.add(i);
        }

        //Wybieram losowo początek krzyżowania
        int start = generator.nextInt(length);
        int from = get(start);
        int to1 = get(start + 1);
        int to2 = ch.get(ch.find(from) + 1);

        //Dodaje wartość na początek potomka i usuwiam miasto z "nie użytych"
        child.set(0, from);
        unused.remove(Integer.valueOf(from));

//        System.out.println("--- Heurystyka ---");

        //Pętla w celu uzupełnienia kolejnych genów potomka
        for (int i = 1; i < length; ++i) {
            int to;
            //Pobieram długości krawędzi z wybranego miasta do kolejnego u obu rodziców
            double ew1 = getEdgeWeight(from, to1);
            double ew2 = ch.getEdgeWeight(from, to2);


            if (ew1 < ew2) { //Jeśli ta wartość jest mniejsza u pierwszego rodzica
                if (unused.contains(to1)) { //Jeśli nie jest użyta to ją wybieram
                    to = to1;
                } else if (unused.contains(to2)) { //Jeśli którsza wartość jest użyta wybieram dłuższą
                    to = to2;
                } else { //Jeśli i dłuższa jest użyta, wybieram najmniej oddaloną z pozostałych
                    to = getClosestNeighbor(from, unused);
                }
            } else { // Jeśli wartość jest mniejsza u drugiego z rodziców
                if (unused.contains(to2)) { //Jeśli nie jest użyta to ją wybieram
                    to = to2;
                } else if (unused.contains(to1)) { //Jeśli którsza wartość jest użyta wybieram dłuższą
                    to = to1;
                } else { //Jeśli i dłuższa jest użyta, wybieram najmniej oddaloną z pozostałych
                    to = getClosestNeighbor(from, unused);
                }
            }

//            Testowanie metody heurystycznej - może sie przydać
//            System.out.println("from: " + from + " | to1: " + to1 + " | to2: " + to2 + " | to: " + to + " | ew1: " + ew1 + " | ew2: " + ew2);
//            Iterator<Integer> iter = unused.iterator();
//            while(iter.hasNext()) {
//                System.out.print(iter.next() + ", ");
//            }
//            System.out.println();

            //Dodaje miasto do potomka i usuwam z "nie użytych"
            child.set(i, to);
            unused.remove(Integer.valueOf(to));

            //Przesuwam wskaźniki:
            // /from/ na ostatnie miasto w potomku
            from = to;
            // /to1/ na kolejne miasto u pierwszego rodzica
            to1 = get(find(to) + 1);
            // /to2/ na kolejne miasto u drugiego rodzica
            to2 = ch.get(ch.find(to) + 1);
        }

//        System.out.println(child);
//        System.out.println();

        return child;
    }

    /**
     * Funkcja mutująca dany chromosom przez zamiane dwóch miast ze sobą
     * @param mutationSize liczba z przedziału (0, 1) określająca rozmiar mutacji
     * @return Zmutowany chromosom
     */
    public Chromosom mutation() {
        int length = size();

        Chromosom child = new Chromosom(length, this.graph);
        for (int i = 0; i < length; ++i) {
            child.set(i, get(i));
        }

        int from = generator.nextInt(length);
        int with = from + generator.nextInt(length - 1);

        int tmp = child.get(from);
        child.set(from, child.get(with));
        child.set(with, tmp);

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

    /**
     * Reprezentacja chromosomu w postaci napisu
     * @return Napis w postaci: miasto1 -> miasto2 ... miaston (fitness)
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Integer i : this) {
            try {
                b.append(this.graph.getNode(i).get("name"));
            } catch (ArrayIndexOutOfBoundsException ex) {
                b.append(" null ");
            }
//            b.append(i).append(", ");
            if (i != this.getLast()) {
                b.append(" -> ");
            }
        }

        b.append(" (" + fitness() + ")");
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

    /**
     * Wartość krawędzi miedzy podanymi miastami (krawędź powinna istnieć zawsze - operujemy na klikach)
     * @param s Miasto źródłowe
     * @param t Miasto docelowe
     * @return Wartość ścieżki
     */
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

    /**
     * Dopasowanie chromosomu - długość trasy przezeń reprezentowanej
     * @return Dopasowanie chromosomu
     */
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

    /**
     * Znajdź najbliższego sąsiada danego miasta spośród nie użytych miast
     * @param from Miasta dla którego szukamy najbliższego sąsiada
     * @param unused Lista dostępnych miast (dziedzina poszukiwań)
     * @return Najbliższe miasto (-1 gdy nie znaleziono)
     */
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
