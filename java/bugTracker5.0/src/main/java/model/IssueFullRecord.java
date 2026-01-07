package model;

public record IssueFullRecord(BugStatus status,
                              String id,
                              String title,
                              String author,
                              String dateCreated,
                              String dueDate,
                              String description
) {
}

