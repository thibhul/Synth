package org.istic.synthlab.core.modules.lineOuts;


import org.istic.synthlab.core.utils.parametrization.Potentiometer;
import org.istic.synthlab.core.Resource;
import org.istic.synthlab.core.modules.io.IInput;

/**
 * @author  Group1
 * The interface Line out.
 */
public interface ILineOut extends Resource {

    void setVolume(double value);

    Potentiometer getPotentiometer();

    void start();

    void stop();

    IInput getInput();

}
