package equipment.inventory.ui.addstaff;

import com.jfoenix.controls.JFXTextField;
import equipment.inventory.alert.AlertMaker;
import equipment.inventory.database.DataHelper;
import equipment.inventory.database.DatabaseHandler;
import equipment.inventory.model.Staff;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddStaffController implements Initializable {

    DatabaseHandler databaseHandler;

    Boolean isInEditMode = Boolean.FALSE;

    @FXML
    private JFXTextField txtId;
    @FXML
    private JFXTextField txtFirstName;
    @FXML
    private JFXTextField txtSurname;
    @FXML
    private JFXTextField txtPhoneNumber;
    @FXML
    private JFXTextField txtEmail;

    @FXML
    private void handleSaveOperation(ActionEvent event) {
        Staff staff = new Staff(txtId.getText(), txtFirstName.getText(), txtSurname.getText(), txtPhoneNumber.getText(),
                txtEmail.getText());

        if (isInEditMode) {
            if (DataHelper.updateStaff(staff)) {
                AlertMaker.showSimpleAlert("Success", "Staff Update Successful");
                ((Stage) txtId.getScene().getWindow()).close();
                return;
            }
            AlertMaker.showErrorMessage("Error", "Failed to update Staff");
            return;
        }


        Boolean result = DataHelper.insertNewStaff(staff);

        if (result) {
            AlertMaker.showSimpleAlert("Success", "Staff Added Successfully");
            clearFields();
            return;

        } else {
            AlertMaker.showErrorMessage("Error", "Failed to Add Staff");
            return;
        }
    }

    void clearFields() {
        txtId.clear();
        txtEmail.clear();
        txtPhoneNumber.clear();
        txtSurname.clear();
        txtFirstName.clear();
    }

    @FXML
    private void handleCancelOperation(ActionEvent event) {
        ((Stage) txtId.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseHandler = DatabaseHandler.getInstance();

    }

    public void inflateUI(Staff selectedStaff) {
        txtId.setText(selectedStaff.getId());
        txtId.setEditable(false);
        txtFirstName.setText(selectedStaff.getFirstName());
        txtSurname.setText(selectedStaff.getSurName());
        txtPhoneNumber.setText(selectedStaff.getPhoneNumber());
        txtEmail.setText(selectedStaff.getEmail());

        isInEditMode = Boolean.TRUE;


    }
}
