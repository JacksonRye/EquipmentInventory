package equipment.inventory.ui.settings;

import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;

public class Preferences {

    public static final String  CONFIG_FILE = "config.txt";

    private String username;
    private String password;

    public Preferences() {
        username = "admin";
        setPassword("admin");
    }

    public static void initConfig(){
        Writer writer = null;
        try {
            Preferences preferences = new Preferences();
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preferences, writer);
        } catch (IOException ex){
            System.out.println("Error initializing config");
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                System.out.println("Error closing file at Initconfig " + ex.getLocalizedMessage() );
            }
        }

    }

    public static Preferences getPreferences(){
        Gson gson = new Gson();
        Preferences preferences = new Preferences();
        try {
            preferences = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);

        } catch (FileNotFoundException e) {
            System.out.println("File not found at getPreferences " + e.getLocalizedMessage());
            initConfig();
        }
        return preferences;
    }

    public static void writePreferenceToFile(Preferences preferences){
        Writer writer = null;

        try {
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preferences, writer);

            System.out.println("Preference added successfully");

        } catch (IOException ex) {
            System.out.println("Error writing to preference file " + ex.getLocalizedMessage());

        } finally {
            try {
                writer.close();
            } catch (IOException ex){
                System.out.println("Error closing file " + ex.getLocalizedMessage());
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.length() < 16){
            this.password = DigestUtils.sha1Hex(password);
        } else {
            this.password = password;
        }
    }
}
