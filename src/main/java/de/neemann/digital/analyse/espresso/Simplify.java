package de.neemann.digital.analyse.espresso;

import de.neemann.digital.analyse.MinimizerInterface;
import de.neemann.digital.analyse.espresso.datastructure.*;
import de.neemann.digital.analyse.espresso.exceptions.EmptyCoverException;
import de.neemann.digital.analyse.expression.Expression;
import de.neemann.digital.analyse.expression.ExpressionException;
import de.neemann.digital.analyse.expression.Variable;
import de.neemann.digital.analyse.expression.format.FormatterException;
import de.neemann.digital.analyse.quinemc.BoolTable;
import de.neemann.digital.analyse.quinemc.BoolTableByteArray;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;
import de.neemann.digital.gui.components.table.ExpressionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static de.neemann.digital.analyse.expression.Not.not;
import static de.neemann.digital.analyse.expression.Operation.and;
import static de.neemann.digital.analyse.expression.Operation.or;

/**
 * The Simplify Algorithm to minimize logical functions.
 * @author Annika Keil, Judith Berthold
 *
 */
public class Simplify implements MinimizerInterface {

    /**
     * TODO rausnehmen, da nur für eigene Main
     * @param input

    public void minimizeCover(List<Variable> vars, String resultName,Cover input){
        Cover inputCover = input;
            System.out.println("Input Cover: \n" + inputCover);
        Cover offset = input.getCover(ThreeStateValue.zero, inputLength);
            System.out.println("Offset: \n" + cover);

        int binate = selectBinate(inputCover);
            System.out.println("Binate:" + binate + "\n");

        Cover cofactorBinate = generateCofactor(inputCover, ThreeStateValue.one, binate);
            System.out.println("Cofactor Binate: \n" + cofactorBinate);
        Cover cofactorAntiBinate = generateCofactor(inputCover, ThreeStateValue.zero, binate);
            System.out.println("Cofactor AntiBinate: \n" + cofactorAntiBinate);

        Cover simplifiedCofactorBinate = simplifyCofactor(cofactorBinate, offset);
            System.out.println("Simplified Cofactor: \n" + simplifiedCofactorBinate);
        Cover simplifiedCofactorAntiBinate = simplifyCofactor(cofactorAntiBinate, offset);
            System.out.println("Simplified AntiCofactor: \n" + simplifiedCofactorAntiBinate);

        Cover simplifiedCover = mergeWithContainment(
                simplifiedCofactorBinate,
                simplifiedCofactorAntiBinate,
                binate);
            System.out.println("END - Simplified Cover: \n" + simplifiedCover);

        Expression e = getExpression(vars, simplifiedCover);
        System.out.println("Expression: " + e); //FormatToExpression.FORMATTER_JAVA.format(e));
        //listener.resultFound(resultName, e);

    }
    */

    /**
     * TODO Beschreibung
     * @param vars       the variables used
     * @param boolTable  the bool table
     * @param resultName name of the result
     * @param listener   the listener to report the result to
     * @throws ExpressionException
     * @throws FormatterException
     */
    @Override
    public void minimize(List<Variable> vars, BoolTable boolTable, String resultName, ExpressionListener listener) throws ExpressionException, FormatterException {

        if (vars == null || vars.size() == 0) {
            throw new FormatterException("Count of vars have to be initialized and greater than 0");
        }
        if (boolTable == null || boolTable.size() == 0) {
            throw new FormatterException(
                    "BoolTable have to be initialized and the Arraylist need to be greater than 0");
        }

        // Reihenfolge der Variablen ändern
        //Collections.reverse(vars);

        int inputLength = vars.size();

        BoolTableTSVArray input = new BoolTableTSVArray((BoolTableByteArray) boolTable);
        //System.out.println(input);

        Cover cover = input.getCover(ThreeStateValue.one, inputLength);
            System.out.println("Cover: \n" + cover);
        Cover offset = input.getCover(ThreeStateValue.zero, inputLength);
            System.out.println("Offset: \n" + offset);

        int binate = selectBinate(cover);
            System.out.println("Binate: " + binate);

        Cover cofactorBinate = generateCofactor(cover, ThreeStateValue.one, binate);
            System.out.println("Cofactor Binate: \n" + cofactorBinate);
        Cover cofactorAntiBinate = generateCofactor(cover, ThreeStateValue.zero, binate);
            System.out.println("Cofactor AntiBinate: \n" + cofactorAntiBinate);

        Cover simplifiedCofactorBinate = simplifyCofactor(cofactorBinate, offset);
            System.out.println("Simplified Cofactor: \n" + simplifiedCofactorBinate);
        Cover simplifiedCofactorAntiBinate = simplifyCofactor(cofactorAntiBinate, offset);
            System.out.println("Simplified AntiCofactor: \n" + simplifiedCofactorAntiBinate);

        Cover simplifiedCover = mergeWithContainment(
                simplifiedCofactorBinate,
                simplifiedCofactorAntiBinate,
                binate);
            System.out.println("END - Simplified Cover: \n" + simplifiedCover);

        Expression e = getExpression(vars, simplifiedCover);
        System.out.println("Expression: " + e);//FormatToExpression.FORMATTER_JAVA.format(e));
        listener.resultFound(resultName, e);
    }

