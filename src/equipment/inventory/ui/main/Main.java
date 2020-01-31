package equipment.inventory.ui.main;

import equipment.inventory.database.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/equipment/inventory/ui/login/login.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        new Thread(() -> {
            DatabaseHandler.getInstance();
        }).start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
