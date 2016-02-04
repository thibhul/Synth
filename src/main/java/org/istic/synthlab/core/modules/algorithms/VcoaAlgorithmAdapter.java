package org.istic.synthlab.core.modules.algorithms;

import org.istic.synthlab.core.IComponent;
import org.istic.synthlab.core.modules.io.IInput;
import org.istic.synthlab.core.modules.io.IOutput;
import org.istic.synthlab.core.modules.parametrization.Potentiometer;
import org.istic.synthlab.core.modules.parametrization.PotentiometerType;
import org.istic.synthlab.core.services.ModulesFactory;
import org.istic.synthlab.core.services.Register;

/**
 *
 */
public class VcoaAlgorithmAdapter implements IVcoaAlgorithm {
    private final Potentiometer potentiometer;
    private IOutput output;
    private IInput input;
    private VcoaAlgorithm algorithm;

    public VcoaAlgorithmAdapter(IComponent component) {
        this.algorithm = new VcoaAlgorithm();
        this.output = ModulesFactory.createOutput(component, algorithm.output);
        this.input = ModulesFactory.createInput(component, algorithm.frequencyModulation);
        this.potentiometer = new Potentiometer("Frequency",PotentiometerType.EXPONENTIAL, 20000.0, 20.0, 320.0);

        // Declare the relation to the mapping
        Register.declare(component, this.algorithm);
        this.output = ModulesFactory.createOutput(component, algorithm.output);
        this.input = ModulesFactory.createInput(component, algorithm.frequencyModulation);
    }

    @Override
    public IInput getInput() {
        return input;
    }

    @Override
    public IOutput getOutput() {
        return output;
    }

    @Override
    public Potentiometer getFrequencyPotentiometer() {
        return potentiometer;
    }

    @Override
    public void setPotentiometerFrequency(double value) {
        algorithm.potentiometer.set(value);
    }

}
