package equipment.inventory.ui.main;

import com.jfoenix.controls.JFXTextField;
import equipment.inventory.alert.AlertMaker;
import equipment.inventory.database.DatabaseHandler;
import equipment.inventory.model.BorrowedEquipment;
import equipment.inventory.ui.issue.IssueDialogController;
import equipment.inventory.utils.EquipmentInventoryUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {

    DatabaseHandler handler = DatabaseHandler.getInstance();

    private final static String EQUIPMENT_DOES_NOT_EXIST = "Equipment Does Not Exist";
    private final static String STAFF_DOES_NOT_EXIST = "Staff Does Not Exist";

    @FXML
    private JFXTextField txtFieldStaffId;
    @FXML
    private Text txtStaffName;
    @FXML
    private Text txtPhoneNumber;
    @FXML
    private Text txtEmail;
    @FXML
    private Text txtQuantityRemaining;
    @FXML
    private Text txtEquipmentName;
    @FXML
    private JFXTextField txtFieldEquipmentId;

    @FXML
    private void handleAddStaffOperation(ActionEvent event) {
        EquipmentInventoryUtils.loadWindow(getClass().getResource("/equipment/inventory/ui/addstaff/add_staff.fxml"),
                "Add Staff", null);
    }

    @FXML
    private void handleAddEquipmentOperation(ActionEvent event) {
        EquipmentInventoryUtils.loadWindow(getClass().getResource("/equipment/inventory/ui/addequipment/add_equipment.fxml"),
                "Add Equipment", null);
    }

    @FXML
    private void handleViewDatabaseOperation(ActionEvent event) {
        EquipmentInventoryUtils.loadWindow(getClass().getResource("/equipment/inventory/ui/tables/view_database.fxml"),
                "View Database", null);
    }

    @FXML
    private void fetchStaff(ActionEvent event) {
        clearStaffCache();
        String staffId = txtFieldStaffId.getText();
        String query = "SELECT * FROM STAFF_TABLE WHERE staffId = '" + staffId + "'";
        ResultSet resultSet = handler.execQuery(query);

        Boolean flag = true;

        try {
            while (resultSet.next()) {
                String staffName = resultSet.getString("staffFirstName") +
                        " " + resultSet.getString("staffSurName");
                String phoneNumber = resultSet.getString("phoneNumber");
                String email = resultSet.getString("email");

                txtStaffName.setText(staffName);
                txtPhoneNumber.setText(phoneNumber);
                txtEmail.setText(email);

                flag = false;
            }
            if (flag) {
                txtStaffName.setText(STAFF_DOES_NOT_EXIST);
            }
        } catch (SQLException e) {
            AlertMaker.showErrorMessage(e);
        }


    }

    private void clearStaffCache() {
        txtStaffName.setText("");
        txtPhoneNumber.setText("");
        txtEmail.setText("");
    }

    private void clearEquipmentCache() {
        txtQuantityRemaining.setText("");
        txtEquipmentName.setText("");
    }

    @FXML
    private void fetchEquipment(ActionEvent event) {
        clearEquipmentCache();
        String equipmentId = txtFieldEquipmentId.getText();
        String query = "SELECT * FROM EQUIPMENT_STOCK WHERE equipmentId = '" + equipmentId + "'";
        ResultSet resultSet = handler.execQuery(query);

        Boolean flag = true;

        try {
            while (resultSet.next()) {

                String equipmentName = resultSet.getString("equipmentName");
                String quantityRemaining = resultSet.getString("quantityRemaining");

                txtQuantityRemaining.setText(quantityRemaining);
                txtEquipmentName.setText(equipmentName);

                flag = false;
            }
            if (flag) {
                txtEquipmentName.setText(EQUIPMENT_DOES_NOT_EXIST);
            }
        } catch (SQLException e) {
            AlertMaker.showErrorMessage(e);
        }

    }

    Boolean checkCorrectFields() {
        fetchEquipment(new ActionEvent());
        fetchStaff(new ActionEvent());

        if (txtEquipmentName.getText().equals(EQUIPMENT_DOES_NOT_EXIST) || txtEquipmentName.getText().isEmpty()
                || txtStaffName.getText().equals(STAFF_DOES_NOT_EXIST) || txtStaffName.getText().isEmpty()) {
            AlertMaker.showErrorMessage("Missing Fields", "Please Enter all Fields");
            return false;
        }

        return true;

    }


    @FXML
    private void handleIssueOperation(ActionEvent event) {

        if (checkCorrectFields()) {

            String selectedEquipment = txtFieldEquipmentId.getText();
            String selectedStaff = txtFieldStaffId.getText();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/equipment/inventory/ui/issue/issue_dialog.fxml"));
            Parent parent = null;
            try {
                parent = loader.load();
            } catch (IOException e) {
                AlertMaker.showErrorMessage(e);
            }

            BorrowedEquipment borrowedEquipment = new BorrowedEquipment(selectedEquipment, txtEquipmentName.getText(),
                    0, selectedStaff, null, null);

            IssueDialogController dialogController = loader.getController();
            dialogController.inflateUI(borrowedEquipment);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Issue Equipments");
            stage.setScene(new Scene(parent));
            stage.show();
            return;
        }

    }
}
