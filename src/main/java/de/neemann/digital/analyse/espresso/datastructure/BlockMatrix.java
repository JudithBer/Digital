package de.neemann.digital.analyse.espresso.datastructure;

import de.neemann.digital.analyse.espresso.exceptions.EmptyCoverException;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;

/**
 * BlockMatrix represent the difference between the (input) cube and the Offset Cover (diffCover)
 */
public class BlockMatrix extends BooleanMatrix {

    /**
     * Generate a new BooleanMatrix and calculate the BlockMatrix out of the difference between
     * considered Cube and Offset Cover
     * @param cover
     *            Offset Cover of the logical function
     * @param cube
     *            Considered Cube
     */
    public BlockMatrix(Cover cover, Cube cube) throws EmptyCoverException {
        super(cover, cube);

        // Calculate the difference Cover between the considered cube and (all cubes of the) offset
        // cover
        for (int i = 0; i < cover.size(); i++) {
            ThreeStateValue[] row = new ThreeStateValue[cube.getInputLength()];

            // Calculate the difference value for each variable value of the cube
            for (int j = 0; j < cube.getInputLength(); j++) {
                boolean element = generateDistanceElement(i, j);
                row[j] = ThreeStateValue.value(element);
            }

            // Save the difference Cubes in the difference Cover
            this.diffCover.addCube(new Cube(row, ThreeStateValue.zero));
        }
    }

    /**
     * Calculate the distance of the variable value of the considered cube and the cube of the
     * offset Cover
     * @param cubeIndex
     *            Index of the actual cube of the offset
     * @param inputIndex
     *            Index of the actual variable of the cubes
     * @return Difference between the variable values
     */
    private boolean generateDistanceElement(int cubeIndex, int inputIndex) {
        ThreeStateValue coverInputState = getOriginalCover().getCube(cubeIndex).getState(inputIndex);
        ThreeStateValue cubeInputState = getCube().getState(inputIndex);

        return (cubeInputState == ThreeStateValue.one && coverInputState == ThreeStateValue.zero)
                || (cubeInputState == ThreeStateValue.zero
                        && coverInputState == ThreeStateValue.one);
    }
}