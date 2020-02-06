package equipment.inventory.ui.settings;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import equipment.inventory.alert.AlertMaker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXPasswordField confirmPassword;

    @FXML
    private void handleSave(ActionEvent event) {
        Preferences preferences = Preferences.getPreferences();

        String usernameText = username.getText().trim();
        String passwordText = password.getText();
        String confirmPasswordText = confirmPassword.getText();

        if (usernameText.isEmpty() ||
                passwordText.trim().isEmpty() || confirmPasswordText.trim().isEmpty()) {
            AlertMaker.showErrorMessage("Error", "Empty Fields");
            return;
        }

        if (!passwordText.equals(confirmPasswordText)) {
            password.getStyleClass().add("wrong-credentials");
            confirmPassword.getStyleClass().add("wrong-credentials");
            return;
        }

        preferences.setPassword(passwordText);
        preferences.setUsername(usernameText);

        Preferences.writePreferenceToFile(preferences);

        AlertMaker.showSimpleAlert("Success", "Settings Updated Successful");

        ((Stage) password.getScene().getWindow()).close();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        ((Stage) password.getScene().getWindow()).close();
    }

    private void initDefaultValues() {
        Preferences preferences = Preferences.getPreferences();
        username.setText(String.valueOf(preferences.getUsername()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initDefaultValues();
    }
}
