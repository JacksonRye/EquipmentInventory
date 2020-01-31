package equipment.inventory.ui.main;

import com.jfoenix.controls.JFXTextField;
import equipment.inventory.database.DatabaseHandler;
import equipment.inventory.utils.EquipmentInventoryUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {

    DatabaseHandler handler = DatabaseHandler.getInstance();

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
                txtStaffName.setText("No Such Staff Available");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

                System.out.println(equipmentName);

                flag = false;
            }
            if (flag) {
                txtEquipmentName.setText("Equipment Does Not Exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleIssueOperation(ActionEvent event) {
    }
}
