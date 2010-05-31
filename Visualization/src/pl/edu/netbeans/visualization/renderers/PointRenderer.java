/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.netbeans.visualization.renderers;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import prefuse.render.AbstractShapeRenderer;
import prefuse.visual.VisualItem;

/**
 *
 * @author bartek
 */
public class PointRenderer extends AbstractShapeRenderer {

    double size;

    public PointRenderer(double size) {
        this.size = size;
    }
    
    @Override
    protected Shape getRawShape(VisualItem item) {
        Ellipse2D e = new Ellipse2D.Double(item.getX(), item.getY(), size, size);
        return e;
    }

}
