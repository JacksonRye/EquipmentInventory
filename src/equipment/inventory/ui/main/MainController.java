package equipment.inventory.ui.main;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import equipment.inventory.alert.AlertMaker;
import equipment.inventory.database.DatabaseHandler;
import equipment.inventory.model.Equipment;
import equipment.inventory.ui.main.item.ItemController;
import equipment.inventory.utils.EquipmentInventoryUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;

import static equipment.inventory.database.DatabaseHandler.getInstance;

public class MainController implements Initializable {

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
    private VBox vboxCart;
    @FXML
    private JFXComboBox comboBoxEquipments;
    private ObservableList<Equipment> databaseEquipmentList = FXCollections.observableArrayList();


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
    private void handleIssueOperation(ActionEvent event) {
//    TODO: HANDLE ISSUE OPERATION

        }


    @FXML
    private void loadEquipmentInfo(ActionEvent event) {
        ObservableList<String> borrowedData = FXCollections.observableArrayList();
        isReadyForReturn = false;

        try {
            String issueId = txtFieldReturnEquipmentId.getText();
            String query = "SELECT BORROWED_TABLE.equipmentId, BORROWED_TABLE.equipmentName, BORROWED_TABLE.quantityBorrowed," +
                    "BORROWED_TABLE.borrowedBy, BORROWED_TABLE.timeBorrowed,\n" +
                    "STAFF_TABLE.staffId, STAFF_TABLE.staffFirstName, STAFF_TABLE.staffSurname, STAFF_TABLE.phoneNumber, STAFF_TABLE.email\n" +
                    "FROM BORROWED_TABLE\n" +
                    "LEFT JOIN STAFF_TABLE\n" +
                    "ON BORROWED_TABLE.borrowedBy = STAFF_TABLE.staffId\n" +
                    "LEFT JOIN EQUIPMENT_STOCK\n" +
                    "ON BORROWED_TABLE.equipmentId = EQUIPMENT_STOCK.equipmentId\n" +
                    "WHERE BORROWED_TABLE.issueId = " + issueId + "";

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


    @FXML
    private void handleEquipmentReturnOperation(ActionEvent event) {
    }

    @FXML
    private void handleReturnCancelOperation(ActionEvent event) {
    }

    @FXML
    private void handleSettingsOperation(ActionEvent event) {
    }

    @FXML
    private void clearCart(ActionEvent event) {
    }

    @FXML
    private void addToCart(ActionEvent event) {
        String selectedItem = String.valueOf(comboBoxEquipments.getSelectionModel().getSelectedItem());
        vboxCart.getChildren().add(new ItemController(new Equipment("1", selectedItem, 3)));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadComboBox();
    }

    //  TODO: CREATE AUTO-COMPLETE COMBO BOX
    public void loadComboBox() {
        databaseEquipmentList.clear();
        try {

            PreparedStatement preparedStatement = DatabaseHandler.getInstance()
                    .getConn().prepareStatement("SELECT * FROM EQUIPMENT_STOCK");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String equipmentName = resultSet.getString("equipmentName");
                String equipmentId = resultSet.getString("equipmentId");
                Integer remainingQuantity = resultSet.getInt("quantityRemaining");
                databaseEquipmentList.add(new Equipment(equipmentId, equipmentName, remainingQuantity));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        comboBoxEquipments.setItems(databaseEquipmentList);
    }
}
