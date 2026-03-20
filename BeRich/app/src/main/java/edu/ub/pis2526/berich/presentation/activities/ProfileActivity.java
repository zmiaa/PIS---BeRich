package edu.ub.pis2526.berich.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis2526.berich.data.services.AuthenticationService;
import edu.ub.pis2526.berich.databinding.ActivityProfileBinding;
import edu.ub.pis2526.berich.domain.Client;
import edu.ub.pis2526.berich.presentation.viewmodels.ProfileViewModel;


public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViewModel();
        initObservers();
        initWidgetListeners();

        profileViewModel.fetchUserData();
    }


    private void initViewModel() {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }
    private void initObservers() {
        profileViewModel.getClientState().observe(this, client -> {
            if (client != null) {
                binding.txtUserFullName.setText(client.getUsername());
            }
        });

        profileViewModel.getLogoutState().observe(this, isLoggedOut -> {
            if (isLoggedOut) {
                navigateToLogin();
            }
        });
    }

    private void initWidgetListeners() {
        binding.btnLogout.setOnClickListener(v -> profileViewModel.logOut());

        binding.navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
        });

        binding.fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, AddTransactionActivity.class));
        });
    }
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}