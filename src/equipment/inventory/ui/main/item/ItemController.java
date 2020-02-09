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
import javafx.scene.control.TextFormatter;
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
    private Spinner spinnerQuantity = new Spinner();
    private Button cancelBtn = new Button("X");

    {
        controlList.addAll(id, name, spinnerQuantity, cancelBtn);

    }


    public ItemController(BorrowedEquipment equipment, MainController mainController) {
        this.equipment = equipment;
        this.mainController = mainController;
        spinnerQuantity.setEditable(true);
        for (Object control : controlList) {
            HBox.setMargin((Node) control, new Insets(10, 10, 10, 10));
        }
        id.setText(equipment.getId());
        name.setText(equipment.getName());

        cancelBtn.setOnAction((e) -> removeItem(ItemController.this));
        this.getChildren().addAll(controlList);

        SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20000, 1);
        spinnerQuantity.setValueFactory(factory);

        TextFormatter formatter = new TextFormatter(factory.getConverter(), factory.getValue());
        spinnerQuantity.getEditor().setTextFormatter(formatter);
        factory.valueProperty().bindBidirectional(formatter.valueProperty());

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

    public Integer getSpinnerQuantity() {
        return Integer.parseInt(spinnerQuantity.getEditor().getText());
    }

    public void setSpinnerQuantity(Integer value) {
        spinnerQuantity.getValueFactory().setValue(value);
    }

}
