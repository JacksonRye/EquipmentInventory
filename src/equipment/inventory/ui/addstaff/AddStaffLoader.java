package equipment.inventory.ui.addstaff;

import equipment.inventory.utils.EquipmentInventoryUtils;
import javafx.application.Application;
import javafx.stage.Stage;

public class AddStaffLoader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        EquipmentInventoryUtils.loadWindow(getClass().getResource("/equipment/inventory/ui/addstaff/add_staff.fxml"),
                "Add Staff", null);
    }
}
