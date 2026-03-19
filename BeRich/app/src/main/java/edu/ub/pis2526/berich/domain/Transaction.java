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

    // per modificar la quantitat per modify transaction
    public void setQuantitat(double quantitat) {
        this.quantitat = quantitat;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIncome(boolean income) {
        isIncome = income;
    }
}

