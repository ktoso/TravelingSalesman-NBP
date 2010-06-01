/*
 */
package pl.edu.netbeans.visualization.actions;

import prefuse.Constants;
import prefuse.action.assignment.DataColorAction;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

/**
 * Koloruje najlepszą w danej iteracji ścieżkę na wyróżniający się kolor
 *
 * Kolorowane jest tyle ostatnich "ścieżek" ile mamy poustawianych oraz kolorów tutaj
 * Można przestać rysować krawędź ustalając marked na 0
 * @author ktoso
 */
public class RouteDataColorAction extends DataColorAction {

//    W algorytmie używam tego tak: 0 - nie używane scieżki, 1 - najlepszy
//    z poprzedniego pokolenia, 2 najlepszy z aktualnego pokolenia
    static private int[] palette = new int[]{
        ColorLib.rgba(200, 200, 200, 0), //marked==0
        ColorLib.rgb(255, 0, 0), //marked==1
        ColorLib.rgb(0, 255, 0), //marked==2
        ColorLib.rgb(0, 0, 255)   //marked==3
    };

    public RouteDataColorAction(){
        super("graph.edges", "marked", Constants.NOMINAL, VisualItem.STROKECOLOR, palette);
    }
    
    public RouteDataColorAction(String group, String dataField, int dataType, String colorField, int[] palette) {
        super(group, dataField, dataType, colorField, palette);
    }

    public RouteDataColorAction(String group, String dataField, int dataType, String colorField) {
        super(group, dataField, dataType, colorField);
    }

}
