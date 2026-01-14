package controller;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.BugStatus;
import model.Issue;
import model.IssueListRecord;

import java.time.format.DateTimeFormatter;

/**
 * A custom {@link ListCell} implementation for displaying {@link Issue} objects
 * inside a {@link javafx.scene.control.ListView}. <br>
 * <br>
 * The cell shows:
 * <ul>
 *     <li>Status indicator (colored circle)</li>
 *     <li>Issue ID (short form)</li>
 *     <li>Title</li>
 *     <li>Author</li>
 *     <li>Date opened</li>
 *     <li>Due date</li>
 * </ul>
 * The layout is built using a {@link GridPane} with percentage-based column widths.
 *
 * @author Artur Twardzik
 * @version 0.2
 */
public class IssueCell extends ListCell<Issue> {

    /**
     * Layout container used to arrange all issue-related labels in a row.
     */
    private GridPane grid = new GridPane();

    /**
     * Small colored circle indicating the current {@link BugStatus} of the issue.
     */
    private Circle statusCircle = new Circle(6);
    /**
     * Label displaying the shortened issue ID (first segment of the UUID).
     */
    private Label idLabel = new Label();
    /**
     * Label displaying the title of the issue.
     */
    private Label titleLabel = new Label();
    /**
     * Label showing the username of the issue reporter.
     */
    private Label authorLabel = new Label();
    /**
     * Label displaying the date when the issue was created.
     */
    private Label dateOpenedLabel = new Label();
    /**
     * Label displaying the issue's due date, if present.
     */
    private Label dueDateLabel = new Label();

    /**
     * Constructs a new {@code IssueCell} and initializes the layout, column
     * constraints, style classes, and graphical components used to render
     * issue information inside a {@link javafx.scene.control.ListView}.
     */
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

    /**
     * Updates the visual representation of this cell based on the provided
     * {@link Issue}. If the cell is empty, the graphic is cleared; otherwise,
     * all labels and indicators are populated with data from the issue and
     * rendered inside the preconfigured layout.
     *
     * @param issue the issue to display, or {@code null} if the cell is empty
     * @param empty whether the cell represents no data
     */
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

            IssueListRecord rec = issue.getRecord();

            statusCircle.setFill(getStatusColor(rec.status()));

            Tooltip tooltip = new Tooltip(rec.status().getStatusName());
            Tooltip.install(statusCircle, tooltip);

            idLabel.setText("#" + issue.getId().toString().split("-")[0]);

            titleLabel.setText(rec.title());
            authorLabel.setText(rec.author());
            dateOpenedLabel.setText(rec.dateCreated());
            dueDateLabel.setText(rec.dueDate());

            VBox wrapper = new VBox(grid);
            wrapper.setPadding(new Insets(0, 0, 10, 0));
            setGraphic(wrapper);
        }
    }

    /**
     * Returns the {@link Color} associated with the given {@link BugStatus}.
     * The color is used to fill the status indicator circle in the list cell.
     *
     * @param status the issue status
     * @return the UI color mapped to the status (e.g., green for OPEN)
     */
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
