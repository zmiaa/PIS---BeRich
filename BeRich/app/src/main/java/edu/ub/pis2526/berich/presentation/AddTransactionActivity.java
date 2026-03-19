package edu.ub.pis2526.berich.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import edu.ub.pis2526.berich.R;
import edu.ub.pis2526.berich.databinding.ActivityAddTransactionBinding;

public class AddTransactionActivity extends AppCompatActivity {

    private ActivityAddTransactionBinding binding;
    private String currentAmount = "0";
    private boolean isIncome = false; // Per defecte despesa segons el mockup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicialitzar binding
        binding = ActivityAddTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        updateDisplay();
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
        } else {
            // Si és "0", posem el número nou
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

            Client loggedClient = AuthenticationService.getInstance().getLoggedInClient();

            if (loggedClient != null && isIncome != null) {
                // Guardem la transacció
                Transaction newTransaction = new Transaction(finalAmount, selectedCategory, isIncome);
                loggedClient.addTransaction(newTransaction);
                Toast.makeText(this, "Transacció guardada", Toast.LENGTH_SHORT).show();

                // Tornem a HomeActivity netejant la pila de pantalles
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
        String textToDisplay = currentAmount + " " + getString(R.string.currency_symbol);
        binding.txtAmountDisplay.setText(textToDisplay);
    }

    public void onTypeClick(View view) {
        // comparar IDs directament des del binding
        if (view.getId() == binding.btnPlus.getId()) {
            isIncome = true;
            binding.txtAmountDisplay.setTextColor(ContextCompat.getColor(this, R.color.blue_light));
        } else if (view.getId() == binding.btnMinus.getId()) {
            isIncome = false;
            binding.txtAmountDisplay.setTextColor(ContextCompat.getColor(this, R.color.orange_light));
        }
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
