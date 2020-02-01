package equipment.inventory.database;

import equipment.inventory.alert.AlertMaker;
import equipment.inventory.model.BorrowedEquipment;
import equipment.inventory.model.Equipment;
import equipment.inventory.model.Staff;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
            AlertMaker.showErrorMessage(ex);
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
            AlertMaker.showErrorMessage(ex);
        }
        return false;
    }


    public static boolean insertBorrowedEquipment(BorrowedEquipment equipment) {
        try {


            PreparedStatement preparedStatement = DatabaseHandler.getInstance().getConn().prepareStatement(
                    "INSERT INTO BORROWED_TABLE(equipmentId, equipmentName, quantityBorrowed," +
                            "borrowedBy) VALUES (?,?,?,?)"
            );

            preparedStatement.setString(1, equipment.getId());
            preparedStatement.setString(2, equipment.getName());
            preparedStatement.setInt(3, equipment.getQuantity());
            preparedStatement.setString(4, equipment.getBorrowedBy());

            PreparedStatement preparedStatement1 = DatabaseHandler.getInstance().getConn().prepareStatement(
                    "UPDATE EQUIPMENT_STOCK SET quantityRemaining = quantityRemaining - ? WHERE equipmentId = ?"
            );

//            TODO: Assert Quantity Remaining in the database is greater than quantity requested
            preparedStatement1.setInt(1, equipment.getQuantity());
            preparedStatement1.setString(2, equipment.getId());


            return (preparedStatement.executeUpdate() > 0 && preparedStatement1.executeUpdate() > 0);

        } catch (SQLException e) {
            AlertMaker.showErrorMessage(e);
        }

        return false;

    }


    public static boolean updateEquipment(Equipment equipment) {
        try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConn().prepareStatement(
                    "UPDATE EQUIPMENT_STOCK SET equipmentName = ?, " +
                            "quantityRemaining = ? WHERE equipmentId = ?"
            );

            statement.setString(1, equipment.getName());
            statement.setInt(2, equipment.getQuantity());
            statement.setString(3, equipment.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean deleteEquipment(Equipment equipment) {
        try {
            PreparedStatement preparedStatement = DatabaseHandler.getInstance().getConn().prepareStatement(
                    "DELETE FROM " + DatabaseHandler.EQUIPMENT_STOCK_TABLE +
                            " WHERE equipmentId = ?"
            );
            preparedStatement.setString(1, equipment.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
