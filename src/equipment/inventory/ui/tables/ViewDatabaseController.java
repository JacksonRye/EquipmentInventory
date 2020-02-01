package equipment.inventory.ui.tables;

import equipment.inventory.alert.AlertMaker;
import equipment.inventory.database.DataHelper;
import equipment.inventory.database.DatabaseHandler;
import equipment.inventory.model.BorrowedEquipment;
import equipment.inventory.model.Equipment;
import equipment.inventory.model.Staff;
import equipment.inventory.ui.addequipment.AddEquipmentController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewDatabaseController implements Initializable {

    DatabaseHandler handler = DatabaseHandler.getInstance();

    ObservableList<Equipment> equipmentList = FXCollections.observableArrayList();
    ObservableList<Staff> staffList = FXCollections.observableArrayList();
    ObservableList<BorrowedEquipment> borrowedEquipmentsList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Equipment, String> stockTabEquipmentID;
    @FXML
    private TableColumn<Equipment, String> stockTabEquipmentName;
    @FXML
    private TableColumn<Equipment, Integer> stockTabEquipmentQuantity;


    @FXML
    private TableView<Equipment> stockTableView;


    @FXML
    private TableColumn<Staff, String> staffIdCol;
    @FXML
    private TableColumn<Staff, String> staffFirstNameCol;
    @FXML
    private TableColumn<Staff, String> staffSurNameCol;
    @FXML
    private TableColumn<Staff, String> staffPhoneNumberCol;
    @FXML
    private TableColumn<Staff, String> staffEmailCol;
    @FXML
    private TableView<Staff> staffTableView;
    @FXML
    private TableView<BorrowedEquipment> inTableView;
    @FXML
    private TableColumn<BorrowedEquipment, String> inEquipmentId;
    @FXML
    private TableColumn<BorrowedEquipment, String> inEquipmentName;
    @FXML
    private TableColumn<BorrowedEquipment, String> inBorrowedBy;
    @FXML
    private TableColumn<BorrowedEquipment, String> inTimeBorrowed;
    @FXML
    private TableColumn<BorrowedEquipment, String> inTimeReturned;
    @FXML
    private TableColumn<BorrowedEquipment, Integer> inEquipmentQuantityBorrowed;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initStockCol();
        loadStockData();
        initStaffCol();
        loadStaffData();
        initInAndOut();
        loadInAndOut();
    }

    private void loadInAndOut() {
        borrowedEquipmentsList.clear();
        String query = "SELECT * FROM BORROWED_TABLE";
        ResultSet resultSet = handler.execQuery(query);

        try {
            while (resultSet.next()) {
                String equipmentId = resultSet.getString("equipmentId");
                String equipmentName = resultSet.getString("equipmentName");
                Integer quantityBorrowed = resultSet.getInt("quantityBorrowed");
                String borrowedBy = resultSet.getString("borrowedBy");
                String timeBorrowed = resultSet.getString("timeBorrowed");
                String timeReturned = resultSet.getString("timeReturned");

                borrowedEquipmentsList.add(new BorrowedEquipment(equipmentId,
                        equipmentName, quantityBorrowed, borrowedBy, timeBorrowed, timeReturned));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        inTableView.setItems(borrowedEquipmentsList);

    }

    private void initInAndOut() {
        inEquipmentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        inEquipmentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        inEquipmentQuantityBorrowed.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        inBorrowedBy.setCellValueFactory(new PropertyValueFactory<>("borrowedBy"));
        inTimeBorrowed.setCellValueFactory(new PropertyValueFactory<>("timeBorrowed"));
        inTimeReturned.setCellValueFactory(new PropertyValueFactory<>("timeReturned"));
    }

    private void initStaffCol() {
        staffIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        staffFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        staffSurNameCol.setCellValueFactory(new PropertyValueFactory<>("surName"));
        staffPhoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        staffEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }


    private void loadStaffData() {
        staffList.clear();

        String query = "SELECT * FROM STAFF_TABLE";
        ResultSet resultSet = handler.execQuery(query);

        try {
            while (resultSet.next()) {
                String id = resultSet.getString("staffId");
                String firstName = resultSet.getString("staffFirstName");
                String surName = resultSet.getString("staffSurName");
                String phoneNumber = resultSet.getString("phoneNumber");
                String email = resultSet.getString("email");

                staffList.add(new Staff(id, firstName, surName, phoneNumber,
                        email));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        staffTableView.setItems(staffList);

    }

    private void initStockCol() {
        stockTabEquipmentID.setCellValueFactory(new PropertyValueFactory<>("id"));
        stockTabEquipmentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockTabEquipmentQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

    }

    private void loadStockData() {
        equipmentList.clear();

        String query = "SELECT * FROM EQUIPMENT_STOCK";
        ResultSet resultSet = handler.execQuery(query);

        try {
            while (resultSet.next()) {
                String equipmentId = resultSet.getString("equipmentId");
                String equipmentName = resultSet.getString("equipmentName");
                Integer quantity = resultSet.getInt("quantityRemaining");

                equipmentList.add(new Equipment(equipmentId, equipmentName, quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        stockTableView.setItems(equipmentList);

    }


    @FXML
    private void handleStockEditMenuOperation(ActionEvent event) {
        Equipment selectedEquipment = stockTableView.getSelectionModel().getSelectedItem();
        if (selectedEquipment == null) {
            AlertMaker.showErrorMessage("No Equipment Selected", "Please select an Equipment");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/equipment/inventory/ui/addequipment/add_equipment.fxml"));
            Parent parent = loader.load();

            AddEquipmentController controller = loader.getController();
            controller.inflateUI(selectedEquipment);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Equipment");
            stage.setScene(new Scene(parent));
            stage.show();

            stage.setOnHiding((e) -> {
                handleRefresh(new ActionEvent());
            });

        } catch (IOException ex) {
            AlertMaker.showErrorMessage(ex);
        }
    }

    private void handleRefresh(ActionEvent event) {
        loadInAndOut();
        loadStaffData();
        loadStockData();
    }

    @FXML
    private void handleStockDeleteMenuOperation(ActionEvent event) {
        Equipment selectedEquipment = stockTableView.getSelectionModel().getSelectedItem();
        if (selectedEquipment == null) {
            AlertMaker.showErrorMessage("Error", "No Equipment Selected");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete " + selectedEquipment.getName() + "?");

        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.get() == ButtonType.OK) {
            if (DataHelper.deleteEquipment(selectedEquipment)) {
                AlertMaker.showSimpleAlert("Success", selectedEquipment.getName() + " deleted");
                handleRefresh(new ActionEvent());
                return;
            }
            AlertMaker.showErrorMessage("Error", "Item currently assigned to a staff. Cannot Delete");
        }

        return;


    }

//    TODO: Handle the following operations

    @FXML
    private void handleStaffEditMenuOperation(ActionEvent event) {
    }

    @FXML
    private void handleStaffDeleteMenuOperation(ActionEvent event) {
    }
}
