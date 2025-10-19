package model;

/**
 * Enum holding a bug status
 *
 * @author Artur Twardzik
 * @version 0.1
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
}
