package de.neemann.digital.analyse.espresso.datastructure.diff;

import de.neemann.digital.analyse.espresso.datastructure.Cover;
import de.neemann.digital.analyse.espresso.datastructure.Cube;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;

public class DifferenceMatrixModeDistance implements IDifferenceMatrixMode {

    /**
     * Calculate the distance of the variable value of the considered Cube and the Cover
     * @param cubeIndex
     *            Index of the actual Cube of the Onset
     * @param inputIndex
     *            Index of the actual variable of the Cubes
     * @return Difference between the variable values
     */
    @Override
    public boolean getElement(Cover originalCover, Cube cube, int cubeIndex, int inputIndex) {
        ThreeStateValue coverInputState = originalCover.getCube(cubeIndex)
                .getState(inputIndex);
        ThreeStateValue cubeInputState = cube.getState(inputIndex);

        //cubeInputState darf nicht gleich coverInputstate sein (1!=1, 0 != 0 , DC != DC)
        return !cubeInputState.equals(coverInputState);
    }
}
