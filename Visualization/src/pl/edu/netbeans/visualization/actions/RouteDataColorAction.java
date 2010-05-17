/*
 */
package pl.edu.netbeans.visualization.actions;

import prefuse.Constants;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.data.expression.BooleanLiteral;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
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

    //TODO: ustalić kolory na kolejne pokolenia!
    //oznaczanie działa na styl "kolejnych pokoleń"
    static private int[] palette = new int[]{
        ColorLib.alpha(0),           //marked==0
        ColorLib.rgb(255, 180, 180), //marked==1
        ColorLib.rgb(150, 200, 150), //marked==2
        ColorLib.rgb(50, 255, 50),   //marked==3
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
