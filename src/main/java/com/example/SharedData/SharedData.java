package com.example.SharedData;

import java.util.HashMap;
import java.util.Map;

public class SharedData {
    private static SharedData instance;
    private Map<String, String> apiKeys;

    private SharedData() {
        apiKeys = new HashMap<>();
    }

    public static synchronized SharedData getInstance() {
        if (instance == null) {
            instance = new SharedData();
        }
        return instance;
    }

    public String getApiKey(String source) {
        return apiKeys.get(source);
    }

    public void setApiKey(String source, String apiKey) {
        apiKeys.put(source, apiKey);
    }

    public Map<String, String> getAllApiKeys() {
        return new HashMap<>(apiKeys);
    }
}
