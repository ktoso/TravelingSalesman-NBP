/*
 */
package pl.edu.netbeans.toolbox;

/**
 * DataTranferObject do przenoszenia informacji z algorytmu do elementu potrafiącego
 * takie dane przedstawić jako wykres
 *
 * Hashcode zapewnia nam iż za każdym kolejnym dodaniem DTO do Lookup, będzie to
 * na pewno rozmoznane jako nowy obiekt, a słuchacze zostaną powiadomieni.
 * @author ktoso
 */
public class ChartDataDTO {

    //czy final tutaj spowoduje jakieś optymalizacje? hmm...
    private int iteracja;
    private double fitness;
    private String symulacja;

    public ChartDataDTO(int iteracja, double fitness, String symulacja) {
        this.iteracja = iteracja;
        this.fitness = fitness;
        this.symulacja = symulacja;
    }

    public double getFitness() {
        return fitness;
    }

    public int getIteracja() {
        return iteracja;
    }

    public String getSimId() {
        return symulacja;
    }
}
