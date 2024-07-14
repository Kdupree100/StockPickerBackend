package com.example.EarningsReport;


public class EarningsReport {
    private double eps;
    private double revenue;

    // Constructor
    public EarningsReport(double eps, double revenue) {
        this.eps = eps;
        this.revenue = revenue;
    }

    // Getter and Setter methods
    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}
