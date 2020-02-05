package equipment.inventory.ui.issue;

import com.jfoenix.controls.JFXComboBox;
import equipment.inventory.alert.AlertMaker;
import equipment.inventory.database.DataHelper;
import equipment.inventory.database.DatabaseHandler;
import equipment.inventory.model.Staff;
import equipment.inventory.ui.main.MainController;
import equipment.inventory.ui.main.item.ItemController;
import equipment.inventory.utils.EquipmentInventoryUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class IssueDialogController implements Initializable {

    private ObservableList<Staff> staffList = FXCollections.observableArrayList();
    @FXML
    private JFXComboBox staffComboBox;
    private MainController mainController;

    @FXML
    private void handleIssueOperation(ActionEvent event) {
        if (staffComboBox.getValue() == null) {
            AlertMaker.showErrorMessage("Error", "No staff selected");
            return;
        }
        Long millis = System.currentTimeMillis();
        String now = EquipmentInventoryUtils.formatDateTimeString(millis);
        String issueId = EquipmentInventoryUtils.formatSQLDateTimeString(millis);
        for (ItemController item : MainController.cartItems) {
            item.getSelectedItem().setIssueId(issueId);
            item.getSelectedItem().setTimeBorrowed(now);
        }
        if (DataHelper.insertBorrowedEquipment(MainController.cartItems, (Staff) staffComboBox.getValue())) {
            mainController.clearCart(new ActionEvent());
            AlertMaker.showSimpleAlert("Success", "Issue inserted successfully");
            ((Stage) staffComboBox.getScene().getWindow()).close();
        }
    }

    @FXML
    private void handleCancelOperation(ActionEvent event) {
        ((Stage) staffComboBox.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            PreparedStatement preparedStatement = DatabaseHandler.getInstance().getConn().prepareStatement("SELECT * FROM STAFF_TABLE");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                staffList.add(new Staff(
                        resultSet.getString("staffId"),
                        resultSet.getString("staffFirstName"),
                        resultSet.getString("staffSurname"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("email")
                ));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        staffComboBox.setItems(staffList);
    }

    public void setUp(MainController mainController) {
        this.mainController = mainController;
    }
}
