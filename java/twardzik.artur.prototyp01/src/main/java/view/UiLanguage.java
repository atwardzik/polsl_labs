package view;

public enum UiLanguage {
    ENGLISH(1),
    GERMAN(2);

    private final int code;

    UiLanguage(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static UiLanguage fromCode(int code) throws IllegalArgumentException {
        for (UiLanguage status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown UiLanguage code: " + code);
    }
}
