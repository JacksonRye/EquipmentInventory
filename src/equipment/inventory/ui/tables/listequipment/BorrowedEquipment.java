package equipment.inventory.ui.tables.listequipment;

import javafx.beans.property.SimpleStringProperty;

public class BorrowedEquipment extends Equipment {

    private final SimpleStringProperty borrowedBy;
    private final SimpleStringProperty timeBorrowed;
    private final SimpleStringProperty timeReturned;

    public BorrowedEquipment(String equipmentId, String equipmentName,
                             Integer quantity,
                             String borrowedBy, String timeBorrowed,
                             String timeReturned) {

        super(equipmentId, equipmentName, quantity);
        this.borrowedBy = new SimpleStringProperty(borrowedBy);
        this.timeBorrowed = new SimpleStringProperty(timeBorrowed);
        this.timeReturned = new SimpleStringProperty(timeReturned);
    }

    public void setQuantity(Integer quantity) {
        super.setQuantity(quantity);
    }

    public String getBorrowedBy() {
        return borrowedBy.get();
    }

    public String getTimeBorrowed() {
        return timeBorrowed.get();
    }

    public String getTimeReturned() {
        return timeReturned.get();
    }


    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public String getName() {
        return super.getName();
    }


}
