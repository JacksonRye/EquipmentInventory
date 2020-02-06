package equipment.inventory.ui.about;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class AboutController {
    @FXML
    private JFXButton okButton;

    @FXML
    private void handleOk(ActionEvent event) {
        ((Stage) okButton.getScene().getWindow()).close();
    }
}
