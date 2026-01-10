package model;

public record IssueListRecord(BugStatus status,
                              String id,
                              String title,
                              String author,
                              String assignee,
                              String dateCreated,
                              String dueDate) {
}
