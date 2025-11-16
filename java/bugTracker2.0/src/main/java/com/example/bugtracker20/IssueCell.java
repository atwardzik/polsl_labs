package com.example.bugtracker20;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.BugStatus;
import model.Issue;

import java.time.format.DateTimeFormatter;

public class IssueCell extends ListCell<Issue> {

    private GridPane grid = new GridPane();

    private Circle statusCircle = new Circle(6);
    private Label idLabel = new Label();
    private Label titleLabel = new Label();
    private Label authorLabel = new Label();
    private Label dateOpenedLabel = new Label();
    private Label dueDateLabel = new Label();

    public IssueCell() {
        grid.setHgap(10);
        grid.setPadding(new Insets(5));

        ColumnConstraints statusCol = new ColumnConstraints();
        statusCol.setPercentWidth(5);
        statusCol.setHgrow(Priority.NEVER);

        ColumnConstraints idCol = new ColumnConstraints();
        idCol.setPercentWidth(10);
        idCol.setHgrow(Priority.NEVER);

        ColumnConstraints titleCol = new ColumnConstraints();
        titleCol.setPercentWidth(50);
        titleCol.setHgrow(Priority.ALWAYS);

        ColumnConstraints authorCol = new ColumnConstraints();
        authorCol.setPercentWidth(10);
        authorCol.setHgrow(Priority.NEVER);

        ColumnConstraints dateOpenedCol = new ColumnConstraints();
        dateOpenedCol.setPercentWidth(10);
        dateOpenedCol.setHgrow(Priority.NEVER);

        ColumnConstraints dueDateCol = new ColumnConstraints();
        dueDateCol.setPercentWidth(10);
        dueDateCol.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(
                statusCol, idCol, titleCol, authorCol, dateOpenedCol, dueDateCol
        );


        grid.add(statusCircle, 0, 0);
        grid.add(idLabel, 1, 0);
        grid.add(titleLabel, 2, 0);
        grid.add(authorLabel, 3, 0);
        grid.add(dateOpenedLabel, 4, 0);
        grid.add(dueDateLabel, 5, 0);

        grid.getStyleClass().add("issue-cell");
        statusCircle.getStyleClass().add("issue-status");
        idLabel.getStyleClass().add("issue-id");
        titleLabel.getStyleClass().add("issue-title");
        authorLabel.getStyleClass().add("issue-author");
        dateOpenedLabel.getStyleClass().add("issue-date-opened");
        dueDateLabel.getStyleClass().add("issue-due-date");
    }

    @Override
    protected void updateItem(Issue issue, boolean empty) {
        super.updateItem(issue, empty);
        if (empty || issue == null) {
            setGraphic(null);
        } else {
            if (getListView() != null) {
                grid.prefWidthProperty().bind(
                        getListView().widthProperty()
                                .subtract(getListView().getInsets().getLeft() + getListView().getInsets().getRight() + 10)
                );
            }

            statusCircle.setFill(getStatusColor(issue.getStatus()));
            Tooltip tooltip = new Tooltip(issue.getStatus().getStatusName());
            Tooltip.install(statusCircle, tooltip);

            idLabel.setText("#" + issue.getId().toString().split("-")[0]);

            titleLabel.setText(issue.getTitle());
            authorLabel.setText(issue.getReporter().getUsername());
            dateOpenedLabel.setText(issue.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            dueDateLabel.setText(issue.getDueDate()
                    .map(date -> date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                    .orElse("")
            );

            VBox wrapper = new VBox(grid);
            wrapper.setPadding(new Insets(0, 0, 10, 0));
            setGraphic(wrapper);
        }
    }

    private Color getStatusColor(BugStatus status) {
        Color color = null;
        switch (status) {
            case OPEN -> color = Color.GREEN;
            case CLOSED -> color = Color.RED;
            case IN_PROGRESS -> color = Color.YELLOW;
            case REOPENED -> color = Color.ORANGE;
        }

        return color;
    }
}
