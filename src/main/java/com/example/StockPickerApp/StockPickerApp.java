package com.example.StockPickerApp;

import com.example.StockPickerFetch.StockPickerFetch;
import com.example.StockPickerModel.StockData;
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

    private TextArea resultArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Stock Picker App");

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

        Button fetchButton = new Button("Fetch Stock Data");
        fetchButton.setLayoutX(220);
        fetchButton.setLayoutY(150);
        fetchButton.setOnAction(e -> fetchStockData(symbolField.getText(), timeSeriesField.getText()));

        resultArea = new TextArea();
        resultArea.setWrapText(true);
        resultArea.setLayoutX(50);
        resultArea.setLayoutY(200);
        resultArea.setPrefSize(500, 150);

        root.getChildren().addAll(symbolField, timeSeriesField, fetchButton, resultArea);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void fetchStockData(String symbol, String timeSeries) {
        if (symbol == null || symbol.isEmpty() || timeSeries == null || timeSeries.isEmpty()) {
            resultArea.setText("Please enter both symbol and time series.");
            return;
        }

        try {
            StockData stockData = stockPickerFetch.fetchStockData(symbol, timeSeries);
            resultArea.setText(stockData.toString());
        } catch (Exception e) {
            resultArea.setText("Error fetching stock data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(StockPickerApp.class, args);
        launch(args);
    }
}
