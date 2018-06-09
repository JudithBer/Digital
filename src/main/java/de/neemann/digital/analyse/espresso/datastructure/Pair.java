/*
 * Copyright (c) 2016 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.analyse.espresso.datastructure;

/**
 * Structure element to group two attributes of different types
 * @param <A>
 *            TODO Beschreibung
 * @param <B>
 *            TODO Beschreibung
 */
public class Pair<A, B> implements Comparable<Pair<A, B>> {
    private A first;
    private B second;

    /**
     * @param first
     *            TODO Beschreibung
     * @param second
     *            TODO Beschreibung
     */
    public Pair(A first, B second) {
        super();
        this.first = first;
        this.second = second;
    }

    /**
     * @return TODO Beschreibung
     */
    public A getFirst() {
        return first;
    }

    /**
     * @param first
     *            TODO Beschreibung
     */
    public void setFirst(A first) {
        this.first = first;
    }

    /**
     * @return TODO Beschreibung
     */
    public B getSecond() {
        return second;
    }

    /**
     * @param second
     *            TODO Beschreibung
     */
    public void setSecond(B second) {
        this.second = second;
    }

    @Override
    public int compareTo(Pair<A, B> p) {
        return ((Comparable) p.second).compareTo(this.second);
    }
}
