package equipment.inventory.ui.main.item;

import equipment.inventory.model.Equipment;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ItemLoader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ItemController itemController = new ItemController(new Equipment("1", "2", 3));

        primaryStage.setScene(new Scene(itemController));
        primaryStage.setTitle("Item Controller");
//        primaryStage.setWidth(300);
//        primaryStage.setHeight(100);
        primaryStage.show();
    }
}
