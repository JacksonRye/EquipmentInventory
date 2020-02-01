package equipment.inventory.ui.issue;

import equipment.inventory.database.DataHelper;
import equipment.inventory.ui.tables.listequipment.BorrowedEquipment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class IssueDialogController implements Initializable {
    @FXML
    private Text txtIssueConfirmation;
    @FXML
    private Spinner spinnerQuantity;

    private BorrowedEquipment equipment;


    @FXML
    private void handleIssueOperation(ActionEvent event) {
        equipment.setQuantity((Integer) spinnerQuantity.getValue());
        if (DataHelper.insertBorrowedEquipment(equipment)) {
            System.out.println("Borrowed Equipment Inserted Successfully");
        } else {
            System.out.println("Borrowed Equipment Failed");
        }

    }

    public void inflateUI(BorrowedEquipment borrowedEquipment) {
        equipment = borrowedEquipment;
        txtIssueConfirmation.setText("You are about to Issue " +
                equipment.getId() + " to " + equipment.getBorrowedBy());

    }

    @FXML
    private void handleCancelOperation(ActionEvent event) {
        ((Stage) (txtIssueConfirmation.getScene().getWindow())).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> quantityValuesFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20000, 1);
        spinnerQuantity.setValueFactory(quantityValuesFactory);
    }
}