    /**
     * Method to select most Binate column (column with most DCs)
     * @param input Cover to search for Binate variable
     * @return Column of the Cover which ist most Binate
     */
    private int selectBinate(Cover input) {

        int inputLength = input.getInputLength();

        int[] zeros = new int[inputLength];
        int[] ones = new int[inputLength];
        int[] dcs = new int[inputLength];

        for(int i = 0; i < input.size(); i++) {

            Cube currentCube = input.getCube(i);
            for(int j = 0; j< inputLength; j++){

                switch (currentCube.getState(j)){
                    case zero:
                        zeros[j]++;
                        break;
                    case one:
                        ones[j]++;
                        break;
                    case dontCare:
                        dcs[j]++;
                        break;
                }
            }
        }

        int binate = 0;

        int minDCAt = -1;
        int minDCAmount = input.size();

        int minDiffAt = -1;
        int minDiffAmount = input.size();

        for (int l = 0; l < dcs.length; l++) {
            if(dcs[l] != 0 && dcs[l] < minDCAmount) {
                minDCAt = l;
                minDCAmount = dcs[l];
            } else if(minDCAt == -1
                    && zeros[l] != 0
                    && ones[l] != 0
                    && Math.abs(zeros[l]-ones[l]) < minDiffAmount
                    ) {
                minDiffAt = l;
                minDiffAmount = Math.abs(zeros[l]-ones[l]);
            }
        }

        if(minDCAt != -1) {
            binate = minDCAt;
        } else if (minDiffAt != -1) {
            binate = minDiffAt;
        }

        return binate;
    }

    /**
     * Method to generate the Cofactor of a Cover
     * @param input Input-Cover to generate the Cofacter of it
     * @param state ThreeStateValue.ZERO or ThreeStateValue.ONE (never ThreeStateValue.DC)
     * @param binate Column of the Cover which is most Binate
     * @return
     */
    private Cover generateCofactor(Cover input, ThreeStateValue state, int binate) {

        // TODO vereinfachen
        ThreeStateValue antiState = ThreeStateValue.zero;
        switch (state) {
            case zero:
                antiState = ThreeStateValue.one;
                break;
            case one:
                antiState = ThreeStateValue.zero;
                break;
        }
//        System.out.println("State: " + state);
//        System.out.println("Antistate: " + antiState);

        Cover cofactor = new Cover(input.getInputLength());

        for(int i = 0; i < input.size(); i++) {

            if(input.getCube(i).getState(binate) != antiState){

                Cube newCube =  new Cube(input.getCube(i));
                newCube.setState(binate, ThreeStateValue.dontCare);
                cofactor.addCube(newCube);
            }

        }

        return cofactor;
    }

