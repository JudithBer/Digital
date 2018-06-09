/*
 * Copyright (c) 2016 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.analyse.espresso.datastructure;

import de.neemann.digital.analyse.espresso.exceptions.EmptyCoverException;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;

/**
 * CoverMatrix represent the difference between the (input) cube and the Onset Cover (diffCover)
 */
public class CoverMatrix extends BooleanMatrix {

    /**
     * Generate a new Boolean-Matrix and calculate the Cover-Matrix out of the difference between
     * considered Cube and Onset Cover
     * @param cover
     *            Onset Cover of the logical function
     * @param cube
     *            Considered Cube
     * @throws EmptyCoverException
     *             if the Cover is empty
     */
    public CoverMatrix(Cover cover, Cube cube) throws EmptyCoverException {
        super(cover, cube);

        // Calculate the difference Cover between the considered Cube and (all Cubes of the) onset
        // Cover
        for (int i = 0; i < cover.size(); i++) {
            ThreeStateValue[] row = new ThreeStateValue[cube.getInputLength()];

            // Calculate the difference value for each variable value of the Cube
            for (int j = 0; j < cube.getInputLength(); j++) {
                boolean element = generateDistanceElement(i, j);
                row[j] = ThreeStateValue.value(element);
            }

            // Save the difference Cubes in the difference Cover
            this.getDiffCover().addCube(new Cube(row, ThreeStateValue.one));
        }
    }

    /**
     * Calculate the distance of the variable value of the considered Cube and the Cube of the onset
     * Cover
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
                        && coverInputState != ThreeStateValue.zero);
    }
}
