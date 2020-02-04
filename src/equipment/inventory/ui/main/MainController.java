package equipment.inventory.ui.main;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import equipment.inventory.database.DatabaseHandler;
import equipment.inventory.model.BorrowedEquipment;
import equipment.inventory.model.Equipment;
import equipment.inventory.ui.main.item.ItemController;
import equipment.inventory.utils.EquipmentInventoryUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static equipment.inventory.database.DatabaseHandler.getInstance;

public class MainController implements Initializable {

    DatabaseHandler handler = getInstance();

    private final static String EQUIPMENT_DOES_NOT_EXIST = "Equipment Does Not Exist";
    private final static String STAFF_DOES_NOT_EXIST = "Staff Does Not Exist";

    @FXML
    private JFXTextField txtFieldReturnEquipmentId;

    private boolean isReadyForReturn = false;

    @FXML
    private VBox vboxCart;

    public static ObservableList<ItemController> cartItems = FXCollections.observableArrayList();

    @FXML
    private JFXComboBox comboBoxEquipments;

    private ObservableList<Equipment> databaseEquipmentList = FXCollections.observableArrayList();
    @FXML
    private JFXTextArea textIssueReport;


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
        EquipmentInventoryUtils.loadWindow(getClass().getResource("/equipment/inventory/ui/issue/issue_dialog.fxml"),
                "Issue", null);
    }


    @FXML
    private void loadIssueInfo(ActionEvent event) {
        ObservableList<String> borrowedData = FXCollections.observableArrayList();
        isReadyForReturn = false;

        try {
            String issueNo = txtFieldReturnEquipmentId.getText();
            String query = "SELECT BORROWED_TABLE.equipmentId, BORROWED_TABLE.equipmentName, BORROWED_TABLE.quantityBorrowed," +
                    "BORROWED_TABLE.borrowedBy, BORROWED_TABLE.timeBorrowed,\n" +
                    "STAFF_TABLE.staffId, STAFF_TABLE.staffFirstName, STAFF_TABLE.staffSurname, STAFF_TABLE.phoneNumber, STAFF_TABLE.email\n" +
                    "FROM BORROWED_TABLE\n" +
                    "LEFT JOIN STAFF_TABLE\n" +
                    "ON BORROWED_TABLE.borrowedBy = STAFF_TABLE.staffId\n" +
                    "LEFT JOIN EQUIPMENT_STOCK\n" +
                    "ON BORROWED_TABLE.equipmentId = EQUIPMENT_STOCK.equipmentId\n" +
                    "WHERE BORROWED_TABLE.issueNo = ?";

            PreparedStatement statement = handler.getConn().prepareStatement(query);
            statement.setString(1, issueNo);
            ResultSet resultSet = statement.executeQuery();
            StringBuilder stringBuilder = new StringBuilder();
            if (resultSet.next()) {
                stringBuilder.append("Staff ID: " + resultSet.getString("staffId"));
            }
            while (resultSet.next()) {

            }
            textIssueReport.setText(stringBuilder.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // TODO: HANDLE EQUIPMENT RETURN
    @FXML
    private void handleEquipmentReturnOperation(ActionEvent event) {
    }

    //    TODO: HANDLE CANCEL RETURN OPERATION
    @FXML
    private void handleReturnCancelOperation(ActionEvent event) {
    }

    //    TODO: CREATE SETTINGS PANEL
    @FXML
    private void handleSettingsOperation(ActionEvent event) {
    }

    //    TODO: IMPLEMENT CLEAR SELECTION ON BUTTON PRESS AND ON ISSUE COMPLETE
    @FXML
    private void clearCart(ActionEvent event) {
    }

    @FXML
    private void addToCart(ActionEvent event) {
        Equipment selectedItem = (Equipment) comboBoxEquipments.getSelectionModel().getSelectedItem();
        for (ItemController item : cartItems) {
            if (item.getIdText().equals(selectedItem.getId())) return;
        }
        ItemController item = new ItemController(new BorrowedEquipment(selectedItem), this);
        cartItems.add(item);
        vboxCart.getChildren().setAll(cartItems);
    }

    public void removeFromCart(ItemController itemController) {
        cartItems.remove(itemController);
        vboxCart.getChildren().setAll(cartItems);
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
