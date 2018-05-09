package de.neemann.digital.analyse.espresso.datastructure;

import java.util.ArrayList;
import java.util.List;

import de.neemann.digital.analyse.quinemc.BoolTable;
import de.neemann.digital.analyse.quinemc.ThreeStateValue;

/**
 * TODO: Beschreibung der Klasse für JavaDoc
 * @author judith
 */
public class BoolTableTSVArray implements BoolTable {
    private List<ThreeStateValue> boolTable = new ArrayList<>();

    /**
     * @param boolTable
     *            TODO Beschreibung
     */
    public BoolTableTSVArray(List<ThreeStateValue> boolTable) {
        this.boolTable = boolTable;
    }

    /**
     * @return TODO Beschreibung
     */
    public List<ThreeStateValue> getBoolTable() {
        return boolTable;
    }

    /**
     * @return TODO Beschreibung
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
     * @return TODO Beschreibung
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
     * @return TODO Beschreibung
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

    public Cover getCover(ThreeStateValue state, int inputLength) {

        Cover newCover = new Cover(inputLength);

        // Check for each minterm...
        for (int i = 0; i < boolTable.size(); i++) {

            // .. if the output has the given state.
            if (boolTable.get(i) == state) {

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
