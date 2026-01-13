package model;

public record IssueFullRecord(BugStatus status,
                              Priority priority,
                              String id,
                              String title,
                              String authorUsername,
                              String authorFullName,
                              String dateCreated,
                              String dueDate,
                              String description
) {
}

