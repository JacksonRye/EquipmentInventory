package equipment.inventory.ui.main.item;

import equipment.inventory.model.Equipment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ItemController extends HBox {

    ObservableList controlList = FXCollections.observableArrayList();

    private Text name = new Text("text");
    private Text id = new Text("text");
    private Spinner quantity = new Spinner();

    {
        controlList.addAll(name, id, quantity);

    }


    public ItemController(Equipment equipment) {
        for (Object control : controlList) {
            HBox.setMargin((Node) control, new Insets(10, 10, 10, 10));
        }
        id.setText(equipment.getId());
        name.setText(equipment.getName());
        this.getChildren().addAll(controlList);
    }


}
