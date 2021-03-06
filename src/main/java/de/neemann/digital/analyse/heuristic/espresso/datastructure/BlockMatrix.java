/*
 * Copyright (c) 2016 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.analyse.heuristic.espresso.datastructure;

import de.neemann.digital.analyse.heuristic.datastructure.Cover;
import de.neemann.digital.analyse.heuristic.datastructure.Cube;
import de.neemann.digital.analyse.heuristic.exceptions.EmptyCoverException;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;

/**
 * BlockMatrix represent the difference between the (input) cube and the Offset Cover (diffCover)
 */
public class BlockMatrix extends BooleanMatrix {

    /**
     * Generate a new Boolean-Matrix and calculate the Block-Matrix out of the difference between
     * considered Cube and Offset Cover
     * @param cover
     *            Offset Cover of the logical function
     * @param cube
     *            Considered Cube
     * @throws EmptyCoverException
     *             if Cover is empty
     */
    public BlockMatrix(Cover cover, Cube cube) throws EmptyCoverException {
        super(cover, cube);

        // Calculate the difference Cover between the considered Cube and (all cubes of the) offset
        // Cover
        for (int i = 0; i < cover.size(); i++) {
            ThreeStateValue[] row = new ThreeStateValue[cube.getInputLength()];

            // Calculate the difference value for each variable value of the Cube
            for (int j = 0; j < cube.getInputLength(); j++) {
                boolean element = generateDistanceElement(i, j);
                row[j] = ThreeStateValue.value(element);
            }

            // Save the difference Cubes in the difference Cover
            this.getDiffCover().addCube(new Cube(row, ThreeStateValue.zero));
        }
    }

    /**
     * Calculate the distance of the variable value of the considered Cube and the Cube of the
     * offset Cover
     * @param cubeIndex
     *            Index of the actual Cube of the Offset
     * @param inputIndex
     *            Index of the actual variable of the Cubes
     * @return Difference between the variable values
     */
    private boolean generateDistanceElement(int cubeIndex, int inputIndex) {
        ThreeStateValue coverInputState = getOriginalCover().getCube(cubeIndex)
                .getState(inputIndex);
        ThreeStateValue cubeInputState = getCube().getState(inputIndex);

        return (cubeInputState == ThreeStateValue.one && coverInputState == ThreeStateValue.zero)
                || (cubeInputState == ThreeStateValue.zero
                        && coverInputState == ThreeStateValue.one);
    }
}
