package equipment.inventory.ui.tables.liststaffs;


import javafx.beans.property.SimpleStringProperty;

public class Staff {
    private final SimpleStringProperty id;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty surName;
    private final SimpleStringProperty phoneNumber;
    private final SimpleStringProperty email;

    public Staff(String id, String firstName, String surName, String phoneNumber,
                 String email) {
        this.id = new SimpleStringProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.surName = new SimpleStringProperty(surName);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
    }

    public String getId() {
        return id.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getSurName() {
        return surName.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }


    public String getEmail() {
        return email.get();
    }

}

