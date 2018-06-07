package de.neemann.digital.analyse.espresso.exceptions;

/**
 * Created Exception which will be thrown if a Cover is empty
 * @author Annika Keil, Judith Berthold
 */
public class EmptyCoverException extends Exception {

    /**
     * Constructor of the Exception
     * @param msg
     *            Messages to print out if Exception is thrown
     */
    public EmptyCoverException(String msg) {
        super(msg);
    }
}
