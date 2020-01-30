package equipment.inventory.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import equipment.inventory.ui.settings.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    Preferences preferences;

    @FXML
    private JFXTextField lblUsername;
    @FXML
    private JFXPasswordField lblPassword;

    @FXML
    private void handleLogin(ActionEvent event) {
        String pass = DigestUtils.sha1Hex(lblPassword.getText());
        if (lblUsername.getText().equals(preferences.getUsername())
                && pass.equals(preferences.getPassword())){
            loadWindow("Equipment Inventory", "/equipment/inventory/ui/main/Main.fxml");
            ((Stage)lblUsername.getScene().getWindow()).close();
            System.out.println("Login Successful");
        } else {
            System.out.println("Error Login");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        preferences = Preferences.getPreferences();
    }

    public void loadWindow(String title, String loc) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(loc));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
