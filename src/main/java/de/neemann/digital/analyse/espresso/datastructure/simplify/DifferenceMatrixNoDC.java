package de.neemann.digital.analyse.espresso.datastructure.simplify;

import de.neemann.digital.analyse.espresso.datastructure.Cover;
import de.neemann.digital.analyse.espresso.datastructure.Cube;
import de.neemann.digital.analyse.espresso.exceptions.EmptyCoverException;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;

public class DifferenceMatrixNoDC extends DifferenceMatrix {

    /**
     * Generate a new Difference-Matrix with the given Cube and Cover
     *
     * @param originalCover Cover to compare with the given Cube to calculate the Difference-Matrix
     * @param cube          Cube to compare with a Cover to calculate the Difference-Matrix
     * @throws EmptyCoverException if the given Cover is empty
     */
    public DifferenceMatrixNoDC(Cover originalCover, Cube cube) throws EmptyCoverException {
        super(originalCover, cube);
    }

    @Override
    protected void calculateDifferenceCover(Cover originalCover, Cube cube) {
        int inputLength = cube.getInputLength();

        for (int cubeIndex = 0; cubeIndex < originalCover.size(); cubeIndex++) {

            Cube coverCube = originalCover.getCube(cubeIndex);

            ThreeStateValue[] row = new ThreeStateValue[inputLength];

            ThreeStateValue[] coverInputStates = coverCube.getInput();
            ThreeStateValue[] cubeInputStates = cube.getInput();


            // Calculate the difference value for each variable value of the cube
            for (int inputIndex = 0; inputIndex < inputLength; inputIndex++) {
                ThreeStateValue coverInputState = coverInputStates[inputIndex];
                ThreeStateValue cubeInputState = cubeInputStates[inputIndex];

                boolean diffElement = (cubeInputState == ThreeStateValue.one && coverInputState == ThreeStateValue.zero)
                        || (cubeInputState == ThreeStateValue.zero
                        && coverInputState == ThreeStateValue.one)
                        || (!(cubeInputState == ThreeStateValue.dontCare));

                row[inputIndex] = ThreeStateValue.value(diffElement);
            }

            Cube c = new Cube(ThreeStateValue.one);
            c.setInput(row);

            // Save the difference Cubes in the difference Cover
            this.differenceCover.addCube(c);
        }
    }
}
