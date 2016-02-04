package org.istic.synthlab.core.modules.algorithms;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.UnitGenerator;

/**
 *
 */
public class VcoaAlgorithm extends UnitGenerator {
    public UnitInputPort potentiometer;
    public UnitInputPort frequencyModulation;
    public UnitOutputPort output;

    public VcoaAlgorithm() {
        potentiometer = new UnitInputPort("PotentiometerIn", 0.5);
        frequencyModulation = new UnitInputPort("FrequencyModulation", 0.0);
        output = new UnitOutputPort("Output");
        addPort(potentiometer);
        addPort(frequencyModulation);
        addPort(output);
    }

    @Override
    public void generate(int start, int limit) {
        double[] potentiometerFreqs = potentiometer.getValues();
        double[] voltageFreqs = this.frequencyModulation.getValues();
        double[] outputs = output.getValues();

        for (int i = start; i < limit; i++) {
            outputs[i] =  potentiometerFreqs[i]*Math.pow(2, voltageFreqs[i]);
        }
    }
}
