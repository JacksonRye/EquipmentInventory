package equipment.inventory.ui.addequipment;


import equipment.inventory.utils.EquipmentInventoryUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class AddEquipmentLoader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        EquipmentInventoryUtils.loadWindow(getClass().getResource("/equipment/inventory/ui/addequipment/add_equipment.fxml"),
                "Add Equipment", null);
    }
}
