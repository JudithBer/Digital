/*
 * Copyright (c) 2016 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.analyse.heuristic.datastructure;

import de.neemann.digital.analyse.quinemc.ThreeStateValue;

import java.util.Arrays;

/**
 * A Cube represents an implicant of a logical function. It is defined by its input values and
 * output value.
 */
public class Cube {

    // Input values of the Cubes (Order is important for the assignment to the variables)
    private ThreeStateValue[] input;
    // Output value of the Cube
    private ThreeStateValue output;

    /**
     * Generate a new Cube completely filled with DCs and the output ONE
     * @param inputLength
     *            Count of input variables
     */
    public Cube(int inputLength) {
        this.input = new ThreeStateValue[inputLength];

        for (int i = 0; i < inputLength; i++) {
            input[i] = ThreeStateValue.dontCare;
        }

        this.output = ThreeStateValue.one;
    }

    /**
     * Generate a new Cube filled with the given values
     * @param input
     *            Input values of the new Cube
     * @param output
     *            Output value of the new Cube
     */
    public Cube(ThreeStateValue[] input, ThreeStateValue output) {
        this.input = Arrays.copyOf(input, input.length);
        this.output = output;
    }

    /**
     * Generate a new Cube (copy) filled with the values of the given Cube
     * @param origin
     *            Origin Cube to copy
     */
    public Cube(Cube origin) {
        this.input = Arrays.copyOf(origin.input, origin.input.length);
        this.output = origin.output;
    }

    public Cube(ThreeStateValue output) {
        this.output = output;
    }

    /**
     * Convert the Cube into Positional Cube Notation (PCN)
     * @return An array with the input values of the Cube in PCN(-> array of double the length of
     *         the input)
     */
    public ThreeStateValue[] convertToPCN() {
        ThreeStateValue[] cubePCN = new ThreeStateValue[this.input.length * 2];

        for (int i = 0; i < input.length * 2; i = i + 2) {
            if (input[i / 2] == ThreeStateValue.zero) {
                cubePCN[i] = ThreeStateValue.one;
                cubePCN[i + 1] = ThreeStateValue.zero;
            } else if (input[i / 2] == ThreeStateValue.one) {
                cubePCN[i] = ThreeStateValue.zero;
                cubePCN[i + 1] = ThreeStateValue.one;
            } else if (input[i / 2] == ThreeStateValue.dontCare) {
                cubePCN[i] = ThreeStateValue.one;
                cubePCN[i + 1] = ThreeStateValue.one;
            }
        }
        return cubePCN;
    }

    /**
     * Checks if the Cube contains the given state for at least one input
     * @param state
     *            The state to check the containment
     * @return If the state is contained in the Cube or not
     */
    public boolean inputContains(ThreeStateValue state) {
        for (int i = 0; i < input.length; i++) {
            if (input[i] == state) {
                return true;
            }
        }
        return false;
    }

    // Getter of the Cube attributes

    /**
     * Get the inputLength (number of input variables)
     * @return Number of input variables
     */
    public int getInputLength() {
        return this.input.length;
    }

    /**
     * Get all Input of the Cube
     * @return an Array of ThreeStateValues which represents the requested Cube
     */
    public ThreeStateValue[] getInput() {
        return input;
    }

    /**
     * Get the state of the variable at position 'index'
     * @param index
     *            Index of the searched variable state
     * @return The state of the variable at position 'index'
     */
    public ThreeStateValue getState(int index) {
        return input[index];
    }

    /**
     * Sets a ThreeStateValue on the given Index
     * @param index
     *            index to set a new State in a Cube
     * @param newState
     *            State which needs to be set at index
     */
    public void setState(int index, ThreeStateValue newState) {
        this.input[index] = newState;
    }

    /**
     * Get the output state of the Cube
     * @return The output state of the Cube
     */
    public ThreeStateValue getOutput() {
        return output;
    }

    /**
     * Print the input values and the output value separated with a space
     * @return
     */
    @Override
    public String toString() {
        return Arrays.toString(input) + " " + output;
    }

    public void setInput(ThreeStateValue[] input) {
        this.input = input;
    }
}
