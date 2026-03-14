package edu.ub.pis2526.berich.presentation;

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
            if (!isIncome) finalAmount *= -1;

            // Aquí en un futur fara la crida al ViewModel/BD
            finish();
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
}
