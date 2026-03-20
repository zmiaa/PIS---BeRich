package edu.ub.pis2526.berich.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import edu.ub.pis2526.berich.R;
import edu.ub.pis2526.berich.data.services.AuthenticationService;
import edu.ub.pis2526.berich.databinding.ActivityAddTransactionBinding;
import edu.ub.pis2526.berich.domain.Client;
import edu.ub.pis2526.berich.domain.Transaction;

public class AddTransactionActivity extends AppCompatActivity {

    private ActivityAddTransactionBinding binding;
    private String currentAmount = "0";
    private Boolean isIncome = null;

    private String selectedCategory = "General";  //Per defecte si l'usuari no selecciona categoria

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicialment el botó de confirmar està desactivat
        binding.btnSave.setEnabled(false);
        binding.btnSave.setAlpha(0.5f); // Es veu "difuminat"

        updateDisplay();
    }


    public void onCategoryClick(View view) {
        Button button = (Button) view;
        selectedCategory = button.getText().toString();

        //Feedback visual ràpid
        Toast.makeText(this, "Categoría: " + selectedCategory, Toast.LENGTH_SHORT).show();
    }
    public void onNumberClick(View view) {
        Button button = (Button) view;
        String input = button.getText().toString();
        String dotChar = getString(R.string.dot); // "."

        if (input.equals(dotChar)) {
            // Només afegim el punt si no n'hi ha cap ja
            if (!currentAmount.contains(dotChar)) {
                currentAmount += dotChar;
            }
        } else {// CONTROL DE 2 DECIMALS:
            if (currentAmount.contains(dotChar)) {
                String decimals = currentAmount.substring(currentAmount.indexOf(dotChar) + 1);
                if (decimals.length() >= 2) return; // Si ja hi ha 2 decimals, no fem res
            }

            if (currentAmount.equals(getString(R.string.inic_num))) {
                currentAmount = input;
            } else {
                currentAmount += input;
            }
        }
        updateDisplay();
    }

    public void onSaveTransaction(View view) {
        try {
            double finalAmount = Double.parseDouble(currentAmount);
            if (finalAmount <= 0) {
                Toast.makeText(this, "Introdueix un import vàlid", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isIncome == null) {
                Toast.makeText(this, "Selecciona si és un ingrés (+) o una despesa (-)", Toast.LENGTH_SHORT).show();
                return;
            }

            Client loggedClient = AuthenticationService.getInstance().getLoggedInClient();

            if (loggedClient != null) {
                Transaction newTransaction = new Transaction(finalAmount, selectedCategory, isIncome);
                loggedClient.addTransaction(newTransaction);

                Toast.makeText(this, "Transacció guardada", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        } catch (NumberFormatException e) {
            currentAmount = "0";
            updateDisplay();
        }
    }

    private void updateDisplay() {
        String symbol = "";
        // Afegim el signe visual segons la selecció
        if (isIncome != null) {
            symbol = isIncome ? "+ " : "- ";
        }

        String formattedText = symbol + currentAmount + " " + getString(R.string.currency_symbol);
        binding.txtAmountDisplay.setText(formattedText);
    }

    public void onTypeClick(View view) {

        binding.btnSave.setEnabled(true);
        binding.btnSave.setAlpha(1.0f);

        // comparar IDs directament des del binding
        if (view.getId() == binding.btnPlus.getId()) {
            isIncome = true;
            binding.txtAmountDisplay.setTextColor(ContextCompat.getColor(this, R.color.blue_light));
        } else if (view.getId() == binding.btnMinus.getId()) {
            isIncome = false;
            binding.txtAmountDisplay.setTextColor(ContextCompat.getColor(this, R.color.orange_light));
        }
        updateDisplay();
    }

    public void onCancelClick(View view) {
        // En cas de cancel·lar, també forcem la tornada a HomeActivity
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onBackspaceClick(View view) {
        if (currentAmount.equals("0")) return;
        if (currentAmount.length() <= 1) {
            currentAmount = "0";
        } else {
            currentAmount = currentAmount.substring(0, currentAmount.length() - 1);
        }
        updateDisplay();
    }
}
