package com.example.StockQuote;



public class StockQuote {
    private String symbol;
    private double latestPrice;

    // Getter methods
    public String getSymbol() {
        return symbol;
    }

    public double getLatestPrice() {
        return latestPrice;
    }

    // Setter methods (optional, if you need to set these values manually)
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setLatestPrice(double latestPrice) {
        this.latestPrice = latestPrice;
    }
}
