package equipment.inventory.ui.main;

import com.jfoenix.controls.JFXTextField;
import equipment.inventory.alert.AlertMaker;
import equipment.inventory.database.DatabaseHandler;
import equipment.inventory.model.BorrowedEquipment;
import equipment.inventory.ui.issue.IssueDialogController;
import equipment.inventory.utils.EquipmentInventoryUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.Timestamp;
import java.util.Date;

import static equipment.inventory.database.DatabaseHandler.getInstance;

public class MainController {

    DatabaseHandler handler = getInstance();

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
    private JFXTextField txtFieldReturnEquipmentId;
    @FXML
    private Text txtReturnEquipmentName;
    @FXML
    private Text txtReturnQuantityBorrowed;
    @FXML
    private Text txtReturnBorrowedBy;
    @FXML
    private Text txtReturnStaffName;
    @FXML
    private Text txtReturnStaffPhoneNumber;
    @FXML
    private Text txtReturnStaffEmail;
    @FXML
    private Text txtReturnDateIssued;
    private boolean isReadyForReturn = false;

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

    public void clearStaffCache() {
        txtStaffName.setText("");
        txtPhoneNumber.setText("");
        txtEmail.setText("");
    }

    public void clearEquipmentCache() {
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

        //  TODO: THIS IS WHERE YOU FIND INTERCONTROLLER INTERACTION

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

            dialogController.inflateUI(borrowedEquipment, this);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Issue Equipments");
            stage.setScene(new Scene(parent));
            stage.show();
            return;
        }

    }

    @FXML
    private void loadEquipmentInfo(ActionEvent event) {
        clearEntries();
        ObservableList<String> borrowedData = FXCollections.observableArrayList();
        isReadyForReturn = false;

        try {
            String equipmentId = txtFieldReturnEquipmentId.getText();
            String query = "SELECT BORROWED_TABLE.equipmentId, BORROWED_TABLE.equipmentName, BORROWED_TABLE.quantityBorrowed," +
                    "BORROWED_TABLE.borrowedBy, BORROWED_TABLE.timeBorrowed,\n" +
                    "STAFF_TABLE.staffId, STAFF_TABLE.staffFirstName, STAFF_TABLE.staffSurname, STAFF_TABLE.phoneNumber, STAFF_TABLE.email\n" +
                    "FROM BORROWED_TABLE\n" +
                    "LEFT JOIN STAFF_TABLE\n" +
                    "ON BORROWED_TABLE.borrowedBy = STAFF_TABLE.staffId\n" +
                    "LEFT JOIN EQUIPMENT_STOCK\n" +
                    "ON BORROWED_TABLE.equipmentId = EQUIPMENT_STOCK.equipmentId\n" +
                    "WHERE BORROWED_TABLE.equipmentId = '" + equipmentId + "'";

            ResultSet resultSet = handler.execQuery(query);
            if (resultSet.next()) {
                txtReturnEquipmentName.setText(resultSet.getString("equipmentName"));
                txtReturnBorrowedBy.setText(resultSet.getString("borrowedBy"));

                Timestamp mDateIssued = resultSet.getTimestamp("timeBorrowed");
                Date dateOfIssue = new Date(mDateIssued.getTime());
                String timeFormatForHumans = EquipmentInventoryUtils.formatDateTimeString(dateOfIssue);
                txtReturnDateIssued.setText(timeFormatForHumans);

                txtReturnQuantityBorrowed.setText(String.valueOf(resultSet.getInt("quantityBorrowed")));
                txtReturnStaffEmail.setText(resultSet.getString("email"));
                txtReturnStaffName.setText(resultSet.getString("staffFirstName")
                        + " " + resultSet.getString("staffSurname"));
                txtReturnStaffPhoneNumber.setText(resultSet.getString("phoneNumber"));

                isReadyForReturn = true;
            } else {
                AlertMaker.showSimpleAlert("Not Found", "Item not in Database");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clearEntries() {
        clearStaffCache();
        clearEquipmentCache();
    }

    @FXML
    private void handleEquipmentReturnOperation(ActionEvent event) {
    }

    @FXML
    private void handleReturnCancelOperation(ActionEvent event) {
    }
}
