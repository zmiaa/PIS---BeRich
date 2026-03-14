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
import edu.ub.pis2526.berich.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthenticationService authenticationService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        authenticationService = new AuthenticationService();

        binding.btnConfirmSignUp.setOnClickListener(v -> {
            String username = binding.etSignUpUsername.getText().toString().trim();
            String email = binding.etSignUpEmail.getText().toString().trim();
            String password = binding.etSignUpPassWord.getText().toString();
            String c_password = binding.etSignUpPassWordConfirmation.getText().toString();

            authenticationService.signUp(username, email, password, c_password, new AuthenticationService.OnSignUpListener() {
                @Override
                public void onSignUpSuccess() {


                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onSignUpError(Throwable throwable) {
                    Toast.makeText(RegisterActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}