/*
 */
package pl.edu.netbeans.generators;

import java.awt.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class GenerateGraphWizardPanel1 implements WizardDescriptor.ValidatingPanel {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private GenerateGraphVisualPanel1 component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    public GenerateGraphVisualPanel1 getComponent() {
        if (component == null) {
            component = new GenerateGraphVisualPanel1();
        }
        return component;
    }

    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx(SampleWizardPanel1.class);
    }

    public boolean isValid() {
        return true;
        // If it depends on some condition (form filled out...), then:
        // return someCondition();
        // and when this condition changes (last form field filled in...) then:
        // fireChangeEvent();
        // and uncomment the complicated stuff below.
    }

    public final void addChangeListener(ChangeListener l) {
    }

    public final void removeChangeListener(ChangeListener l) {
    }
    /*
    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0
    public final void addChangeListener(ChangeListener l) {
    synchronized (listeners) {
    listeners.add(l);
    }
    }
    public final void removeChangeListener(ChangeListener l) {
    synchronized (listeners) {
    listeners.remove(l);
    }
    }
    protected final void fireChangeEvent() {
    Iterator<ChangeListener> it;
    synchronized (listeners) {
    it = new HashSet<ChangeListener>(listeners).iterator();
    }
    ChangeEvent ev = new ChangeEvent(this);
    while (it.hasNext()) {
    it.next().stateChanged(ev);
    }
    }
     */

    // You can use a settings object to keep track of state. Normally the
    // settings object will be the WizardDescriptor, so you can use
    // WizardDescriptor.getProperty & putProperty to store information entered
    // by the user.
    public void storeSettings(Object settings) {
        ((WizardDescriptor) settings).putProperty("nodeCount", ((GenerateGraphVisualPanel1) getComponent()).getNodeCount());
        ((WizardDescriptor) settings).putProperty("graphFilename", ((GenerateGraphVisualPanel1) getComponent()).getGraphFilename());
        ((WizardDescriptor) settings).putProperty("maxGenerations", ((GenerateGraphVisualPanel1) getComponent()).getMaxGenerations());
        ((WizardDescriptor) settings).putProperty("maxGenerationsWGB", ((GenerateGraphVisualPanel1) getComponent()).getMaxGenerationsWithoutGettingBetter());
        ((WizardDescriptor) settings).putProperty("loadingExistingGraph", ((GenerateGraphVisualPanel1) getComponent()).isLoadingExisting());
        ((WizardDescriptor) settings).putProperty("populationSize", ((GenerateGraphVisualPanel1) getComponent()).getPopulationSize());
    }

    public void readSettings(Object settings) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void validate() throws WizardValidationException {
        if ( ! component.isLoadingExisting() ) {
            String nodeCount = component.getNodeCount();
            if (nodeCount.isEmpty()) {
                throw new WizardValidationException(null, "Proszę podać liczbę wierzchołków", null);
            }
            if (!nodeCount.matches("\\d+")) {
                throw new WizardValidationException(null, "Proszę podać poprawną liczbę całkowitą", null);
            }
        }

        String maxGenerations = component.getMaxGenerations();
        if (maxGenerations.isEmpty()) {
            throw new WizardValidationException(null, "Proszę podać maksymalną ilość generacji", null);
        }
        if (!maxGenerations.matches("\\d+")) {
            throw new WizardValidationException(null, "Proszę podać poprawną liczbę całkowitą", null);
        }

        String maxGenerationsWGB = component.getMaxGenerationsWithoutGettingBetter();
        if (maxGenerationsWGB.isEmpty()) {
            throw new WizardValidationException(null, "Proszę podać maksymalną ilość generacji bez poprawy dopasowania", null);
        }
        if (!maxGenerationsWGB.matches("\\d+")) {
            throw new WizardValidationException(null, "Proszę podać poprawną liczbę całkowitą", null);
        }

        String populationSize = component.getPopulationSize();
        if (populationSize.isEmpty()) {
            throw new WizardValidationException(null, "Proszę podać rozmiar populacji", null);
        }
        if (!populationSize.matches("\\d+")) {
            throw new WizardValidationException(null, "Proszę podać poprawną liczbę całkowitą", null);
        }
    }
}

