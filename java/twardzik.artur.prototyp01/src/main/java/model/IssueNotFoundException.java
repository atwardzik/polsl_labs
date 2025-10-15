package model;

import java.util.UUID;

/**
 * Custom exception for signalizing that no such issue exists
 *
 * @author Artur Twardzik
 * @version 0.1
 */
public class IssueNotFoundException extends RuntimeException {
    public IssueNotFoundException(UUID issueId) {
        super("Issue not found with ID: " + issueId);
    }

    public IssueNotFoundException(String title) {
        super("Issue not found with title/ID: " + title);
    }
}
