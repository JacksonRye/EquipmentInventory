package equipment.inventory.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Equipment {
    private final SimpleStringProperty id;
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty quantity;

    public Equipment(String id, String name, Integer quantity) {
        super();
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }


    public int getQuantity() {
        return quantity.get();
    }


    @Override
    public String toString() {
        return name.get();
    }
}
