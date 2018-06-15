package de.neemann.digital.analyse.espresso.datastructure.diff;

import de.neemann.digital.analyse.espresso.datastructure.Cover;
import de.neemann.digital.analyse.espresso.datastructure.Cube;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;

public class DifferenceMatrixModeContainment implements IDifferenceMatrixMode {

    /**
     * Generate the difference between the given Cube and the original Cube on a given index to
     * check whether a Cube is contained in another Cube
     * @param cubeIndex
     *            Index of the Cube to generate the Element
     * @param inputIndex
     *            Index of the state to compare
     * @return boolean if Inputs differ. True if: one && zero, zero && one or DC && !DC
     */
    @Override
    public boolean getElement(Cover originalCover, Cube cube, int cubeIndex, int inputIndex) {
        ThreeStateValue coverInputState = originalCover.getCube(cubeIndex)
                .getState(inputIndex);
        ThreeStateValue cubeInputState = cube.getState(inputIndex);

        return (cubeInputState == ThreeStateValue.one && coverInputState == ThreeStateValue.zero)
                || (cubeInputState == ThreeStateValue.zero
                && coverInputState == ThreeStateValue.one)
                || (cubeInputState == ThreeStateValue.dontCare
                && coverInputState != ThreeStateValue.dontCare);
    }
}
