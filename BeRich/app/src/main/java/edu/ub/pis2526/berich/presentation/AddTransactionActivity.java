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
        String digit = button.getText().toString();

        if (currentAmount.equals("0")) {
            currentAmount = digit;
        } else {
            currentAmount += digit;
        }

        updateDisplay();
    }

    public void onSaveTransaction(View view) {
        // Recollim l'import final
        double finalAmount = Double.parseDouble(currentAmount);

        // Si el botó "-" (Despesa) està actiu, el fem negatiu
        if (!isIncome) {
            finalAmount = finalAmount * -1;
        }

        // Tornem a la pantalla principal
        finish();
    }

    private void updateDisplay() {
        // Mostrem el número amb el símbol de l'euro o dòlar segons el mockup
        binding.txtAmountDisplay.setText(currentAmount + " €");
    }

    public void onTypeClick(View view) {
        if (view.getId() == R.id.btnPlus) {
            isIncome = true;
            binding.txtAmountDisplay.setTextColor(ContextCompat.getColor    (this, R.color.blue_light));
        } else if (view.getId() == R.id.btnMinus) {
            isIncome = false;
            binding.txtAmountDisplay.setTextColor(ContextCompat.getColor(this, R.color.orange_light));
        }
    }
}
