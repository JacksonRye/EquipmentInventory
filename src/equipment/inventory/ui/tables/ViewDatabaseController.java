package equipment.inventory.ui.tables;

import equipment.inventory.database.DatabaseHandler;
import equipment.inventory.ui.tables.listequipment.BorrowedEquipment;
import equipment.inventory.ui.tables.listequipment.Equipment;
import equipment.inventory.ui.tables.liststaffs.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
