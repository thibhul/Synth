package org.istic.synthlab.core.modules.io;

import com.jsyn.ports.UnitOutputPort;
import org.istic.synthlab.core.IComponent;
import org.istic.synthlab.core.services.Register;


/**
 * @author  Group1
 * The type Output adapter
 */
public class OutputAdapter implements IOutput {

    private UnitOutputPort unitOutputPort;
    private IComponent component;
    private String name;

    /**
     * Instantiates a new Output adapter.
     *
     * @param name
     * @param unitOutputPort the unit output port
     */
    public OutputAdapter(String name, IComponent component, UnitOutputPort unitOutputPort) {
        this.name = name;
        this.unitOutputPort = unitOutputPort;
        this.component = component;
        // Declare this association to the register
        Register.declare(component, this, unitOutputPort);
    }

    /**
     * Connect the OutputAdapter to the input
     *
     * @param in:IInput
     */
    public void connect(IInput in) {
        Register.connect(in, this);
    }

    @Override
    public void deconnect() {
        Register.disconnect(this);
    }

    public UnitOutputPort getUnitOutputPort() {
        return this.unitOutputPort;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.component + "::" + this.getName() + "<" + this.hashCode() + ">";
    }
}
