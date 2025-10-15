package model;

public enum BugStatus {
    OPEN(1),
    IN_PROGRESS(2),
    CLOSED(3),
    REOPENED(4);

    private final int code;

    BugStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static BugStatus fromCode(int code) throws IllegalArgumentException {
        for (BugStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown BugStatus code: " + code);
    }
}
