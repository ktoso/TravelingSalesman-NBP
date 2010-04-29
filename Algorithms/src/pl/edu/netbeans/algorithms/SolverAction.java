/*
 */
package pl.edu.netbeans.algorithms;

import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import prefuse.action.Action;
import prefuse.data.Graph;

/**
 * Use this only to create your own solver classes
 * It has some minor NetBeans Platform related helpers - allowing you to log into the output window etc
 * @author ktoso
 */
abstract public class SolverAction extends Action {

    private InputOutput io = getIO();
    protected final Graph graph;

    public SolverAction(Graph graph) {
        this.graph = graph;
    }

    protected void log(String line) {
        io.getOut().print(line + "\n");
    }

    protected void err(String line) {
        getIO().getErr().append(line + "\n");
    }

    private InputOutput getIO() {
        if (io == null) {
            io = IOProvider.getDefault().getIO("Algorithm log", false);
        }
        return io;
    }
}