    /**
     * Method to simplify the given Cofactor
     * @param cofactor which need to be simplified.
     * @param offset Offset of the original function
     * @return simplified Cofactor
     */
    private Cover simplifyCofactor(Cover cofactor, Cover offset) {

        Cover inputCofactor = cofactor; // zu vereinfachender Cofactor
        Cover tempCofactor; // temporares Cover während der Vereinfachung mit den teil-vereinfachten Cubes
        int inputCofactorSize; // anzahl der Input-Variablen

        do {
             inputCofactorSize = inputCofactor.size(); // Anzahl der Cubes im Cofactor

            // Alle Cubes durchlaufen und vereinfachen
            for (int i = 0; i < inputCofactor.size(); i++) {

                int countUse = 0; // Anzahl wie oft mit anderem Cube vereinfacht werden konnte
                tempCofactor = new Cover(cofactor.getInputLength()); // Initialisieren

                Cube currentCube = inputCofactor.getCube(0); // 0-ter da akteller am Ende immer hinten angehängt wird und danach mit dem nächsten, also wieder 0-ten weiter gemacht werden muss

                //DifferenceMatrix stellt die Unterschiede zwischen dem currentCube
                // und allen Cubes des Cofactor-Covers [One, wenn nicht die gleichen States]
                DifferenceMatrix differenceMatrix = null;
                try {
                    differenceMatrix = new DifferenceMatrix(inputCofactor, currentCube);
                } catch (EmptyCoverException e) {
                    // TODO inputCofactor leer -> was tun? wo prüfen?
                    e.printStackTrace();
                }

                // Mit allen Cubes vergleichen - Alle Cubes der zugehörigen Difference-Matrix durchlaufen
                for (int j = 0; j < differenceMatrix.getDiffCover().size(); j++) {

                    Cube currentDifferenceCube = differenceMatrix.getDiffCover().getCube(j); // Difference Matrix des aktuell betrachteten Cubes
                    int rowSum = rowSum(currentDifferenceCube); // Anzahl der Unterschiede zwischen Cube und Cofactor-Cube

                    // Wenn nur ein Unterschied -> Entsprechende Stelle DC setzen
                    if (rowSum == 1) {
                        Cube simplifiedCube = new Cube(inputCofactor.getCube(j));

                        //Index des Unterschiedes finden
                        Cube diffCube = differenceMatrix.getDiffCover().getCube(j);
                        int indexNewDC = Arrays.asList(diffCube.getInput()).indexOf(ThreeStateValue.one);

                        // Entsprechenden Index DC setzen
                        simplifiedCube.setState(indexNewDC , ThreeStateValue.dontCare);

                        // Geänderten Cube in tempCofactor
                        tempCofactor.addCube(simplifiedCube);
                        // Verwendung notieren
                        countUse++;

                    } else if (rowSum > 1){ // Wenn mehr als eine Position unterschiedlich

                        List<Integer> indexNewDC = new ArrayList<Integer>();
                        boolean expandable = true;

                        // Cube durchlaufen und alle Stellen mit Unterschied zwischen den Cubes betrachten
                        for(int k = 0; k < currentCube.getInputLength() && expandable; k++){

                            if(currentDifferenceCube.getState(k) == ThreeStateValue.one) {

                                if ((currentCube.getState(k) == ThreeStateValue.zero
                                            && inputCofactor.getCube(j).getState(k) == ThreeStateValue.one)
                                        || (currentCube.getState(k) == ThreeStateValue.one
                                            && inputCofactor.getCube(j).getState(k) == ThreeStateValue.zero)) {

                                        indexNewDC.add(k);
                                        countUse++;

                                        // TODO countUse überall richtig hochgesetzt?

                                } else if (inputCofactor.getCube(j).getState(k) != ThreeStateValue.dontCare) {
                                    // wir ändern nur uns, daher keine offset prüfung für diese Stelle notwendig - TODO Kommentar raus
                                    expandable = false;
                                }
                            }
                        }

                        if (indexNewDC.size() == 1 && expandable) {

                            tempCofactor.addCube(inputCofactor.getCube(j));

                            Cube modifiedCube = currentCube;
                            modifiedCube.setState(indexNewDC.get(0), ThreeStateValue.dontCare);

                            if(!checkOffset(offset, currentCube, indexNewDC.get(0))) {
                                tempCofactor.addCube(modifiedCube);
                            }

                        } else {
                            tempCofactor.addCube(inputCofactor.getCube(j));
                        }
                    }
                }

                if(countUse == 0) {
                    tempCofactor.addCube(currentCube);
                }

                inputCofactor = tempCofactor;
            }
            //System.out.println("Zwischenschritt Simplified Cofactor: \n" + inputCofactor);

        } while (inputCofactorSize != inputCofactor.size());

        return inputCofactor;
    }

    /**
     * TODO sonstige Kommentare
     * Checks if the cube is contained in the offset (in conflict with the offset)
     * @param offset
     * @param cube
     * @param index
     * @return
     */
    private boolean checkOffset(Cover offset, Cube cube, int index) {
        boolean containedInOffset = false;
        Cover differenceCover;

        //Prüfen, dass Anitstate nicht in Offset
        Cube antiCube = new Cube(cube);
        ThreeStateValue newState;
        if(cube.getState(index)== ThreeStateValue.one){
            newState = ThreeStateValue.zero;
        } else {
            newState = ThreeStateValue.one;
        }
        antiCube.setState(index, newState);

        try {
            differenceCover = new DifferenceMatrix(offset, cube, 0).getDiffCover();
        } catch (EmptyCoverException e) {
            // If the Offset is empty, the cube couldn't be contained
            return false;
        }

        for(int i = 0; i < differenceCover.size(); i++) {
            if(differenceCover.getCube(i).inputContains(ThreeStateValue.one)){
                continue;
            } else {
                containedInOffset = true;
            }
        }

        return containedInOffset;
    }

