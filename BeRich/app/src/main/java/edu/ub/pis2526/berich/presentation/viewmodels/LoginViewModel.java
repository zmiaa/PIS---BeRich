package edu.ub.pis2526.berich.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import edu.ub.pis2526.berich.data.services.AuthenticationService;
import edu.ub.pis2526.berich.domain.Client;

public class LoginViewModel extends ViewModel {
    private final AuthenticationService authenticationService = new AuthenticationService();
    private final MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
    public LiveData<Boolean> getLoginResult() {
        return loginResult;
    }

    public void doLogin(String email, String password) {
        authenticationService.logIn(email, password, new AuthenticationService.OnLogInListener() {
            @Override
            public void onLogInSuccess(Client client) {
                loginResult.postValue(true);
            }
            @Override
            public void onLogInError(Throwable throwable) {
                loginResult.postValue(false);
            }
        });
    }

}
