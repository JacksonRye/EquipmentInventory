package equipment.inventory.ui.addequipment;

import com.jfoenix.controls.JFXTextField;
import equipment.inventory.alert.AlertMaker;
import equipment.inventory.database.DataHelper;
import equipment.inventory.database.DatabaseHandler;
import equipment.inventory.model.Equipment;
import equipment.inventory.ui.main.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.StringConverter;

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
    private MainController mainController;

    private <T> void commitEditorText(Spinner<T> spinner) {
        if (!spinner.isEditable()) return;
        String text = spinner.getEditor().getText();
        SpinnerValueFactory<T> valueFactory = spinner.getValueFactory();
        if (valueFactory != null) {
            StringConverter<T> converter = valueFactory.getConverter();
            if (converter != null) {
                T value = converter.fromString(text);
                valueFactory.setValue(value);
            }
        }
    }


    @FXML
    private void handleSaveOperation(ActionEvent event) {

        String mId = inputId.getText();
        String mName = inputName.getText();
        Integer mQuantity = (Integer) spinnerQuantity.getValueFactory().getValue();

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
            AlertMaker.showSimpleAlert("Success", "Equipment Added Successfully");
            inputId.clear();
            inputName.clear();
            spinnerQuantity.getValueFactory().setValue(1);
            mainController.loadComboBox();
        } else {
            AlertMaker.showErrorMessage("Error", "Equipment with Id may already exist");
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
        spinnerQuantity.setEditable(true);

        TextFormatter formatter = new TextFormatter(quantityValuesFactory.getConverter(), quantityValuesFactory.getValue());
        spinnerQuantity.getEditor().setTextFormatter(formatter);
        quantityValuesFactory.valueProperty().bindBidirectional(formatter.valueProperty());
    }

    public void inflateUI(Equipment selectedEquipment) {
        isInEditMode = true;
        inputId.setText(selectedEquipment.getId());
        inputId.setEditable(false);
        inputName.setText(selectedEquipment.getName());
    }

    public void setUp(MainController mainController) {
        this.mainController = mainController;
    }
}
