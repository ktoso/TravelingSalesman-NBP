/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.netbeans.visualization.actions;

import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

/**
 *
 * @author bartek
 */
public class StopAlgorithmAction extends CallableSystemAction {

    @Override
    public void performAction() {
        //TODO: znajdz i zatrzymaj solvera w aktywnym Vizualizerze
    }

    @Override
    public String getName() {
        return "Stop algorithm";
    }

    @Override
    public HelpCtx getHelpCtx() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
