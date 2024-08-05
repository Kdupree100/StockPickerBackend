package com.example.ApiKeyController;

import com.example.SharedData.SharedData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiKeyController {

    @PostMapping("/setApiKey")
    public ResponseEntity<String> setApiKey(@RequestBody ApiKeyRequest apiKeyRequest) {
        try {
            String source = apiKeyRequest.getSource();
            String apiKey = apiKeyRequest.getApiKey();
            SharedData.getInstance().setApiKey(source, apiKey);
            return ResponseEntity.ok("API key saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving API key: " + e.getMessage());
        }
    }
    @GetMapping("/getApiKeys")
    public Map<String, String> getAllApiKeys() {
        return SharedData.getInstance().getAllApiKeys();
    }


    public static class ApiKeyRequest {
        private String source;
        private String apiKey;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getApiKey() {
            SharedData.getInstance().getApiKey(source);
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
    }
}
