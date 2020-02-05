package equipment.inventory.ui.returndialog;

import equipment.inventory.database.DataHelper;
import equipment.inventory.ui.main.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ReturnController implements Initializable {
    @FXML
    private VBox vBox;

    private String issueNo;

    @FXML
    private void handleSaveReturn(ActionEvent event) {
        DataHelper.returnEquipments(MainController.returnItems, issueNo);
        MainController.returnItems.clear();
    }

    @FXML
    private void handleCancelReturn(ActionEvent event) {
        ((Stage) vBox.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vBox.getChildren().setAll(MainController.returnItems);
    }

    public void setIssueNo(String text) {
        issueNo = text;
    }
}
