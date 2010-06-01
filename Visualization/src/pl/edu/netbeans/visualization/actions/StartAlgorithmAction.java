/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.netbeans.visualization.actions;

import java.io.IOException;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import pl.edu.netbeans.visualization.VisualizerTopComponent;

/**
 *
 * @author bartek
 */
public class StartAlgorithmAction extends CallableSystemAction {

    @Override
    public void performAction() {
        //TODO: znajdz i uruchom solvera w aktywnym Vizualizerze
        System.out.println("Start action preformed");
        TopComponent tc = TopComponent.getRegistry().getActivated();
        if (tc instanceof VisualizerTopComponent) {
            ((VisualizerTopComponent) tc).play();
            System.out.println("play");
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
        return "Start algorithm";
    }



    @Override
    public HelpCtx getHelpCtx() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
