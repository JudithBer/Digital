package de.neemann.digital.analyse.espresso.datastructure.simplify;

import de.neemann.digital.analyse.MinimizerInterface;
import de.neemann.digital.analyse.espresso.datastructure.BoolTableTSVArray;
import de.neemann.digital.analyse.espresso.datastructure.Cover;
import de.neemann.digital.analyse.espresso.datastructure.Cube;
import de.neemann.digital.analyse.espresso.exceptions.EmptyCoverException;
import de.neemann.digital.analyse.expression.Expression;
import de.neemann.digital.analyse.expression.ExpressionException;
import de.neemann.digital.analyse.expression.Variable;
import de.neemann.digital.analyse.expression.format.FormatterException;
import de.neemann.digital.analyse.quinemc.BoolTable;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;
import de.neemann.digital.gui.components.table.ExpressionListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.neemann.digital.analyse.expression.Not.not;
import static de.neemann.digital.analyse.expression.Operation.and;
import static de.neemann.digital.analyse.expression.Operation.or;

public class Simplify implements MinimizerInterface{

    private int inputLength;

    @Override
    public void minimize(List<Variable> vars, BoolTable boolTable, String resultName, ExpressionListener listener) throws ExpressionException, FormatterException {
        long start = System.currentTimeMillis();
        if (vars == null || vars.size() == 0) {
            throw new FormatterException("Count of vars has to be initialized and greater than 0");
        }
        if (boolTable == null || boolTable.size() == 0) {
            throw new FormatterException(
                    "BoolTable has to be initialized and the Arraylist need to be greater than 0");
        }

        inputLength = vars.size();

        System.out.println("vars:" + vars.size());

        BoolTableTSVArray input = new BoolTableTSVArray(boolTable);

        Cover cover = input.getCover(ThreeStateValue.one,  vars.size());

        Cover simplifiedCover = simplify(cover);

        Expression e = getExpression(vars, simplifiedCover, inputLength);

        long end = System.currentTimeMillis();

        System.out.println("Simplify - minimize - Took: " + (end - start));

        System.out.println("Expression: " + e + "\n");
        listener.resultFound(resultName, e);
    }

    private Cover simplify(Cover cover) {

        if(isUnate(cover)){
            Cover returnCover = unateSimplifiy(cover);
            return returnCover;
        }

        int binate = selectBinate(cover).y;

        Cover cofactor = generateCofactor(cover, ThreeStateValue.one, binate);
        Cover antiCofactor = generateCofactor(cover, ThreeStateValue.zero, binate);

        Cover simplifiedCover = mergeWithContainment(simplify(cofactor), simplify(antiCofactor), binate);

        if (cover.size() < simplifiedCover.size()){
            return simplifiedCover;
        }
        return cover;
    }

    private Cover unateSimplifiy(Cover cover) {
        Cover simplifiedCover = new Cover(inputLength);

        for (int i = 0; i < cover.size(); i++) {
            Cube cube = cover.getCube(i);
            if(!containedCover(cover, cube, i)) {
               simplifiedCover.addCube(cube);
            }
        }

        return simplifiedCover;
    }

    private boolean containedCover(Cover cover, Cube cube, int index) {
        DifferenceMatrix containMatrix = null;

        // Calculate if the cube could be contained by the cubes of the opposite CofactorCover
        // ZERO - no contradiction, ONE - contradiction -> not possible to cover the cube
        // If the opposite Cofactor cover is empty, the cube is not covered by it
        try {
            containMatrix = new DifferenceMatrixContainment(cover, cube);
        } catch (EmptyCoverException e) {
            return false;
        }
        Cover containCover = containMatrix.getDifferenceCover();

        // Check for each cube of the opposite cofactor cover (or more precisely, the difference
        // cube)
        // if it contains the input cube
        for (int i = 0; i < containCover.size(); i++) {

            if (i == index) {
                break;
            }
            // If the cube (containCube of the oppositeCofactor)contains no ONE - just ZEROs -
            // it could be contain the input cube
            if (!containCover.getCube(i).inputContains(ThreeStateValue.one)) {
                return true;
            }

            // TODO Prüfung in welchem Cube das DC an der gefundenen Stelle
        }

        // If the cube couldn't be covered by any cube of the opposite Cofactor
        return false;
    }

