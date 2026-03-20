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
import edu.ub.pis2526.berich.databinding.ActivityLoginBinding;
import edu.ub.pis2526.berich.domain.Client;
import edu.ub.pis2526.berich.presentation.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViewModel();
        initObservers();
        initWidgetListeners();

    }

    private void initViewModel(){
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    private void initObservers(){
        loginViewModel.getLoginResult().observe(this, success -> {
            if (success) {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Si falla
                Toast.makeText(this, "Error de login: les dades introduïdes són incorrectes",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initWidgetListeners(){
        binding.btnLogIn.setOnClickListener(v -> {
            String email = binding.etLoginEmail.getText().toString().trim();
            String password = binding.etLoginPassword.getText().toString();

            loginViewModel.doLogin(email, password);
        });

        binding.btnSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}