/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.netbeans.toolbox;

import java.util.Random;

/**
 *
 * @author bartek
 */
public class RandomNG extends Random {

    /**
     * Generuje zmienną boolean z szansą percent
     * @param percent Procent szansy na wylosowanie prawdy
     * @return prawda/fałsz
     */
    public boolean nextBoolean(double percent) {
        return (nextDouble() < percent);
    }
}
