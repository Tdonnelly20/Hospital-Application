package edu.wpi.veganvampires.Controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.veganvampires.Dao.EquipmentDeliveryDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EquipmentRequestController extends Controller {
  @FXML private TextField status;
  @FXML private TextField pos;
  @FXML private JFXComboBox<Object> dropDown;
  @FXML private TextField quant;
  @FXML private TextArea notes;
  @FXML private Button sendRequest;
  private static EquipmentDeliveryDao equipmentDeliveryDao = new EquipmentDeliveryDao();

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
  private void sendRequest() {
    equipmentDeliveryDao.addEquipmentDelivery(
        pos.getText(),
        dropDown.getValue().toString(),
        notes.getText(),
        Integer.parseInt(quant.getText()));
    resetForm();
  }

  @Override
  public void start(Stage primaryStage) {}
}
