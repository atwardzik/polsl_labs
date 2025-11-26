package model;

/**
 * Enum holding user role
 *
 * @author Artur Twardzik
 * @version 0.2
 */
public enum UserRole {
    ADMIN,
    REVIEWER,
    STANDARD_USER,
    VIEW_ONLY_USER;

    public String getRoleName() {
        String name = "";

        switch (this) {
            case ADMIN -> name = "Admin";
            case REVIEWER -> name = "Reviewer";
            case STANDARD_USER -> name = "Standard User";
            case VIEW_ONLY_USER -> name = "View-Only User";
        }

        return name;
    }
}
