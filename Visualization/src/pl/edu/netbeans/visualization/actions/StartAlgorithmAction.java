/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.netbeans.visualization.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.TopComponent;
import pl.edu.netbeans.visualization.VisualizerTopComponent;

/**
 *
 * @author bartek
 */
public class StartAlgorithmAction extends CallableSystemAction implements PropertyChangeListener {

    public StartAlgorithmAction() {
        TopComponent.getRegistry().addPropertyChangeListener(this);

        updateEnablement();
    }

    @Override
    public void performAction() {
        //TODO: znajdz i uruchom solvera w aktywnym Vizualizerze
        TopComponent tc = TopComponent.getRegistry().getActivated();
        if (tc instanceof VisualizerTopComponent) {
            ((VisualizerTopComponent) tc).play();
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

    public void propertyChange(PropertyChangeEvent evt) {
        if (TopComponent.Registry.PROP_ACTIVATED.equals(evt.getPropertyName())) {
            TopComponent.getRegistry().getActivated().addPropertyChangeListener(this);
        }
        updateEnablement();
    }

    private void updateEnablement() {
        TopComponent tc = TopComponent.getRegistry().getActivated();
        if (tc instanceof VisualizerTopComponent) {
            setEnabled(((VisualizerTopComponent) tc).isPlayable());
        } else {
            setEnabled(false);
        }
    }
}
