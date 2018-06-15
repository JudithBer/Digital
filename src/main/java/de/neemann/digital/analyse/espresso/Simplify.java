/*
 * Copyright (c) 2016 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */

package de.neemann.digital.analyse.espresso;

import static de.neemann.digital.analyse.expression.Not.not;
import static de.neemann.digital.analyse.expression.Operation.and;
import static de.neemann.digital.analyse.expression.Operation.or;

import java.util.*;

import de.neemann.digital.analyse.MinimizerInterface;
import de.neemann.digital.analyse.espresso.datastructure.BoolTableTSVArray;
import de.neemann.digital.analyse.espresso.datastructure.Cover;
import de.neemann.digital.analyse.espresso.datastructure.Cube;
import de.neemann.digital.analyse.espresso.datastructure.simplify.DifferenceMatrix;
import de.neemann.digital.analyse.espresso.datastructure.simplify.DifferenceMatrixContainment;
import de.neemann.digital.analyse.espresso.datastructure.simplify.DifferenceMatrixDistance;
import de.neemann.digital.analyse.espresso.exceptions.EmptyCoverException;
import de.neemann.digital.analyse.expression.Expression;
import de.neemann.digital.analyse.expression.ExpressionException;
import de.neemann.digital.analyse.expression.Variable;
import de.neemann.digital.analyse.expression.format.FormatterException;
import de.neemann.digital.analyse.quinemc.BoolTable;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;
import de.neemann.digital.gui.components.table.ExpressionListener;

/**
 * The Simplify Algorithm to minimize logical functions.
 * @author Annika Keil, Judith Berthold
 */
