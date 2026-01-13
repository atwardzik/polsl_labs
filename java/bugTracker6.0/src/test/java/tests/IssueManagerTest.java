package tests;

import model.Issue;
import model.IssueManager;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import repository.InMemoryIssueRepository;
import repository.IssueRepository;

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
        IssueRepository issueRepository = new InMemoryIssueRepository();
        IssueManager issueManager = new IssueManager(issueRepository);

        Issue issue1 = issueManager.createIssue("Title1", "Desc1", new User());
        Issue issue2 = issueManager.createIssue("Title2", "Desc2", new User());
        Issue issue3 = issueManager.createIssue("Title3", "Desc3", new User());

        assertEquals(issue2, issueManager.getIssue(issue2.getId()));
    }

    /**
     * Verifies that an Issue can be found by its title.
     */
    @Test
    public void testFindIssueByName() {
        IssueRepository issueRepository = new InMemoryIssueRepository();
        IssueManager issueManager = new IssueManager(issueRepository);

        Issue issue1 = issueManager.createIssue("Title1", "Desc1", new User());
        Issue issue2 = issueManager.createIssue("Title2", "Desc2", new User());
        Issue issue3 = issueManager.createIssue("Title3", "Desc3", new User());

        assertEquals(issue2, issueManager.findByTitle(issue2.getTitle()));
    }
}
