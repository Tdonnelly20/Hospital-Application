package edu.wpi.veganvampires.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.veganvampires.dao.EquipmentDeliveryDao;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.EquipmentDelivery;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;

public class EquipmentRequestController extends Controller {

  @FXML private TreeTableView<EquipmentDelivery> equipmentRequestTable;
  @FXML private TreeTableColumn<EquipmentDelivery, String> posCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> equipCol;
  @FXML private TreeTableColumn<EquipmentDelivery, Integer> quantCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> notesCol;

  @FXML private TextField status;
  @FXML private TextField pos;
  @FXML private JFXComboBox<Object> dropDown;
  @FXML private TextField quant;
  @FXML private TextArea notes;
  @FXML private Button sendRequest;

  private static EquipmentDeliveryDao equipmentDeliveryDao = Vdb.equipmentDeliveryDao;

  @FXML
  private void updateTreeTable() {
    posCol.setCellValueFactory(new TreeItemPropertyValueFactory("location"));
    equipCol.setCellValueFactory(new TreeItemPropertyValueFactory("equipment"));
    quantCol.setCellValueFactory(new TreeItemPropertyValueFactory("quantity"));
    notesCol.setCellValueFactory(new TreeItemPropertyValueFactory("notes"));

    ArrayList<EquipmentDelivery> currEquipmentDeliveries =
        (ArrayList<EquipmentDelivery>) equipmentDeliveryDao.getAllEquipmentDeliveries();

    ArrayList<TreeItem> treeItems = new ArrayList<>();

    if (!currEquipmentDeliveries.isEmpty()) {

      for (EquipmentDelivery delivery : currEquipmentDeliveries) {
        TreeItem<EquipmentDelivery> item = new TreeItem(delivery);
        treeItems.add(item);
      }
      equipmentRequestTable.setShowRoot(false);
      TreeItem root = new TreeItem(equipmentDeliveryDao.getAllEquipmentDeliveries().get(0));
      equipmentRequestTable.setRoot(root);
      root.getChildren().addAll(treeItems);
    }
  }

  @FXML
  private void resetForm() {
    status.setText("Status: Blank");
    pos.setText("");
    notes.setText("");
    quant.setText("");
    dropDown.setValue(null);
    sendRequest.setDisable(true);
  }

  @FXML
  private void validateButton() {
    if (!(status.getText().isEmpty())
        && !(pos.getText().isEmpty())
        && !(dropDown.getValue() == null)
        && !(notes.getText().isEmpty())
        && !(quant.getText().isEmpty())
        && isInteger(quant.getText())) {
      status.setText("Status: Done");
      sendRequest.setDisable(false);

    } else if (!(status.getText().isEmpty())
        || !(pos.getText().isEmpty())
        || !(dropDown.getValue() == null)
        || !(notes.getText().isEmpty())
        || !(quant.getText().isEmpty())) {
      status.setText("Status: Processing");
      sendRequest.setDisable(true);

      if (!isInteger(quant.getText()) && !quant.getText().isEmpty()) {
        status.setText("Status: Quantity is not a number");
      }

    } else {
      status.setText("Status: Blank");
      sendRequest.setDisable(true);
    }
  }

  @FXML
  private void sendRequest() throws SQLException {
    equipmentDeliveryDao.addEquipmentDelivery(
        pos.getText(),
        dropDown.getValue().toString(),
        notes.getText(),
        Integer.parseInt(quant.getText()));
    resetForm();
    updateTreeTable();
  }

  @Override
  public void start(Stage primaryStage) {}
}
