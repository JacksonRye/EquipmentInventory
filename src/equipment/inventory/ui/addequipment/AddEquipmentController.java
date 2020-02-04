package equipment.inventory.ui.addequipment;

import com.jfoenix.controls.JFXTextField;
import equipment.inventory.alert.AlertMaker;
import equipment.inventory.database.DataHelper;
import equipment.inventory.database.DatabaseHandler;
import equipment.inventory.model.Equipment;
import equipment.inventory.ui.main.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

        String mId = inputId.getText();
        String mName = inputName.getText();
        Integer mQuantity = (Integer) spinnerQuantity.getValue();

        Boolean flag = mName.isEmpty() || mId.isEmpty();

        if (flag) {
            AlertMaker.showErrorMessage("Empty Field", "Please Fill All Fields");
            return;
        }

        if (isInEditMode) {
            handleUpdateEquipment();
            return;
        }

        Equipment equipment = new Equipment(mId, mName,
                mQuantity);

        boolean result = DataHelper.insertNewEquipment(equipment);

        if (result) {
            try {
//               TODO: REFESH COMBOBOX AFTER ADD EQUIPMENTS

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/equipment/inventory/ui/main/Main.fxml"));
                loader.load();
                MainController controller = loader.getController();
                controller.loadComboBox();
            } catch (Exception e) {
                e.printStackTrace();
            }
            AlertMaker.showSimpleAlert("Success", "Equipment Added Successfully");
            clearCache();
        } else {
            AlertMaker.showErrorMessage("Error", "Equipment Addition Failed");
        }

    }

    private void handleUpdateEquipment() {
        Equipment equipment = new Equipment(inputId.getText(), inputName.getText(), (Integer) spinnerQuantity.getValue());
        if (DataHelper.updateEquipment(equipment)) {
            AlertMaker.showSimpleAlert("Success", "Edit SuccessFul");
            ((Stage) inputId.getScene().getWindow()).close();
        } else {
            AlertMaker.showErrorMessage("Error", "Edit Unsuccessful");
        }
    }

    void clearCache() {
        inputId.setText("");
        inputName.setText("");
    }

    @FXML
    private void handleCancelOperation(ActionEvent event) {
        ((Stage) inputId.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseHandler = DatabaseHandler.getInstance();

        SpinnerValueFactory<Integer> quantityValuesFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20000, 1);
        spinnerQuantity.setValueFactory(quantityValuesFactory);
    }

    public void inflateUI(Equipment selectedEquipment) {
        isInEditMode = true;
        inputId.setText(selectedEquipment.getId());
        inputId.setEditable(false);
        inputName.setText(selectedEquipment.getName());
    }
}
