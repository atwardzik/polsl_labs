package model;

import java.util.UUID;

public class IssueNotFoundException extends RuntimeException {
    public IssueNotFoundException(UUID issueId) {
        super("Issue not found with ID: " + issueId);
    }

    public IssueNotFoundException(String title) {
        super("Issue not found with title/ID: " + title);
    }
}
