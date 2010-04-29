/*
 */

package pl.edu.netbeans.algorithms;

/**
 * <strong>An TSSolverAction implementation MUST also extend prefuse.action.Action</stron>
 * A common interface to mars classes that are able to SOLVE the Traveling Salesman Problem<br/>
 * @author ktoso
 */
public interface TSSolverAction {
    //it's just a marker interface, to enable loopups for algorithm implementations
    //

    public String getHumanReadibleName();
    public String getDescription();

    //basically "please implement the run for prefuse.action.Action
    public void run(double frac);
}