/*
 * Copyright (c) 2016 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.analyse.espresso.datastructure;

import de.neemann.digital.analyse.espresso.exceptions.EmptyCoverException;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;

/**
 * Generates a Matrix with the difference between all Cubes of a Cover and the given Cube.
 * @author Annika Keil, Judith Berthold
 */
public class DifferenceMatrix extends BooleanMatrix {

    /**
     * Generate a new Boolean-Matrix with the given Cube and Cover
     * @param originalCover
     *            Cover to compare with the given Cube to calculate the Difference-Matrix
     * @param cube
     *            Cube to compare with a Cover to calculate the Difference-Matrix
     * @param mode
     *            Kind of the Difference-Matrix ("distance", "containment", "NoDcElement")
     * @throws EmptyCoverException
     *             if the given Cover is empty
     */
    public DifferenceMatrix(Cover originalCover, Cube cube, String mode)
            throws EmptyCoverException {
        super(originalCover, cube);

        // Calculate the difference Cover between the considered cube and the cubes of the cover
        for (int i = 0; i < originalCover.size(); i++) {
            ThreeStateValue[] row = new ThreeStateValue[cube.getInputLength()];

            // Calculate the difference value for each variable value of the cube
            for (int j = 0; j < cube.getInputLength(); j++) {
                boolean element = false;
                switch (mode) {
                case "distance":
                    element = generateDistanceElement(i, j);
                    break;
                case "containment":
                    element = generateContainmentElement(i, j);
                    break;
                case "NoDcElement":
                    element = generateNoDCElement(i, j);
                    break;
                }
                row[j] = ThreeStateValue.value(element);
            }

            // Save the difference Cubes in the difference Cover
            this.getDiffCover().addCube(new Cube(row, ThreeStateValue.one));
        }
    }

    /**
     * Calculate the distance of the variable value of the considered Cube and the Cover
     * @param cubeIndex
     *            Index of the actual Cube of the Onset
     * @param inputIndex
     *            Index of the actual variable of the Cubes
     * @return Difference between the variable values
     */
    private boolean generateDistanceElement(int cubeIndex, int inputIndex) {
        ThreeStateValue coverInputState = getOriginalCover().getCube(cubeIndex)
                .getState(inputIndex);
        ThreeStateValue cubeInputState = getCube().getState(inputIndex);

        return (cubeInputState == ThreeStateValue.one && coverInputState != ThreeStateValue.one)
                || (cubeInputState == ThreeStateValue.zero
                        && coverInputState != ThreeStateValue.zero)
                || (cubeInputState == ThreeStateValue.dontCare
                        && coverInputState != ThreeStateValue.dontCare);
    }

    /**
     * Generate the difference between the given Cube and the original Cube on a given index to
     * check whether a Cube is contained in another Cube
     * @param cubeIndex
     *            Index of the Cube to generate the Element
     * @param inputIndex
     *            Index of the state to compare
     * @return boolean if Inputs differ. True if: one && zero, zero && one or DC && !DC
     */
    private boolean generateContainmentElement(int cubeIndex, int inputIndex) {
        ThreeStateValue coverInputState = getOriginalCover().getCube(cubeIndex)
                .getState(inputIndex);
        ThreeStateValue cubeInputState = getCube().getState(inputIndex);

        return (cubeInputState == ThreeStateValue.one && coverInputState == ThreeStateValue.zero)
                || (cubeInputState == ThreeStateValue.zero
                        && coverInputState == ThreeStateValue.one)
                || (cubeInputState == ThreeStateValue.dontCare
                        && coverInputState != ThreeStateValue.dontCare);
    }

    /**
     * Generate the difference between the given Cube and the original Cube on a given index to
     * check whether a Cube differs the other if there is no DC on the current Cube
     * @param cubeIndex
     *            Index of the Cube to generate the Element
     * @param inputIndex
     *            Index of the state to compare
     * @return boolean if Inputs differ. True if: one && zero, zero && one or cubeInput != DC
     */
    private boolean generateNoDCElement(int cubeIndex, int inputIndex) {
        ThreeStateValue coverInputState = getOriginalCover().getCube(cubeIndex)
                .getState(inputIndex);
        ThreeStateValue cubeInputState = getCube().getState(inputIndex);

        return (cubeInputState == ThreeStateValue.one && coverInputState == ThreeStateValue.zero)
                || (cubeInputState == ThreeStateValue.zero
                        && coverInputState == ThreeStateValue.one)
                || (!(cubeInputState == ThreeStateValue.dontCare));
    }
}
