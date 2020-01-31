package equipment.inventory.ui.main;

import equipment.inventory.utils.EquipmentInventoryUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class MainController {


    @FXML
    private TableView stockTableView;

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
    }

    @FXML
    private void fetchEquipment(ActionEvent event) {
    }
}
