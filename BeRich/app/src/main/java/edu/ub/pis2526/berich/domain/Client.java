package edu.ub.pis2526.berich.domain;

import java.util.ArrayList;
import java.util.List;

public class Client {
    // Atributs client
    private String username;
    private String email;
    private String password;

    //Llista de transaccions, guardar moviments
    private List<Transaction> transactions;

    //Constructor
    public Client(String username, String email, String password){
        this.username= username;
        this.email=email;
        this.password=password;
        this.transactions= new ArrayList<>();
    }

    //Getters
    public String getUsername(){
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    //Setters

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addTransaction(Transaction t) {
        this.transactions.add(t);
    }

    //Per calcular el total de diners que té l'usuari fent operacions amb el ArrayList del client
    public double getTotalBalance() {
        double total = 0;
        for (Transaction t : transactions) {
            if (t.isIncome()) {
                total += t.getQuantitat();
            } else {
                total -= t.getQuantitat();
            }
        }
        return total;
    }
}
