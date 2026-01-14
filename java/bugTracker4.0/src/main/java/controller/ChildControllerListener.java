package controller;

/**
 * Interface for pages communication, specifically for printing errors
 *
 * @author Artur Twardzik
 * @version 0.2
 */
public interface ChildControllerListener {
    /**
     * Method called on error that occured in a child
     *
     * @param e found exception
     */
    void onError(Exception e);
}
