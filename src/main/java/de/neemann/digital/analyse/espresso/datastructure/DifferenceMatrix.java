package de.neemann.digital.analyse.espresso.datastructure;

import de.neemann.digital.analyse.quinemc.ThreeStateValue;

public class DifferenceMatrix extends BooleanMatrix {

    /**
     * Generate a new BooleanMatrix with the given Cube and Cover
     *
     * @param originalCover TODO Beschreibung
     * @param cube
     */
    public DifferenceMatrix(Cover originalCover, Cube cube) {
        super(originalCover, cube);

        // Calculate the difference Cover between the considered cube and the cubes of the cover
        for (int i = 0; i < originalCover.size(); i++) {
            ThreeStateValue[] row = new ThreeStateValue[cube.getInputLength()];

            // Calculate the difference value for each variable value of the cube
            for (int j = 0; j < cube.getInputLength(); j++) {
                boolean element = generateDistanceElement(i, j);
                row[j] = ThreeStateValue.value(element);
            }

            // Save the difference Cubes in the difference Cover
            this.diffCover.addCube(new Cube(row, ThreeStateValue.one));
        }
    }

    /**
     * TODO: Prüfen, ob verwendet
     * @param originalCover
     * @param cube
     * @param containment
     */
    public DifferenceMatrix(Cover originalCover, Cube cube, boolean containment) {
        super(originalCover, cube);

        // Calculate the difference Cover between the considered cube and the cubes of the cover
        for (int i = 0; i < originalCover.size(); i++) {
            ThreeStateValue[] row = new ThreeStateValue[cube.getInputLength()];

            // Calculate the difference value for each variable value of the cube
            for (int j = 0; j < cube.getInputLength(); j++) {
                boolean element = generateContainmentElement(i, j);
                row[j] = ThreeStateValue.value(element);
            }

            // Save the difference Cubes in the difference Cover
            this.diffCover.addCube(new Cube(row, ThreeStateValue.one));
        }

    }

    /**
     * Calculate the distance of the variable value of the considered cube and the cover
     *
     * @param cubeIndex
     *            Index of the actual cube of the Onset
     * @param inputIndex
     *            Index of the actual variable of the cubes
     * @return Difference between the variable values
     */
    private boolean generateDistanceElement(int cubeIndex, int inputIndex) {
        ThreeStateValue coverInputState = getOriginalCover().getCube(cubeIndex).getState(inputIndex);
        ThreeStateValue cubeInputState = getCube().getState(inputIndex);

        return (cubeInputState == ThreeStateValue.one && coverInputState != ThreeStateValue.one)
                || (cubeInputState == ThreeStateValue.zero && coverInputState != ThreeStateValue.zero)
                || (cubeInputState == ThreeStateValue.dontCare && coverInputState != ThreeStateValue.dontCare);
    }

    private boolean generateContainmentElement(int cubeIndex, int inputIndex) {
        ThreeStateValue coverInputState = getOriginalCover().getCube(cubeIndex).getState(inputIndex);
        ThreeStateValue cubeInputState = getCube().getState(inputIndex);

        return (cubeInputState == ThreeStateValue.one && coverInputState == ThreeStateValue.zero)
                || (cubeInputState == ThreeStateValue.zero && coverInputState == ThreeStateValue.one)
                || (cubeInputState == ThreeStateValue.dontCare && coverInputState != ThreeStateValue.dontCare);
    }
}
