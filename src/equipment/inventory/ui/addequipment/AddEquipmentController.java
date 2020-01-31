package equipment.inventory.ui.addequipment;

import com.jfoenix.controls.JFXTextField;
import equipment.inventory.database.DataHelper;
import equipment.inventory.database.DatabaseHandler;
import equipment.inventory.ui.tables.listequipment.Equipment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEquipmentController implements Initializable {

    DatabaseHandler databaseHandler;

    private Boolean isInEditMode = Boolean.FALSE;
    @FXML
    private JFXTextField inputId;
    @FXML
    private JFXTextField inputName;
    @FXML
    private Spinner spinnerQuantity;


    @FXML
    private void handleSaveOperation(ActionEvent event) {
        Equipment equipment = new Equipment(inputId.getText(), inputName.getText(),
                (int) spinnerQuantity.getValue());

        boolean result = DataHelper.insertNewEquipment(equipment);

        if (result) {
            System.out.println("Equipment Added Successfully");
        } else {
            System.out.println("Equipment Addition Failed");
        }

    }

    @FXML
    private void handleCancelOperation(ActionEvent event) {
        ((Stage) inputId.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseHandler = DatabaseHandler.getInstance();

        SpinnerValueFactory<Integer> quantityValuesFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spinnerQuantity.setValueFactory(quantityValuesFactory);
    }

}
