package equipment.inventory.ui.tables;

import equipment.inventory.utils.EquipmentInventoryUtils;
import javafx.application.Application;
import javafx.stage.Stage;

public class ViewDatabaseLoader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        EquipmentInventoryUtils.loadWindow(getClass().getResource("/equipment/inventory/ui/tables/view_database.fxml"),
                "View Database", null);
    }
}