    /**
     * TODO BEschreibung
     * @param cover
     * @return
     */
    private boolean isUnate(Cover cover) {
        Tuple<int[], int[]> amounts = calculateAmounts(cover);

        List<Integer> minimums = new ArrayList<Integer>(inputLength);
        for (int i = 0; i < inputLength; i++) {
            if (amounts.x[i] < amounts.y[i]){
//                minimums.set(i, amounts.x[i]);
                minimums.add(i, amounts.x[i]);

            } else {
//                minimums.set(i, amounts.y[i]);
                minimums.add(i, amounts.y[i]);
            }
        }

        int max = Collections.max(minimums);
        if(max == 0) {
            return true;
        }
        return false;
    }

    /**
     * TODO BEschreibung
     * @param cover
     * @return
     */
    private Tuple<int[], int[]> calculateAmounts(Cover cover) {
        int inputLength = cover.getInputLength();
        int[] zeros = new int[inputLength];
        int[] ones = new int[inputLength];

        for (int i = 0; i < cover.size(); i++) {

            ThreeStateValue[] currentCubeInputs = cover.getCube(i).getInput();
            for (int j = 0; j < inputLength; j++) {
                switch (currentCubeInputs[j]) {
                    case zero:
                        zeros[j]++;
                        break;
                    case one:
                        ones[j]++;
                        break;
                    default:
                        break;
                }
            }
        }

        return new Tuple<>(zeros, ones);
    }

    /**
     * TODO BEschreibung
     * @param cover
     * @return
     */
    private Tuple<Boolean, Integer> selectBinate(Cover cover) {

        Tuple<int[], int[]> amounts = calculateAmounts(cover);

        // Nochmal unate-Prüfung notwendig?
        if(isUnate(cover)) {
            return new Tuple(true, 0);
        }

        List<Integer> binateVariables = new ArrayList<Integer>();
        for(int i = 0; i < inputLength; i++){
            if(amounts.x[i] != 0 && amounts.y[i] != 0){
                binateVariables.add(i);
            }
        }

        int binate = binateVariables.get(0);
        int binateMax = amounts.x[binate] + amounts.y[binate];

        for (int j: binateVariables) {
            int currentSum = amounts.x[j] + amounts.y[j];
            if (binateMax < currentSum){
                binate = j;
                binateMax = currentSum;
            }
        }

        return new Tuple<>(false, binate);
    }

    /**
     * Method to generate the Cofactor of a Cover
     * @param input
     *            Input-Cover to generate the Cofacter of it
     * @param state
     *            ThreeStateValue.ZERO or ThreeStateValue.ONE (never ThreeStateValue.DC)
     * @param binate
     *            Column of the Cover which is most Binate
     * @return Cofactor of the input Cover
     */
    private Cover generateCofactor(Cover input, ThreeStateValue state, int binate) {

        ThreeStateValue antiState = state.invert();

        Cover cofactor = new Cover(inputLength);

        for (int i = 0; i < input.size(); i++) {

            Cube inputCube = input.getCube(i);
            if (inputCube.getState(binate) != antiState) {

                Cube newCube = new Cube(inputCube);
                newCube.setState(binate, ThreeStateValue.dontCare);
                cofactor.addCube(newCube);
            }
        }

        return cofactor;
    }

