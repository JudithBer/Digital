package de.neemann.digital.analyse.espresso.datastructure;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.neemann.digital.analyse.espresso.exceptions.EmptyCoverException;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;

/**
 * BooleanMatrix is the abstraction for the BlockMatrix and CoverMatrix
 * @author Annika Keil, Judith Berthold
 */
public class BooleanMatrix {

    private Cover diffCover;
    private Cube cube;
    private Cover originalCover;

    private Set<Integer> ignoredColumns = new HashSet<>();
    private Set<Integer> ignoredRows = new HashSet<>();

    /**
     * Generate a new BooleanMatrix with the given Cube and Cover
     * @param originalCover
     *            Cover to calculate the Boolean Matrix of
     * @param cube
     *            Cube which is needed to compare the Cover with
     * @throws EmptyCoverException
     *             if the given Cover is empty
     */
    public BooleanMatrix(Cover originalCover, Cube cube) throws EmptyCoverException {
        // Validate the input parameters
        if (originalCover.size() == 0) {
            throw new EmptyCoverException("Cover may not be empty.");
        }
        if (originalCover.getInputLength() != cube.getInputLength()) {
            throw new IllegalArgumentException(
                    "Cover and Cube need to have the same number of input variables.");
        }

        this.cube = cube;
        this.originalCover = originalCover;
        this.diffCover = new Cover(originalCover.getInputLength());
    }

    /**
     * @return all ignored Columns
     */
    public Iterator<Integer> ignoredColumnsIterator() {
        return new IgnoredIndexIterator(getColumnCount(), ignoredColumns);
    }

    /**
     * @return all ignored Rows
     */
    public Iterator<Integer> ignoredRowsIterator() {
        return new IgnoredIndexIterator(getRowCount(), ignoredRows);
    }

    /**
     * @return Length of the diffCover (Count of the Columns)
     */
    private int getColumnCount() {
        return diffCover.getInputLength();
    }

    /**
     * @return Size of the diffCover (Count of the Rows)
     */
    private int getRowCount() {
        return diffCover.size();
    }

    /**
     * @param index
     *            Column to be ignored
     */
    public void addIgnoredColumn(Integer index) {
        ignoredColumns.add(index);
    }

    /**
     * @param index
     *            Row to be ignored
     */
    public void addIgnoredRow(Integer index) {
        ignoredRows.add(index);
    }

    // Getter of the BooleanMatrix attributes

    /**
     * @return the current DiffCover
     */
    public Cover getDiffCover() {
        return diffCover;
    }

    /**
     * @return the current Cube
     */
    public Cube getCube() {
        return cube;
    }

    /**
     * @return get the original Cover of the Matrix
     */
    public Cover getOriginalCover() {
        return originalCover;
    }

    /**
     * Get a special Element on the position (row, column)
     * @param row
     *            Row of the requested Element
     * @param column
     *            Column of the requested Element
     * @return the TSV of the requested Element
     */
    public ThreeStateValue getElement(int row, int column) {
        return diffCover.getCube(row).getState(column);
    }

    /**
     * Checks whether a Column is ignored
     * @param index
     *            Index of the column
     * @return boolean whether the column is ignored or not
     */
    public boolean isIgnoredColumn(Integer index) {
        if (ignoredColumns.contains(index)) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether a Row is ignored
     * @param index
     *            Index of the Row
     * @return boolean whether the row is ignored or not
     */
    public boolean isIgnoredRow(Integer index) {
        if (ignoredRows.contains(index)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return diffCover.toString();
    }

}
