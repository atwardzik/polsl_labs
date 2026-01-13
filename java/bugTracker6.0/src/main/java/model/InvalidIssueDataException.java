package model;

/**
 * Custom exception for signalizing impropoer Issue creation/edition
 *
 * @author Artur Twardzik
 * @version 0.1
 */
public class InvalidIssueDataException extends RuntimeException {
    public InvalidIssueDataException(String message) {
        super(message);
    }
}
