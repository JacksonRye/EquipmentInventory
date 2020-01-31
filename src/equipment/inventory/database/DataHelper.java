package equipment.inventory.database;

import equipment.inventory.ui.tables.listequipment.Equipment;
import equipment.inventory.ui.tables.liststaffs.Staff;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class DataHelper {

    public static boolean insertNewEquipment(Equipment equipment) {
        try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConn().prepareStatement(
                    "INSERT INTO EQUIPMENT_STOCK(equipmentId, equipmentName, quantityRemaining) " +
                            "VALUES (?, ?, ?)");
            statement.setString(1, equipment.getId());
            statement.setString(2, equipment.getName());
            statement.setInt(3, equipment.getQuantity());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean insertNewStaff(Staff staff) {
        try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConn().prepareStatement(
                    "INSERT INTO STAFF_TABLE(staffId, " +
                            "staffFirstName, staffSurname, phoneNumber, email) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, staff.getId());
            statement.setString(2, staff.getFirstName());
            statement.setString(3, staff.getSurName());
            statement.setString(4, staff.getPhoneNumber());
            statement.setString(5, staff.getEmail());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }


}
