/*
 */

package pl.edu.netbeans.visualization.actions;

import prefuse.action.layout.SpecifiedLayout;

/**
 * This layout gets the X and Y coordinates from all the nodes and arranges them
 * in a way that all of them are able to fit on the screen.
 *
 * It's magic! ;o)
 *
 * @author ktoso
 */
public class CitiesLayoutAction extends SpecifiedLayout{

    public CitiesLayoutAction(String group, String xField, String yField) {
        super(group, xField, yField);
    }


}
