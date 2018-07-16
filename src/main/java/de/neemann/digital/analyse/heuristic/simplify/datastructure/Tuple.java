package de.neemann.digital.analyse.heuristic.simplify.datastructure;

/**
 * Structure element to group two attributes of different types
 * @author Judith Berthold, Annika Keil
 * @param <X>
 *            Generic type of the first item of the tuple
 * @param <Y>
 *            Generic type of the second item of the tuple
 */
public class Tuple<X, Y> {
    public final X x;
    public final Y y;

    /**
     * Constructor for the Tuple
     * @param x
     *            First Item of the tuple
     * @param y
     *            Second item of the tuple
     */
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
}
