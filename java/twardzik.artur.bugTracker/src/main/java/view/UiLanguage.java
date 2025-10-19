package view;

public enum UiLanguage {
    ENGLISH(1),
    GERMAN(2);

    /**
     * Language code
     */
    private final int code;

    /**
     * Enum constructor
     *
     * @param code language code
     */
    UiLanguage(int code) {
        this.code = code;
    }

    /**
     * Converts int to user interface language option
     *
     * @param code language code
     * @return language option
     * @throws IllegalArgumentException on illegal code
     */
    public static UiLanguage fromCode(int code) throws IllegalArgumentException {
        for (UiLanguage status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown UiLanguage code: " + code);
    }
}
