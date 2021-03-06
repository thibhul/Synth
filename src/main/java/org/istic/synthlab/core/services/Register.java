package org.istic.synthlab.core.services;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.UnitGenerator;
import org.istic.synthlab.components.oscilloscope.Oscilloscope;
import org.istic.synthlab.components.out.Out;
import org.istic.synthlab.core.Channel;
import org.istic.synthlab.components.IComponent;
import org.istic.synthlab.core.modules.io.IInput;
import org.istic.synthlab.core.modules.io.IOutput;

import java.util.*;

/**
 * This class pretends to help I/O associations management.
 * Domain object with JSyn object relations also.
 *
 * It can be viewed as an active register that manage JSin equivalent semantics.
 *
 * All adapters MUST implement their own calls to the register when creating jsyn adaptee instances
 *
 * @author Stephane Mangin <stephane[dot]mangin[at]freesbee[dot]fr>
 *
 */
public class Register {

    // Keep an eye on components generators
    protected static Map<IComponent, List<UnitGenerator>> mappingGenerator = new HashMap<>();
    // Same for Inputs
    protected static Map<IComponent, Map<IInput, UnitInputPort>> mappingInput = new HashMap<>();
    // Same for Outputs
    protected static Map<IComponent, Map<IOutput, UnitOutputPort>> mappingOutput = new HashMap<>();
    // The most important one, inputs/outputs associations
    protected static Map<IInput, IOutput> associations = new HashMap<>();



    /**
     * Declare an dual association for a components and a generator
     *
     * @param component IComponents
     * @param unitGenerator UnitGenerator
     */
    public static void declare(IComponent component, UnitGenerator unitGenerator) {
        assert component != null;
        assert unitGenerator != null;
        if (!mappingGenerator.containsKey(component)) {
            mappingGenerator.put(component, new ArrayList<>());
        }
        mappingGenerator.get(component).add(unitGenerator);
        Factory.createSynthesizer().add(unitGenerator);
        //System.out.println(component + " connected to " + unitGenerator);
    }

    /**
     * Declare a triple assocation for a component and inputs.
     *
     * @param component IComponent
     * @param in IInput
     * @param unitIn UnitInputPort
     */
    public static void declare(IComponent component, IInput in, UnitInputPort unitIn) {
        assert component != null;
        assert in != null;
        assert unitIn != null;
        Map<IInput, UnitInputPort> assoc = new HashMap<>();
        assoc.put(in, unitIn);
        if (!mappingInput.containsKey(component)) {
            mappingInput.put(component, assoc);
        } else {
            mappingInput.get(component).putAll(assoc);
        }
        //System.out.println(component + " with input " + in + " connected to " + unitIn);
    }

    /**
     * Declare a triple assocation for a component and outputs.
     *
     * @param component IComponent
     * @param out IOutput
     * @param unitOut UnitOutputPort
     */
    public static void declare(IComponent component, IOutput out, UnitOutputPort unitOut) {
        assert component != null;
        assert out != null;
        assert unitOut != null;
        Map<IOutput, UnitOutputPort> assoc = new HashMap<>();
        assoc.put(out, unitOut);
        if (!mappingOutput.containsKey(component)) {
            mappingOutput.put(component, assoc);
        } else {
            mappingOutput.get(component).putAll(assoc);
        }
        //System.out.println(component + " with output " + out + " connected to " + unitOut);
    }

    /**
     * Connect two line together.
     * Lets it follow to JSyn synthetizer.
     *
     * @param in IInput
     * @param out IOutput
     *
     * @see Channel::connect(in, out)
     */
    public static void connect(IInput in, IOutput out) {
        assert in != null;
        assert out != null;

        if (!associations.containsKey(out)) {
            UnitInputPort unitIn = retrieve(in);
            UnitOutputPort unitOut = retrieve(out);
            if (unitIn == null) {
                throw new ExceptionInInitializerError(out + " has not been declared properly");
            }
            if (unitOut == null) {
                throw new ExceptionInInitializerError( in + " has not been declared properly");
            }
            Channel.connect(in, out);
            unitOut.connect(unitIn);
            associations.put(in, out);
            //System.out.println(in + " connected to " + out);

            //System.out.println(prettyPrint());
        }
    }

    /**
     * Disables an association from an input.
     *
     * @param in IInput
     *
     * @see Channel
     * @see UnitInputPort
     */
    public static void disconnect(IInput in) {
        UnitInputPort unitIn = retrieve(in);
        IOutput out = associations.get(in);
        // If there is no link, forget
        if (out == null) {
            return;
        }
        UnitOutputPort unitOut = retrieve(out);

        if (unitIn == null) {
            throw new ExceptionInInitializerError(out + " has not been declared properly");
        }
        if (unitOut == null) {
            throw new ExceptionInInitializerError(in + " has not been declared properly");
        }

        Channel.disconnect(in, out);
        unitOut.disconnect(unitIn);
        associations.remove(in, out);
        //System.out.println(in + " disconnected");

        //System.out.println(prettyPrint());
    }

