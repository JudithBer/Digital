package de.neemann.digital.analyse.espresso.datastructure;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.neemann.digital.analyse.quinemc.ThreeStateValue;

/**
 * BooleanMatrix is the abstraction for the BlockMatrix and CoverMatrix
 * @author Annika Keil, Judith Berthold
 */
public class BooleanMatrix {

    protected Cover diffCover;
    private Cube cube;
    private Cover originalCover;

    private Set<Integer> ignoredColumns = new HashSet<>();
    private Set<Integer> ignoredRows = new HashSet<>();

    /**
     * Generate a new BooleanMatrix with the given Cube and Cover
     * @param originalCover
     *            TODO Beschreibung
     * @param cube
     *            TODO Beschreibung
     */
    public BooleanMatrix(Cover originalCover, Cube cube) {
        // Validate the input parameters
        if (originalCover.size() == 0) {
            throw new IllegalArgumentException("Cover may not be empty.");
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
     * @return TODO Beschreibung
     */
    public Iterator<Integer> ignoredColumnsIterator() {
        return new IgnoredIndexIterator(getColumnCount(), ignoredColumns);
    }

    /**
     * @return TODO Beschreibung
     */
    public Iterator<Integer> ignoredRowsIterator() {
        return new IgnoredIndexIterator(getRowCount(), ignoredRows);
    }

    /**
     * @return TODO Beschreibung
     */
    private int getColumnCount() {
        return diffCover.getInputLength();
    }

    /**
     * @return TODO Beschreibung
     */
    private int getRowCount() {
        return diffCover.size();
    }

    /**
     * @param index
     *            TODO Beschreibung
     */
    public void addIgnoredColumn(Integer index) {
        ignoredColumns.add(index);
    }

    /**
     * @param index
     *            TODO Beschreibung
     */
    public void addIgnoredRow(Integer index) {
        ignoredRows.add(index);
    }

    // Getter of the BooleanMatrix attributes

    /**
     * @return TODO Beschreibung
     */
    public Cover getDiffCover() {
        return diffCover;
    }

    /**
     * @return TODO Beschreibung
     */
    public Cube getCube() {
        return cube;
    }

    /**
     * @return TODO Beschreibung
     */
    public Cover getOriginalCover() {
        return originalCover;
    }

    // TODO: Nur für Testausgaben -> RAUS
    /**
     * @return TODO Beschreibung
     */
    public Set<Integer> getIgnoredColumns() {
        return ignoredColumns;
    }

    // TODO: Nur für Testausgaben -> RAUS
    /**
     * @return TODO Beschreibung
     */
    public Set<Integer> getIgnoredRows() {
        return ignoredRows;
    }

    /**
     * TODO Beschreibung
     * @param row
     *            TODO Beschreibung
     * @param column
     *            TODO Beschreibung
     * @return TODO Beschreibung
     */
    public ThreeStateValue getElement(int row, int column) {
        return diffCover.getCube(row).getState(column);
    }

    /**
     * @param index
     *            TODO Beschreibung
     * @return TODO Beschreibung
     */
    public boolean isIgnoredColumn(Integer index) {
        if (ignoredColumns.contains(index)) {
            return true;
        }
        return false;
    }

    /**
     * @param index
     *            TODO Beschreibung
     * @return TODO Beschreibung
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
