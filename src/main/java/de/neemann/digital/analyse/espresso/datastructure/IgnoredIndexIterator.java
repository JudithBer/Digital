package de.neemann.digital.analyse.espresso.datastructure;

import java.util.Iterator;
import java.util.Set;

/**
 * TODO: JavaDoc Kommentar für Klasse
 * @author judith
 */
public class IgnoredIndexIterator implements Iterator {

    private int currentIndex = -1;
    private Set<Integer> ignoredIndexes;
    private int upperLimit;

    /**
     * @param upperLimit
     *            TODO: Beschreibung
     * @param ignoredIndexes
     *            TODO: Beschreibung
     */
    public IgnoredIndexIterator(int upperLimit, Set<Integer> ignoredIndexes) {
        this.upperLimit = upperLimit;
        this.ignoredIndexes = ignoredIndexes;
    }

    @Override
    public boolean hasNext() {
        for (int i = currentIndex + 1; i < upperLimit; i++) {
            if (!ignoredIndexes.contains(i)) {
                currentIndex = i;
                return true;
            }
        }
        return false;
    }

    @Override
    public Object next() {
        return currentIndex;
    }
}
