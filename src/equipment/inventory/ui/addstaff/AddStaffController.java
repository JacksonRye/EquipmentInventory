package equipment.inventory.ui.addstaff;

import com.jfoenix.controls.JFXTextField;
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

        Boolean result = DataHelper.insertNewStaff(staff);

        if (result) {
            System.out.println("Staff Added Successfully");
        } else {
            System.out.println("Failed to Add Staff");
        }
    }

    @FXML
    private void handleCancelOperation(ActionEvent event) {
        ((Stage)txtId.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseHandler = DatabaseHandler.getInstance();

    }
}
