package tests;

import model.Issue;
import model.IssueManager;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the IssueManager class.
 * These tests verify issue lookup and filtering functionality.
 */
public class IssueManagerTest {
    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Verifies that an Issue can be found by its unique ID.
     */
    @Test
    public void testFindIssueById() {
        Issue issue1 = new Issue("Title1", "Desc1", new User("a", "b", "c"));
        Issue issue2 = new Issue("Title2", "Desc2", new User("a", "b", "c"));
        Issue issue3 = new Issue("Title3", "Desc3", new User("a", "b", "c"));

        IssueManager manager = new IssueManager();
        manager.addIssue(issue1);
        manager.addIssue(issue2);
        manager.addIssue(issue3);

        assertEquals(issue2, manager.findIssue(issue2.getId()).get());
    }

    /**
     * Verifies that an Issue can be found by its title.
     */
    @Test
    public void testFindIssueByName() {
        Issue issue1 = new Issue("Title1", "Desc1", new User("a", "b", "c"));
        Issue issue2 = new Issue("Title2", "Desc2", new User("a", "b", "c"));
        Issue issue3 = new Issue("Title3", "Desc3", new User("a", "b", "c"));

        IssueManager manager = new IssueManager();
        manager.addIssue(issue1);
        manager.addIssue(issue2);
        manager.addIssue(issue3);

        assertEquals(issue2, manager.findIssue(issue2.getTitle()).get());
    }

    /**
     * Verifies that filtering by a specific title fragment
     * returns only the issues whose titles contain that fragment.
     */
    @Test
    public void testFilterByTitleSpecific() {
        Issue issue1 = new Issue("Another1", "Desc1", new User("a", "b", "c"));
        Issue issue2 = new Issue("Title1", "Desc2", new User("a", "b", "c"));
        Issue issue3 = new Issue("Another2", "Desc3", new User("a", "b", "c"));
        Issue issue4 = new Issue("Title2", "Desc4", new User("a", "b", "c"));
        Issue issue5 = new Issue("xyzf", "Desc5", new User("a", "b", "c"));

        IssueManager manager = new IssueManager();
        manager.addIssue(issue1);
        manager.addIssue(issue2);
        manager.addIssue(issue3);
        manager.addIssue(issue4);
        manager.addIssue(issue5);

        List<Issue> expextedIssues1 = List.of(issue1, issue3);
        List<Issue> expextedIssues2 = List.of(issue2, issue4);

        assertEquals(expextedIssues1, manager.filterByTitle("Another"));
        assertEquals(expextedIssues2, manager.filterByTitle("Title"));
    }

    /**
     * Verifies that filtering by an empty or blank title
     * returns all stored issues.
     */
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    public void testFilterByTitleEmpty(String title) {
        Issue issue1 = new Issue("Another1", "Desc1", new User("a", "b", "c"));
        Issue issue2 = new Issue("Title1", "Desc2", new User("a", "b", "c"));
        Issue issue3 = new Issue("Another2", "Desc3", new User("a", "b", "c"));

        IssueManager manager = new IssueManager();
        manager.addIssue(issue1);
        manager.addIssue(issue2);
        manager.addIssue(issue3);

        List<Issue> expextedIssues = List.of(issue1, issue2, issue3);

        assertEquals(expextedIssues, manager.filterByTitle(title));
    }
}