    /**
     * Calculates the Sum of the given Cube (every TSV.one increments the rowSum)
     * @param cube Cube of which the Sum should be calculated
     * @return Sum of the Cube
     */
    private int rowSum(Cube cube) {
        int rowSum = 0;
        for (int i = 0; i < cube.getInputLength(); i++) {
            if (cube.getState(i) == ThreeStateValue.one){
                rowSum++;
            }
        }
        return rowSum;
    }

    /**
     * TODO Beschreibung
     * @param simpleCofactor
     * @param simpleAntiCofactor
     * @param binate
     * @return
     */
    private Cover mergeWithContainment(Cover simpleCofactor, Cover simpleAntiCofactor, int binate){
        Cover result = new Cover(simpleCofactor.getInputLength());

        for (int i = 0; i < simpleCofactor.size(); i++) {
            Cube currentCube = new Cube (simpleCofactor.getCube(i));

            if(containedOppositeCofactor(simpleAntiCofactor, currentCube)) {
                currentCube.setState(binate, ThreeStateValue.dontCare);
            } else {
                currentCube.setState(binate, ThreeStateValue.one);
            }

            if(!result.contains(currentCube)){
                result.addCube(currentCube);
            }
        }

        for (int j = 0; j < simpleAntiCofactor.size(); j++) {
            Cube currentCube = new Cube(simpleAntiCofactor.getCube(j));

            if(containedOppositeCofactor(simpleCofactor, currentCube)) {
                currentCube.setState(binate, ThreeStateValue.dontCare);
            } else {
                currentCube.setState(binate, ThreeStateValue.zero);
            }

            if(!result.contains(currentCube)){
                result.addCube(currentCube);
            }
        }


        return result;
    }

    /**
     * Checks if the cube is covered by a cube of the other cover
     * @param cube              Cube for which the covering should be tested
     * @param oppositeCofactor  TODO
     * @return
     */
    private boolean containedOppositeCofactor(Cover oppositeCofactor, Cube cube) {
        DifferenceMatrix containMatrix = null;

        // Calculate if the cube could be contained by the cubes of the opposite CofactorCover
        // ZERO - no contradiction, ONE - contradition -> not possible to cover the cube 
        // If the opposite cofactor cover is empty, the cube is not covered by it
        try {
            containMatrix = new DifferenceMatrix(oppositeCofactor, cube, true);
        } catch (EmptyCoverException e) {
            return false;
        }
        Cover containCover = containMatrix.getDiffCover();

        // Check for each cube of the opposite cofactor cover (or more precisely, the difference cube)
        // if it contains the input cube
        for(int i = 0; i < containCover.size(); i++){

            // If the cube (containCube of the oppositeCofactor)contains no ONE - just ZEROs -
            // it could be contain the input cube
            if(!containCover.getCube(i).inputContains(ThreeStateValue.one)) {
                return true;
            }

            //TODO Prüfung in welchem Cube das DC an der gefundenen Stelle
        }

        // If the cube couldn't be covered by any cube of the opposite Cofactor
        return false;
    }

    /**
     * TODO Beschreibung
     * @param vars
     * @param simplifiedCover
     * @return
     */
    private Expression getExpression(List<Variable> vars, Cover simplifiedCover) {
        // TODO Prüfung ob Cover leer oder voll oder so s. QuineMcCluskey Constant.Zero/.One

        Cube currentCube;
        int inputLength = simplifiedCover.getInputLength();
        Expression expression = null;

        for (int i = 0; i < simplifiedCover.size(); i++) {
            currentCube = simplifiedCover.getCube(i);
            Expression cubeExpression = getTermExpression(vars, currentCube);

            if(cubeExpression == null) {
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
     *
     * @param vars
     * @param cube
     * @return
     */
    private Expression getTermExpression(List<Variable> vars, Cube cube) {
        Expression cubeExpression = null;

        for (int j = 0; j < cube.getInputLength(); j++) {
            Expression term = null;

            switch (cube.getState(j)){
                case dontCare:
                    break;
                case zero:
                    term = not(vars.get(j));
                    break;
                case one:
                    term = vars.get(j);
                    break;
            }

            if(term == null) {
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