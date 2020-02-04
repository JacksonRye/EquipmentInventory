package equipment.inventory.ui.main.item;

import equipment.inventory.model.BorrowedEquipment;
import equipment.inventory.ui.main.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemController extends HBox implements Initializable {

    private final MainController mainController;
    ObservableList controlList = FXCollections.observableArrayList();
    private BorrowedEquipment equipment;

    private Text name = new Text("text");
    private Text id = new Text("text");
    private Spinner quantity = new Spinner();
    private Button cancelBtn = new Button("X");

    {
        controlList.addAll(id, name, quantity, cancelBtn);

    }


    public ItemController(BorrowedEquipment equipment, MainController mainController) {
        this.equipment = equipment;
        this.mainController = mainController;
        for (Object control : controlList) {
            HBox.setMargin((Node) control, new Insets(10, 10, 10, 10));
        }
        id.setText(equipment.getId());
        name.setText(equipment.getName());
        quantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 1));
        cancelBtn.setOnAction((e) -> removeItem(ItemController.this));
        this.getChildren().addAll(controlList);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private void removeItem(ItemController itemController) {
        mainController.removeFromCart(itemController);
    }

    public String getIdText() {
        return id.getText();
    }

    public BorrowedEquipment getSelectedItem() {
        return equipment;
    }

    public Integer getQuantity() {
        return (Integer) quantity.getValue();
    }
}