    /**
     * TODO Beschreibung
     * @param simpleCofactor
     *            simplified Cofactor of the function
     * @param simpleAntiCofactor
     *            simplified Anti-Cofactor of the function
     * @param binate
     *            Column which is most Binate
     * @return Cover where Cofactor and Anti-Cofactor is merged
     */
    private Cover mergeWithContainment(Cover simpleCofactor, Cover simpleAntiCofactor, int binate) {
        Cover result = new Cover(inputLength);

        for (int i = 0; i < simpleCofactor.size(); i++) {
            Cube currentCube = new Cube(simpleCofactor.getCube(i));

            if (containedOppositeCofactor(simpleAntiCofactor, currentCube)) {
                currentCube.setState(binate, ThreeStateValue.dontCare);
            } else {
                currentCube.setState(binate, ThreeStateValue.one);
            }

            if (!result.contains(currentCube)) {
                result.addCube(currentCube);
            }
        }

        for (int j = 0; j < simpleAntiCofactor.size(); j++) {
            Cube currentCube = new Cube(simpleAntiCofactor.getCube(j));

            if (containedOppositeCofactor(simpleCofactor, currentCube)) {
                currentCube.setState(binate, ThreeStateValue.dontCare);
            } else {
                currentCube.setState(binate, ThreeStateValue.zero);
            }

            if (!result.contains(currentCube)) {
                result.addCube(currentCube);
            }
        }

        return result;
    }

    /**
     * Checks if the cube is covered by a cube of the other cover
     * @param cube
     *            Cube for which the covering should be tested
     * @param oppositeCofactor
     *            Cofactor which is the opposite of the current Cofactor (Cofactor <->
     *            Anti-Cofactor)
     * @return
     */
    private boolean containedOppositeCofactor(Cover oppositeCofactor, Cube cube) {
        DifferenceMatrix containMatrix = null;

        // Calculate if the cube could be contained by the cubes of the opposite CofactorCover
        // ZERO - no contradiction, ONE - contradiction -> not possible to cover the cube
        // If the opposite Cofactor cover is empty, the cube is not covered by it
        try {
            containMatrix = new DifferenceMatrixContainment(oppositeCofactor, cube);
        } catch (EmptyCoverException e) {
            return false;
        }
        Cover containCover = containMatrix.getDifferenceCover();

        // Check for each cube of the opposite cofactor cover (or more precisely, the difference
        // cube)
        // if it contains the input cube
        for (int i = 0; i < containCover.size(); i++) {

            // If the cube (containCube of the oppositeCofactor)contains no ONE - just ZEROs -
            // it could be contain the input cube
            if (!containCover.getCube(i).inputContains(ThreeStateValue.one)) {
                return true;
            }

            // TODO Prüfung in welchem Cube das DC an der gefundenen Stelle
        }

        // If the cube couldn't be covered by any cube of the opposite Cofactor
        return false;
    }

    /**
     * Transforms the simplified Cover into an Expression
     * @param vars
     *            List of all given variables
     * @param simplifiedCover
     *            The fully simplified Cover
     * @return Expression of the simplified function
     */
    private Expression getExpression(List<Variable> vars, Cover simplifiedCover, int inputLength) {
        // TODO Prüfung ob Cover leer oder voll oder so s. QuineMcCluskey Constant.Zero/.One

        Cube currentCube;
        Expression expression = null;

        for (int i = 0; i < simplifiedCover.size(); i++) {
            currentCube = simplifiedCover.getCube(i);
            Expression cubeExpression = getTermExpression(vars, currentCube, inputLength);

            if (cubeExpression == null) {
                continue;
            }

            if (expression == null)
                expression = cubeExpression;
            else
                expression = or(expression, cubeExpression);
        }

        return expression;
    }

    /**
     * Building the Expression of one cube
     * @param vars
     *            List of all variables from the booltable
     * @param cube
     *            Cube to convert into Expression
     * @return Expression of one cube
     */
    private Expression getTermExpression(List<Variable> vars, Cube cube, int inputLength) {
        Expression cubeExpression = null;
        ThreeStateValue[] cubeInputs = cube.getInput();

        for (int j = 0; j < inputLength; j++) {
            Expression term = null;

            switch (cubeInputs[j]) {
                case dontCare:
                    break;
                case zero:
                    term = not(vars.get(j));
                    break;
                case one:
                    term = vars.get(j);
                    break;
            }

            if (term == null) {
                continue;
            } else if (cubeExpression == null) {
                cubeExpression = term;
            } else {
                cubeExpression = and(cubeExpression, term);
            }
        }

        return cubeExpression;
    }

}
