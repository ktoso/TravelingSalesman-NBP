/*
 */
package pl.edu.netbeans.visualization;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import org.openide.util.Lookup;
import org.openide.windows.WindowManager;
import pl.edu.netbeans.toolbox.GraphDrawer;

public final class KillAlgorithms implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        GraphDrawer graphDrawer = (GraphDrawer) WindowManager.getDefault().findTopComponent("VisualizerTopComponent");

        graphDrawer.stopAlgorithm();
    }
}
