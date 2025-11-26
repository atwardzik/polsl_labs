package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import model.BugStatus;
import model.Issue;
import model.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * Controller for the user account view. Displays user profile information,
 * loads and filters the issues assigned to the user, and supports viewing
 * individual issues from the table. Handles initialization of table columns
 * and keyboard shortcuts.
 *
 * @author Artur Twardzik
 * @version 0.2
 */
public class AccountController {
    /**
     * Reference to the main window controller for navigation and callbacks.
     * -- SETTER --
     * Sets the parent controller that manages the main application window.
     *
     * @param parent the main window controller to associate with this account view
     */
    @Setter
    private MainAppWindowController parent;

    /**
     * Observable list of issues assigned to the current user.
     */
    private ObservableList<Issue> userIssues;

    /**
     * The user whose account information is displayed.
     */
    private User user;

    /**
     * Label displaying account creation date.
     */
    @FXML
    private Label activeSinceLabel;

    /**
     * Table column containing issue due dates.
     */
    @FXML
    private TableColumn<Issue, String> dueDateCol;

    /**
     * Table column containing issue titles.
     */
    @FXML
    private TableColumn<Issue, String> issueTitleCol;

    /**
     * Label displaying the last time the user was active.
     */
    @FXML
    private Label lastSeenAtLabel;

    /**
     * Label displaying the user's given name.
     */
    @FXML
    private Label nameLabel;

    /**
     * ImageView displaying the user's profile picture.
     */
    @FXML
    private ImageView profilePhoto;

    /**
     * Table column containing the usernames of issue reporters.
     */
    @FXML
    private TableColumn<Issue, String> reporterCol;

    /**
     * Label displaying the name of the user's primary role.
     */
    @FXML
    private Label roleLabel;

    /**
     * Table column displaying or editing issue status values.
     */
    @FXML
    private TableColumn<Issue, String> statusCol;

    /**
     * Label displaying the user's surname.
     */
    @FXML
    private Label surnameLabel;

    /**
     * Table displaying issues assigned to the user.
     */
    @FXML
    private TableView<Issue> tableView;

    /**
     * Label showing the user's username.
     */
    @FXML
    private Label usernameLabel;

    /**
     * Initializes the account view once its FXML is loaded. Configures table
     * styling, enables editing, initializes all table columns, and registers a
     * keyboard shortcut (Ctrl/Cmd + O) to open the selected issue.
     */
    @FXML
    public void initialize() {
        tableView.setStyle(
                "-fx-font-family: 'JetBrains Mono';" + "-fx-font-size: 14px;"
        );

        tableView.setEditable(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setRowFactory(tv -> {
            TableRow<Issue> row = new TableRow<>();

            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                if (!row.isEmpty()) {
                    int index = row.getIndex();
                    if (tableView.getSelectionModel().getSelectedIndices().contains(index)) {
                        event.consume();
                        tableView.edit(index, statusCol);
                    }
                }
            });

            return row;
        });
        statusCol.setEditable(true);

        initializeIssueTitleCol();
        initializeDueDateCol();
        initializeStatusCol();
        initializeReporterCol();

        tableView.sceneProperty().addListener((obs, old, newScene) -> {
            if (newScene != null) {
                newScene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN),
                        () -> {
                            Issue selected = tableView.getSelectionModel().getSelectedItem();
                            if (selected != null) {
                                parent.setIssuePane(selected);
                            }
                        }
                );
            }
        });
    }

    /**
     * Configures the Issue Title column to display titles with tooltips and
     * ellipsis when text overflows. Also enables double-click to open the issue
     * in the main window.
     */
    private void initializeIssueTitleCol() {
        issueTitleCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTitle()));
        issueTitleCol.setCellFactory(col -> {
            TableCell<Issue, String> cell = new TableCell<>() {

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setTooltip(null);
                    } else {
                        setText(item);

                        Tooltip tooltip = new Tooltip(item);
                        setTooltip(tooltip);

                        setStyle("-fx-text-overrun: ellipsis;");
                    }

                    setText(item);
                }
            };

            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !cell.isEmpty()) {
                    Issue issue = cell.getTableView().getItems().get(cell.getIndex());

                    parent.setIssuePane(issue);
                }
            });

            return cell;
        });
    }

    /**
     * Configures the Reporter column to display the username of the issue's reporter.
     */
    private void initializeReporterCol() {
        reporterCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReporter().getUsername()));
    }

    /**
     * Configures the Due Date column to display formatted due dates, or "N/A"
     * when no due date is assigned.
     */
    private void initializeDueDateCol() {
        dueDateCol.setCellValueFactory(cellData -> {
            String dueDate = cellData.getValue().getDueDate()
                    .map(date -> date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                    .orElse("N/A");

            return new SimpleStringProperty(dueDate);
        });
    }

    /**
     * Configures the Status column to display and allow editing of issue status via
     * a combo box. Updates the underlying issue object on edit commit.
     */
    private void initializeStatusCol() {
        statusCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus().name()));
        statusCol.setCellFactory(ComboBoxTableCell.<Issue, String>forTableColumn(BugStatus.toArrayOfStrings()));
        statusCol.setOnEditCommit(t -> {
            TableView<Issue> table = t.getTableView();
            String newStatus = t.getNewValue();

            ObservableList<Issue> selected = table.getSelectionModel().getSelectedItems();

            if (selected.size() > 1) {
                selected.forEach(issue -> issue.setStatus(BugStatus.valueOf(newStatus)));
                table.getSelectionModel().clearAndSelect(0);
                table.refresh();
            } else {
                Issue edited = t.getRowValue();
                edited.setStatus(BugStatus.valueOf(newStatus));
            }
        });
    }

    /**
     * Loads the user's profile information into labels, including username, name,
     * roles, creation date, and last seen timestamp.
     *
     * @param user the user whose account information should be displayed
     */
    public void setUser(User user) {
        usernameLabel.setText(user.getUsername());
        nameLabel.setText(user.getName());
        surnameLabel.setText(user.getSurname());
        roleLabel.setText(user.getRoles().getFirst().getRoleName());
        activeSinceLabel.setText("Active since: " + user.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        lastSeenAtLabel.setText("Last seen on: " + user.getLastSeenAt().map(date -> date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).orElse("N/A"));
        this.user = user;
    }

    /**
     * Sets and displays the list of issues assigned to the current user. Filters
     * the provided list so that only issues assigned to this user appear in the
     * table.
     *
     * @param userIssues a list of issues to filter and display
     * @throws RuntimeException if called before a user is assigned via {@link #setUser(User)}
     */
    public void setUserIssues(List<Issue> userIssues) {
        if (user == null) {
            throw new RuntimeException("You should log in first");
        }
        userIssues = userIssues.stream().filter(issue -> Objects.equals(issue.getAssignee().orElse(null), user)).toList();

        this.userIssues = FXCollections.observableArrayList(userIssues);
        tableView.setItems(this.userIssues);
    }
}
