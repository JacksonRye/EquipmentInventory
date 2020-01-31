package equipment.inventory.ui.tables.listequipment;

import javafx.beans.property.SimpleStringProperty;

public class BorrowedEquipment {

    private final SimpleStringProperty equipmentId;
    private final SimpleStringProperty equipmentName;
    private final SimpleStringProperty borrowedBy;
    private final SimpleStringProperty timeBorrowed;
    private final SimpleStringProperty timeReturned;

    public BorrowedEquipment(String equipmentId, String equipmentName,
                             String borrowedBy, String timeBorrowed,
                             String timeReturned) {
        super();
        this.equipmentId = new SimpleStringProperty(equipmentId);
        this.equipmentName = new SimpleStringProperty(equipmentName);
        this.borrowedBy = new SimpleStringProperty(borrowedBy);
        this.timeBorrowed = new SimpleStringProperty(timeBorrowed);
        this.timeReturned = new SimpleStringProperty(timeReturned);
    }

    public String getEquipmentId() {
        return equipmentId.get();
    }

    public String getEquipmentName() {
        return equipmentName.get();
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

}
