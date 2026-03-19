package edu.ub.pis2526.berich.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis2526.berich.data.services.AuthenticationService;
import edu.ub.pis2526.berich.databinding.ActivityRegisterBinding;
import edu.ub.pis2526.berich.presentation.viewmodels.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        initViewModel();
        initObservers();
        initWidgetListeners();
    }

    private void initViewModel(){
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
    }

    private void initObservers() {

        registerViewModel.getRegisterState().observe(this, state -> {
            if (state.success) {
                Toast.makeText(this, "Registro completado con éxito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, state.errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initWidgetListeners() {
        binding.btnConfirmSignUp.setOnClickListener(v -> {
            String username = binding.etSignUpUsername.getText().toString().trim();
            String email = binding.etSignUpEmail.getText().toString().trim();
            String password = binding.etSignUpPassWord.getText().toString();
            String c_password = binding.etSignUpPassWordConfirmation.getText().toString();

            registerViewModel.signUp(username, email, password, c_password);
        });
    }
}