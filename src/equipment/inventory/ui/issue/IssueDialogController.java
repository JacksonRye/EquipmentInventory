package equipment.inventory.ui.issue;

import equipment.inventory.alert.AlertMaker;
import equipment.inventory.database.DataHelper;
import equipment.inventory.model.BorrowedEquipment;
import equipment.inventory.ui.main.MainController;
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

    private MainController controller;


    @FXML
    private void handleIssueOperation(ActionEvent event) {
        equipment.setQuantity((Integer) spinnerQuantity.getValue());
        if (DataHelper.insertBorrowedEquipment(equipment)) {
            AlertMaker.showSimpleAlert("Success", "Items Issued Successfully");
            ((Stage) spinnerQuantity.getScene().getWindow()).close();
        } else {
            System.out.println("Borrowed Equipment Failed");
        }

    }


    public void inflateUI(BorrowedEquipment borrowedEquipment, MainController mainController) {
        controller = mainController;
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
