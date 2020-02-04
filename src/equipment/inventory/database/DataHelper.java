package equipment.inventory.database;

import equipment.inventory.alert.AlertMaker;
import equipment.inventory.model.BorrowedEquipment;
import equipment.inventory.model.Equipment;
import equipment.inventory.model.Staff;
import equipment.inventory.ui.main.item.ItemController;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public static boolean insertBorrowedEquipment(ObservableList<ItemController> itemList, Staff staff) {
        try {
            DatabaseHandler.getInstance().getConn().setAutoCommit(false);
            System.out.println("autocommit off");
            for (ItemController item : itemList) {
                PreparedStatement preparedStatement = DatabaseHandler.getInstance().getConn().prepareStatement(
                        "SELECT quantityRemaining FROM EQUIPMENT_STOCK where equipmentId = ?"
                );
                preparedStatement.setString(1, item.getSelectedItem().getId());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    if (resultSet.getInt("quantityRemaining") < item.getQuantity()) {
                        AlertMaker.showErrorMessage("Out of Stock", "You only have " + resultSet.getInt("quantityRemaining")
                                + " of " + item.getSelectedItem().getName() + " in stock");
                        DatabaseHandler.getInstance().getConn().rollback();
                        System.out.println("Performed Rollback");
                        return false;
                    }
                }

                PreparedStatement statement = DatabaseHandler.getInstance().getConn().prepareStatement(
                        "INSERT INTO BORROWED_TABLE(equipmentId, equipmentName," +
                                "quantityBorrowed, borrowedBy, timeBorrowed, timeReturned, issueNo) VALUES (?, ?, ?, ?, ?, ?, ?)"
                );

                statement.setString(1, item.getSelectedItem().getId());
                statement.setString(2, item.getSelectedItem().getName());
                statement.setInt(3, item.getQuantity());
                statement.setString(4, staff.getId());
                statement.setString(5, item.getSelectedItem().getTimeBorrowed());
                statement.setString(6, null);
                statement.setString(7, item.getSelectedItem().getIssueId());

                if (statement.executeUpdate() > 0) {
                    System.out.println("Did we get here?");
                    PreparedStatement statement1 = DatabaseHandler.getInstance().getConn().prepareStatement(
                            "UPDATE EQUIPMENT_STOCK SET quantityRemaining = quantityRemaining - ?"
                    );
                    statement1.setInt(1, item.getQuantity());
                    if (statement1.executeUpdate() > 0) continue;
                }

            }
            DatabaseHandler.getInstance().getConn().commit();
            System.out.println("commit");
            DatabaseHandler.getInstance().getConn().setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DatabaseHandler.getInstance().getConn().setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public static boolean insertBorrowedEquipment(BorrowedEquipment equipment) {
        try {
            String query = "SELECT quantityRemaining FROM EQUIPMENT_STOCK WHERE equipmentId = ?";
            PreparedStatement statement = DatabaseHandler.getInstance().getConn().prepareStatement(query);
            statement.setString(1, equipment.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Integer quantityRemaining = rs.getInt("quantityRemaining");
                if (equipment.getQuantity() > quantityRemaining) {
                    AlertMaker.showErrorMessage("Error", "You only have "
                            + quantityRemaining + " of " + equipment.getName() + " in the database," +
                            " please select 2 or less");
                    return false;
                }
            }


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

    public static boolean deleteStaff(Staff staff) {
        try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConn().prepareStatement(
                    "DELETE FROM " + DatabaseHandler.STAFF_TABLE + " WHERE staffId = ?"
            );
            statement.setString(1, staff.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean updateStaff(Staff staff) {
        try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConn().prepareStatement(
                    "UPDATE " + DatabaseHandler.STAFF_TABLE + " SET staffFirstName = ?, " +
                            "staffSurname = ?, phoneNumber = ?, email = ? " +
                            "WHERE staffId = ?"
            );
            statement.setString(1, staff.getFirstName());
            statement.setString(2, staff.getSurName());
            statement.setString(3, staff.getPhoneNumber());
            statement.setString(4, staff.getEmail());
            statement.setString(5, staff.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
