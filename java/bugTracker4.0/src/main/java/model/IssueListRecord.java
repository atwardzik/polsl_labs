package model;

public record IssueListRecord(BugStatus status,
                              String id,
                              String title,
                              String author,
                              String dateCreated,
                              String dueDate) {
}
