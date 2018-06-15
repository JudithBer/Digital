package de.neemann.digital.analyse.espresso.datastructure.diff;

import de.neemann.digital.analyse.espresso.datastructure.BooleanMatrix;
import de.neemann.digital.analyse.espresso.datastructure.Cover;
import de.neemann.digital.analyse.espresso.datastructure.Cube;
import de.neemann.digital.analyse.espresso.exceptions.EmptyCoverException;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;

import java.util.ArrayList;
import java.util.List;

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
    public DifferenceMatrix(Cover originalCover, Cube cube, IDifferenceMatrixMode mode)
            throws EmptyCoverException {
        super(originalCover, cube);

        long start = System.nanoTime();

        int inputLength = cube.getInputLength();

        /*
        // Calculate the difference Cover between the considered cube and the cubes of the cover
        for (int i = 0; i < originalCover.size(); i++) {

            // Calculate the difference value for each variable value of the cube
            for (int j = 0; j < inputLength; j++) {
                row[j] = ThreeStateValue.value(mode.getElement(getOriginalCover(), getCube(), i, j));
            }

            // Save the difference Cubes in the difference Cover
            this.getDiffCover().addCube(new Cube(row, ThreeStateValue.one));
        }*/

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
            this.getDiffCover().addCube(c);
        }

        long end = System.nanoTime();
        System.out.println("DifferenceMarix Constructor Outer: " + (end - start));
        System.out.println("DifferenceMatrix-Länge" + getDiffCover().size());
    }
}
