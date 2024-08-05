package com.example.ApiKeyPage;

import com.example.SharedData.SharedData;
import com.example.StockPickerApp.StockPickerApp;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ApiKeyPage {
    private VBox layout;
    private TextField alphaKeyField;
    private TextField polygonKeyField;

    private TextField finnhubKeyField;
    private StockPickerApp mainApp;

    public ApiKeyPage(StockPickerApp mainApp) {
        this.mainApp = mainApp;

        alphaKeyField = new TextField();
        alphaKeyField.setPromptText("Alpha Vantage API Key");

        polygonKeyField = new TextField();
        polygonKeyField.setPromptText("Polygon API Key");

        finnhubKeyField = new TextField();
        finnhubKeyField.setPromptText("Finnhub API Key");

        Button saveButton = new Button("Save API Keys");
        saveButton.setOnAction(e -> {
            // Ensure both API keys are saved with their respective sources
            SharedData.getInstance().setApiKey("alpha_vantage", alphaKeyField.getText());
            SharedData.getInstance().setApiKey("polygon", polygonKeyField.getText());
            SharedData.getInstance().setApiKey("finnhub", finnhubKeyField.getText());
            mainApp.showMainPage();  // Navigate back to main page
        });

        layout = new VBox(10, new Label("Enter API Keys:"), alphaKeyField, polygonKeyField, saveButton);
    }

    public VBox getLayout() {
        return layout;
    }
}
