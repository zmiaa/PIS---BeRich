package edu.ub.pis2526.berich.domain;

public class Transaction {
    private double quantitat;
    private String category;
    private boolean isIncome;

    public Transaction(double quantitat, String category, boolean isIncome){
        this.quantitat=quantitat;
        this.category=category;
        this.isIncome=isIncome;
    }

    public double getQuantitat() {
        return quantitat;
    }
    public String getCategory(){
        return category;
    }

    public boolean isIncome() {
        return isIncome;
    }
}

