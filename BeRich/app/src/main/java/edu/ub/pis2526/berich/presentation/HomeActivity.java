package edu.ub.pis2526.berich.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

import edu.ub.pis2526.berich.R;
import edu.ub.pis2526.berich.data.services.AuthenticationService;
import edu.ub.pis2526.berich.databinding.ActivityHomeBinding;
import edu.ub.pis2526.berich.databinding.ActivityLoginBinding;
import edu.ub.pis2526.berich.domain.Client;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onResume() {
        super.onResume();
        updateHomeData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityHomeBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        binding.navSettings.setOnClickListener(v-> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        binding.fabAdd.setOnClickListener(v-> {
            Intent intent = new Intent(HomeActivity.this, AddTransactionActivity.class);
            startActivity(intent);
        });

        // US15 - Visualització general sobre Benestar Emocional i Diari
        // Despres de fer click al simbol emoji, es va la la pantalla de Calendari d'emocions
        binding.navMood.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, EmotionCalendarActivity.class);
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    //AActualització de home page
    private void updateHomeData() {
        Client client = AuthenticationService.getInstance().getLoggedInClient();
        if (client != null) {
            //Obtenim el total
            double total = client.getTotalBalance();

            String formattedBalance = String.format(Locale.getDefault(), "%.2f €", total);
            binding.tvTotalBalance.setText(formattedBalance);
        }
    }
}