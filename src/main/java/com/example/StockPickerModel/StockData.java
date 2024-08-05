package com.example.StockPickerModel;

public class StockData {
    private String symbol;
    private double currentPrice;
    private double entryPoint;
    private double exitPoint;
    private String earningsDate;
    private String growthPotential;
    private String bargainPotential;
    private String riskLevel;
    private String sentiment;
    private String analystRating;

    private int quarter;

    private int volume;

    // Constructor

    public StockData(String symbol, double currentPrice, double entryPoint, double exitPoint, String earningsDate,
                     String growthPotential, String bargainPotential, String riskLevel, String sentiment, String analystRating, int quarter) {
        this.symbol = symbol;
        this.currentPrice = currentPrice;
//        this.volume = volume;
        this.entryPoint = entryPoint;
        this.exitPoint = exitPoint;
        this.earningsDate = earningsDate;
        this.growthPotential = growthPotential;
        this.bargainPotential = bargainPotential;
        this.riskLevel = riskLevel;
        this.sentiment = sentiment;
        this.analystRating = analystRating;
        this.quarter = quarter;
    }

    public StockData() {

    }


    // Getters and setters for all fields
    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(double entryPoint) {
        this.entryPoint = entryPoint;
    }

    public double getExitPoint() {
        return exitPoint;
    }

    public void setExitPoint(double exitPoint) {
        this.exitPoint = exitPoint;
    }

    public String getEarningsDate() {
        return earningsDate;
    }

    public void setEarningsDate(String earningsDate) {
        this.earningsDate = earningsDate;
    }

    public String getGrowthPotential() {
        return growthPotential;
    }

    public void setGrowthPotential(String growthPotential) {
        this.growthPotential = growthPotential;
    }

    public String getBargainPotential() {
        return bargainPotential;
    }

    public void setBargainPotential(String bargainPotential) {
        this.bargainPotential = bargainPotential;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getAnalystRating() {
        return analystRating;
    }

    public void setAnalystRating(String analystRating) {
        this.analystRating= analystRating;
    }
}
