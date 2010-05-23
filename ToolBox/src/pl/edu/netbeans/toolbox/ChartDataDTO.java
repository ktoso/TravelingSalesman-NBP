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
    private final int iteration;
    private final double avgFitness;
    private final String simulationId;
    private final double maxFitness;
    private final double minFitness;

    public ChartDataDTO(String symulacja, int iteracja, double avg, double max, double min) {
        iteration = iteracja;
        avgFitness = avg;
        maxFitness = max;
        minFitness = min;
        simulationId = symulacja;
    }

    public double getAvgFitness() {
        return avgFitness;
    }

    public double getMaxFitness() {
        return maxFitness;
    }

    public double getMinFitness() {
        return minFitness;
    }

    public int getIteracja() {
        return iteration;
    }

    public String getSimId() {
        return simulationId;
    }

    @Override
    public String toString() {
        return "[@" + iteration + ": " + avgFitness + "] ";
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
        if (this.avgFitness != other.avgFitness) {
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
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.avgFitness) ^ (Double.doubleToLongBits(this.avgFitness) >>> 32));
        hash = 37 * hash + (this.simulationId != null ? this.simulationId.hashCode() : 0);
        return hash;
    }
}
