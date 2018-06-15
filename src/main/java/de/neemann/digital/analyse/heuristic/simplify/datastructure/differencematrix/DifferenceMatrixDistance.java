package de.neemann.digital.analyse.heuristic.simplify.datastructure.differencematrix;

import de.neemann.digital.analyse.heuristic.datastructure.Cover;
import de.neemann.digital.analyse.heuristic.datastructure.Cube;
import de.neemann.digital.analyse.heuristic.exceptions.EmptyCoverException;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;

public class DifferenceMatrixDistance extends DifferenceMatrix {

    /**
     * Generate a new Difference-Matrix with the given Cube and Cover
     *
     * @param originalCover Cover to compare with the given Cube to calculate the Difference-Matrix
     * @param cube          Cube to compare with a Cover to calculate the Difference-Matrix
     * @throws EmptyCoverException if the given Cover is empty
     */
    public DifferenceMatrixDistance(Cover originalCover, Cube cube) throws EmptyCoverException {
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

                row[inputIndex] = ThreeStateValue.value(coverInputState != cubeInputState);
            }

            Cube c = new Cube(ThreeStateValue.one);
            c.setInput(row);

            // Save the difference Cubes in the difference Cover
            this.differenceCover.addCube(c);
        }
    }
}
