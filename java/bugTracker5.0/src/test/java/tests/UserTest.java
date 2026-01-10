package tests;

import model.User;
import model.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Tests if the roles are assigned to the user
     */
    @Test
    public void testRoles() {
        User user = new User("a", "b", "c", "d");

        user.addRole(UserRole.REVIEWER);

        List<UserRole> expectedRoles = List.of(UserRole.REVIEWER);

        assertEquals(user.getRoles(), expectedRoles);
    }
}
