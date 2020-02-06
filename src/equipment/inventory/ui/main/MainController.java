package equipment.inventory.ui.main;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import equipment.inventory.alert.AlertMaker;
import equipment.inventory.database.DataHelper;
import equipment.inventory.database.DatabaseHandler;
import equipment.inventory.model.BorrowedEquipment;
import equipment.inventory.model.Equipment;
import equipment.inventory.ui.addequipment.AddEquipmentController;
import equipment.inventory.ui.issue.IssueDialogController;
import equipment.inventory.ui.main.item.ItemController;
import equipment.inventory.ui.returndialog.ReturnController;
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

    public static ObservableList<ItemController> returnItems = FXCollections.observableArrayList();

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
        AddEquipmentController addEquipmentController = (AddEquipmentController) EquipmentInventoryUtils.loadWindow(getClass().getResource("/equipment/inventory/ui/addequipment/add_equipment.fxml"),
                "Add Equipment", null);
        addEquipmentController.setUp(this);
    }

    @FXML
    private void handleViewDatabaseOperation(ActionEvent event) {
        EquipmentInventoryUtils.loadWindow(getClass().getResource("/equipment/inventory/ui/tables/view_database.fxml"),
                "View Database", null);
    }


    @FXML
    private void handleIssueOperation(ActionEvent event) {
        if (cartItems.isEmpty()) {
            AlertMaker.showErrorMessage("Error", "Cart is Empty, Add Items to it");
            return;
        }
        IssueDialogController issueDialogController = (IssueDialogController) EquipmentInventoryUtils.loadWindow(getClass().getResource("/equipment/inventory/ui/issue/issue_dialog.fxml"),
                "Issue", null);
        issueDialogController.setUp(this);
    }


    @FXML
    private void loadIssueInfo(ActionEvent event) {
        isReadyForReturn = false;
        returnItems.clear();

        if (DataHelper.isAlreadyReturned(txtFieldReturnEquipmentId.getText())) {
            textIssueReport.setText("These Issue has already been returned");
            return;
        }

        if (textIssueReport.getText() != null) {

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
                    stringBuilder.append("Staff Information---").append("\n").append("\n");

                    stringBuilder.append("\t").append(resultSet.getString("staffId")).append("\n");
                    stringBuilder.append("\t").append(resultSet.getString("staffSurname"))
                            .append(", ").append(resultSet.getString("staffFirstName")).append("\n");
                    stringBuilder.append("\t").append(resultSet.getString("phoneNumber")).append("\n");
                    stringBuilder.append("\t").append(resultSet.getString("email")).append("\n");
                    stringBuilder.append("\t").append(resultSet.getString("timeBorrowed")).append("\n");


                    stringBuilder.append("Equipment Information").append("\n").append("\n");

                    String equipmentName = resultSet.getString("equipmentName");
                    String equipmentId = resultSet.getString("equipmentId");
                    Integer quantityBorrowed = resultSet.getInt("quantityBorrowed");

                    stringBuilder.append("\t").append(equipmentName).append("\n");
                    stringBuilder.append("\t").append(quantityBorrowed).append("\n");
                    stringBuilder.append("\t").append(equipmentId).append("\n");

                    returnItems.add(new ItemController(new BorrowedEquipment(equipmentId, equipmentName, quantityBorrowed),
                            this));


                    while (true) {
                        if (resultSet.next()) {
                            equipmentName = resultSet.getString("equipmentName");
                            equipmentId = resultSet.getString("equipmentId");
                            quantityBorrowed = resultSet.getInt("quantityBorrowed");

                            stringBuilder.append("\t").append(equipmentName).append("\n");
                            stringBuilder.append("\t").append(quantityBorrowed).append("\n");
                            stringBuilder.append("\t").append(equipmentId).append("\n");
                            returnItems.add(new ItemController(new BorrowedEquipment(equipmentId, equipmentName, quantityBorrowed),
                                    this));
                        } else break;
                    }

                } else {
                    textIssueReport.setText("Issue No. not Valid");
                    return;
                }
                textIssueReport.setText(stringBuilder.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }


    @FXML
    private void handleEquipmentReturnOperation(ActionEvent event) {
        ReturnController controller = (ReturnController) EquipmentInventoryUtils.loadWindow(getClass().getResource("/equipment/inventory/ui/returndialog/return.fxml"),
                "Return Equipments", null);
        controller.setIssueNo(txtFieldReturnEquipmentId.getText());
//        txtFieldReturnEquipmentId.clear();
    }

    @FXML
    private void handleReturnCancelOperation(ActionEvent event) {
        txtFieldReturnEquipmentId.clear();
        textIssueReport.clear();
    }

    //    TODO: CREATE SETTINGS PANEL
    @FXML
    private void handleSettingsOperation(ActionEvent event) {
        EquipmentInventoryUtils.loadWindow(getClass().getResource("/equipment/inventory/ui/settings/settings.fxml"),
                "Settings", null);
    }

    @FXML
    public void clearCart(ActionEvent event) {
        cartItems.clear();
        setCartItems();
    }

    @FXML
    private void addToCart(ActionEvent event) {
        Equipment selectedItem = (Equipment) comboBoxEquipments.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            AlertMaker.showErrorMessage("Error", "Invalid Equipment selected");
            return;
        }
        for (ItemController item : cartItems) {
            if (item.getIdText().equals(selectedItem.getId())) return;
        }
        ItemController item = new ItemController(new BorrowedEquipment(selectedItem), this);
        cartItems.add(item);
        setCartItems();
    }

    public void removeFromCart(ItemController itemController) {
        cartItems.remove(itemController);
        setCartItems();
    }

    void setCartItems() {
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

    @FXML
    private void handleCloseButton(ActionEvent event) {
        AlertMaker.showSimpleAlert("Coming Soon", "In the future version");
        return;
    }

    @FXML
    private void handleLoadAbout(ActionEvent event) {
        EquipmentInventoryUtils.loadWindow(getClass().getResource("/equipment/inventory/ui/about/about.fxml"),
                "About", null);
        return;
    }
}
