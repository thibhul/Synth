package org.istic.synthlab.components.vcoa;

import org.istic.synthlab.core.*;
import org.istic.synthlab.core.modules.io.IOutput;
import org.istic.synthlab.core.modules.oscillators.IOscillator;
import org.istic.synthlab.core.modules.oscillators.OscillatorType;

public class Vcoa extends AComponent {

    private IOscillator sineOscillator;
    private IOutput output;

    public Vcoa(String name) {
        super(name);
        this.sineOscillator = AdapterFactory.createOscillator(this, OscillatorType.SINE);
        this.output = this.sineOscillator.getOutput();
    }

    @Override
    public void activate() {
        this.sineOscillator.activate();
    }

    @Override
    public void desactivate() {
        this.sineOscillator.desactivate();
    }

    @Override
    public void init() {
    }

    @Override
    public void run() {
    }

    public IOutput getOutput() {
        return this.output;
    }

    public IOscillator getSineOscillator() {
        return this.sineOscillator;
    }
}
