package edu.ub.pis2526.berich.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import edu.ub.pis2526.berich.data.services.AuthenticationService;

public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<RegisterState> registerState = new MutableLiveData<>();
    private final AuthenticationService authService = AuthenticationService.getInstance();

    public LiveData<RegisterState> getRegisterState() {
        return registerState;
    }

    public void signUp(String username, String email, String password, String c_password) {
        authService.signUp(username, email, password, c_password, new AuthenticationService.OnSignUpListener() {
            @Override
            public void onSignUpSuccess() {
                registerState.postValue(new RegisterState(true, null));
            }

            @Override
            public void onSignUpError(Throwable throwable) {

                registerState.postValue(new RegisterState(false, throwable.getMessage()));
            }
        });
    }

    public static class RegisterState {
        public final boolean success;
        public final String errorMessage;

        public RegisterState(boolean success, String errorMessage) {
            this.success = success;
            this.errorMessage = errorMessage;
        }
    }
}
