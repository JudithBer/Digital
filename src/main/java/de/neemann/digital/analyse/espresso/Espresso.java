package de.neemann.digital.analyse.espresso;

import java.util.List;

import de.neemann.digital.analyse.espresso.datastructure.BoolTableTSVArray;
import de.neemann.digital.analyse.espresso.datastructure.Cover;
import de.neemann.digital.analyse.espresso.datastructure.Cube;
import de.neemann.digital.analyse.expression.Variable;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;

/**
 * The Espresso minimizer. Based on the espresso algorithm of Robert K. Brayton (1984)
 * @author Annika Keil, Judith Berthold
 */
public final class Espresso {
    // TODO FUNCTION: implements MinimizerInterface (Digital)

    // Static instance variable
    private static Espresso instance = new Espresso();

    private Espresso() {
    };

    /**
     * @return Instance of espresso
     */
    public static Espresso getInstance() {
        return instance;
    }

    // Number of input variables of the function
    private int inputLength;

    /**
     * Minimizes the given function according to the espresso algorithm
     * @param vars
     *            List of all Variables of the function
     * @param boolTable
     *            Function to minimize - given as BoolTable
     * @throws Exception
     *             Throws Exception, if BoolTable or the Variable is null
     */
    public void minimize(List<Variable> vars, BoolTableTSVArray boolTable) throws Exception {
        // TODO weitere Übergabeparameter von Digital: String resultName, ExpressionListener
        // listener

        // Validate the input parameters
        if (vars == null || vars.size() == 0) {
            throw new Exception("Count of vars have to be initialized and greater than 0");
        }
        if (boolTable == null || boolTable.getBoolTable().size() == 0) {
            throw new Exception(
                    "BoolTable have to be initialized and the Arraylist need to be greater than 0");
        }

        this.inputLength = vars.size();

        // Generate a Cover with the oneSet(output value = 1) and one with the offSet (output value
        // = 0)
        Cover onSet = generateCover(ThreeStateValue.one, boolTable);
        Cover offSet = generateCover(ThreeStateValue.zero, boolTable);

        // TODO OUT: Testausgaben
        System.out.println("onSet-Cover:");
        System.out.println(onSet);

        System.out.println("offSet-Cover:");
        System.out.println(offSet);

        // Generate a Cover to work with and one for the best minimization at the moment
        Cover currentOnSet = new Cover(onSet);
        Cover latestMinSet = new Cover(onSet);

        // TODO FUNCTION: Schleife (nach Algorithmusablauf inkl. Überprüfung der Verbesserung)

        Cover expandedOnSet = Expand.expandCover(currentOnSet, offSet);

        // TODO FUNCTION: Irredundant, EssentialPrimes, Reduce, LastGasp

        // TODO FUNCTION: "Rückgabe"
    }

    /**
     * Generates a Cover out of the boolTable for the given state (zero --> offSet, one -->onSet,
     * dontcare --> dcset)
     * @param state
     *            Output value of the minterms to take
     * @param boolTable
     *            Function to minimize (in original format)
     * @return Cover with the taken minterms as cubes
     */
    private Cover generateCover(ThreeStateValue state, BoolTableTSVArray boolTable) {

        Cover newCover = new Cover(inputLength);
        List<ThreeStateValue> inputBoolTable = boolTable.getBoolTable();

        // Check for each minterm...
        for (int i = 0; i < inputBoolTable.size(); i++) {

            // .. if the output has the given state.
            if (inputBoolTable.get(i) == state) {

                // Temporary Array for the cube input of the currently working minterm
                ThreeStateValue[] tempCubeInput = new ThreeStateValue[inputLength];

                // Convert the index from Integer to String/CharArray
                char[] binary = Integer.toBinaryString(i).toCharArray();

                // Set the bits of the minterm as ThreeStateValue into the tempCubeInput
                int inputCount = inputLength - 1;
                for (int j = binary.length - 1; j >= 0; j--) {
                    tempCubeInput[inputCount] = ThreeStateValue
                            .value(Character.getNumericValue(binary[j]));
                    inputCount--;
                }

                // Fill up the previous empty fields with zeros
                for (; inputCount >= 0; inputCount--) {
                    tempCubeInput[inputCount] = ThreeStateValue.zero;
                }

                // Add the Cube of the minterm with the given state to the new Cover
                newCover.addCube(new Cube(tempCubeInput, state));
            }
        }

        return newCover;
    }

}
