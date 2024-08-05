package com.example.MainPage;

import com.example.SharedData.SharedData;
import com.example.StockPickerApp.StockPickerApp;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MainPage {
    private VBox layout;
    private TextField symbolField;
    private TextField timeSeriesField;
    private StockPickerApp mainApp;

    public MainPage(StockPickerApp mainApp, String source) {
        this.mainApp = mainApp;

        symbolField = new TextField();
        timeSeriesField = new TextField();

        Button fetchButton = new Button("Fetch Stock Data");
        fetchButton.setOnAction(e -> {

            String apiKey = SharedData.getInstance().getApiKey(source);
            fetchStockData(symbolField.getText(), timeSeriesField.getText(), apiKey);
        });

        layout = new VBox(10, new Label("Symbol:"), symbolField, new Label("Time Series:"), timeSeriesField, fetchButton);
    }

    public VBox getLayout() {
        return layout;
    }

    private void fetchStockData(String symbol, String timeSeries, String apiKey) {
        // Call your backend service and pass the apiKey along with symbol and timeSeries
        // Example using HttpURLConnection or other HTTP client library
    }
}
