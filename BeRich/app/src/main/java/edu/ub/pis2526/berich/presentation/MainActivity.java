package edu.ub.pis2526.berich.presentation;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.ub.pis2526.berich.data.services.AuthenticationService;
import edu.ub.pis2526.berich.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Adaptacio de la pantalla
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Boto Registrar
        binding.btnGoRegister.setOnClickListener(v->{
            Intent intent_register= new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent_register);
        });

        binding.btnGoLogin.setOnClickListener(v -> {
            Intent intent_login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent_login);
        });

    }
}