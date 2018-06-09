/*
 * Copyright (c) 2016 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */

package de.neemann.digital.analyse.espresso;

import java.util.*;

import de.neemann.digital.analyse.espresso.exceptions.EmptyCoverException;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;
import de.neemann.digital.analyse.espresso.datastructure.*;

/**
 * Expand Operator to expand the cover.
 * @author Annika Keil, Judith Berthold
 */
public final class Expand {

    private Expand() {

    }

    /**
     * Expand the given function
     * @param onSet
     *            OnSet of the function to minimize - cubes to minimize
     * @param offSet
     *            OffSet of the function to minimize - cubes that can't use to minimize
     * @return Expanded Cover
     */
    public static Cover expandCover(Cover onSet, Cover offSet) {
        // Generate Cover to return expanded implicants
        Cover retCover = new Cover(onSet.getInputLength());

        // Sort the cubes of the given OnSet by their weight
        Cover sortedCover = new Cover(onSet);
        sortedCover.sort(true);

        // TODO OUT: Testausgaben
        System.out.println("Sorted-Cover (onSet):");
        System.out.println(sortedCover);

        // TODO FUNCTION: Schleife über alle Cubes (in sortierter Reihenfolge)
        for (int i = 0; i < sortedCover.size(); i++) {
            // Calculate the expansion of the cube and the covered Cubes
            Pair<Cube, List<Integer>> pair = cubeExpand(sortedCover.getCube(i), sortedCover,
                    offSet);
            // TODO FUNCTION: Neuen Cube übernehmen, überdeckte Cubes löschen
        }

        // TODO FUNCTION: Erweiterung durch Schneiden
        // TODO FUNCTION: Erweiterung mit DCs

        return retCover;
    }

    /**
     * Calculate the possible expansions for the given cube
     * @param cube
     *            Considered Cube to expand
     * @param onSet
     *            Defines all Cubes with TSV ONE
     * @param offSet
     *            Defines all Cubes TSV ZERO
     * @return A Pair with the expanded Cube and a List of the indexes of the covered Cubes
     */
    public static Pair<Cube, List<Integer>> cubeExpand(Cube cube, Cover onSet, Cover offSet) {
        BlockMatrix blockMatrix = null;
        try {
            blockMatrix = new BlockMatrix(offSet, cube);
        } catch (EmptyCoverException e) {
            // TODO
            e.printStackTrace();
        }
        CoverMatrix coverMatrix = null;
        try {
            coverMatrix = new CoverMatrix(onSet, cube);
        } catch (EmptyCoverException e) {
            // TODO
            e.printStackTrace();
        }
        System.out.println("Blocking Matrix: \n" + blockMatrix);

        Set<Integer> loweringSet = new HashSet<>();
        Set<Integer> raisingSet = new HashSet<>();

        loweringSet.addAll(essentialColumns(blockMatrix));
        // System.out.println("Lowering set: " + loweringSet);

        elim1(blockMatrix, loweringSet);
        // System.out.println("Blocking Matrix - Ignored Columns: " +
        // blockMatrix.getIgnoredColumns());
        // System.out.println("Blocking Matrix - Ignored Rows: " + blockMatrix.getIgnoredRows());
        // System.out.println("Blocking Matrix (nach ELIM1): ");
        // for (Iterator<Integer> rowIterator = blockMatrix.ignoredRowsIterator();
        // rowIterator.hasNext(); ) {
        // int row = rowIterator.next();
        // System.out.println(blockMatrix.getDiffCover().getCube(row));
        // }

        elim1(coverMatrix, loweringSet);
        // System.out.println("Cover Matrix: \n" + coverMatrix);
        // System.out.println("Cover Matrix - Ignored Columns: " + coverMatrix.getIgnoredColumns());
        // System.out.println("Cover Matrix - Ignored Rows: " + coverMatrix.getIgnoredRows());
        // System.out.println("Cover Matrix (nach ELIM1): ");
        // for (Iterator<Integer> rowIterator = coverMatrix.ignoredRowsIterator();
        // rowIterator.hasNext(); ) {
        // int row = rowIterator.next();
        // System.out.println(coverMatrix.getDiffCover().getCube(row));
        // }

        // TODO FUNCTION: Expansion eines einzelnen Cubes

        List<Integer> containedRows = new ArrayList<>(); // TODO FUNCTION: coveredCubes

        return new Pair<>(new Cube(5), containedRows);
    }

    /**
     * @param blockMatrix
     *            Difference between actual Cube and Off-Set
     * @return Columns which cannot be expanded
     */
    private static Set<Integer> essentialColumns(BlockMatrix blockMatrix) {
        Set<Integer> essentials = new HashSet<>();

        for (Iterator<Integer> rowIterator = blockMatrix.ignoredRowsIterator(); rowIterator
                .hasNext();) {
            int row = rowIterator.next();

            int rowSum = 0;
            int oneIndex = 0;

            for (Iterator<Integer> columnIterator = blockMatrix
                    .ignoredColumnsIterator(); columnIterator.hasNext();) {
                int col = columnIterator.next();

                // Es wird die Zeilensumme berechnet
                // (Wenn das Element der BlockingMatrix an der Stelle eine Eins hat, addiere sie zur
                // Zeilensumme SONST (0||2) nicht)
                if ((blockMatrix.getElement(row, col) == ThreeStateValue.one)) {
                    rowSum += 1;
                    oneIndex = col;
                }
            }
            // Wenn eine Zeile nur eine 1 hat,
            // wird der Spaltenindex dieser 1 dem Ergebnissarray (und damit dem Lowering Set)
            // hinzugefügt
            if (rowSum == 1) {
                essentials.add(oneIndex);
            }
        }

        return essentials;
    }

    /**
     * @param matrix
     *            A Boolean Matrix which describes the difference between a cube and the On-/Off-Set
     * @param loweringSet
     *            All columns of a cube which cannot be expanded.
     */
    public static void elim1(BooleanMatrix matrix, Set<Integer> loweringSet) {
        for (Integer column : loweringSet) {
            if (matrix.isIgnoredColumn(column)) {
                continue;
            }

            matrix.addIgnoredColumn(column);

            for (Iterator<Integer> rowsIterator = matrix.ignoredRowsIterator(); rowsIterator
                    .hasNext();) {
                int row = rowsIterator.next();

                if (matrix.getElement(row, column) == ThreeStateValue.one) {
                    matrix.addIgnoredRow(row);
                }
            }
        }
    }
}
