package de.neemann.digital.analyse.heuristic.simplify.datastructure.differencematrix;

import de.neemann.digital.analyse.heuristic.datastructure.Cover;
import de.neemann.digital.analyse.heuristic.datastructure.Cube;
import de.neemann.digital.analyse.heuristic.exceptions.EmptyCoverException;

/**
 * Abstract Class for the different kinds of Difference Matrix
 * @author Judith Berthold, Annika Keil
 */
public abstract class DifferenceMatrix {

    protected Cover differenceCover;

    /**
     * Generate a new Difference-Matrix with the given Cube and Cover
     * @param originalCover
     *            Cover to compare with the given Cube to calculate the Difference-Matrix
     * @param cube
     *            Cube to compare with a Cover to calculate the Difference-Matrix
     * @throws EmptyCoverException
     *             if the given Cover is empty
     */
    public DifferenceMatrix(Cover originalCover, Cube cube) throws EmptyCoverException {

        if (originalCover.size() == 0) {
            throw new EmptyCoverException("Cover may not be empty.");
        }
        if (originalCover.getInputLength() != cube.getInputLength()) {
            throw new IllegalArgumentException(
                    "Cover and Cube need to have the same number of input variables.");
        }

        this.differenceCover = new Cover(cube.getInputLength());

        this.calculateDifferenceCover(originalCover, cube);
    }

    /**
     * Abstract Method to calculate a new Difference Cover
     * @param originalCover
     *            current Cover of which the Difference Matrix is calculated
     * @param cube
     *            Cube to compare the given Cover with
     */
    protected abstract void calculateDifferenceCover(Cover originalCover, Cube cube);

    /**
     * Returns the calculated difference cover
     * @return differenceCover the calculated Difference Cover
     */
    public Cover getDifferenceCover() {
        return differenceCover;
    }
}
