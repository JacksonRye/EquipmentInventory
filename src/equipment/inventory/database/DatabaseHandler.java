package equipment.inventory.database;

import javax.swing.*;
import java.sql.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class DatabaseHandler {

    private static DatabaseHandler handler = null;

    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;

    {
        createConnection();
        setUpStaffTable();
        setUpStockTable();
        setUpBorrowedTable();
    }

    private void setUpStaffTable() {
        String TABLE_NAME = "STAFF_TABLE";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();

            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(),
                    null);

            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists, Ready to go!");
            }

            else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "(" +
                        "staffId varchar(200) primary key, \n" +
                        "staffFirstName varchar(200), \n" +
                        "staffSurname varchar(200), \n" +
                        "phoneNumber varchar(20), \n" +
                        "email varchar(30)" +
                        ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void setUpStockTable() {
        String TABLE_NAME = "EQUIPMENT_STOCK";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();

            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(),
                    null);

            if (tables.next()){
                System.out.println("Table " + TABLE_NAME + " already exists, Ready to go!");
            }

            else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "( " +
                        "equipmentId varchar(200) primary key, \n" +
                        "equipmentName varchar(200), \n" +
                        "quantityRemaining INTEGER default 0" +
                        ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

        }
    }

    void setUpBorrowedTable() {
        String TABLE_NAME = "BORROWED_TABLE";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();

            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(),
                    null);

            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists, Ready to go!");
            }

            else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "(" +
                        "equipmentId varchar(200) primary key, \n" +
                        "equipmentName varchar(200),\n" +
                        "borrowedBy varchar(200), \n" +
                        "timeBorrowed timestamp default CURRENT_TIMESTAMP, \n" +
                        "timeReturned timestamp default null, \n " +
                        "FOREIGN KEY (equipmentId) REFERENCES EQUIPMENT_STOCK(equipmentId), \n" +
                        "FOREIGN KEY (equipmentName) REFERENCES EQUIPMENT_STOCK(equipmentName), \n" +
                        "FOREIGN KEY (borrowedBy) REFERENCES STAFF_TABLE(staffId)" +
                        ")");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

        }

    }

    private DatabaseHandler(){

    }



    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:databaseHandler " + ex.getLocalizedMessage());
            return null;
        }
        finally {

        }
        return result;
    }

    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " +
                    ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler " + ex.getLocalizedMessage());
            return false;
        }
        finally {

        }
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    private static void createConnection(){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Can't load database",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(0);
        }
    }

    public Connection getConn() {
        return conn;
    }
}
