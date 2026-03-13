package edu.ub.pis2526.berich.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.ub.pis2526.berich.R;
import edu.ub.pis2526.berich.data.services.AuthenticationService;
import edu.ub.pis2526.berich.databinding.ActivityLoginBinding;
import edu.ub.pis2526.berich.domain.Client;

public class LoginActivity extends AppCompatActivity {
    private AuthenticationService authenticationService;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authenticationService = new AuthenticationService();

        binding.btnLogIn.setOnClickListener(v -> {
            String email = binding.etLoginEmail.getText().toString().trim();
            String password = binding.etLoginPassword.getText().toString();

            authenticationService.logIn(email, password, new AuthenticationService.OnLogInListener() {
                @Override
                public void onLogInSuccess(Client client) {

                    //Cuando tengamos Home Page, descomentamos!!!!!!!!!
                    // Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    // startActivity(intent);
                }

                @Override
                public void onLogInError(Throwable throwable) {

                }
            });
        });

        //Saltar a la pagina de SignUp si encara no s'ha registrat
        binding.btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.login, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}