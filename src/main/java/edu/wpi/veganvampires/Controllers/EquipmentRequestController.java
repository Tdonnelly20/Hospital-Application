package edu.wpi.veganvampires.Controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EquipmentRequestController extends Controller {
  @FXML private TextField status;
  // @FXML private TextField location;
  @FXML private JFXComboBox<Object> dropDown;
  @FXML private TextArea notes;
  @FXML private Button sendRequest;

  @FXML
  private void resetForm() {
    status.setText("Status: Blank");
    // location.setText("");
    notes.setText("");
    dropDown.setValue(null);
    sendRequest.setDisable(true);
  }

  @FXML
  private void validateButton() {
    if (!(status.getText().isEmpty())
        // && !(location.getText().isEmpty())
        && !(dropDown.getValue() == null)
        && !(notes.getText().isEmpty())) {
      status.setText("Status: Done");
      sendRequest.setDisable(false);

    } else if (!(status.getText().isEmpty())
        // || !(location.getText().isEmpty())
        || !(dropDown.getValue() == null)
        || !(notes.getText().isEmpty())) {
      status.setText("Status: Processing");
      sendRequest.setDisable(true);

    } else {
      status.setText("Status: Blank");
      sendRequest.setDisable(true);
    }
  }

  @Override
  public void start(Stage primaryStage) {}
}
