package equipment.inventory.model;

import javafx.beans.property.SimpleStringProperty;

public class BorrowedEquipment extends Equipment {

    private final SimpleStringProperty borrowedBy;
    private final SimpleStringProperty timeBorrowed;
    private final SimpleStringProperty timeReturned;
    private final SimpleStringProperty issueId;

    public BorrowedEquipment(String equipmentId, String equipmentName,
                             Integer quantity,
                             String borrowedBy, String timeBorrowed,
                             String timeReturned, String issueId) {

        super(equipmentId, equipmentName, quantity);
        this.borrowedBy = new SimpleStringProperty(borrowedBy);
        this.timeBorrowed = new SimpleStringProperty(timeBorrowed);
        this.timeReturned = new SimpleStringProperty(timeReturned);
        this.issueId = new SimpleStringProperty(issueId);
    }

    public BorrowedEquipment(Equipment equipment) {
        super(equipment.getId(), equipment.getName(), equipment.getQuantity());
        this.issueId = new SimpleStringProperty();
        this.borrowedBy = new SimpleStringProperty();
        this.timeBorrowed = new SimpleStringProperty();
        this.timeReturned = new SimpleStringProperty();
    }

    public BorrowedEquipment(String id, String name, Integer quantity) {
        super(id, name, quantity);
        this.issueId = new SimpleStringProperty();
        this.borrowedBy = new SimpleStringProperty();
        this.timeBorrowed = new SimpleStringProperty();
        this.timeReturned = new SimpleStringProperty();
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

    public void setTimeBorrowed(String timeBorrowed) {
        this.timeBorrowed.set(timeBorrowed);
    }

    public void setTimeReturned(String timeReturned) {
        this.timeReturned.set(timeReturned);
    }

    public String getIssueId() {
        return issueId.get();
    }

    public void setIssueId(String issueId) {
        this.issueId.set(issueId);
    }

    public SimpleStringProperty issueIdProperty() {
        return issueId;
    }
}
