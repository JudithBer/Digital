/*
 * Copyright (c) 2016 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.analyse.espresso.datastructure;

import java.util.Iterator;
import java.util.Set;

/**
 * Iterator to ignore special Indexes for the Espresso algorithm
 * @author Annika Keil, Judith Berthold
 */
public class IgnoredIndexIterator implements Iterator {

    private int currentIndex = -1;
    private Set<Integer> ignoredIndexes;
    private int upperLimit;

    /**
     * Constructor for the Iterator
     * @param upperLimit
     *            Limit for the Iteration
     * @param ignoredIndexes
     *            Set of the Indexes to be ignored
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
