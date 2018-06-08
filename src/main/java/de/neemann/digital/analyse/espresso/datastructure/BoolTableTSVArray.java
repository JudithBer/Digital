package de.neemann.digital.analyse.espresso.datastructure;

import java.util.ArrayList;
import java.util.List;

import de.neemann.digital.analyse.quinemc.BoolTable;
import de.neemann.digital.analyse.quinemc.BoolTableByteArray;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;

/**
 * Booltable with ThreeStateValues as Inputs
 * @author Annika Keil, Judith Berthold
 */
public class BoolTableTSVArray implements BoolTable {
    private List<ThreeStateValue> boolTable = new ArrayList<>();

    public BoolTableTSVArray(BoolTable boolTableInput){
        for(int i = 0; i < boolTableInput.size(); i++) {
            this.boolTable.add(boolTableInput.get(i));
        }
    }

    /**
     * Contructor of the Booltable
     * @param boolTable
     *            A Booltable with TSV as Inputs
     */
//    public BoolTableTSVArray(List<ThreeStateValue> boolTable) {
//        this.boolTable = boolTable;
//    }

    /**
     * Copy-Contructor of the BoolTable
     * @param boolTable
     *            given Booltable to generate a Copy
     */
//    public BoolTableTSVArray(BoolTableByteArray boolTable) {
//        List<ThreeStateValue> tempBoolTable = new ArrayList<>();
//        for (int i = 0; i < boolTable.size(); i++) {
//            tempBoolTable.add(boolTable.get(i));
//        }
//        this.boolTable = tempBoolTable;
//    }

    /**
     * Get the current Booltable
     * @return the given Booltable
     */
    public List<ThreeStateValue> getBoolTable() {
        return boolTable;
    }

    /**
     * @return Count of all TSV.one in the Booltable
     */
    public int getQuantityOne() {
        int sum = 0;
        for (ThreeStateValue state : boolTable) {
            if (state.asInt() == 1) {
                sum++;
            }
        }
        return sum;
    }

    /**
     * @return Count of all TSV.zero in the Booltable
     */
    public int getQuantityZero() {
        int sum = 0;
        for (ThreeStateValue state : boolTable) {
            if (state.asInt() < 1) {
                sum++;
            }
        }
        return sum;
    }

    /**
     * @return Count of all TSV.dontCare in the Booltable
     */
    public int getQuantityDC() {
        int sum = 0;
        for (ThreeStateValue state : boolTable) {
            if (state.asInt() > 1) {
                sum++;
            }
        }
        return sum;
    }

    @Override
    public int size() {
        return boolTable.size();
    }

    @Override
    public ThreeStateValue get(int i) {
        return boolTable.get(i);
    }

    /**
     * Generates the Cover out of the given Booltable
     * @param state
     *            defines whether the On-Set, Off-Set or DC-Set needs to be generated
     * @param inputLength
     *            defines the Length of the Cover
     * @return the requested Cover (On-Set, Off-Set, DC-Set)
     */
    public Cover getCover(ThreeStateValue state, int inputLength) {

        Cover newCover = new Cover(inputLength);

        // Check for each minterm...
        for (int i = 0; i < boolTable.size(); i++) {

            // .. if the output has the given state.
            if (boolTable.get(i) == state) {

                // Temporary Array for the Cube input of the currently working minterm
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

    @Override
    public String toString() {
        String result = "";
        for (ThreeStateValue state : boolTable) {
            result += state;
        }
        return result;
    }

}
