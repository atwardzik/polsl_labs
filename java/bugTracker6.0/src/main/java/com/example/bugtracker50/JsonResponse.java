package com.example.bugtracker50;

import java.util.Map;

/**
 * JsonResponse is a generic container for sending JSON responses
 * that include both the main data and localized field labels.
 *
 * @param <T> the type of the main data included in the response
 * @author Artur Twardzik
 * @version 0.5
 */
public class JsonResponse<T> {
    /**
     * General data holder
     */
    private T data;
    /**
     * Localization data holder
     */
    private Map<String, String> locale;

    /**
     * Constructs a JsonResponse with the given data and localization map.
     *
     * @param data   the main data object to include in the response
     * @param locale a map of localized field names for the response
     */
    public JsonResponse(T data, Map<String, String> locale) {
        this.data = data;
        this.locale = locale;
    }
}
