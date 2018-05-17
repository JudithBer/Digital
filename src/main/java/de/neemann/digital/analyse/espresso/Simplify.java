package de.neemann.digital.analyse.espresso;

import de.neemann.digital.analyse.MinimizerInterface;
import de.neemann.digital.analyse.espresso.datastructure.*;
import de.neemann.digital.analyse.expression.ExpressionException;
import de.neemann.digital.analyse.expression.Variable;
import de.neemann.digital.analyse.expression.format.FormatterException;
import de.neemann.digital.analyse.quinemc.BoolTable;
import de.neemann.digital.analyse.quinemc.BoolTableByteArray;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;
import de.neemann.digital.gui.components.table.ExpressionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Simplify Algorithm to minimize logical functions.
 * @author Annika Keil, Judith Berthold
 *
 */
public class Simplify implements MinimizerInterface {

    /**
     * TODO rausnehmen, da nur für eigene Main
     * @param input
     */
    public void minimizeCover(Cover input){
        Cover inputCover = input;
            System.out.println("Input Cover: \n" + inputCover);

        int binate = selectBinate(inputCover);
            System.out.println("Binate:" + binate + "\n");

        Cover cofactorBinate = generateCofactor(inputCover, ThreeStateValue.one, binate);
            System.out.println("Cofactor Binate: \n" + cofactorBinate);
        Cover cofactorAntiBinate = generateCofactor(inputCover, ThreeStateValue.zero, binate);
            System.out.println("Cofactor AntiBinate: \n" + cofactorAntiBinate);

        Cover simplifiedCofactorBinate = simplifyCofactor(cofactorBinate);
            System.out.println("Simplified Cofactor: \n" + simplifiedCofactorBinate);
        Cover simplifiedCofactorAntiBinate = simplifyCofactor(cofactorAntiBinate);
            System.out.println("Simplified AntiCofactor: \n" + simplifiedCofactorAntiBinate);

        Cover simplifiedCover = mergeWithContainment(
                simplifiedCofactorBinate,
                simplifiedCofactorAntiBinate,
                binate);
            System.out.println("END - Simplified Cover: \n" + simplifiedCover);

    }

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

        int inputLength = vars.size();



        BoolTableTSVArray input = new BoolTableTSVArray((BoolTableByteArray) boolTable);
        //System.out.println(input);

        Cover cover = input.getCover(ThreeStateValue.one, inputLength);
            System.out.println("Cover: \n" + cover);

        int binate = selectBinate(cover);
            System.out.println("Binate: " + binate);

        Cover cofactorBinate = generateCofactor(cover, ThreeStateValue.one, binate);
            System.out.println("Cofactor Binate: \n" + cofactorBinate);
        Cover cofactorAntiBinate = generateCofactor(cover, ThreeStateValue.zero, binate);
            System.out.println("Cofactor AntiBinate: \n" + cofactorAntiBinate);

        Cover simplifiedCofactorBinate = simplifyCofactor(cofactorBinate);
            System.out.println("Simplified Cofactor: \n" + simplifiedCofactorBinate);
        Cover simplifiedCofactorAntiBinate = simplifyCofactor(cofactorAntiBinate);
            System.out.println("Simplified AntiCofactor: \n" + simplifiedCofactorAntiBinate);

        Cover simplifiedCover = mergeWithContainment(
                simplifiedCofactorBinate,
                simplifiedCofactorAntiBinate,
                binate);
            System.out.println("END - Simplified Cover: \n" + simplifiedCover);

        //TODO Listener eine Antwort geben

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

        // TODO: Gleichgewicht der 0er und 1er prüfen

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
     * @return simplified Cofactor
     */
    private Cover simplifyCofactor(Cover cofactor) {
        Cover inputCofactor = cofactor;
        Cover tempCofactor;
        int inputCofactorSize;

        do {
             inputCofactorSize = inputCofactor.size();

            // Alle Cubes durchlaufen
            for (int i = 0; i < inputCofactor.size(); i++) {



                int countUse = 0;
                tempCofactor = new Cover(cofactor.getInputLength());

                Cube currentCube = inputCofactor.getCube(0); // 0-ter da akteller am Ende immer hinten angehängt wird und danach mit dem nächsten, also wieder 0-ten weiter gemacht werden muss
                DifferenceMatrix differenceMatrix = new DifferenceMatrix(inputCofactor, currentCube);

                // Mit allen Cubes vergleichen - Alle Cubes der zugehörigen Difference-Matrix durchlaufen
                for (int j = 0; j < differenceMatrix.getDiffCover().size(); j++) {

                    Cube currentDifferenceCube = differenceMatrix.getDiffCover().getCube(j);
                    int rowSum = rowSum(currentDifferenceCube);

                    if (rowSum == 1) {
                        Cube simplifiedCube = new Cube(inputCofactor.getCube(j));

                        Cube diffCube = differenceMatrix.getDiffCover().getCube(j);
                        int indexNewDC = Arrays.asList(diffCube.getInput()).indexOf(ThreeStateValue.one);

                        simplifiedCube.setState(indexNewDC , ThreeStateValue.dontCare);
                        tempCofactor.addCube(simplifiedCube);
                        countUse++;

                    } else if (rowSum > 1){

                        // TODO prüfen

                        List<Integer> indexNewDC = new ArrayList<Integer>();
                        boolean expandable = true;

                        for(int k = 0; k < currentCube.getInputLength() && expandable; k++){

                            // Cube durchlaufen und alle Stellen mit Unterschied zwischen den Cubes betrachten
                            if(currentDifferenceCube.getState(k) == ThreeStateValue.one) {

                                if ((currentCube.getState(k) == ThreeStateValue.zero
                                            && inputCofactor.getCube(j).getState(k) == ThreeStateValue.one)
                                        || (currentCube.getState(k) == ThreeStateValue.one
                                            && inputCofactor.getCube(j).getState(k) == ThreeStateValue.zero)) {
                                    indexNewDC.add(k);
                                } else if (inputCofactor.getCube(j).getState(k) != ThreeStateValue.dontCare) {
                                    expandable = false;
                                }
                            }
                        }

                        if (indexNewDC.size() == 1 && expandable) {

                            tempCofactor.addCube(inputCofactor.getCube(j));

                            Cube modifiedCube = currentCube;
                            modifiedCube.setState(indexNewDC.get(0), ThreeStateValue.dontCare);

                            tempCofactor.addCube(modifiedCube);

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
     * TODO Beschreibugn
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

            result.addCube(currentCube);
        }

        for (int j = 0; j < simpleAntiCofactor.size(); j++) {
            Cube currentCube = new Cube(simpleAntiCofactor.getCube(j));

            if(containedOppositeCofactor(simpleCofactor, currentCube)) {
                currentCube.setState(binate, ThreeStateValue.dontCare);
            } else {
                currentCube.setState(binate, ThreeStateValue.zero);
            }

            result.addCube(currentCube);
        }


        return result;
    }

    /**
     * TODO Beschreibung
     * @param cube
     * @param oppositeCofactor
     * @return
     */
    private boolean containedOppositeCofactor(Cover oppositeCofactor, Cube cube) {
        Cover diffCover =  new DifferenceMatrix(oppositeCofactor, cube, true).getDiffCover();

        for(int i = 0; i < diffCover.size(); i++){
            if(!diffCover.getCube(i).inputContains(ThreeStateValue.one)) {
                return true;
            }
        }

        return false;
    }
}