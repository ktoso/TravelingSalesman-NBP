/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.netbeans.visualization.actions;

import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.TopComponent;
import pl.edu.netbeans.visualization.VisualizerTopComponent;

/**
 *
 * @author bartek
 */
public class StepAlgorithmAction extends CallableSystemAction {

    @Override
    public void performAction() {
        TopComponent tc = TopComponent.getRegistry().getActivated();
        if (tc instanceof VisualizerTopComponent) {
            ((VisualizerTopComponent) tc).step();
        } else {
            System.err.println("Coś dziwnego!");
        }
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }


    @Override
    public String getName() {
        return "Do step in algorithm";
    }

    @Override
    public HelpCtx getHelpCtx() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
