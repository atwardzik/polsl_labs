package com.example.bugtracker50;

import java.util.Map;

public class JsonResponse<T> {
    private T data;
    private Map<String, String> locale;

    public JsonResponse(T data, Map<String, String> locale) {
        this.data = data;
        this.locale = locale;
    }
}
