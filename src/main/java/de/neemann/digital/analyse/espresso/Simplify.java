package de.neemann.digital.analyse.espresso;

import de.neemann.digital.analyse.MinimizerInterface;
import de.neemann.digital.analyse.espresso.datastructure.*;
import de.neemann.digital.analyse.expression.ExpressionException;
import de.neemann.digital.analyse.expression.Variable;
import de.neemann.digital.analyse.expression.format.FormatterException;
import de.neemann.digital.analyse.quinemc.BoolTable;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;
import de.neemann.digital.gui.components.table.ExpressionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simplify implements MinimizerInterface {


    public void minimizeCover(Cover input){
        Cover inputCover = input;
            System.out.println("Input Cover: \n" + inputCover);

        int binate = selectBinate(inputCover);
            System.out.println("Binate:" + binate + "\n");

        Cover cofactorBinate = generateCofactor(inputCover, ThreeStateValue.one, binate);
//            System.out.println("Cofactor Binate: \n" + cofactorBinate);
        Cover cofactorAntiBinate = generateCofactor(inputCover, ThreeStateValue.zero, binate);
            System.out.println("Cofactor AntiBinate: \n" + cofactorAntiBinate);

        Cover simplifiedCofactorBinate = simplifyCofactor(cofactorBinate);
//            System.out.println("Simplified Cofactor: \n" + simplifiedCofactorBinate);
        Cover simplifiedCofactorAntiBinate = simplifyCofactor(cofactorAntiBinate);
            System.out.println("Simplified AntiCofactor: \n" + simplifiedCofactorAntiBinate);

        Cover simplifiedCover = mergeWithContainment(
                simplifiedCofactorBinate,
                simplifiedCofactorAntiBinate,
                binate);
            System.out.println("END - Simplified Cover: \n" + simplifiedCover);

    }

    @Override
    public void minimize(List<Variable> vars, BoolTable boolTable, String resultName, ExpressionListener listener) throws ExpressionException, FormatterException {

        int inputLength = vars.size();

        BoolTableTSVArray input = (BoolTableTSVArray) boolTable;
        Cover cover = input.getCover(ThreeStateValue.one, inputLength);

        int binate = selectBinate(cover);

        Cover cofactorBinate = generateCofactor(cover, ThreeStateValue.one, binate);
        Cover cofactorAntiBinate = generateCofactor(cover, ThreeStateValue.zero, binate);

        Cover simplifiedCofactorBinate = simplifyCofactor(cofactorBinate);
        Cover simplifiedCofactorAntiBinate = simplifyCofactor(cofactorAntiBinate);


    }

    /**
     * TODO Beschreibung
     * @param input
     * @return
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

        int minAt = -1;
        int min = input.size();

        for (int l = 0; l < dcs.length; l++) {
            if(dcs[l] < min) {
                minAt = l;
                min = dcs[l];
            }
        }

        if(minAt != -1) {
            binate = minAt;
        }

        // TODO: Gleichgewicht der 0er und 1er prüfen

        return binate;
    }

    /**
     *
     * @param input
     * @param state ThreeStateValue.ZERO or ThreeStateValue.ONE (never ThreeStateValue.DC)
     * @param binate
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
        System.out.println("State: " + state);
        System.out.println("Antistate: " + antiState);

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
     * TODO: Beschreibung
     * @param cofactor
     * @return
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
     * TODO Beschreibung
     * @param cube
     * @return
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
            if(!Arrays.asList(diffCover.getCube(i)).contains(ThreeStateValue.one)) {
                return true;
            }
        }

        return false;
    }
}
