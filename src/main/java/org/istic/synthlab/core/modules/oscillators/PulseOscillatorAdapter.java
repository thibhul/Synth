package org.istic.synthlab.core.modules.oscillators;

import com.jsyn.unitgen.PulseOscillator;
import org.istic.synthlab.core.IComponent;
import org.istic.synthlab.core.modules.modulators.ModulatorType;
import org.istic.synthlab.core.services.Register;
import org.istic.synthlab.core.services.ModulesFactory;
import org.istic.synthlab.core.utils.parametrization.Potentiometer;
import org.istic.synthlab.core.utils.parametrization.PotentiometerType;

/**
 * A pulse generator.
 *
 */
public class PulseOscillatorAdapter extends AbstractOscillator {

    private PulseOscillator pulseOscillator;

    /*
    * the constructor
    */
    public PulseOscillatorAdapter(IComponent component) {
        super(component);
        this.pulseOscillator = new PulseOscillator();
        // Declare the relation to the register
        Register.declare(component, this.pulseOscillator);
        this.output = ModulesFactory.createOutput(component, pulseOscillator.output);
        this.frequencyPotentiometer = new Potentiometer("Frequency", pulseOscillator.frequency, PotentiometerType.EXPONENTIAL, 20000.0, 20.0, 1000.0);
    }

    @Override
    public void activate() {
        this.pulseOscillator.setEnabled(true);
    }

    @Override
    public void desactivate() {
        this.pulseOscillator.setEnabled(false);
    }

}
