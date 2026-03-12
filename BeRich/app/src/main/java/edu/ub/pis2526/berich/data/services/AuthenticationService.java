package edu.ub.pis2526.berich.data.services;

import edu.ub.pis2526.berich.data.services.mockdata.MockClientsHashMap;
import edu.ub.pis2526.berich.domain.Client;

/**
 * Cloud Firestore implementation of the data store.
 */
public class AuthenticationService {
  /* Attributes */
  private static MockClientsHashMap clients;
  static { clients = new MockClientsHashMap(); }

  /**
   * Empty constructor
   */
  @SuppressWarnings("unused")
  public AuthenticationService() { }

  /**
   * Log in client with username and password.
   * @param username the username
   * @param enteredPassword the password
   */
  public void logIn(String username, String enteredPassword, OnLogInListener listener)  {
    try{
      if (username.isEmpty())
        throw new Throwable("Username cannot be empty");
      if (enteredPassword.isEmpty())
        throw new Throwable("Password cannot be empty");
      if (!clients.containsKey(username))
        throw new Throwable("Username does not exist");

      Client client = clients.get(username);
      if (!client.getPassword().equals(enteredPassword))
        throw new Throwable("Incorrect password");

      listener.onLogInSuccess(client);
    }catch(Throwable throwable){
      listener.onLogInError(throwable);
    }
  }

  /**
   * Sign up a new client.
   * @param username the username
   * @param password the password
   * @param c_password the password confirmation
   */
  public void signUp(
      String username,
      String email,
      String password,
      String c_password,
      OnSignUpListener listener
  ){
    try {

      if (username.isEmpty())
        throw new Throwable("Username cannot be empty");
      if (email.isEmpty())
        throw new Throwable("Email cannot be empty");
      if (password.isEmpty())
        throw new Throwable("Password cannot be empty");
      if (!password.equals(c_password))
        throw new Throwable("Passwords do not match");
      if (clients.containsKey(username))
        throw new Throwable("User already exists");

      if (!isEmailValid(email)) {
        listener.onSignUpError(new Exception("El format del correu no és vàlid"));
        return;
      }
      // Si es correcto, creamos el cliente y lo añadimos al HashMap
      Client newClient = new Client(username, email, password);
      clients.put(username, newClient);
      listener.onSignUpSuccess();

    } catch (Throwable throwable) {
      // Notificamos error
      listener.onSignUpError(throwable);
    }
  }

  public interface OnLogInListener{
    void onLogInSuccess(Client client);
    void onLogInError(Throwable throwable);
  }

  public interface OnSignUpListener{
    void onSignUpSuccess();
    void onSignUpError(Throwable throwable);
  }

  private boolean isEmailValid(String email) {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
  }
}
