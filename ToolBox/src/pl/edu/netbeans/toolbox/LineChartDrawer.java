/*
 */

package pl.edu.netbeans.toolbox;

/**
 * Służy oznazczeniu oraz odnajdywaniu elementu który potrafi rysować wykres liniowy
 * konkretniej - FitnessGraphTopComponent w naszym przypadku
 * @author ktoso
 */
public interface LineChartDrawer {
    public void updateAndDraw(int x, double y, String id);
}
