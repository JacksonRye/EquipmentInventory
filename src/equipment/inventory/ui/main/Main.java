package equipment.inventory.ui.main;

import equipment.inventory.database.DatabaseHandler;
import equipment.inventory.utils.EquipmentInventoryUtils;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        EquipmentInventoryUtils.loadWindow(getClass().getResource("/equipment/inventory/ui/login/login.fxml"),
                "Equipment Inventory", null);

        new Thread(() -> {
            DatabaseHandler.getInstance();
        }).start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
