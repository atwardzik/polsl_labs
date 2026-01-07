package tests;

import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Issue class.
 * These tests verify correct validation, state changes,
 * and behavior of Issue objects.
 */
public class IssueTest {
    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Verifies that creating an Issue without a reporter (User)
     * throws an InvalidIssueDataException.
     */
    @Test
    public void testShortIssueNoUserCreation() {
        InvalidIssueDataException exception = assertThrows(InvalidIssueDataException.class,
                () -> new Issue("Ok", "desc", null),
                "An exception should be thrown when the User is null"
        );

        assertEquals("Reporter cannot be empty.", exception.getMessage());
    }

    /**
     * Verifies that an Issue with valid title, description,
     * and reporter is created successfully.
     */
    @Test
    public void testShortIssueValidCreation() {
        assertDoesNotThrow(
                () -> new Issue("Title", "Description", new User("Name", "Surname", "Username")),
                "All data is correct, an exception should not be thrown");
    }

    /**
     * Verifies that creating an Issue with a null or empty title
     * throws an InvalidIssueDataException.
     */
    @ParameterizedTest
    @NullSource
    @EmptySource
    public void testShortIssueNoTitleCreation(String title) {
        InvalidIssueDataException exception = assertThrows(InvalidIssueDataException.class,
                () -> new Issue(title, "desc", new User("A", "B", "C")),
                "An exception should be thrown when the User is null"
        );

        assertEquals("Issue name cannot be empty.", exception.getMessage());
    }

    /**
     * Verifies that creating an Issue with a null or empty description
     * throws an InvalidIssueDataException.
     */
    @ParameterizedTest
    @NullSource
    @EmptySource
    public void testShortIssueNoDescCreation(String description) {
        InvalidIssueDataException exception = assertThrows(InvalidIssueDataException.class,
                () -> new Issue("Title", description, new User("A", "B", "C")),
                "An exception should be thrown when the issue description is null or missing"
        );

        assertEquals("Issue description cannot be empty.", exception.getMessage());
    }

    /**
     * Verifies that setting a due date in the past
     * throws an InvalidIssueDataException.
     */
    @Test
    public void testAddDueDateBeforeNow() {
        Issue issue = new Issue("Title", "Desc", new User("a", "b", "c"));

        InvalidIssueDataException exception = assertThrows(InvalidIssueDataException.class,
                () -> issue.setDueDate(LocalDateTime.now().minusDays(1)),
                "An exception should be thrown when due date is before now");

        assertEquals("Due date must be in the future.", exception.getMessage());
    }

    /**
     * Verifies that all Priority enum values
     * can be set without throwing an exception.
     */
    @ParameterizedTest
    @EnumSource(Priority.class)
    public void testSetPriority(Priority priority) {
        Issue issue = new Issue("Title", "Desc", new User("a", "b", "c"));

        assertDoesNotThrow(
                () -> issue.setPriority(priority),
                "All data is correct, an exception should not be thrown");
    }

    /**
     * Verifies that all BugStatus enum values
     * can be set without throwing an exception.
     */
    @ParameterizedTest
    @EnumSource(BugStatus.class)
    public void testSetStatus(BugStatus status) {
        Issue issue = new Issue("Title", "Desc", new User("a", "b", "c"));

        assertDoesNotThrow(
                () -> issue.setStatus(status),
                "All data is correct, an exception should not be thrown");
    }

    /**
     * Verifies that assigning a null user
     * throws an InvalidIssueDataException.
     */
    @Test
    public void testAssignNullUser() {
        Issue issue = new Issue("Title", "Desc", new User("a", "b", "c"));

        InvalidIssueDataException exception = assertThrows(InvalidIssueDataException.class,
                () -> issue.assignUser(null),
                "An exception should be thrown when the User is null"
        );

        assertEquals("Cannot assign to non-existing user.", exception.getMessage());
    }

    /**
     * Verifies that a valid user can be assigned
     * and is correctly stored in the Issue.
     */
    @Test
    public void testAssignUser() {
        Issue issue = new Issue("Title", "Desc", new User("a", "b", "c"));
        User assignee = new User("as", "ig", "nee");

        issue.assignUser(assignee);

        assertEquals(issue.getAssignee().get(), assignee);
    }

    /**
     * Verifies that adding various tag values
     * (including empty or blank strings) does not throw exceptions.
     */
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "tag1"})
    public void testAddVariousTags(String tag) {
        Issue issue = new Issue("Title", "Desc", new User("a", "b", "c"));

        assertDoesNotThrow(() -> issue.addTag(tag), "All data is correct, an exception should not be thrown");
    }

    /**
     * Verifies that valid tags are correctly stored
     * and can be retrieved from the Issue.
     */
    @Test
    public void testAddCorrectTags() {
        Issue issue = new Issue("Title", "Desc", new User("a", "b", "c"));
        List<String> initTags = List.of("tag1", "tag2", "tag3");

        initTags.forEach(issue::addTag);

        Set<String> tags = issue.getTags();

        assertTrue(tags.containsAll(initTags));
    }

    /**
     * Verifies that updateFrom correctly updates all mutable fields
     * of an Issue using another Issue instance.
     */
    @Test
    public void testUpdateFromDifferentFields() {
        Issue original = new Issue("Old title",
                "Old Description",
                new User("Alice", "A", "aalice"),
                LocalDateTime.now().plusDays(1),
                new User("Bob", "B", "bbob"),
                BugStatus.OPEN, Priority.HIGH, Set.of("Tag")
        );

        Issue updated = new Issue("New title",
                "New Description",
                new User("Alice", "A", "aalice"),
                LocalDateTime.now().plusDays(1),
                new User("Evil", "E", "eevil"),
                BugStatus.CLOSED, Priority.CRITICAL, Set.of("Tag")
        );


        original.updateFrom(updated);

        assertEquals("New title", original.getTitle());
        assertEquals("New Description", original.getDescription());
        assertEquals("eevil", original.getAssignee().get().getUsername());
        assertEquals(BugStatus.CLOSED, original.getStatus());
        assertEquals(Priority.CRITICAL, original.getPriority());
        assertEquals(Set.of("Tag"), original.getTags());
    }
}
