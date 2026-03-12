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
   * @param email the username
   * @param enteredPassword the password
   */
  public void logIn(String email, String enteredPassword, OnLogInListener listener)  {
    try{
      if (email.isEmpty())
        throw new Throwable("Email cannot be empty");
      if (enteredPassword.isEmpty())
        throw new Throwable("Password cannot be empty");
      if (!isEmailValid(email)) {
        throw new Throwable("El format del correu no és vàlid");
      }

      Client clientFound = null;
      for (Client c : clients.values()) {
        if (c.getEmail().equalsIgnoreCase(email)) {
          clientFound = c;
          break;
        }
      }
      if (clientFound == null) {
        throw new Throwable("No user found with this email");
      }

      if (!clientFound.getPassword().equals(enteredPassword)) {
        throw new Throwable("Incorrect password");
      }


      listener.onLogInSuccess(clientFound);
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

      if (!isPasswordStrong(password)) {
        throw new Throwable("La contrasenya ha de tenir almenys 6 caràcters, una majúscula i un número");
      }

      for (Client c : clients.values()) {
        if (c.getEmail().equals(email)) {
          throw new Throwable("This email is already registered");
        }
      }

      Client newClient = new Client(username, email, password);
      clients.put(username, newClient);
      listener.onSignUpSuccess();

    } catch (Throwable throwable) {

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

  //Afegim requisits de la contrasenya
  private boolean isPasswordStrong(String password) {
    //Tener minimo 6 caracteres
    if (password.length() < 6) return false;

    boolean hasUppercase = false;
    boolean hasDigit = false;

    for (char c : password.toCharArray()) {
      if (Character.isUpperCase(c)) hasUppercase = true;
      if (Character.isDigit(c)) hasDigit = true;
      //Si cumple todo
      if (hasUppercase && hasDigit) return true;
    }
    return false;
  }
}