public class Simplify /*implements MinimizerInterface*/ {
//
//    /**
//     * Minimize the given function with the Simplify-Algorithm
//     * and answer the given listener with the minimized expression of the function
//     * @param vars
//     *            the variables used
//     * @param boolTable
//     *            the Booltable of the function
//     * @param resultName
//     *            name of the result
//     * @param listener
//     *            the listener to report the result to
//     * @throws ExpressionException
//     * @throws FormatterException
//     */
//    @Override
//    public void minimize(List<Variable> vars, BoolTable boolTable, String resultName,
//            ExpressionListener listener) throws ExpressionException, FormatterException {
//
//        long start = System.currentTimeMillis();
//
//        if (vars == null || vars.size() == 0) {
//            throw new FormatterException("Count of vars has to be initialized and greater than 0");
//        }
//        if (boolTable == null || boolTable.size() == 0) {
//            throw new FormatterException(
//                    "BoolTable has to be initialized and the Arraylist need to be greater than 0");
//        }
//
//        System.out.println("vars:" + vars.size());
//
//        int inputLength = vars.size();
//
//        BoolTableTSVArray input = new BoolTableTSVArray(boolTable);
//
//        Cover cover = input.getCover(ThreeStateValue.one, inputLength);
//
////        Cover offset = input.getCover(ThreeStateValue.zero, inputLength);
//        int binate = selectBinate(cover, inputLength);
//
//        //inputLength mitgeben, damit es nicht mehr neu berechnet werden muss jedes mal
//        Cover cofactorBinate = generateCofactor(cover, ThreeStateValue.one, binate, inputLength);
//        Cover cofactorAntiBinate = generateCofactor(cover, ThreeStateValue.zero, binate, inputLength);
//
//        Cover simplifiedCofactorBinate = simplifyCofactor(cofactorBinate/*, offset*/, binate, inputLength);
//        Cover simplifiedCofactorAntiBinate = simplifyCofactor(cofactorAntiBinate/*, offset*/, binate, inputLength);
//
//        Cover simplifiedCover = mergeWithContainment(simplifiedCofactorBinate,
//                simplifiedCofactorAntiBinate, binate, inputLength);
//
//        Expression e = getExpression(vars, simplifiedCover, inputLength);
//
//        long end = System.currentTimeMillis();
//
//        System.out.println("Simplify - minimize - Took: " + (end - start));
//
//        System.out.println("Expression: " + e + "\n");
//        // FormatToExpression.FORMATTER_JAVA.format(e));
//        listener.resultFound(resultName, e);
//    }
//
//    /**
//     * Method to select most Binate column (column with most DCs or with balanced TSV.zero and
//     * TSV.one)
//     * @param input
//     *            Cover to search for Binate variable
//     * @return Column of the Cover which ist most Binate
//     */
//    private int selectBinate(Cover input, int inputLength) {
//        int[] zeros = new int[inputLength];
//        int[] ones = new int[inputLength];
//        int[] dcs = new int[inputLength];
//
//        for (int i = 0; i < input.size(); i++) {
//
//            Cube currentCube = input.getCube(i);
//            ThreeStateValue[] currentCubeInputs = currentCube.getInput();
//            for (int j = 0; j < inputLength; j++) {
//                switch (currentCubeInputs[j]) {
//                    case zero:
//                        zeros[j]++;
//                        break;
//                    case one:
//                        ones[j]++;
//                        break;
//                    case dontCare:
//                        dcs[j]++;
//                        break;
//                }
//            }
//        }
//
//        int binate = 0;
//
//        int minDCAt = -1;
//        int minDCAmount = input.size();
//
//        int minDiffAt = -1;
//        int minDiffAmount = input.size();
//
//        for (int l = 0; l < dcs.length; l++) {
//            if (dcs[l] != 0 && dcs[l] < minDCAmount) {
//                minDCAt = l;
//                minDCAmount = dcs[l];
//            } else if (minDCAt == -1 && zeros[l] != 0 && ones[l] != 0
//                    && Math.abs(zeros[l] - ones[l]) < minDiffAmount) {
//                minDiffAt = l;
//                minDiffAmount = Math.abs(zeros[l] - ones[l]);
//            }
//        }
//
//        if (minDCAt != -1) {
//            binate = minDCAt;
//        } else if (minDiffAt != -1) {
//            binate = minDiffAt;
//        }
//
//        return binate;
//    }
//
//    /**
//     * Method to generate the Cofactor of a Cover
//     * @param input
//     *            Input-Cover to generate the Cofacter of it
//     * @param state
//     *            ThreeStateValue.ZERO or ThreeStateValue.ONE (never ThreeStateValue.DC)
//     * @param binate
//     *            Column of the Cover which is most Binate
//     * @return Cofactor of the input Cover
//     */
//    private Cover generateCofactor(Cover input, ThreeStateValue state, int binate, int inputLength) {
//
//        // TODO vereinfachen
//        ThreeStateValue antiState = state.invert();
////        switch (state) {
////        case zero:
////            antiState = ThreeStateValue.one.invert();
////            break;
////        case one:
////            antiState = ThreeStateValue.zero;
////            break;
////        }
//        // System.out.println("State: " + state);
//        // System.out.println("Antistate: " + antiState);
//
//        Cover cofactor = new Cover(inputLength);
//
//        for (int i = 0; i < input.size(); i++) {
//
//            Cube inputCube = input.getCube(i);
//            if (inputCube.getState(binate) != antiState) {
//
//                Cube newCube = new Cube(inputCube);
//                newCube.setState(binate, ThreeStateValue.dontCare);
//                cofactor.addCube(newCube);
//            }
//
//        }
//
//        return cofactor;
//    }
//
//    /**
//     * Method to simplify the given Cofactor
//     * @param cofactor
//     *            which need to be simplified.
//     * @return simplified Cofactor
//     */
//    private Cover simplifyCofactor(Cover cofactor, /*Cover offset,*/ int binate, int inputLength) {
//
//        Cover inputCofactor = cofactor; // zu vereinfachender Cofactor
//        Cover tempCofactor; // temporares Cover während der Vereinfachung mit den teil-vereinfachten
//                            // Cubes
//        int inputCofactorSize; // anzahl der Input-Variablen
//
//        do {
//            inputCofactorSize = inputCofactor.size(); // Anzahl der Cubes im Cofactor
//
//            // Alle Cubes durchlaufen und vereinfachen
//            for (int i = 0; i < inputCofactor.size(); i++) {
//
//                int doppelteCubes=0;
//
//                int countUse = 0; // Anzahl wie oft mit anderem Cube vereinfacht werden konnte
//                tempCofactor = new Cover(inputLength); // Initialisieren
//
//                Map<String, Cube> containment = new HashMap<String, Cube>();
//
//                Cube currentCube = inputCofactor.getCube(0); // 0-ter da akteller am Ende immer
//                                                             // hinten angehängt wird und danach mit
//                                                             // dem nächsten, also wieder 0-ten
//                                                             // weiter gemacht werden muss
//
//                // DifferenceMatrix stellt die Unterschiede zwischen dem currentCube
//                // und allen Cubes des Cofactor-Covers [One, wenn nicht die gleichen States]
//                DifferenceMatrix differenceMatrix = null;
//                try {
//                    differenceMatrix = new DifferenceMatrixDistance(inputCofactor, currentCube);
//                } catch (EmptyCoverException e) {
//                    // TODO inputCofactor leer -> was tun? wo prüfen?
//                    e.printStackTrace();
//                }
//                Cover differenceCover = differenceMatrix.getDifferenceCover();
//
//                // Mit allen Cubes vergleichen - Alle Cubes der zugehörigen Difference-Matrix
//                // durchlaufen
//                for (int j = 0; j < differenceCover.size(); j++) {
//
//                 // Difference Matrix des aktuell betrachteten Cubes
//                    Cube currentDifferenceCube = differenceCover.getCube(j);
//                    int rowSum = rowSumGreater1(currentDifferenceCube, inputLength);
//
//                    // Wenn nur ein Unterschied -> Entsprechende Stelle DC setzen
//                    if (rowSum == 1) {
//                        Cube simplifiedCube = new Cube(inputCofactor.getCube(j));
//
//                        // Index des Unterschiedes finden
//                        Cube diffCube = differenceCover.getCube(j);
//                        int indexNewDC = Arrays.asList(diffCube.getInput())
//                                .indexOf(ThreeStateValue.one);
//
//                        // Entsprechenden Index DC setzen
//                        simplifiedCube.setState(indexNewDC, ThreeStateValue.dontCare);
//
//                        // Geänderten Cube in tempCofactor
//                        tempCofactor.addCube(simplifiedCube);
//                        containment.put(simplifiedCube.toString(), simplifiedCube);
//
//                        // Verwendung notieren
//                        countUse++;
//
//                    } else if (rowSum > 1) { // Wenn mehr als eine Position unterschiedlich
//
//                        List<Integer> indexNewDC = new ArrayList<Integer>();
//                        boolean expandable = true;
//
//                        ThreeStateValue[] currentDifferenceCubeInputs = currentDifferenceCube.getInput();
//                        ThreeStateValue[] currentCubeInputs = currentCube.getInput();
//                        ThreeStateValue[] inputCofactorCubeInputs = inputCofactor.getCube(j).getInput();
//
//                        // Cube durchlaufen und alle Stellen mit Unterschied zwischen den Cubes
//                        // betrachten
//                        for (int k = 0; k < inputLength && expandable; k++) {
//
//                            if (currentDifferenceCubeInputs[k] == ThreeStateValue.one) {
//
//                                if ((currentCubeInputs[k] == ThreeStateValue.zero
//                                        && inputCofactorCubeInputs[k] == ThreeStateValue.one)
//                                        || (currentCubeInputs[k] == ThreeStateValue.one
//                                                && inputCofactorCubeInputs[k] == ThreeStateValue.zero)) {
//
//                                    indexNewDC.add(k);
//                                    //falls indexNewDc bereits größer 1, dann kann die for-Schleife abgebrochen werden
//                                    if (indexNewDC.size()>1) {
//                                        break;
//                                    }
//                                    // countUse++;
//
//                                    // TODO countUse überall richtig hochgesetzt?
//
//                                } else if (currentCubeInputs[k] == ThreeStateValue.dontCare
//                                        || inputCofactorCubeInputs[k] != ThreeStateValue.dontCare) {
//                                    // wir ändern nur uns, daher keine offset prüfung für diese
//                                    // Stelle notwendig
//                                    expandable = false;
//                                }
//                            }
//                        }
//
//                        Cube comparedCube = inputCofactor.getCube(j);
//                        if (indexNewDC.size() == 1 && expandable) {
//                            tempCofactor.addCube(comparedCube);
//                            containment.put(comparedCube.toString(), comparedCube);
//
////                            if (!checkOffset(offset, currentCube, indexNewDC.get(0), currentCube.getState(indexNewDC.get(0)) )) {
//                                Cube modifiedCube = new Cube(currentCube);
//                                modifiedCube.setState(indexNewDC.get(0), ThreeStateValue.dontCare);
//
//                                if (containment.containsKey(modifiedCube.toString())) {
//                                    tempCofactor.addCube(modifiedCube);
//                                    containment.put(modifiedCube.toString(), modifiedCube);
//
//                                    countUse++;
//                                    doppelteCubes++;
//                                }
////                            }
//
//                        } else {
//                            tempCofactor.addCube(comparedCube);
//                            containment.put(comparedCube.toString(), comparedCube);
//                        }
//                    }
//                }
//
//                if (countUse == 0) {
//                    tempCofactor.addCube(currentCube);
//                    containment.put(currentCube.toString(), currentCube);
//                }
//                inputCofactor = tempCofactor;
//            }
//        } while (inputCofactorSize != inputCofactor.size());
//
//        return inputCofactor;
//    }
//
//    /**
//     * Checks if the cube is contained in the offset (in conflict with the offset)
//     * @param offset
//     *            Set of all Cubes with output 0
//     * @param cube
//     *            Cube to check whether it covers an Offset-Cube
//     * @param index
//     *            Index of the ThreeStateValue we need to check
//     * @return Boolean whether cube is covered by offset
//     */
////    private boolean checkOffset(Cover offset, Cube cube, int index, ThreeStateValue currentState) {
////        boolean containedInOffset = false;
////        Cover differenceCover;
////
////        // Prüfen, dass Anitstate nicht in Offset
////        Cube antiCube = new Cube(cube);
////        ThreeStateValue newState;
////        if (currentState == ThreeStateValue.one) {
////            newState = ThreeStateValue.zero;
////        } else {
////            newState = ThreeStateValue.one;
////        }
////        antiCube.setState(index, newState);
////
////        try {
////            differenceCover = new DifferenceMatrix(offset, antiCube, "NoDcElement").getDiffCover();
////        } catch (EmptyCoverException e) {
////            // If the Offset is empty, the cube cannot be contained
////            return false;
////        }
////
////        for (int i = 0; i < differenceCover.size(); i++) {
////            if (differenceCover.getCube(i).inputContains(ThreeStateValue.one)) {
////                continue;
////            } else {
////                containedInOffset = true;
////            }
////        }
////
////        return containedInOffset;
////    }
//
//    /**
//     * Calculates the Sum of the given Cube (every TSV.one increments the rowSum)
//     * @param cube
//     *            Cube of which the Sum should be calculated
//     * @return Sum of the Cube
//     */
////    private int rowSum(Cube cube, int inputLength) {
////        int rowSum = 0;
////        for (int i = 0; i < inputLength; i++) {
////            if (cube.getState(i) == ThreeStateValue.one) {
////                rowSum++;
////            }
////        }
////        return rowSum;
////    }
//
//    private int rowSumGreater1(Cube cube, int inputLength){
//        int rowSum = 0;
//        ThreeStateValue[] states = cube.getInput();
//        for (int i = 0; i < inputLength; i++) {
//            if (states[i] == ThreeStateValue.one) {
//                rowSum++;
//            }
//            if (rowSum >1) {
//                return rowSum;
//            }
//        }
//        return rowSum;
//    }
//
//    /**
//     * TODO Beschreibung
//     * @param simpleCofactor
//     *            simplified Cofactor of the function
//     * @param simpleAntiCofactor
//     *            simplified Anti-Cofactor of the function
//     * @param binate
//     *            Column which is most Binate
//     * @return Cover where Cofactor and Anti-Cofactor is merged
//     */
//    private Cover mergeWithContainment(Cover simpleCofactor, Cover simpleAntiCofactor, int binate, int inputLength) {
//        Cover result = new Cover(inputLength);
//
//        for (int i = 0; i < simpleCofactor.size(); i++) {
//            Cube currentCube = new Cube(simpleCofactor.getCube(i));
//
//            if (containedOppositeCofactor(simpleAntiCofactor, currentCube)) {
//                currentCube.setState(binate, ThreeStateValue.dontCare);
//            } else {
//                currentCube.setState(binate, ThreeStateValue.one);
//            }
//
//            if (!result.contains(currentCube)) {
//                result.addCube(currentCube);
//            }
//        }
//
//        for (int j = 0; j < simpleAntiCofactor.size(); j++) {
//            Cube currentCube = new Cube(simpleAntiCofactor.getCube(j));
//
//            if (containedOppositeCofactor(simpleCofactor, currentCube)) {
//                currentCube.setState(binate, ThreeStateValue.dontCare);
//            } else {
//                currentCube.setState(binate, ThreeStateValue.zero);
//            }
//
//            if (!result.contains(currentCube)) {
//                result.addCube(currentCube);
//            }
//        }
//
//        return result;
//    }
//
//    /**
//     * Checks if the cube is covered by a cube of the other cover
//     * @param cube
//     *            Cube for which the covering should be tested
//     * @param oppositeCofactor
//     *            Cofactor which is the opposite of the current Cofactor (Cofactor <->
//     *            Anti-Cofactor)
//     * @return
//     */
//    private boolean containedOppositeCofactor(Cover oppositeCofactor, Cube cube) {
//        DifferenceMatrix containMatrix = null;
//
//        // Calculate if the cube could be contained by the cubes of the opposite CofactorCover
//        // ZERO - no contradiction, ONE - contradiction -> not possible to cover the cube
//        // If the opposite Cofactor cover is empty, the cube is not covered by it
//        try {
//            containMatrix = new DifferenceMatrixContainment(oppositeCofactor, cube);
//        } catch (EmptyCoverException e) {
//            return false;
//        }
//        Cover containCover = containMatrix.getDifferenceCover();
//
//        // Check for each cube of the opposite cofactor cover (or more precisely, the difference
//        // cube)
//        // if it contains the input cube
//        for (int i = 0; i < containCover.size(); i++) {
//
//            // If the cube (containCube of the oppositeCofactor)contains no ONE - just ZEROs -
//            // it could be contain the input cube
//            if (!containCover.getCube(i).inputContains(ThreeStateValue.one)) {
//                return true;
//            }
//
//            // TODO Prüfung in welchem Cube das DC an der gefundenen Stelle
//        }
//
//        // If the cube couldn't be covered by any cube of the opposite Cofactor
//        return false;
//    }
//
//    /**
//     * Transforms the simplified Cover into an Expression
//     * @param vars
//     *            List of all given variables
//     * @param simplifiedCover
//     *            The fully simplified Cover
//     * @return Expression of the simplified function
//     */
//    private Expression getExpression(List<Variable> vars, Cover simplifiedCover, int inputLength) {
//        // TODO Prüfung ob Cover leer oder voll oder so s. QuineMcCluskey Constant.Zero/.One
//
//        Cube currentCube;
//        Expression expression = null;
//
//        for (int i = 0; i < simplifiedCover.size(); i++) {
//            currentCube = simplifiedCover.getCube(i);
//            Expression cubeExpression = getTermExpression(vars, currentCube, inputLength);
//
//            if (cubeExpression == null) {
//                continue;
//            }
//
//            if (expression == null)
//                expression = cubeExpression;
//            else
//                expression = or(expression, cubeExpression);
//        }
//
//        return expression;
//    }
//
//    /**
//     * Building the Expression of one cube
//     * @param vars
//     *            List of all variables from the booltable
//     * @param cube
//     *            Cube to convert into Expression
//     * @return Expression of one cube
//     */
//    private Expression getTermExpression(List<Variable> vars, Cube cube, int inputLength) {
//        Expression cubeExpression = null;
//        ThreeStateValue[] cubeInputs = cube.getInput();
//
//        for (int j = 0; j < inputLength; j++) {
//            Expression term = null;
//
//            switch (cubeInputs[j]) {
//            case dontCare:
//                break;
//            case zero:
//                term = not(vars.get(j));
//                break;
//            case one:
//                term = vars.get(j);
//                break;
//            }
//
//            if (term == null) {
//                continue;
//            } else if (cubeExpression == null) {
//                cubeExpression = term;
//            } else {
//                cubeExpression = and(cubeExpression, term);
//            }
//        }
//
//        return cubeExpression;
//    }
//
}
