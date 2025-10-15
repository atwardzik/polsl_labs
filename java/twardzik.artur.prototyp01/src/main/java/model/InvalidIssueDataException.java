package model;

public class InvalidIssueDataException extends RuntimeException {
    public InvalidIssueDataException(String message) {
        super(message);
    }
}
