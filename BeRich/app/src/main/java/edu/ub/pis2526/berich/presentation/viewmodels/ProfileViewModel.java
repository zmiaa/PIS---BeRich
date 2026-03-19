package edu.ub.pis2526.berich.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.ub.pis2526.berich.data.services.AuthenticationService;
import edu.ub.pis2526.berich.domain.Client;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<Client> clientState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutState = new MutableLiveData<>();

    private final AuthenticationService authService = AuthenticationService.getInstance();

    public LiveData<Client> getClientState() { return clientState; }
    public LiveData<Boolean> getLogoutState() { return logoutState; }

    public void fetchUserData() {
        Client client = authService.getLoggedInClient();
        clientState.postValue(client);
    }

    public void logOut() {
        authService.logOut();
        logoutState.postValue(true);
    }
}
