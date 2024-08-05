package com.example.StockPickerApp;

import com.example.StockPickerFetch.StockPickerFetch;
import com.example.StockPickerModel.StockData;
import com.example.SharedData.SharedData;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockPickerApp extends Application {

    @Autowired
    private StockPickerFetch stockPickerFetch;

    private Stage primaryStage;
    private TextArea resultArea;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Stock Picker App");
        showMainPage();
    }

    public void showMainPage() {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 600, 400);

        TextField symbolField = new TextField();
        symbolField.setPromptText("Enter Stock Symbol");
        symbolField.setLayoutX(50);
        symbolField.setLayoutY(50);

        TextField timeSeriesField = new TextField();
        timeSeriesField.setPromptText("Enter Time Series (daily or monthly)");
        timeSeriesField.setLayoutX(50);
        timeSeriesField.setLayoutY(100);

        TextField sourceField = new TextField();
        sourceField.setPromptText("Enter Data Source (alpha_vantage or polygon)");
        sourceField.setLayoutX(50);
        sourceField.setLayoutY(150);

        Button fetchButton = new Button("Fetch Stock Data");
        fetchButton.setLayoutX(220);
        fetchButton.setLayoutY(200);
        fetchButton.setOnAction(e -> fetchStockData(symbolField.getText(), timeSeriesField.getText(), sourceField.getText()));

        resultArea = new TextArea();
        resultArea.setWrapText(true);
        resultArea.setLayoutX(50);
        resultArea.setLayoutY(250);
        resultArea.setPrefSize(500, 100);

        root.getChildren().addAll(symbolField, timeSeriesField, sourceField, fetchButton, resultArea);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void fetchStockData(String symbol, String timeSeries, String source) {
        if (symbol == null || symbol.isEmpty() || timeSeries == null || timeSeries.isEmpty() || source == null || source.isEmpty()) {
            resultArea.setText("Please enter symbol, time series, and data source.");
            return;
        }

        try {
            String apiKey = SharedData.getInstance().getApiKey(source);
            if (apiKey == null) {
                resultArea.setText("API key for source " + source + " is not set.");
                return;
            }

            StockData stockData = null;
            if ("alpha_vantage".equals(source)) {
                stockData = stockPickerFetch.fetchStockData(symbol, timeSeries, apiKey);
            } else if ("polygon".equals(source)) {
                stockData = stockPickerFetch.fetchStockDataFromPolygon(symbol, apiKey);
            }
            else if ("finnhub".equals(source)) {
                stockData = stockPickerFetch.fetchLiveStockDataFromFinnhub(symbol, apiKey);
            }


            if (stockData != null) {
                resultArea.setText(stockData.toString());
            } else {
                resultArea.setText("Error fetching stock data: Invalid source.");
            }
        } catch (Exception e) {
            resultArea.setText("Error fetching stock data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(StockPickerApp.class, args);
        launch(args);
    }
}
