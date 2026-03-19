package edu.ub.pis2526.berich.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import edu.ub.pis2526.berich.R;
import edu.ub.pis2526.berich.data.services.AuthenticationService;
import edu.ub.pis2526.berich.databinding.ActivityModifyTransactionBinding;
import edu.ub.pis2526.berich.domain.Client;
import edu.ub.pis2526.berich.domain.Transaction;

public class ModifyTransactionActivity extends AppCompatActivity {

    private ActivityModifyTransactionBinding binding;
    private String currentAmount;
    private boolean isIncome;
    private String selectedCategory;
    private int transactionIndex; // l'índex per identificar la transacció del client
    private Transaction transactionToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModifyTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // el recuperem per intent
        transactionIndex = getIntent().getIntExtra("TRANSACTION_INDEX", -1);

        loadTransactionData();
    }

    private void loadTransactionData() {
        Client client = AuthenticationService.getInstance().getLoggedInClient();
        if (client != null && transactionIndex != -1) {
            transactionToEdit = client.getTransactions().get(transactionIndex);

            // carreguem els valors actuals
            currentAmount = String.valueOf(transactionToEdit.getQuantitat());
            isIncome = transactionToEdit.isIncome();
            selectedCategory = transactionToEdit.getCategory();

            updateDisplay();
            updateUIStyle();
        }
    }

    public void onNumberClick(View view) {
        Button button = (Button) view;
        String input = button.getText().toString();
        // lògica similar al AddTransactionActivity per gestionar els punts decimals
        if (input.equals(getString(R.string.dot))) {
            if (!currentAmount.contains(getString(R.string.dot))) currentAmount += input;
        } else {
            if (currentAmount.equals("0")) currentAmount = input;
            else currentAmount += input;
        }
        updateDisplay();
    }

    public void onCategoryClick(View view) {
        selectedCategory = ((Button) view).getText().toString();
    }

    public void onTypeClick(View view) {
        if (view.getId() == binding.btnPlus.getId()) {
            isIncome = true;
        } else if (view.getId() == binding.btnMinus.getId()) {
            isIncome = false;
        }
        updateUIStyle();
    }

    public void onSaveTransaction(View view) {
        try {
            double finalAmount = Double.parseDouble(currentAmount);
            if (transactionToEdit != null && finalAmount > 0) {
                // modifiquem l'objecte original directament, actualitzant la ja existent
                transactionToEdit.setQuantitat(finalAmount);
                transactionToEdit.setCategory(selectedCategory);
                transactionToEdit.setIncome(isIncome);
                finish();
            }
        } catch (NumberFormatException e) {
            finish();
        }
    }

    private void updateDisplay() {
        binding.txtAmountDisplay.setText(currentAmount + " " + getString(R.string.currency_symbol));
    }

    private void updateUIStyle() {
        int color = isIncome ? R.color.blue_light : R.color.orange_light;
        binding.txtAmountDisplay.setTextColor(ContextCompat.getColor(this, color));
    }

    public void onCancelClick(View view) {
        finish();
    }
}