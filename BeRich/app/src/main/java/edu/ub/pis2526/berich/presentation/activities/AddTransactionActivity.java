package edu.ub.pis2526.berich.presentation.activities;

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
    private boolean isIncome = false; // Per defecte despesa segons el mockup

    private String selectedCategory = "General";  //Per defecte si l'usuari no selecciona categoria

    // variables per gestionar la modificació
    private int editIndex = -1;
    private Transaction transactionToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicialitzar binding
        binding = ActivityAddTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // comprovem si l'intent conté un índex per modificar
        editIndex = getIntent().getIntExtra("TRANSACTION_INDEX", -1);

        if (editIndex != -1) {
            prepararModificacio();
        }

        updateDisplay();
    }

    private void prepararModificacio() {
        Client loggedClient = AuthenticationService.getInstance().getLoggedInClient();
        if (loggedClient != null) {
            transactionToEdit = loggedClient.getTransactions().get(editIndex);

            // carreguem dades de la transacció existent
            currentAmount = String.valueOf(transactionToEdit.getQuantitat());
            isIncome = transactionToEdit.isIncome();
            selectedCategory = transactionToEdit.getCategory();

            // confirmem la modificació
            binding.btnSave.setText(R.string.confirmar);
        }
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
            if (finalAmount <= 0) return; // Evitar transaccions de 0

            Client loggedClient = AuthenticationService.getInstance().getLoggedInClient();

            if (loggedClient != null) {
                if (editIndex != -1) {
                    // actualitzem la transacció ja existent
                    transactionToEdit.setQuantitat(finalAmount);
                    transactionToEdit.setCategory(selectedCategory);
                    transactionToEdit.setIncome(isIncome);
                    Toast.makeText(this, "Transacció actualitzada", Toast.LENGTH_SHORT).show();
                } else {
                    // creem transacció i guardem a Client
                    Transaction newTransaction = new Transaction(finalAmount, selectedCategory, isIncome);
                    loggedClient.addTransaction(newTransaction);
                }
            }
            finish();
        } catch (NumberFormatException e) {
            currentAmount = "0";
            updateDisplay();
        }
    }

    private void updateDisplay() {
        String textToDisplay = currentAmount + " " + getString(R.string.currency_symbol);
        binding.txtAmountDisplay.setText(textToDisplay);

        // actualitzem el color segons si és ingrés o despesa (important per quan carreguem dades)
        int colorRes = isIncome ? R.color.blue_light : R.color.orange_light;
        binding.txtAmountDisplay.setTextColor(ContextCompat.getColor(this, colorRes));
    }

    public void onTypeClick(View view) {
        // comparar IDs directament des del binding
        if (view.getId() == binding.btnPlus.getId()) {
            isIncome = true;
        } else if (view.getId() == binding.btnMinus.getId()) {
            isIncome = false;
        }
        updateDisplay();
    }

    public void onCancelClick(View view) {
        // Tanquem l'activitat sense guardar res
        finish();
    }
}