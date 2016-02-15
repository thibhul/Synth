package org.istic.synthlab.core.modules.modulators;

import com.jsyn.ports.UnitInputPort;
import org.istic.synthlab.components.IComponent;
import org.istic.synthlab.core.modules.io.IInput;
import org.istic.synthlab.core.modules.io.IOutput;
import org.istic.synthlab.core.utils.parametrization.Potentiometer;
import org.istic.synthlab.core.utils.parametrization.PotentiometerType;

/**
 * Create an abstraction to manage a potentiometer throught an internal logic
 *
 * 'Abstract Modulator' representation
 * -----------------------------------
 *
 *        External View
 *        +---------------------------------------------------------------+
 *        |                 +-------------------------------+             |
 *      +-+-+               |                               |           +-+-+
 * input|   +---------------> Internal logic                +----------->   | Output
 *      +-+-+               |                               |           +-+-+
 *        |                 |                               |             |
 *        |                 |                               |             |
 *        |           +-----+                               |             |
 *        |           |     |                               |             |
 *        |           |     +-------------------------------+             |
 *        |           |                                                   |
 *        |           |setPotentiometer                                   |
 *        |         +-v-------------------+                               |
 *        |         |                     |                               |
 *        |         | Potentiometer       |                               |
 *        |         |                     +-----------------------------------> getPotentiometer
 *        |         |                     |                               |
 *        |         +---------------------+                               |
 *        |                                                               |
 *        +---------------------------------------------------------------+
 *                                        Made with : http://asciiflow.com/
 *
 * @author Stephane Mangin <stephane[dot]mangin[at]freesbee[dot]fr>
 */
public abstract class AbstractModulator implements IModulator {
    private final IComponent component;
    private Potentiometer potentiometer;
    protected IOutput output;
    protected IInput input;
    private final String name;

    public AbstractModulator(String name, IComponent component) {
        this.name = name;
        this.component = component;
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
    public String getName() {
        return this.name;
    }

    public double getRawValue() {
        return potentiometer.getOriginalValue();
    }

    public double getValue() {
        return potentiometer.getValue();
    }

    public void setValue(double value) {
        potentiometer.setValue(value);
    }

    public double getMax() {
        return potentiometer.getMax();
    }

    public double getMin() {
        return potentiometer.getMin();
    }

    public void setMax(double value) {
        potentiometer.setMax(value);
    }

    public void setMin(double value) {
        potentiometer.setMin(value);
    }

    protected void setPotentiometer(Potentiometer potentiometer) {
        this.potentiometer = potentiometer;
    }
}
