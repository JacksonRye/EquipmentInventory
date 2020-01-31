package equipment.inventory.ui.tables;

import equipment.inventory.database.DatabaseHandler;
import equipment.inventory.ui.tables.listequipment.Equipment;
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

    @FXML
    private TableColumn<Equipment, String> stockTabEquipmentID;
    @FXML
    private TableColumn<Equipment, String> stockTabEquipmentName;
    @FXML
    private TableColumn<Equipment, Integer> stockTabEquipmentQuantity;


    @FXML
    private TableView<Equipment> stockTableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initStockCol();
        loadData();
    }

    private void initStockCol() {
        stockTabEquipmentID.setCellValueFactory(new PropertyValueFactory<>("id"));
        stockTabEquipmentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockTabEquipmentQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

    }

    private void loadData() {
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
