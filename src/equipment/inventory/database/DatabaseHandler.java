package equipment.inventory.database;

import equipment.inventory.alert.AlertMaker;

import javax.swing.*;
import java.sql.*;

public final class DatabaseHandler {

    private static DatabaseHandler handler = null;

    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;

    public static final String STAFF_TABLE = "STAFF_TABLE";
    public static final String EQUIPMENT_STOCK_TABLE = "EQUIPMENT_STOCK";
    public static final String BORROWED_TABLE = "BORROWED_TABLE";

    {
        createConnection();
        setUpStaffTable();
        setUpStockTable();
        setUpBorrowedTable();
    }

    private void setUpStaffTable() {
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();

            ResultSet tables = dbm.getTables(null, null, STAFF_TABLE.toUpperCase(),
                    null);

            if (tables.next()) {
                System.out.println("Table " + STAFF_TABLE + " already exists, Ready to go!");
            }

            else {
                stmt.execute("CREATE TABLE " + STAFF_TABLE + "(" +
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
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();

            ResultSet tables = dbm.getTables(null, null, EQUIPMENT_STOCK_TABLE.toUpperCase(),
                    null);

            if (tables.next()){
                System.out.println("Table " + EQUIPMENT_STOCK_TABLE + " already exists, Ready to go!");
            } else {
                stmt.execute("CREATE TABLE " + EQUIPMENT_STOCK_TABLE + "( " +
                        "equipmentId varchar(200) primary key, \n" +
                        "equipmentName varchar(200), \n" +
                        "quantityRemaining INTEGER default 0" +
                        ")");

            }
        } catch (SQLException e) {
            AlertMaker.showErrorMessage(e)
            ;
        } finally {

        }
    }

    void setUpBorrowedTable() {
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();

            ResultSet tables = dbm.getTables(null, null, BORROWED_TABLE.toUpperCase(),
                    null);

            if (tables.next()) {
                System.out.println("Table " + BORROWED_TABLE + " already exists, Ready to go!");
            }

            else {
                stmt.execute("CREATE TABLE " + BORROWED_TABLE + "(" +
                        "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), \n" +
                        "issueId varchar(30) not null, \n" +
                        "equipmentId varchar(200), \n" +
                        "equipmentName varchar(200),\n" +
                        "quantityBorrowed INTEGER, \n" +
                        "borrowedBy varchar(200), \n" +
                        "timeBorrowed varchar(30) default null, \n" +
                        "timeReturned varchar(30) default null, \n" +
                        "FOREIGN KEY (equipmentId) REFERENCES " + EQUIPMENT_STOCK_TABLE +
                        "(equipmentId)," +
                        "FOREIGN KEY (borrowedBy) REFERENCES " + STAFF_TABLE + "(staffId)" +
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
            ex.printStackTrace();
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
            AlertMaker.showErrorMessage(ex);
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
