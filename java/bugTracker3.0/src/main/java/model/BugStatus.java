package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum holding a bug status
 *
 * @author Artur Twardzik
 * @version 0.2
 */
public enum BugStatus {
    OPEN(1),
    IN_PROGRESS(2),
    CLOSED(3),
    REOPENED(4);

    /**
     * Bug Status code
     */
    private final int code;

    /**
     * Enum constructor
     *
     * @param code
     */
    BugStatus(int code) {
        this.code = code;
    }

    /**
     * Converts int to bug status option
     *
     * @param code
     * @return bug status
     * @throws IllegalArgumentException on illegal code
     */
    public static BugStatus fromCode(int code) throws IllegalArgumentException {
        for (BugStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown BugStatus code: " + code);
    }

    /**
     * Converts enum value for pretty name
     *
     * @return String containing pretty name (without underscores)
     */
    public String getStatusName() {
        String name = "";

        switch (this) {
            case OPEN -> name = "Open";
            case CLOSED -> name = "Closed";
            case IN_PROGRESS -> name = "In progress";
            case REOPENED -> name = "Reopened";
        }

        return name;
    }

    /**
     * Converts enum values to the string array for the purposes of ComboBox
     *
     * @return array of Strings representing enum names
     */
    public static String[] toArrayOfStrings() {
        List<String> result = new ArrayList<>();
        for (var el : BugStatus.values()) {
            result.add(el.name());
        }
        return result.toArray(String[]::new);
    }
}
