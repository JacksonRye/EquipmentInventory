package equipment.inventory.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import equipment.inventory.ui.settings.Preferences;
import equipment.inventory.utils.EquipmentInventoryUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

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
                && pass.equals(preferences.getPassword())) {
            EquipmentInventoryUtils.loadWindow(getClass().getResource("/equipment/inventory/ui/main/Main.fxml"),
                    "Equipment Inventory", null);
            ((Stage) lblUsername.getScene().getWindow()).close();
            System.out.println("Login Successful");
        } else {
            lblUsername.getStyleClass().add("wrong-credentials");
            lblPassword.getStyleClass().add("wrong-credentials");

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

}