    /**
     * Disables an association from an output.
     *
     * @param out IOutput
     *
     * @see Channel
     * @see UnitOutputPort
     */
    public static void disconnect(IOutput out) {
        IInput in = null;
        for (IInput in1: associations.keySet()) {
            if (out.equals(associations.get(in1))) {
                in = in1;
            }
        }
        // If there is no link, forget
        if (in == null) {
            return;
        }
        UnitInputPort unitIn = retrieve(in);
        UnitOutputPort unitOut = retrieve(out);
        if (unitIn == null) {
            throw new ExceptionInInitializerError(out + " has not been declared properly");
        } else if (unitOut == null) {
            throw new ExceptionInInitializerError(in + " has not been declared properly");
        }
        Channel.disconnect(in, out);
        // Stereo connections
        unitIn.disconnect(unitOut);
        associations.remove(in, out);
        //System.out.println(out + " disconnected");

        //System.out.println(prettyPrint());
    }

    /**
     * Get the related unitport from an input.
     *
     * @param in IInput
     * @return UnitInputPort or null
     */
    public static UnitInputPort retrieve(IInput in) {
        assert in != null;
        for (IComponent component: mappingInput.keySet()) {
            for (IInput input: mappingInput.get(component).keySet()) {
                if (in.equals(input)) {
                    return mappingInput.get(component).get(in);
                }
            }
        }
        return null;
    }

    /**
     * Get the related unitport from an output.
     *
     * @param out IOutput
     * @return UnitOutputPort or null
     */
    public static UnitOutputPort retrieve(IOutput out) {
        assert out != null;
        for (IComponent component: mappingOutput.keySet()) {
            for (IOutput output: mappingOutput.get(component).keySet()) {
                if (out.equals(output)) {
                    return mappingOutput.get(component).get(out);
                }
            }
        }
        return null;
    }

    /**
     * Get the component for a given input.
     *
     * @param in IInput
     * @return IComponent or null
     */
    public static IComponent getComponent(IInput in) {
        assert in != null;
        for (IComponent component: mappingInput.keySet()) {
            for (IInput input: mappingInput.get(component).keySet()) {
                if (in.equals(input)) {
                    return component;
                }
            }
        }
        return null;
    }

    /**
     * Get the component for a given output.
     *
     * @param out IOutput
     * @return IComponent or null
     */
    public static IComponent getComponent(IOutput out) {
        assert out != null;
        for (IComponent component: mappingOutput.keySet()) {
            for (IOutput output: mappingOutput.get(component).keySet()) {
                if (out.equals(output)) {
                    return component;
                }
            }
        }
        return null;
    }


    /**
     *
     * TODO: WTF ??
     */
    // FIXME
    // #SigneScrumMaster
    public static void startComponents() {
        for (IComponent component : mappingGenerator.keySet()) {
            if (component instanceof Out) {
                Out out = (Out) component;
                out.start();
            }
            else if (component instanceof Oscilloscope) {
                Oscilloscope scope = (Oscilloscope) component;
                scope.activate();
            }
        }
    }

    /**
     * Helper class to prety print content
      */
    public static String prettyPrint() {
        StringBuilder sb = new StringBuilder();

/*        sb.append("Mapping INPUTS :");
        Iterator<Map.Entry<IComponent, Map<IInput, UnitInputPort>>> iter = mappingInput.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<IComponent, Map<IInput, UnitInputPort>> entry = iter.next();
            sb.append("\n\t" + entry.getKey());
            Iterator<Map.Entry<IInput, UnitInputPort>> iter01 = entry.getValue().entrySet().iterator();
            while (iter01.hasNext()) {
                Map.Entry<IInput, UnitInputPort> entry01 = iter01.next();
                sb.append("\n\t\t" + entry01.getKey());
                sb.append(" => ");
                sb.append(entry01.getValue());
            }
        }
        sb.append("\n" + "Mapping OUTPUTS :");
        Iterator<Map.Entry<IComponent, Map<IOutput, UnitOutputPort>>> iter2 = mappingOutput.entrySet().iterator();
        while (iter2.hasNext()) {
            Map.Entry<IComponent, Map<IOutput, UnitOutputPort>> entry = iter2.next();
            sb.append("\n\t" + entry.getKey());
            Iterator<Map.Entry<IOutput, UnitOutputPort>> iter02 = entry.getValue().entrySet().iterator();
            while (iter02.hasNext()) {
                Map.Entry<IOutput, UnitOutputPort> entry02 = iter02.next();
                sb.append("\n\t\t" + entry02.getKey());
                sb.append(" => ");
                sb.append(entry02.getValue());
            }
        }*/
        sb.append("\nAssociated ports");
        sb.append("\n================");
        for (Map.Entry<IInput, IOutput> entry : associations.entrySet()) {
            sb.append("\n\t").append(entry.getValue());
            sb.append("\n\t\t => ").append(entry.getKey());
        }
        return sb.toString();
    }

    /**
     * Check if this input has been connected already
     * @param input
     * @return
     */
    public static boolean isConnected(IInput input) {
        return associations.keySet().contains(input) && associations.get(input) != null;
    }

    /**
     * Check if this output has been connected already
     * @param output
     * @return
     */
    public static boolean isConnected(IOutput output) {
        return associations.values().contains(output);
    }
}
