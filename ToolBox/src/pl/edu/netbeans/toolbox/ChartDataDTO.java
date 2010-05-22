/*
 */
package pl.edu.netbeans.toolbox;

import java.util.Random;

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
    private int iteration;
    private double fitness;
    private String simulationId;

    public ChartDataDTO(int iteracja, double fitness, String symulacja) {
        this.iteration = iteracja;
        this.fitness = fitness;
        this.simulationId = symulacja;
    }

    public double getFitness() {
        return fitness;
    }

    public int getIteracja() {
        return iteration;
    }

    public String getSimId() {
        return simulationId;
    }

    @Override
    public String toString() {
        return "[@" + iteration + ": " + fitness + "] ";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChartDataDTO other = (ChartDataDTO) obj;
        if (this.iteration != other.iteration) {
            return false;
        }
        if (this.fitness != other.fitness) {
            return false;
        }
        if ((this.simulationId == null) ? (other.simulationId != null) : !this.simulationId.equals(other.simulationId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.iteration;
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.fitness) ^ (Double.doubleToLongBits(this.fitness) >>> 32));
        hash = 37 * hash + (this.simulationId != null ? this.simulationId.hashCode() : 0);
        return hash;
    }
}
