package edu.wpi.veganvampires.Controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.veganvampires.Dao.SanitationRequestDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SanitationRequestController extends Controller {
  @Override
  public void start(Stage primaryStage) throws Exception {}

  @FXML private TextField patientID;

  @FXML private TextField firstName;
  @FXML private TextField lastName;
  /*
    @FXML private TextField location;
  */
  @FXML private JFXComboBox<Object> sanitationDropDown;
  @FXML private Button sendRequest;
  @FXML private TextArea requestDetails;
  @FXML private Label statusLabel;

  private static SanitationRequestDao SanitationRequestDao = new SanitationRequestDao();

  @FXML
  public void checkValidation() throws Exception {}
}
