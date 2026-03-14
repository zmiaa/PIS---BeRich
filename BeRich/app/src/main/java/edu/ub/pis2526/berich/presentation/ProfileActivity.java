package edu.ub.pis2526.berich.presentation;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import edu.ub.pis2526.berich.data.services.AuthenticationService;
import edu.ub.pis2526.berich.databinding.ActivityProfileBinding;
import edu.ub.pis2526.berich.domain.Client;

/**
 * Activity que gestiona el perfil del usuari i la configuració (US6).
 * Se centra en mostrar la informació del client i permetre el tancament de sessió.
 */
public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private AuthenticationService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // obtenir la instància del servei d'autenticació
        authService = AuthenticationService.getInstance();

        // carregar les dades de l'usuari a la UI
        setupProfileInfo();

        // configurar el botó de Log out
        setupClickListeners();
        binding.navHome.setOnClickListener(v-> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }

    private void setupProfileInfo() {
        // recuperem el client que ha fet login prèviament
        Client loggedClient = authService.getLoggedInClient();

        if (loggedClient != null) {
            // assignem el nom d'usuari al TextView corresponent
            binding.txtUserFullName.setText(loggedClient.getUsername());
        }
    }

    private void setupClickListeners() {
        // configuració del botó de Log out (ID: btnLogout al XML)
        binding.btnLogout.setOnClickListener(v -> {
            // netegem la sessió al servei
            authService.logOut();

            // naveguem cap a la pantalla de Login
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            // netegem l'historial d'activitats per seguretat
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // alliberem la referència del binding per evitar fugues de memòria
        binding = null;
    }
}