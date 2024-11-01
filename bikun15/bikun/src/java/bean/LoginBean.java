package bean;

import DAO.AdminDAO;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class LoginBean {
    private String username;
    private String password;
    private String message;

    private AdminDAO adminDAO = new AdminDAO();

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // AJAX validation methods
    public void validateUsername() {
        if (username == null || username.isEmpty()) {
            message = "Username cannot be empty";
        } else if (!adminDAO.isUsernameExists(username)) {
            message = "Username does not exist";
        } else {
            message = "";
        }
    }

    public void validatePassword() {
        if (password == null || password.isEmpty()) {
            message = "Password cannot be empty";
        } else if (!adminDAO.isPasswordCorrect(username, password)) {
            message = "Incorrect password";
        } else {
            message = "";
        }
    }

    public String login() {
        boolean isValid = adminDAO.validate(username, password);
        if (isValid) {
            message = "";  // Set message to empty string on successful login
            return "success"; // Redirect to success page
        } else {
            message = "Invalid Username or Password";
            return "error"; // Redirect to error page
        }
    }
}
