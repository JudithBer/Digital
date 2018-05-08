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

}
