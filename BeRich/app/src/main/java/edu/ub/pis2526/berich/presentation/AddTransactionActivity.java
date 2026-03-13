package edu.ub.pis2526.berich.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import edu.ub.pis2526.berich.R;

public class AddTransactionActivity extends AppCompatActivity {

    private TextView txtAmountDisplay;
    private String currentAmount = "0";
    private boolean isIncome = false; // Per defecte despesa segons el mockup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Això enllaça el codi amb el disseny
        setContentView(R.layout.activity_add_transaction);

        // Vinculem el visor de text del XML
        txtAmountDisplay = findViewById(R.id.txtAmountDisplay);
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
        txtAmountDisplay.setText(currentAmount + " €");
    }

    public void onTypeClick(View view) {
        if (view.getId() == R.id.btnPlus) {
            isIncome = true;
            txtAmountDisplay.setTextColor(getResources().getColor(R.color.blue_income));
        } else if (view.getId() == R.id.btnMinus) {
            isIncome = false;
            txtAmountDisplay.setTextColor(getResources().getColor(R.color.orange_expense));
        }
    }
}
