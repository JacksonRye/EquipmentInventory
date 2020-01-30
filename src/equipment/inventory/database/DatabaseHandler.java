package equipment.inventory.database;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class DatabaseHandler {

    private static DatabaseHandler handler = null;

    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;

    static {
        createConnection();
    }

    private DatabaseHandler(){

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
            System.exit(0);
        }
    }



}
