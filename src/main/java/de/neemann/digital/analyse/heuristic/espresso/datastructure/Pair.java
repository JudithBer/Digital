/*
 * Copyright (c) 2016 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.analyse.heuristic.espresso.datastructure;

/**
 * Structure element to group two attributes of different types
 * @param <A> Generic type of the first item of the pair
 * @param <B> Generic type of the second item of the pair
 */
public class Pair<A, B> implements Comparable<Pair<A, B>> {
    private A first;
    private B second;

    /**
     * @param first First Item ob the pair
     * @param second Second  item of the pair
     */
    public Pair(A first, B second) {
        super();
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first item ob the pair.
     *
     * @return firts, first item of the pair
     */
    public A getFirst() {
        return first;
    }

    /**
     * Sets the first item of the pair
     *
     * @param first, first item of the pair
     */
    public void setFirst(A first) {
        this.first = first;
    }

    /**
     * Returns the second item of the pair.
     *
     * @return second, second item of the pair
     */
    public B getSecond() {
        return second;
    }

    /**
     * Sets the second item of the pair
     *
     * @param second, second item of the pair
     */
    public void setSecond(B second) {
        this.second = second;
    }

    @Override
    public int compareTo(Pair<A, B> p) {
        return ((Comparable) p.second).compareTo(this.second);
    }
}
